package kk.common.lib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Alexander Kuznetsov
 */
public class AppConfig {

	// имя файла который должен находиться в /src/main/resources (там его можно
	// создавать автоматически через Gradle). В нем должны быть поля:
	// applicationName = [имя приложения]
	// version = [номер текущей версии]
	private static String VarAppFileNameProperties = "app.properties";

	private String AppPropsFileFullPath;

	public String appPropertiesFile;
	public String kkCommonLibFileName;
	public String pgJarFileName;
	public String soapJarFileName;
	public String changePgRoleJarFileName;
	public String csStatusJarFileName;

	public String defaultEncoding;

	public String logFilePath;
	public String logFormat;
	public boolean logOwnconsole;
	public boolean logAddInLog;
	public boolean debugToLog;

	public boolean checkAppName = false;

	public AppConfig() {

		// по-умолчанию, имена файлов properties и log для приложения формируется из
		// имени приложения и соответствующего суффикса
		String DefaultAppPropsFileName = replaceSpaceToUnderscores(getAppName()).toLowerCase() + ".properties";
		String DefaultAppLogFileName = "_" + replaceSpaceToUnderscores(getAppName()).toLowerCase() + ".log";
		String DefaultEncoding = "UTF-8";

		// String AppPath = AppStartUpPath.getDir() + File.separator;
		// System.out.println("DefaultAppLogFileName: " + DefaultAppLogFileName);
		// System.out.println("DefaultAppPropsFileName: " + DefaultAppPropsFileName);
		// System.out.println("-------------------------------------");

		// здесь список тех приложений, для которых не нужен файл properties, например
		// kk-common-lib. Далее, в цикле каждый элемент списка проверяется с именем
		// текущего приложения и если имя приложения встречается в списке, то переменной
		// checkAppname присваивается true
		String[] listapp4notprops = { "qwertyuiop1", "kk-common-lib", "qwertyuiop2" };
		for (int i = 0; i < listapp4notprops.length; i++)
			if (getAppName().toLowerCase().trim().equals(listapp4notprops[i].toLowerCase().trim())) {
				checkAppName = true;
			}
		// System.out.println("checkAppname = " + checkAppname);

		// если checkAppname = false, то проверяем наличие файла properties и считываем
		// его элементы
		if (!checkAppName) {
			if (Check.file(DefaultAppPropsFileName)) {
				File pfn = new File(DefaultAppPropsFileName);
				AppPropsFileFullPath = pfn.getAbsolutePath();
			} else {
				LogApp.setPathLogFile(AppStartUpPath.getDir() + File.separator + DefaultAppLogFileName);
				LogApp.setLogFormat("txt");
				LogApp.setOwnconsole(true);
				LogApp.setAddInLog(true);
				LogApp.info("ErrorAppConfig: Properties file: \"" + new File(DefaultAppPropsFileName).getAbsolutePath()
						+ "\" NOT EXIST.\n");
				System.exit(2);
			}

			// загружаем содержимое VarAppFileNameProperties
			InputStream inputStream = new PropertiesFile().getStreamFromResource(VarAppFileNameProperties);
			Properties appProps = new Properties();
			try {
				appProps.load(inputStream);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			// проверяем наличие элементов с key = "jarFileName"
			String key = "jarFileName";
			String value;
			List<String> listFileName = new ArrayList<String>();
			// выбираем все ключи вида: [key].0, [key].1 и т.д
			for (int i = 0; (value = appProps.getProperty(key + "." + i)) != null; i++) {
				// System.out.println(key + "." + i + " = " value));
				listFileName.add(value);
			}
			// проверяем на существование файлы, указанные в VarAppFileNameProperties
			// System.out.println("Вывод ключей и их значений из файла " +
			// VarAppFileNameProperties
			// + ", если в нем есть ключи вида: key0, key1... и т.д.");
			// System.out.println("Всего элементов: " + listFileName.size());
			for (int i = 0; i < listFileName.size(); i++) {
				String checkFileName = listFileName.get(i).trim();

				if (!Check.file(checkFileName)) {
					LogApp.setPathLogFile(AppStartUpPath.getDir() + File.separator + DefaultAppLogFileName);
					LogApp.setLogFormat("txt");
					LogApp.setOwnconsole(true);
					LogApp.setAddInLog(true);
					LogApp.info("ErrorAppConfig: File: \"" + new File(checkFileName).getAbsolutePath()
							+ "\" is NOT EXIST.\n");
					System.exit(2);
				}

				// если проверка пройдена, то присваиваем значения
				String getFileName = checkFileName.toUpperCase().toLowerCase().trim();
				// System.out.println("getFileName = " + getFileName);
				if (getFileName.contains("kk-common-lib")) {
					kkCommonLibFileName = checkFileName;
				} else if (getFileName.contains("postgresqlmanager")) {
					this.pgJarFileName = checkFileName;
				} else if (getFileName.contains("soapclientsaaj")) {
					this.soapJarFileName = checkFileName;
				} else if (getFileName.contains("changepgrole")) {
					this.changePgRoleJarFileName = checkFileName;
				} else if (getFileName.contains("csstatus")) {
					this.csStatusJarFileName = checkFileName;
				} else {
					System.out.println("ErrorAppConfig: In VarAppFileNameProperties nothing NOT founded.");
				}
			}

			/*
			 * System.out.println("kkCommonLibFileName = " + kkCommonLibFileName);
			 * System.out.println("pgJarFileName = " + pgJarFileName);
			 * System.out.println("soapJarFileName = " + soapJarFileName);
			 * System.out.println("changePgRoleJarFileName = " + changePgRoleJarFileName);
			 * System.out.println("csStatusJarFileName = " + csStatusJarFileName);
			 */

			Properties pf = PropertiesFile.getProps(AppPropsFileFullPath);

			this.appPropertiesFile = DefaultAppPropsFileName;

			this.defaultEncoding = pf.getProperty("encoding", DefaultEncoding);

			this.logFilePath = pf.getProperty("logFileName", DefaultAppLogFileName);
			this.logFormat = pf.getProperty("logFormat", "txt");
			// если boolean, то нужно ставить !=null
			this.logOwnconsole = pf.getProperty("logOwnconsole") != null;
			this.logAddInLog = pf.getProperty("logAddInLog") != null;
			this.debugToLog = pf.getProperty("debugToLog") != null;

		}
	}

	/**
	 * Вывод имени приложения и ее версии
	 */
	public static void printAppVersion() {
		System.out.println("Application Name: " + getAppName().trim());
		System.out.println("Version: " + getVersion());
	}

	/**
	 * Вывод на экран содержимого файла помощи - [имя приложения].help
	 * 
	 * @param szEncoding
	 */
	public static void printAppHelp(String szEncoding) {
		printAppVersion();
		String helpFileName = replaceSpaceToUnderscores(getAppName()) + ".help";
		System.out.println("Help File: " + helpFileName);
		System.out.println("-----------------------------------------");
		String txtHelp = ObjUtil.getContentAsString(helpFileName, szEncoding);
		System.out.println(txtHelp);
	}

	/**
	 * Получаем значение из VarAppFileNameProperties, который должен находиться в
	 * /resources (можно создавать автоматически в Gradle). После создания jar к
	 * этому файлу нет доступа как к файлу, поэтому используем здесь
	 * getStreamFromResource и тогда имеем возможность получить значения
	 * 
	 * @return String
	 */
	public static String getAppName() {

		String strVersion = null;

		InputStream inputStream = new PropertiesFile().getStreamFromResource(VarAppFileNameProperties);

		Properties defaultProps = new Properties();
		try {
			defaultProps.load(inputStream);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		strVersion = defaultProps.getProperty("applicationName");
		// System.out.println("version = " + strVersion);
		return strVersion;

	}

	/**
	 * Получаем значение из VarAppFileNameProperties, который должен находиться в
	 * /resources (можно создавать автоматически в Gradle). После создания jar к
	 * этому файлу нет доступа как к файлу, поэтому используем здесь
	 * getStreamFromResource и тогда имеем возможность получить значения
	 * 
	 * @return String
	 */
	public static String getVersion() {

		String strVersion = null;

		InputStream inputStream = new PropertiesFile().getStreamFromResource(VarAppFileNameProperties);

		Properties defaultProps = new Properties();
		try {
			defaultProps.load(inputStream);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		strVersion = defaultProps.getProperty("version");
		// System.out.println("version = " + strVersion);
		return strVersion;

	}

	/**
	 * Замена во входящей строке всех последовательных пробелов на один и его замена
	 * на символ "подчеркивание". Полезно при обработке имен файлов с пробелами.
	 * 
	 * В приведенном ниже коде регулярное выражение (^ +) используется для поиска
	 * всех пробелов в начале строки. Символ '^' указывает на начало строки с
	 * последующим ' +'.
	 * 
	 * Во второй части регулярного выражения используется ( +$) для поиска пробелов
	 * начиная с последнего символа.
	 * 
	 * В третьей части <space>(?= )соответствует пробелу, за которым следует другой
	 * пробел. Таким образом, в последовательных пробелах он будет соответствовать
	 * всем пробелам, кроме последнего, потому что за ним не следует пробел.
	 * 
	 * Объединяет эти условия в одном регулярном выражении знак '|'.
	 * 
	 * Далее, все пробелы заменяются на символ подчеркивания.
	 * 
	 * @param inputString
	 * @return String - строка, в которой удалены пробелы в начале, в конце и более
	 *         одного последовательных пробелов заменены на один пробел. Далее, все
	 *         пробелы заменяются на символ подчеркивания
	 */

	private static String replaceSpaceToUnderscores(String inputString) {

		String outputString = inputString.replaceAll("^ +| +$| (?= )", "").replaceAll(" ", "_");

		return outputString;
	}

	private static void TestDelSpaceSimbols(String inputString) {
		String[] tests = { "  x  ", // [x]
				"  1   2   3  ", // [1 2 3]
				"", // []
				"1   ", // []
				"    ab    cd    ", //
				"   a    abcd    jhg  ",//
		};
		for (String test : tests) {
			System.out.format("[%s]%n", test.replaceAll("^ +| +$| (?= )", ""));
			System.out.format("[%s]%n", test.replaceAll("^ +| +$| (?= )", "").replaceAll(" ", "_"));
		}
	}

	// недоделанная проверка для случая перечисления значений в массиве
	public void checkPropsElement() {
		List<String> listkeys = Arrays.asList("psJarFileName", "soapJarFileName", "soapPropertiesFileName",
				"soapEndpointUrl", "soapAction", "soapxml", "statusSQLquery", "logFileName", "logFormat",
				"logOwnconsole", "logAddInLog", "debugToLog");
		for (String key : listkeys) {
			if (key == null) {

			}
		}
	}

	// Недоделанный вывод значений переменных из конфига
	public static StringBuffer configInfo() {

		StringBuffer sb = new StringBuffer();
		sb.append("Current config info:\n");
		sb.append("-------------------" + "\n");
		// sb.append("psJarFileFullPath = " + pgJarFileFullPath + "\n");
		// sb.append("soapJarFileFullPath = " + soapJarFileFullPath + "\n");
		sb.append("-------------------\n");
		return sb;
	}

}
