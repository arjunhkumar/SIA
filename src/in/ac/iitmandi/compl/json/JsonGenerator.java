/**
 * 
 */
package in.ac.iitmandi.compl.json;


import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import in.ac.iitmandi.compl.helper.CommonUtils;
import in.ac.iitmandi.compl.helper.CompLLogManager;
import in.ac.iitmandi.compl.json.structure.Container;
import in.ac.iitmandi.compl.json.structure.InlineField;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.util.Chain;
import soot.util.HashChain;

/**
 * @author arjun
 *
 */
public class JsonGenerator {

//	public static void  iterateClasses() {
//		if(CommonUtils.isNotNull(PrimaryReachabilityResult.getInstance().getPrimitiveClasses())) {
//			final Chain<SootClass> sootClasses = new HashChain<>(Scene.v().getApplicationClasses());
//			clearDirectoryAndCreateNew();
//			long inlineableFieldCount = 0;
//			CommonUtils.createFieldOutFile();
//			for(SootClass classToBeAnalyzed : sootClasses) {
//				if(!classToBeAnalyzed.isInterface()) {
//					List<SootField> inlineableFields = getInlineableFields(classToBeAnalyzed);
//					if(CommonUtils.isNotNull(inlineableFields)) {
//						inlineableFieldCount+= inlineableFields.size();
//						writeJson(classToBeAnalyzed,inlineableFields);
//					}
//				}
//			}
//			CommonUtils.writeToOutFile(inlineableFieldCount);
//		}
//	}
//
//	private static List<SootField> getInlineableFields(SootClass classToBeAnalyzed) {
//		if(null != classToBeAnalyzed) {
//			List<SootField> inlineableFields = new ArrayList<>();
//			List<SootClass> primitiveClasses = PrimaryReachabilityResult.getInstance().getPrimitiveClasses();
//			Chain<SootField> fields = classToBeAnalyzed.getFields();
//			for(SootField field : fields) {
//				if(!field.isStatic()) {
//					SootClass sootClass = Scene.v().getSootClass(field.getType().toQuotedString());
//					if(primitiveClasses.contains(sootClass)){
//						inlineableFields.add(field);
//					}
//				}
//			}
//			return inlineableFields;
//		}
//		return null;
//	}
//
//
//
//	private static void writeJson(SootClass classToBeAnalyzed, List<SootField> inlineableFields) {
//		Container container = createContainer(classToBeAnalyzed,inlineableFields);
//		CommonUtils.writeToOutFile(container);
//		if(container != null) {
//			Gson gson = new GsonBuilder()
//					.setPrettyPrinting()
//					.create();
//			String jsonTxt = gson.toJson(container);
//			createJson(classToBeAnalyzed.getName(),jsonTxt);
//		}
//	}
//
//	private static Container createContainer(SootClass classToBeAnalyzed, List<SootField> inlineableFields) {
//		if(CommonUtils.isNotNull(inlineableFields)) {
//			List<InlineField> inlineFieldList = new ArrayList<>();
//			for(SootField field : inlineableFields) {
//				InlineField iField = new InlineField(field.getName(), field.getType().toQuotedString());
//				inlineFieldList.add(iField);
//			}
//			Container container = new Container(classToBeAnalyzed.getType().toQuotedString());
//			container.setInlineFieldList(inlineFieldList);
//			return container;
//		}
//		return null;
//	}
//
//	public static void createJson(String className, String jsonString) {
//		if(CommonUtils.isNotNull(className)) {
//			Path jsonFile = Paths.get(CommonConstants.OUT_JSON_FILE_PATH+File.separator+className+".json");
//			try (FileWriter fWriter = new FileWriter(jsonFile.toFile())){
//				fWriter.write(jsonString);
//				CompLLogManager.logInfoMessage("File "+className+".json written to path "+jsonFile.toString());
//			} catch (IOException ioexc) {
//				CompLLogManager.logErrorMessage("Exception occured: "+ioexc.getLocalizedMessage());
//			}
//		}
//	}
//
//	public static void clearDirectoryAndCreateNew() {
//		Path outPath = Paths.get(CommonConstants.OUT_JSON_FILE_PATH);
//		try {
//			if(outPath.toFile().exists()) {
//				FileUtils.cleanDirectory(outPath.toFile());
//			}else {
//				outPath.toFile().mkdirs();
//			}
//		} catch (IOException ioexc) {
//			CompLLogManager.logErrorMessage("Exception occured: "+ioexc.getLocalizedMessage());
//		}
//	}
}
	
