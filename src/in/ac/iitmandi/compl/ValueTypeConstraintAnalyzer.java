/**
 * 
 */
package in.ac.iitmandi.compl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import in.ac.iitmandi.compl.ds.ConstraintViolator;
import in.ac.iitmandi.compl.ds.ViolationData;
import in.ac.iitmandi.compl.ds.ViolationTypes.VIOLATIONTYPE;
import in.ac.iitmandi.compl.helper.CommonUtils;
import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

/**
 * @author arjun
 *
 */
public class ValueTypeConstraintAnalyzer{
	
	
	/** Set of primitive types. */
	private Set<String> primitiveTypes = Stream.of("int", "boolean", "float", "long", "double", "byte", "char")
									.collect(Collectors.toCollection(HashSet::new));
	
	private Set<SootClass> analysisSet;
	private Set<ConstraintViolator> violatorSet;
	private static final SootClass OBJECT_CLASS = Scene.v().getSootClass("java.lang.Object");
	
	
	public void analyzeWorkingSet(SIAAnalyzer siaAnalyzer) {
		Set<SootClass> workingSet = siaAnalyzer.getWorkingSet();
		if(CommonUtils.isNotNull(workingSet)) {
			this.analysisSet = new HashSet<>(workingSet);
			for(SootClass appClass : this.analysisSet) {
				validateConstraints(appClass,siaAnalyzer);
			}
			validateFieldRegularity();
		}
	}
	
	
	private boolean validateConstraints(SootClass appClass, SIAAnalyzer siaAnalyzer) {
		return checkStructuralValidity(appClass, siaAnalyzer) && checkInstanceMethodValidity(appClass)
				&& checkSuperClassConstraint(appClass) ;
	}


	/** This method is primarily responsible for checking whether a field follows structural validity. 
	 * By structural validity we mean that any instance field of a class is subject to either of the following conditions:
	 * 1. Type should be a primitive type as defined in the set primitiveTypes. 
	 * 2. If it is a user-defined type it should be a value-type as defined in the set SIAAnalyzer.valuetype*/
	public boolean checkStructuralValidity(SootClass appClass, SIAAnalyzer siaAnalayzer) {
		if(null != appClass && containsInstanceFields(appClass)) {
			Set<SootField> instanceFields = getInstanceFields(appClass);
			for(SootField instanceField : instanceFields) {
				if(!isPrimitiveType(instanceField.getType()) 
						|| !siaAnalayzer.isValueType(instanceField.getType().toQuotedString())){
					addConstraintViolation(appClass,VIOLATIONTYPE.STRUCTURAL_VALIDITY);
					return false;
				}
			}
			return true;
		}
		addConstraintViolation(appClass,VIOLATIONTYPE.NO_INSTANCE_FIELDS);
		return false;
	}
	
	/** This method is responsible for checking the properties of instance methods.
	 * 1. There should not be any synchronized methods. 
	 * 2. There should not be any finalize method. */
	public boolean checkInstanceMethodValidity(SootClass appClass) {
		if(containsInstanceMethods(appClass)) {
			Set<SootMethod> instanceMethods = getInstanceMethods(appClass);
			for(SootMethod method : instanceMethods) {
				if(method.isSynchronized() || isFinalizeMethod(method)) {
					int lineNumber = method.getJavaSourceStartLineNumber();
					addConstraintViolation(appClass,method,-1,lineNumber , VIOLATIONTYPE.METHOD_SYNC);
					return false;
				}
			}
		}
		return true;
	}
	

	/** Filtering useful classes. If a class has no instance fields eventhough it can become a value type is 
	 * not useful to us. (Inlining cannot be performed). Hence filtering out such candidates. */
	public boolean containsInstanceFields(SootClass appClass) {
		if(null != appClass && appClass.getFieldCount() > 0) {
			for(SootField field : appClass.getFields()) {
				if(!field.isStatic()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Returns a set of instance fields of a class.
	 * @param appClass
	 * @return
	 */
	public Set<SootField> getInstanceFields(SootClass appClass) {
		Set<SootField> instanceFieldSet = new HashSet<>();
		if(null != appClass && appClass.getFieldCount() > 0) {
			for(SootField field : appClass.getFields()) {
				if(!field.isStatic()) {
					instanceFieldSet.add(field);
				}
			}
		}
		return instanceFieldSet;
	}
	
	
	/** Validate if a class contains any instance methods.*/
	public boolean containsInstanceMethods(SootClass appClass) {
		if(null != appClass && appClass.getMethodCount() > 0) {
			for(SootMethod method : appClass.getMethods()) {
				if(!method.isStatic()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Returns a set of instance methods of a class.
	 * @param appClass
	 * @return
	 */
	public Set<SootMethod> getInstanceMethods(SootClass appClass) {
		Set<SootMethod> instanceMethodSet = new HashSet<>();
		if(null != appClass && appClass.getMethodCount() > 0) {
			for(SootMethod method : appClass.getMethods()) {
				if(!method.isStatic()) {
					instanceMethodSet.add(method);
				}
			}
		}
		return instanceMethodSet;
	}
	
	/**
	 * Returns true if a fieldType is a primitive datatype.
	 * @param Type fieldType
	 * @return
	 */
	private boolean isPrimitiveType(Type fieldType) {
		if(null != fieldType && CommonUtils.isNotNull(fieldType.toQuotedString())) {
			if(primitiveTypes.contains(fieldType.toQuotedString())) {
				return true;
			}
		}
		return false;
	}
	
	/** TO-DO: Stricter validation based on debug info required here.
	 * Naive check to see if a method is a finalize method. */
	private boolean isFinalizeMethod(SootMethod method) {
		return null != method && CommonUtils.isNotNull(method.getName()) && method.getName().equals("finalize");
	}
	
	private void validateFieldRegularity() {
		if(CommonUtils.isNotNull(analysisSet)) {
			HashSet<ConstraintViolator> violatorSet = new HashSet<>();
			for(SootClass appClass : analysisSet) {
				analyzeClass(appClass, violatorSet);
			}
		}
	}
	
	private boolean checkSuperClassConstraint(SootClass appClass) {
		if(null != appClass) {
			SootClass superClass = appClass.getSuperclass();
			if(superClass.equals(OBJECT_CLASS) || superClass.isInterface()) {
				return true;
			}
		}
		addConstraintViolation(appClass,VIOLATIONTYPE.SUPERCLASS_CONSTRAINT);
		return false;
	}
	
	private void analyzeClass(SootClass appClass, Set<ConstraintViolator> violatorSet) {
		if(null != appClass && CommonUtils.isNotNull(appClass.getMethods())) {
			for(SootMethod method : appClass.getMethods()) {
				analyzeMethod(method, violatorSet);
			}
		}
	}

	private void analyzeMethod(SootMethod method,Set<ConstraintViolator> violatorSet) {
		Body body = getSootMethodBody(method);
		if(null != body && !method.isConstructor()) {
			PatchingChain<Unit> units = body.getUnits();
			for (Unit unit : units) {
				if(unit instanceof JAssignStmt) {
					JAssignStmt assignStmt = (JAssignStmt) unit;
					List<ValueBox> defBoxes = assignStmt.getDefBoxes();
					if(CommonUtils.isNotNull(defBoxes) && defBoxes.size() == 1) {
						ValueBox defBox = defBoxes.get(0);
						Value def = defBox.getValue();
						if(def instanceof JInstanceFieldRef) {
							JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef)def;
							Type fieldType = instanceFieldRef.getBase().getType();
							if(CommonUtils.isNotNull(fieldType.toQuotedString())){
								SootClass typeClass = Scene.v().getSootClass(fieldType.toQuotedString());
								if(analysisSet.contains(typeClass)) {
									addConstraintViolation(typeClass,method,getBCO(unit),unit.getJavaSourceStartLineNumber(), VIOLATIONTYPE.FIELD_MUTATION); 
									//unit.getJavaSourceStartLineNumber();
								}
							}
						}
					}else {
						System.err.println("Invalid def count for unit: "+unit.toString());
					}
				}
			}
		// Else the method is a constructor.
		}else if(null != body) {
			SootClass constructorClass = method.getDeclaringClass();
			PatchingChain<Unit> units = body.getUnits();
			for (Unit unit : units) {
				if(unit instanceof JAssignStmt) {
					JAssignStmt assignStmt = (JAssignStmt) unit;
					List<ValueBox> defBoxes = assignStmt.getDefBoxes();
					if(CommonUtils.isNotNull(defBoxes) && defBoxes.size() == 1) {
						ValueBox defBox = defBoxes.get(0);
						Value def = defBox.getValue();
						if(def instanceof JInstanceFieldRef) {
							JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef)def;
							Type fieldType = instanceFieldRef.getBase().getType();
							if(CommonUtils.isNotNull(fieldType.toQuotedString())){
								SootClass typeClass = Scene.v().getSootClass(fieldType.toQuotedString());
								if(!typeClass.equals(constructorClass) && analysisSet.contains(typeClass)) {
									addConstraintViolation(typeClass,method,getBCO(unit),unit.getJavaSourceStartLineNumber(), VIOLATIONTYPE.FIELD_MUTATION); 
								} /** Else a definition is being made inside the constructor. Ensure regulated constructors. */
								else if(analysisSet.contains(typeClass)) {
									if(assignStmt.getRightOp().equals(soot.jimple.NullConstant.v())) {
										addConstraintViolation(typeClass,method,getBCO(unit),unit.getJavaSourceStartLineNumber(), VIOLATIONTYPE.NULL_ASSIGN); 
									}
								}
								
							}
						}
					}else {
						System.err.println("Invalid def count for unit: "+unit.toString());
					}
				}
			}
		}
	}

	/**
	 * @param unit
	 */
	private int getBCO(Unit unit) {
		final Tag BCO_TAG = unit.getTag(BytecodeOffsetTag.NAME);
		if(BCO_TAG instanceof BytecodeOffsetTag) {
			BytecodeOffsetTag bcoTag = (BytecodeOffsetTag)BCO_TAG;
			return bcoTag.getBytecodeOffset();
		}
		return -1;
	}
	
	
	/**
	 * @param classMethod
	 * @return
	 */
	private Body getSootMethodBody(SootMethod classMethod) {
		Body body = null;
		if(classMethod.hasActiveBody()) {
			body = classMethod.getActiveBody();
		}else if(classMethod.getDeclaringClass().isApplicationClass() && !classMethod.isJavaLibraryMethod()){
			try {
				body = classMethod.retrieveActiveBody();
			}catch(Exception e) {
				System.err.println("Exception occured while retrieving soot body for method: "+classMethod.toString());
			}
		}
		return body;
	}
	
	
	private void addConstraintViolation(SootClass typeClass, SootMethod method, int bco, int javaSrcLineNo, VIOLATIONTYPE violationType) {
		ConstraintViolator existingViolator = findExistingViolator4Class(typeClass);
		if(existingViolator == null) {
			ConstraintViolator violator = new ConstraintViolator(typeClass,1, new ViolationData(method,bco,javaSrcLineNo,violationType));
			violatorSet.add(violator);
		}else {
			existingViolator.addNewViolation(method,bco,javaSrcLineNo,violationType);
		}
	}
	
	private void addConstraintViolation(SootClass typeClass, VIOLATIONTYPE violationType) {
		ConstraintViolator existingViolator = findExistingViolator4Class(typeClass);
		if(existingViolator == null) {
			ConstraintViolator violator = new ConstraintViolator(typeClass,1, new ViolationData(violationType));
			violatorSet.add(violator);
		}else {
			existingViolator.addNewViolation(violationType);
		}
	}

	private ConstraintViolator findExistingViolator4Class(SootClass typeClass) {
		if(null != typeClass && CommonUtils.isNotNull(violatorSet)) {
			for(ConstraintViolator violator : violatorSet) {
				if(null != violator.getAppClass() && violator.getAppClass().equals(typeClass)) {
					return violator;
				}
			}
		}
		return null;
	}

	private void addToViolatorSet(ConstraintViolator violator) {
		if(null != violator){
			if(null == violatorSet) {
				this.violatorSet = new HashSet<>();
			}
			this.violatorSet.add(violator);
		}
	}
	
}
