/**
 * 
 */
package in.ac.iitmandi.compl.ds;

import java.util.Objects;

import soot.SootMethod;

/**
 * @author arjun
 *
 */
public class ViolationData {

	private SootMethod methodInfo;
	private int BCI;
	private int javaSourceLineNumber;
	
	public ViolationData(SootMethod method, int bco, int javaSrcLineNo) {
		this.methodInfo = method;
		this.BCI = bco;
		this.javaSourceLineNumber = javaSrcLineNo;
	}
	/**
	 * @return the methodInfo
	 */
	public SootMethod getMethodInfo() {
		return methodInfo;
	}
	/**
	 * @param methodInfo the methodInfo to set
	 */
	public void setMethodInfo(SootMethod methodInfo) {
		this.methodInfo = methodInfo;
	}
	/**
	 * @return the bCI
	 */
	public int getBCI() {
		return BCI;
	}
	/**
	 * @param bCI the bCI to set
	 */
	public void setBCI(int bCI) {
		BCI = bCI;
	}
	
	/**
	 * @return the javaSourceLineNumber
	 */
	public int getJavaSourceLineNumber() {
		return javaSourceLineNumber;
	}
	/**
	 * @param javaSourceLineNumber the javaSourceLineNumber to set
	 */
	public void setJavaSourceLineNumber(int javaSourceLineNumber) {
		this.javaSourceLineNumber = javaSourceLineNumber;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(BCI, methodInfo, javaSourceLineNumber);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ViolationData)) {
			return false;
		}
		ViolationData other = (ViolationData) obj;
		return BCI == other.BCI && javaSourceLineNumber == other.javaSourceLineNumber 
				&& Objects.equals(methodInfo, other.methodInfo);
	}
	
	@Override
	public String toString() {
		return "ViolationData [" + (methodInfo != null ? "methodInfo=" + methodInfo + ", " : "") + "BCI=" + BCI
				+ ", javaSourceLineNumber=" + javaSourceLineNumber + "]";
	}
	
}
