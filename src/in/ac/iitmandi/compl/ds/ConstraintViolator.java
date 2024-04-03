/**
 * 
 */
package in.ac.iitmandi.compl.ds;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import in.ac.iitmandi.compl.ds.ViolationTypes.VIOLATIONTYPE;
import soot.SootClass;
import soot.SootMethod;

/**
 * @author arjun
 *
 */
public class ConstraintViolator {

	private SootClass appClass;
	private int violationCount;
	private Set<ViolationData> violationData;

	
	public ConstraintViolator(SootClass typeClass, int i, ViolationData violationData2) {
		this.appClass = typeClass;
		this.violationCount = i;
		this.violationData = new HashSet<>();
		this.violationData.add(violationData2);
	}

	/**
	 * @return the appClass
	 */
	public SootClass getAppClass() {
		return appClass;
	}

	/**
	 * @param appClass the appClass to set
	 */
	public void setAppClass(SootClass appClass) {
		this.appClass = appClass;
	}

	/**
	 * @return the violationCount
	 */
	public int getViolationCount() {
		return violationCount;
	}

	/**
	 * @param violationCount the violationCount to set
	 */
	public void setViolationCount(int violationCount) {
		this.violationCount = violationCount;
	}

	/**
	 * @return the violationData
	 */
	public Set<ViolationData> getViolationData() {
		return violationData;
	}

	/**
	 * @param violationData the violationData to set
	 */
	public void setViolationData(Set<ViolationData> violationData) {
		this.violationData = violationData;
	}

	public void addNewViolation(SootMethod method, int bco, int javaSrcLineNo, VIOLATIONTYPE type) {
		if(null != method) {
			ViolationData violation = new ViolationData(method, bco, javaSrcLineNo, type);
			this.violationData.add(violation);
			this.violationCount++;
		}
	}
	
	public void addNewViolation(VIOLATIONTYPE type) {
		if(null != type) {
			ViolationData violation = new ViolationData(type);
			this.violationData.add(violation);
			this.violationCount++;
		}
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(appClass);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ConstraintViolator)) {
			return false;
		}
		ConstraintViolator other = (ConstraintViolator) obj;
		return Objects.equals(appClass, other.appClass);
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "ConstraintViolator [" + (appClass != null ? "appClass=" + appClass + ", " : "") + "violationCount="
				+ violationCount + ", "
				+ (violationData != null ? "violationData=" + toString(violationData, maxLen) : "") + "]";
	}

	
	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	
}
