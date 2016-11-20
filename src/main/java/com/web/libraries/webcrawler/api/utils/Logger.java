package com.web.libraries.webcrawler.api.utils;

/**
 * Logger utility class
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @since Java 1.8
 */
public class Logger {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);

	/**
	 * Log messages with an information severity
	 * @param text Message text
	 * @param owner Class that has required the logging
	 */
	public static final void info(String text, final Class<?> owner) {
		logger.info("Log for " + owner.getCanonicalName() + " :: " + text);
	}
	/**
	 * Log messages with a warning severity
	 * @param text Message text
	 * @param owner Class that has required the logging
	 */
	public static final void warn(String text, final Class<?> owner) {
		logger.warn("Log for " + owner.getCanonicalName() + " :: " + text);
	}
	/**
	 * Log messages with a debug severity
	 * @param text Message text
	 * @param owner Class that has required the logging
	 */
	public static final void debug(String text, final Class<?> owner) {
		logger.debug("Log for " + owner.getCanonicalName() + " :: " + text);
	}
	/**
	 * Log messages with an error severity
	 * @param text Message text
	 * @param owner Class that has required the logging
	 */
	public static final void error(String text, final Class<?> owner) {
		logger.error("Log for " + owner.getCanonicalName() + " :: " + text);
	}
	/**
	 * Log messages and cause exceptions with an error severity
	 * @param text Message text
	 * @param exception Exception to be reported as root cause 
	 * @param owner Class that has required the logging
	 */
	public static final void error(String text, Throwable exception, final Class<?> owner) {
		logger.error("Log for " + owner.getCanonicalName() + " :: " + text, exception);
	}
} 
