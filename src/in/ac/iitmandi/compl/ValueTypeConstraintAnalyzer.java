/**
 * 
 */
package in.ac.iitmandi.compl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import in.ac.iitmandi.compl.helper.CommonUtils;
import soot.Body;
import soot.PatchingChain;
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

/**
 * @author arjun
 *
 */
public class ValueTypeConstraintAnalyzer{
	
	
	/** Set of primitive types. */
	Set<String> primitiveTypes = Stream.of("int", "boolean", "float", "long", "double", "byte", "char")
									.collect(Collectors.toCollection(HashSet::new));

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
					return false;
				}
			}
			return true;
		}
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
		if(null != method && CommonUtils.isNotNull(method.getName()) && method.getName().equals("finalize")) {
			return true;
		}
		return false;
	}
	
	
	
	private void validateFieldRegularity(Set<SootClass> validClasses) {
		if(CommonUtils.isNotNull(validClasses)) {
			for(SootClass appClass : validClasses) {
				analyzeClass(appClass);
			}
		}
	}

	private void analyzeClass(SootClass appClass) {
		if(null != appClass && CommonUtils.isNotNull(appClass.getMethods())) {
			for(SootMethod method : appClass.getMethods()) {
				analyzeMethod(method);
			}
		}
	}

	private void analyzeMethod(SootMethod method) {
		Body body = getSootMethodBody(method);
		if(null != body) {
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
						}
					}else {
						System.err.println("Invalid def count for unit: "+unit.toString());
					}
				}
			}
		}
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
				
			}
		}
		return body;
	}
}
