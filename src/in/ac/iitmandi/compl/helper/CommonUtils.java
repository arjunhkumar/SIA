/**
 * 
 */
package in.ac.iitmandi.compl.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import soot.SootClass;
import soot.SootField;
import soot.util.Chain;
/**
 * @author arjun
 *
 */
public class CommonUtils {

	static CommonUtils commonUtils = null;
	private String benchName = null;
	public static final String OUT_FILE_PATH = "./out/out.log";
	public static final String FIELDS_METADATA_FILE_PATH = "./out/field-metadata.log";
	private static String process_dir = "";
	/**
	 * Private Constructor for singleton implementation
	 */
	private CommonUtils() {
//		globalEnv = new MiniSchemeEnvironment();
	}
	

	/**
	 * @return the benchName
	 */
	public String getBenchName() {
		return benchName;
	}


	/**
	 * @param benchName the benchName to set
	 */
	public void setBenchName(String benchName) {
		this.benchName = benchName;
	}


	public static CommonUtils getInstance() {
		if(null==commonUtils) {
			commonUtils = new CommonUtils();
		}
		return commonUtils;
	}

	
	public static boolean isNotNull(String str) {
		return !(null==str || "".equalsIgnoreCase(str));
	}
	
	public static boolean isNotNull(List<?> list) {
		return (null!=list && !list.isEmpty());
	}
	
	public static boolean isNotNull(Map<?, ?> map) {
		return (null!=map && !map.isEmpty());
	}
	
	public static boolean isNotNull(Set<?> set) {
		return (null!=set && !set.isEmpty());
	}
	
	public static boolean isNotNull(Vector<?> vector) {
		return (null!=vector && !vector.isEmpty());
	}
	
	public static boolean isNotNull(Chain<?> chain) {
		return (null!=chain && !chain.isEmpty());
	}
	
	public static Map<String,List<String>> populateJGFSectionMap() {
		Map<String,List<String>> jgfMap = new HashMap<>();
		List<String> section1List = Arrays.asList("JGFBarrierBench","JGFForkJoinBench","JGFSyncBench");
		List<String> section2List = Arrays.asList("JGFCryptBenchSizeA","JGFCryptBenchSizeB","JGFCryptBenchSizeC",
				"JGFLUFactBenchSizeA","JGFLUFactBenchSizeB","JGFLUFactBenchSizeC",
				"JGFSORBenchSizeA","JGFSORBenchSizeB","JGFSORBenchSizeC",
				"JGFSparseMatmultBenchSizeA","JGFSparseMatmultBenchSizeB","JGFSparseMatmultBenchSizeC",
				"JGFSeriesBenchSizeA","JGFSeriesBenchSizeB","JGFSeriesBenchSizeC");
		List<String> section3List = Arrays.asList("JGFMolDynBenchSizeA","JGFMolDynBenchSizeB","JGFMonteCarloBenchSizeA",
				"JGFMonteCarloBenchSizeB","JGFRayTracerBenchSizeA","JGFRayTracerBenchSizeB");
		jgfMap.put("SECTION1", section1List);
		jgfMap.put("SECTION2", section2List);
		jgfMap.put("SECTION3", section3List);
		return jgfMap;
	}
	
	
	public static String getBenchmark() {
		return getInstance().getBenchName();
	}
	
	
	public static boolean isBothNullorEmpty(List<?> list1, List<?> list2) {
		return (null == list1 || list1.isEmpty()) && (list2 == null || list2.isEmpty());
	}
	
	public static boolean isBothNullorEmpty(Set<?> list1, Set<?> list2) {
		return (null == list1 || list1.isEmpty()) && (list2 == null || list2.isEmpty());
	}
	
	public static boolean isBothNullorEmpty(Map<?,?> map1, Map<?,?> map2) {
		return (null == map1 || map1.isEmpty()) && (map2 == null || map2.isEmpty());
	}
	
	public static boolean isBothNullorEmpty(String str1, String str2) {
		return (null == str1 || str1.isEmpty()) && (str2 == null || str2.isEmpty());
	}
	
	public static boolean hasInstanceFields(SootClass sootClass) {
		if(null != sootClass && CommonUtils.isNotNull(sootClass.getFields())) {
			Chain<SootField> fields = sootClass.getFields();
			for(SootField field : fields) {
				if(!field.isStatic()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	public static void createOutFile() {
		Path path = Paths.get(OUT_FILE_PATH);
		File file = new File(OUT_FILE_PATH);
		if (!file.exists()) {
			try {
				Files.createDirectories(path.getParent()); 
				Files.createFile(path);
			} catch (IOException e) {
				System.out.println("File :"+OUT_FILE_PATH+" could not be created.");
			}
		}
	}
	
	/**
	 * 
	 */
	public static void createFieldOutFile() {
		Path path = Paths.get(FIELDS_METADATA_FILE_PATH);
		File file = new File(FIELDS_METADATA_FILE_PATH);
		if (!file.exists()) {
			try {
				Files.createDirectories(path.getParent()); 
				Files.createFile(path);
			} catch (IOException e) {
				System.out.println("File :"+FIELDS_METADATA_FILE_PATH+" could not be created.");
			}
		}
	}

	public static void writeToOutFile(long execTime) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write(execTime+"\t");
			}
		} catch (IOException e) {
			System.out.println("File :"+OUT_FILE_PATH+" could not be opened.");
		}
	}
	
	public static void writeToOutFile(String msg) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write(msg+"\t");
			}
		} catch (IOException e) {
			System.out.println("File :"+OUT_FILE_PATH+" could not be opened.");
		}
	}
	
	public static void writeLineToOutFile(String msg) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write(msg+"\n");
			}
		} catch (IOException e) {
			System.out.println("File :"+OUT_FILE_PATH+" could not be opened.");
		}
	}


	public static String getProcess_dir() {
		return process_dir;
	}


	public static void setProcess_dir(String process_dir) {
		CommonUtils.process_dir = process_dir;
	}

	
	
}
