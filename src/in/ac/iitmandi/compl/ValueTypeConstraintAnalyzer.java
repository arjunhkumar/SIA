/**
 * 
 */
package in.ac.iitmandi.compl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import in.ac.iitmandi.compl.helper.CommonUtils;
import soot.SootClass;
import soot.SootField;
import soot.Type;

/**
 * @author arjun
 *
 */
public class ValueTypeConstraintAnalyzer{
	
	Set<String> primitiveTypes = Stream.of("int", "boolean", "float", "long", "double", "byte", "char")
									.collect(Collectors.toCollection(HashSet::new));

	public boolean checkStructuralValidity(SootClass appClass,SIAAnalyzer siaAnalayzer) {
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
	
	private boolean isPrimitiveType(Type fieldType) {
		if(null != fieldType && CommonUtils.isNotNull(fieldType.toQuotedString())) {
			if(primitiveTypes.contains(fieldType.toQuotedString())) {
				return true;
			}
		}
		return false;
	}
}
