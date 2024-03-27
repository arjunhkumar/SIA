/**
 * 
 */
package in.ac.iitmandi.compl.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author arjun
 *
 */
public class CompLLogManager {
	
	private static final String LOG4J2_XML_PATH = "resources/log4j2.xml";
	private static final String LOG4J_SYS_PROP = "log4j.configurationFile";
	private Logger LOGGER = null;
	private Logger OUT_LOGGER = null;
	static CompLLogManager logInstance = null;
	
	private CompLLogManager() {
	}
	
	
	public static synchronized CompLLogManager getInstance() {
		if(null==logInstance) {
			logInstance = new CompLLogManager();
		}
		return logInstance;
	}
	
	public static synchronized void configureLogger() {
		System.setProperty(LOG4J_SYS_PROP,LOG4J2_XML_PATH);
		getInstance().setLogger(LogManager.getLogger("in.ac.iitmandi.compl.Log4jConfigurer"));
		getInstance().setOutLog(LogManager.getLogger("in.ac.iitmandi.compl.Log4jConfigurerOut"));
		getInstance().getOutLog().info("Logger intialized");
		getInstance().getLogger().info("Configuration File: "+System.getProperty(LOG4J_SYS_PROP));
		getInstance().getLogger().info("Logger intialized.");
	}
	
	public Logger getOutLog() {
		return OUT_LOGGER;
	}
	
	public Logger getLogger() {
		return LOGGER;
	}
	
	
	public void setOutLog(Logger outLogger) {
		this.OUT_LOGGER = outLogger;
	}
	
	public void setLogger(Logger mainLogger) {
		this.LOGGER = mainLogger;
	}
	
	public static synchronized void  logInfoMessage(String msg) {
		if(CommonUtils.isNotNull(msg)) {
			getInstance().getLogger().info(msg);
		}
	}
	
	public static synchronized void logInfoAndOutMessage(String msg) {
		if(CommonUtils.isNotNull(msg)) {
			getInstance().getLogger().info(msg);
			getInstance().getOutLog().info(msg);
		}
	}
	
	
	public static synchronized void logErrorMessage(String msg) {
		if(CommonUtils.isNotNull(msg)) {
			getInstance().getLogger().error(msg);
		}
	}
	
	public static synchronized void logDebugMessage(String msg) {
		if(CommonUtils.isNotNull(msg)) {
			getInstance().getLogger().debug(msg);
		}
	}
	
	public static synchronized void writeOut(String msg) {
		if(CommonUtils.isNotNull(msg)) {
			getInstance().getOutLog().info(msg);
		}
	}
}
