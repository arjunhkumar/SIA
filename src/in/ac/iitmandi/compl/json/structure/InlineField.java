package in.ac.iitmandi.compl.json.structure;

import in.ac.iitmandi.compl.helper.CommonUtils;

public class InlineField {

	String fieldName;
	String typeClassName;
	/**
	 * 
	 */
	public InlineField() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param fieldName
	 * @param className
	 */
	public InlineField(String fieldName, String className) {
		this.fieldName = fieldName;
		this.typeClassName = className;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return typeClassName;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.typeClassName = className;
	}
	
	@Override
	public String toString() {
		return "InlineField [fieldName=" + fieldName + ", typeClassName=" + typeClassName + "]";
	}
	
	/**
	 * @param arg0
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object comparee) {
		if (comparee == this)
	        return true;
	    if (!(comparee instanceof InlineField))
	        return false;
	    
	    InlineField ifComparee = (InlineField)comparee;
	    
	    boolean fieldNameComparison = CommonUtils.isBothNullorEmpty(this.fieldName,ifComparee.fieldName)
				|| ( ( CommonUtils.isNotNull(this.fieldName) && CommonUtils.isNotNull(ifComparee.fieldName) ) 
				&& ( this.fieldName.equals(ifComparee.fieldName) ) );
	    
	    boolean classNameComparison = CommonUtils.isBothNullorEmpty(this.typeClassName,ifComparee.typeClassName)
	    								|| ( ( CommonUtils.isNotNull(this.typeClassName) && CommonUtils.isNotNull(ifComparee.typeClassName) ) 
	    								&& ( this.typeClassName.equals(ifComparee.typeClassName) ) );
		
	    return classNameComparison 
				&& fieldNameComparison;
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode() {
		int result = 17;
	    if (CommonUtils.isNotNull(this.typeClassName)) {
	        result = 31 * result + this.typeClassName.hashCode();
	    }
	    if (CommonUtils.isNotNull(this.fieldName)) {
	        result = 31 * result + this.fieldName.hashCode();
	    }
		return result;
	}
	
}
