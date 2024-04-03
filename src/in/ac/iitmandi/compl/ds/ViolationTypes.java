/**
 * 
 */
package in.ac.iitmandi.compl.ds;

/**
 * @author arjun
 *
 */
public class ViolationTypes {


	public enum VIOLATIONTYPE {
		
		FIELD_MUTATION (0){
			@Override
			public boolean isFieldMutationViolation() {
				return true;
			}
		},
		
		SUBCLASS_EXISTS (1){
			public boolean isSubClassViolation() {
				return true;
			}
		},
		
		METHOD_SYNC (2){
			public boolean isMethodSyncViolation() {
				return true;
			}
		},
		
		SUPERCLASS_CONSTRAINT (3){
			public boolean isSuperClassViolation() {
				return true;
			}
		},
		
		STRUCTURAL_VALIDITY (4){
			public boolean isStructuralViolation() {
				return true;
			}
		},
		
		NULL_ASSIGN (5){
			public boolean isNullAssignViolation() {
				return true;
			}
		},
		
		NO_INSTANCE_FIELDS (6){
			public boolean isNoInstanceFieldViolation() {
				return true;
			}
		};

		private int violationID;


		VIOLATIONTYPE (int violationID) {
			this.violationID = this.ordinal();
		}
		
		public boolean isFieldMutationViolation() {
			return false;
		}
		
		public boolean isSubClassViolation() {
			return false;
		}
		
		public boolean isSuperClassViolation() {
			return false;
		}
		
		public boolean isMethodSyncViolation() {
			return false;
		}
		
		public boolean isStructuralViolation() {
			return false;
		}
		
		public boolean isNullAssignViolation() {
			return false;
		}
		
		public boolean isNoInstanceFieldViolation() {
			return false;
		}
	}


}
