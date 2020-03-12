package kk.common.lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * Класс позволяет настраивать вывод строк на консоль и в лог файл. *
 * Использование: LogApp.info("any text message);
 * 
 * Устанавливаем значения входящих параметров:
 * LogApp.setPathLogFile("FullPathToLogFile") - полный путь к файлу с логом;
 * LogApp.setLogFormat("txt_or_xml") - формат вывода, по-умолчанию - текст;
 * LogApp.setOwnconsole(true or false) - вывод только в лог (false) или также на
 * консоль (true), по-умолчанию false; * LogApp.setAddInLog(true or false) -
 * добавить в существующий лог или все время писать в новый.
 * 
 * @version 1.0 02 14 Oct 2019
 * 
 * @author Alexander Kuznetsov
 *
 */

public class LogApp {
	private static FileHandler fh;
	private static Logger logger = Logger.getLogger(LogApp.class.getName());
	private static String pathLogFile;
	/** logFormat - if to write "xml" then xml format, else text, default text */
	private static String logFormat = "txt";
	private static boolean ownconsole = false;
	private static boolean addinlog = false;

	public static void LogAppSetting(String pathLogFile, String logFormat, boolean ownconsole, boolean addinlog) {

		// установка формата вывода в Logger в виде:
		// 2018-11-16 15:55:51 ClassName:[ru.atgs.test.Test_SettingLogger]
		// MethodName:[main] Level:[INFO]
		Formatter formatter = new Formatter() {

			@Override
			public String format(LogRecord arg0) {
				StringBuilder b = new StringBuilder();
				// b.append(new Date());
				b.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				b.append(" ClassName:[");
				b.append(arg0.getSourceClassName());
				// b.append("] MethodName:[");
				// b.append(arg0.getSourceMethodName());
				b.append("] Level:[");
				b.append(arg0.getLevel());
				b.append("]\n");
				b.append(System.getProperty("line.separator"));
				b.append(arg0.getMessage());
				b.append(System.getProperty("line.separator"));
				return b.toString();
			}
		};

		LogManager.getLogManager().reset();
		Logger globalLogger = Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(java.util.logging.Level.OFF);

		LogManager lm = LogManager.getLogManager();
		lm.addLogger(logger);

		logger.setLevel(Level.ALL);
		// Creating FileHandler.
		// False - logfile каждый раз новый, true - дописывает в старый
		try {
			fh = new FileHandler(getPathLogFile(), isAddInLog());
			fh.setFormatter(formatter);
			logger.addHandler(fh);
			// Если LogFormat = "xml", то лог - в xml, иначе - текст
			if (getLogFormat().toUpperCase().toLowerCase().trim().equals("xml")) {
				setLogFormat(logFormat);
				fh.setFormatter(new XMLFormatter());
			}
		} catch (Throwable exception) {
			logger.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}

		if (isOwnconsole()) {
			// Creating ConsoleHandler.
			try {
				Handler ch = new ConsoleHandler();
				ch.setFormatter(formatter);
				logger.addHandler(ch);
			} catch (Throwable exception) {
				logger.log(Level.SEVERE, "Error occur in ConsoleHandler.", exception);
			}
		}

	}

	public static void info(String MessageTxt) {

		LogAppSetting(getPathLogFile(), getLogFormat(), isOwnconsole(), isAddInLog());
		logger.log(Level.INFO, MessageTxt);

		// fh.flush();
		// fh.close();
	}

	public static void warning(String MessageTxt) {
		LogAppSetting(getPathLogFile(), getLogFormat(), isOwnconsole(), isAddInLog());
		logger.log(Level.WARNING, MessageTxt);
	}

	public static void severe(String MessageTxt) {

		LogAppSetting(getPathLogFile(), getLogFormat(), isOwnconsole(), isAddInLog());
		logger.log(Level.SEVERE, MessageTxt);
	}

	// getters and setters
	public static void setPathLogFile(String pathLogFile) {
		// TODO: сделать проверку входящей строки
		LogApp.pathLogFile = pathLogFile;
	}

	public static String getPathLogFile() {
		return pathLogFile;
	}

	public static void setLogFormat(String logFormat) {
		LogApp.logFormat = logFormat;
	}

	public static String getLogFormat() {
		return logFormat;
	}

	public static void setOwnconsole(boolean ownconsole) {
		LogApp.ownconsole = ownconsole;
	}

	public static boolean isOwnconsole() {
		return ownconsole;
	}

	public static void setAddInLog(boolean addinlog) {
		LogApp.addinlog = addinlog;
	}

	public static boolean isAddInLog() {
		return addinlog;
	}
}
