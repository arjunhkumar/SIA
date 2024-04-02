/**
 * 
 */

package in.ac.iitmandi.compl;

import in.ac.iitmandi.compl.helper.CommonUtils;
import in.ac.iitmandi.compl.helper.CompLLogManager;
import in.ac.iitmandi.compl.json.JsonGenerator;
import soot.PackManager;
import soot.Transform;

/**
 * @author arjun
 *
 */
public class MainKlass {

	public static void main(String[] args) {
		CompLLogManager.configureLogger();
		String[] sootArgs = generateSootArgs(args);
		if(null != sootArgs) {
			PackManager.v().getPack("wjtp").add(new Transform("wjtp.pea", new SIAAnalyzer()));
			soot.Main.main(sootArgs);
		}
	}
	
	
	private static String[] generateSootArgs(String[] args) {
		if(null == args || args.length < 3) {
			CompLLogManager.logErrorMessage("Provide args in the format. [Reflection-Mode] [Process Dir] [Main Class]");
			return null;
		}else {
			String processDir = args[1];
			String mainClass = args[2];
			String reflectionMode = args[0];
			CommonUtils.setProcess_dir(processDir);
			if(reflectionMode.equalsIgnoreCase("TRUE")) {
				String reflLogPath="reflection-log:"+processDir+"/refl.log";
				return new String[] {
						"-cp", ".", "-pp",
						"-w",
						"-f","c",
						"-no-bodies-for-excluded",
						"-include", "org.apache", "-include", "org.w3c",
						"-p", "cg", reflLogPath,
						"-p","cg.spark","on",
						"-process-dir" , processDir,
						"-main-class", mainClass
				};
			}else {
				return new String[] {
						"-cp", ".", "-pp",
		                "-w",
		                "-f","c",
		                "-no-bodies-for-excluded",
		                "-p","cg.spark","on",
		                "-process-dir" , processDir,
		                "-main-class", mainClass
				};
			}
		}
	}
	
}
