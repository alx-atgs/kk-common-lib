package kk.common.lib;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//import sun.misc.Launcher;

public class ServicesUtils {

	private static String result;

	public static void main(String args[]) {

		String ServiceName = "postgresql-x64-9.6";

		// String NewServiceName = "AAMyService";
		// String ServiceName = "AdobeARMservice";

		System.out.println("Проверка службы по имени \"" + ServiceName + "\" вернула: "
				+ ServicesUtils.checkServiceName(ServiceName));

		System.out.println("\nПопытка запустить службу ----------------------");
		System.out.println("текущий статус: " + ServicesUtils.getStatus(ServiceName));
		// boolean startsrv = ServicesUtils.startService(ServiceName);
		// System.out.println("При выполнении startService cлужба " + ServiceName + "
		// вернула: " + startsrv);
		// System.out.println("текущий статус: " +
		// ServicesUtils.getStatus(ServiceName));

		System.out.println("\nПопытка остановить службу ----------------------");
		System.out.println("текущий статус: " + ServicesUtils.getStatus(ServiceName));
		// boolean stopsrv = ServicesUtils.stopService(ServiceName);
		// System.out.println("При выполнении stopService cлужба " + ServiceName + "
		// вернула: " + stopsrv);
		System.out.println("текущий статус: " + ServicesUtils.getStatus(ServiceName));

	}

	/**
	 * Получить статус службы
	 * 
	 * @param ServiceName - имя службы
	 * @return строка с одним из 4 возможных статусов службы
	 */
	public static String getStatus(String ServiceName) {

		result = "";

		if (!ServicesUtils.checkServiceName(ServiceName)) {
			System.out.println("ErrorServicesUtils.getStatus: Службы с именем: " + ServiceName + " не найдено.");
			return result;
		}

		// String szCommand = "sc queryex " + ServiceName;
		String strOnlyEng = "cmd /c @CHCP 855>nul && cmd /c ";
		String szCommand = strOnlyEng + "sc queryex " + ServiceName;
		// String szEncoding = "Cp866";
		String szEncoding = "cp866";
		String input = WinCommandLine.backstring(szCommand, szEncoding);

		String status;
		String oslocal;
		if (input.contains("STATE")) {
			oslocal = "eng";
			status = "STATE";
		} else if (input.contains("Состояние")) {
			oslocal = "rus";
			status = "Состояние";
		} else {
			System.out.println("============");
			oslocal = "ErrorServicesUtils.getStatus: Не опознанна языковая локаль (ни Rus, ни Eng).";
			return result = oslocal;
		}

		try {
			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(input.getBytes());
			// read it with BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine();

			while (line != null) {
				// System.out.println(line);
				if (line.trim().startsWith(status)) {
					if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim()
							.equals("1"))
						result = "STOPPED";
					else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim()
							.equals("2"))
						result = "START_PENDING";
					// System.out.println("Starting....");
					else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim()
							.equals("3"))
						result = "STOP_PENDING";
					// System.out.println("Stopping....");
					else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim()
							.equals("4"))
						result = "RUNNING";
				}
				line = br.readLine();
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	/**
	 * Запуск службы по ее имени
	 * 
	 * @param ServiceName
	 * @return
	 */
	public static boolean startService(String ServiceName) {

		String CheckStatusBefore = ServicesUtils.getStatus(ServiceName);
		if (CheckStatusBefore.trim().equals("STOPPED")) {

			String szCommand = "sc start \"" + ServiceName + "\"";
			String szEncoding = "Cp866";

			if (WinCommandLine.backlogicAdm(szCommand, szEncoding)) {
				// если вернулось true, по ждем 2 сек и проверим статус
				DateTimeUtils.waitTime(2);
				String CheckStatusAfter = ServicesUtils.getStatus(ServiceName);
				if (CheckStatusAfter.trim().equals("RUNNING")) {
					return true;
				} else {
					// если вернулось false, подождем еще раз 2 сек и проверим статус
					DateTimeUtils.waitTime(2);
					String CheckStatusAfter2 = ServicesUtils.getStatus(ServiceName);
					if (CheckStatusAfter2.trim().equals("RUNNING")) {
						return true;
					}
				}
			} else {
				System.out.println("startServiceError: WinCommandLine.logicAdm вернула false");
			}
		} else {
			System.out.println("startServiceError: Служба не имеет статус STOPPED");
			// System.out.println("Служба не может быть запущена, т.к. не имеет статус
			// STOPPED");
		}
		return false;

	}

	/**
	 * Остановка службы по ее имени
	 * 
	 * @param ServiceName
	 * @return
	 */
	public static boolean stopService(String ServiceName) {

		String CheckStatusBefore = ServicesUtils.getStatus(ServiceName);
		if (CheckStatusBefore.trim().equals("RUNNING")) {

			String szCommand = "sc stop \"" + ServiceName + "\"";
			String szEncoding = "Cp866";

			if (WinCommandLine.backlogicAdm(szCommand, szEncoding)) {
				// если вернулось true, по ждем 2 сек и проверим статус
				DateTimeUtils.waitTime(2);
				String CheckStatusAfter = ServicesUtils.getStatus(ServiceName);
				if (CheckStatusAfter.trim().equals("STOPPED")) {
					return true;
				} else {
					// если вернулось false, подождем еще раз 2 сек и проверим статус
					DateTimeUtils.waitTime(2);
					String CheckStatusAfter2 = ServicesUtils.getStatus(ServiceName);
					if (CheckStatusAfter2.trim().equals("STOPPED")) {
						return true;
					}
				}
			} else {
				System.out.println("stopServiceError: WinCommandLine.logicAdm вернула false");
			}
		} else {
			System.out.println("stopServiceError: Служба не имеет статус RUNNING");
		}
		return false;
	}

	/**
	 * Проверяет существование службы
	 * 
	 * @param ServiceName - имя службы
	 * @return boolean - служба существует - true, иначе - false
	 */
	public static boolean checkServiceName(String ServiceName) {

		String szCommand = "sc queryex \"" + ServiceName + "\"";
		String szEncoding = "Cp866";
		boolean checkService = WinCommandLine.backlogic(szCommand, szEncoding);

		return checkService;
	}

	/**
	 * Создание службы (не полностью готов, служба создавалась, но не запускалась
	 * потом)
	 * 
	 * @param NewServiceName    - имя службы
	 * @param ExePathforService - полный путь к файлу или команда, которые нужны для
	 *                          функционирования службы
	 * @return истина, если не упали в процессе выполнения.
	 */
	public static String createService(String NewServiceName, String ExePathforService) {

		if (ExePathforService.contentEquals(null) || ExePathforService.isEmpty()) {
			System.out.println("Не задан аргумент: ExePathforService");
			return "false";
		}
		String szCommand = "sc create " + NewServiceName + " binPath= \"" + ExePathforService + "\" DisplayName= \""
				+ NewServiceName + "\" type= own start= auto";
		String szEncoding = "Cp866";
		System.out.println(szCommand);
		return WinCommandLine.backstring(szCommand, szEncoding);
	}

	/**
	 * Удаление службы (не проверялось)
	 * 
	 * @param ServiceName - имя службы
	 * @return истина, если не упали в процессе выполнения.
	 */
	public static String deleteService(String ServiceName) {

		String szCommand = "sc delete " + ServiceName;
		String szEncoding = "Cp866";
		System.out.println(szCommand);
		return WinCommandLine.backstring(szCommand, szEncoding);
	}

	/**
	 * ПРОВЕРИТЬ! Выше в методах пока не используется.
	 * 
	 * @param cls
	 * @return
	 */

	public static File getJarDir(Class<?> cls) {
		URL url;
		String extURL;

		try {
			url = cls.getProtectionDomain().getCodeSource().getLocation();
		} catch (SecurityException ex) {
			url = cls.getResource(cls.getSimpleName() + ".class");
		}

		extURL = url.toExternalForm();

		if (extURL.endsWith(".jar"))
			extURL = extURL.substring(0, extURL.lastIndexOf("/"));
		else {
			String suffix = "/" + (cls.getName()).replace(".", "/") + ".class";
			extURL = extURL.replace(suffix, "");
			if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
				extURL = extURL.substring(4, extURL.lastIndexOf("/"));
		}

		try {
			url = new URL(extURL);
		} catch (MalformedURLException mux) {
		}

		try {
			return new File(url.toURI());
		} catch (URISyntaxException ex) {
			return new File(url.getPath());
		}
	}

	/**
	 * ПРОВЕРИТЬ!
	 * 
	 */
	public static void name() {

		final String dirpath = "resources/";
		final File jarFile = new File("");
		System.out.println(jarFile.toString());

		if (jarFile.isFile()) { // Run with JAR file
			JarFile jar = null;
			try {
				jar = new JarFile(jarFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
			while (entries.hasMoreElements()) {
				final String name = entries.nextElement().getName();
				if (name.startsWith(dirpath + "/")) { // filter according to the path
					System.out.println("Files list:\n" + name);
				}
			}
			try {
				jar.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // Run with IDE
			System.out.println(dirpath);
//		final URL url = Launcher.class.getResource("/" + dirpath);
			final URL url = AppStartUpPath.class.getResource("/" + dirpath);
			System.out.println(url.toString());
			if (url != null) {
				try {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
						System.out.println("Files list:\n" + app);
					}
				} catch (URISyntaxException ex) {
					// never happens
				}
			}
		}
	}

}