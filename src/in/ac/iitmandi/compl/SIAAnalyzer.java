/**
 * 
 */
package in.ac.iitmandi.compl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import in.ac.iitmandi.compl.helper.CommonUtils;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.util.Chain;

/**
 * @author arjun
 *
 */
public class SIAAnalyzer extends SceneTransformer {

	private Set<SootClass> workingSet;
	private Set<SootClass> valueTypes;
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		populateWorkingSet();
		findInlineableTypeClasses();
	}

	private void findInlineableTypeClasses() {
		ValueTypeConstraintAnalyzer vtAnalyzer = new ValueTypeConstraintAnalyzer();
//		vtAnalyzer.
	}

	/**
	 * 
	 */
	private void populateWorkingSet() {
		Chain<SootClass> appClasses = Scene.v().getApplicationClasses();
		if(CommonUtils.isNotNull(appClasses)) {
			for(SootClass appClass : appClasses) {
				if(!appClass.isJavaLibraryClass() && !isLibraryPackagedClass(appClass)) {
					addToWorkingSet(appClass);
				}
			}
		}
	}
 
	
	public void addToWorkingSet(SootClass appClass) {
		if(null == this.workingSet) {
			this.workingSet = new HashSet<>();
		}
		this.workingSet.add(appClass);
	}
	
	public void addToValueTypeSet(SootClass appClass) {
		if(null == this.valueTypes) {
			this.valueTypes = new HashSet<>();
		}
		this.valueTypes.add(appClass);
	}
	
	public boolean isLibraryPackagedClass(SootClass sootClass) {
		if(null != sootClass && CommonUtils.isNotNull(sootClass.getPackageName())) {
			String packageName = sootClass.getPackageName();
			if(packageName.startsWith("java.") || packageName.startsWith("jdk.")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValueType(String appClassString) {
		SootClass appClass = Scene.v().getSootClass(appClassString);
		return null != appClass 
				&& CommonUtils.isNotNull(valueTypes) 
				&& this.valueTypes.contains(appClass);
	}
}
