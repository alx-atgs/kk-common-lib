/**
 * 
 */
package kk.common.lib;

/**
 * @author Alexander Kuznetsov
 * @author Pavel Komarov
 *
 */
public class CommonLibMain {
	private static String appPropertiesFile;
	private static String szEncoding;
	private static boolean setDebug;
	// конструктор для сбора текста в лог
	private static StringBuffer sbLog = new StringBuffer();

	/**
	 * Присваиваем значения из AppConfig в отдельном методе, чтобы не делать этого
	 * непосредственно в коде.
	 * 
	 * @param config
	 */
	public CommonLibMain(AppConfig config) {
		// задаем установки логгера
		LogApp.setPathLogFile(config.logFilePath);
		LogApp.setLogFormat(config.logFormat);
		LogApp.setOwnconsole(config.logOwnconsole);
		LogApp.setAddInLog(config.logAddInLog);
		setDebug = config.debugToLog;

		// читаем остальные значения из properties
		appPropertiesFile = config.appPropertiesFile;
		szEncoding = config.defaultEncoding;
	}

	public static void main(String[] args) {

		// загрузим конфигурацию, чтобы можно было прочитать значения из properties
		AppConfig config = new AppConfig();
		new CommonLibMain(config);

		if (args.length != 1 || args.equals(null)) {
			printUsage();
			return;
		}

		String arg_0 = args[0].toUpperCase().toLowerCase().trim();
		if (arg_0.equals("run")) {
			// тут ничего не делаем, если аргумент из перечисленных выше
		} else {
			if (arg_0.equals("version") || arg_0.equals("-v") || arg_0.equals("help") || arg_0.equals("-h")) {
				if (arg_0.equals("-v") || arg_0.equals("version")) {
					AppConfig.printAppVersion();
					return;
				}
				if (arg_0.equals("-h") || arg_0.equals("help")) {
					AppConfig.printAppHelp(szEncoding);
					return;
				}
			} else {
				printUsage();
				return;
			}
		}

		if (arg_0.equals("run")) {
			printUsage();
		}
	}

	private static void printUsage() {
		// System.out.println("Usage : java -jar " + AppConfig.getAppName() + "-" +
		// AppConfig.getVersion() + ".jar <command>\n");
		AppConfig.printAppVersion();
	}

}
