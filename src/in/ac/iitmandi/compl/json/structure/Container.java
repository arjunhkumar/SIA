/**
 * 
 */
package in.ac.iitmandi.compl.json.structure;

import java.util.List;
import java.util.ArrayList;

import in.ac.iitmandi.compl.helper.CommonUtils;

/**
 * @author arjun
 *
 */
public class Container {

	String className;
	List<InlineField> inlineFieldList;
	
	/**
	 * 
	 */
	public Container() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param className
	 */
	public Container(String className) {
		this.className = className;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the inlineFieldList
	 */
	public List<InlineField> getInlineFieldList() {
		return inlineFieldList;
	}
	/**
	 * @param inlineFieldList the inlineFieldList to set
	 */
	public void setInlineFieldList(List<InlineField> inlineFieldList) {
		this.inlineFieldList = inlineFieldList;
	}
	
	public void addInlineField(InlineField field) {
		if(null == inlineFieldList) {
			inlineFieldList = new ArrayList<>();
		}
		inlineFieldList.add(field);
	}
	
	@Override
	public String toString() {
		return "Container [className=" + className + ", inlineFieldList=" + inlineFieldList + "]";
	}
	
	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object comparee) {
		if (comparee == this)
	        return true;
	    if (!(comparee instanceof Container))
	        return false;
	    
	    Container containerComparee = (Container)comparee;
	    
	    boolean classNameComparison = CommonUtils.isBothNullorEmpty(this.className,containerComparee.className)
	    								|| ( ( CommonUtils.isNotNull(this.className) && CommonUtils.isNotNull(containerComparee.className) ) 
	    								&& ( this.className.equals(containerComparee.className) ) );
		

	    boolean fieldUnitComparison = CommonUtils.isBothNullorEmpty(this.inlineFieldList, containerComparee.inlineFieldList)
	    								|| (CommonUtils.isNotNull(this.inlineFieldList) && CommonUtils.isNotNull(containerComparee.inlineFieldList)
	    										&& this.inlineFieldList.equals(containerComparee.inlineFieldList))
	    		;
	    
	    return classNameComparison 
				&& fieldUnitComparison;
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode() {
		int result = 17;
	    if (CommonUtils.isNotNull(this.className)) {
	        result = 31 * result + this.className.hashCode();
	    }
	    if (CommonUtils.isNotNull(this.inlineFieldList)) {
	        result = 31 * result + this.inlineFieldList.hashCode();
	    }
		return result;
	}
	
}
