package kk.common.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class WinCommandLine {

	// путь файловой системы, откуда выполняется запуск класса
	private static String AppPath = AppStartUpPath.getDir();
	// cmd.lnk - ярлык на файл C:\Windows\SysWOW64\cmd.exe в 64-разрядной Windows,
	// который настроен на запуск от Администратора. Без него не хватает прав для
	// выполнения операций со службами.
	static final String cmdFile = AppPath + File.separator + "cmd.lnk";

	private static String szCommand;
	private static String szEncoding;
	// Кодировка может быть по написанию именно: "866", "Windows-1251", "UTF8"

	public static void main(String args[]) {

		szCommand = "cmd /c chcp 855 1>null && cmd /c dir";
		// String jarPath =
		// "D:/Work/WorkProject/AtgsApp/PostgresService/1.1/PostgresqlService-1.1.01.jar";
		String szEncoding = "cp866";
//szEncoding = "Cp866";
		// szEncoding = "cp1251";
		// szCommand = "java -jar " + jarPath + " version";

		// Locale.setDefault(new Locale("en", "US"));
		System.out.println(WinCommandLine.backstring(szCommand, szEncoding));
		System.out.println("========================");
		// System.out.println(WinCommandLine.backlogic(szCommand, szEncoding));

		// String ServiceName = "postgresql-x64-9.6";

		// String a = "sc queryex \"" + ServiceName + "\"";
		// System.out.println(WinCommandLine.runAs(a, szEncoding));
		// System.out.println("------------------------");
		// System.out.println(WinCommandLine.backlogic(a, szEncoding));
		System.out.println("========================");

		// String b = "sc stop \"" + ServiceName + "\"";
		// System.out.println("Stop: " + WinCommandLine.backlogicAdm(b, szEncoding));
		// String c = "sc start \"" + ServiceName + "\"";
		// System.out.println("Start: " + WinCommandLine.backlogicAdm(c, szEncoding));
		// System.out.println("========================");

	}

	/**
	 * Возвращает результат выполнения командной строки как Boolean. Не все команды
	 * одинаково определяют и возвращают ошибки, но многие. Поэтому, нужно
	 * тестировать каждую команду, на корректность возврата.
	 * 
	 * 
	 * @param szCommand
	 * @param szEncoding
	 * @return boolean
	 */
	public static boolean backlogic(String szCommand, String szEncoding) {

		String bCommand = "cmd /c 2>nul >nul " + szCommand + "&& echo true||echo false";

		String resultString = WinCommandLine.backstring(bCommand, szEncoding);
		if (resultString.trim().equals("true")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Возвращает результат выполнения командной строки как Boolean с помощью ярлыка
	 * с правами администратора. Не все команды одинаково определяют и возвращают
	 * ошибки, но многие. Поэтому, нужно тестировать каждую команду, на корректность
	 * возврата.
	 * 
	 * 
	 * @param szCommand
	 * @param szEncoding
	 * @return boolean
	 */
	public static boolean backlogicAdm(String szCommand, String szEncoding) {

		if (!Check.file(cmdFile)) {
			System.out.println("Файл: " + cmdFile + " не найден. Невозможно выполнить команду.");
			return false;
		}

		String cmdString = "cmd /c " + AppPath + File.separator + "cmd.lnk /c ";
		String bCommand = cmdString + " 2>nul >nul " + szCommand + " && echo true||echo false";

		// System.out.println("logicAdmError:" + bCommand);

		String resultString = WinCommandLine.backstring(bCommand, szEncoding);
		// System.out.println("logicAdm: resultString = " + resultString);

		if (resultString.trim().equals("true")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Выполнить команду операционной системы с помощью ярлыка с правами
	 * администратора. Не все команды могут выполняться, необходима проверка. Нужно
	 * предварительно тестировать каждую команду, на корректность выполнения.
	 * 
	 * @param szCommand
	 * @param szEncoding
	 * @return String
	 */
	public static String backstringAdm(String szCommand, String szEncoding) {

		String Result = "false";

		if (!Check.file(cmdFile)) {
			System.out.println("Файл: " + cmdFile + " не найден. Невозможно выполнить команду.");
			return Result;
		}
		String cmdString = "cmd /c " + AppPath + File.separator + "cmd.lnk /c ";
		String szCommandAdm = cmdString + szCommand;

		BufferedReader in = null;
		BufferedReader err = null;
		try {
			Process p = Runtime.getRuntime().exec(szCommandAdm);
			in = new BufferedReader(new InputStreamReader(p.getInputStream(), szEncoding));
			err = new BufferedReader(new InputStreamReader(p.getErrorStream(), szEncoding));

			String szLine = null;
			StringBuilder theBuffer = new StringBuilder();
			// theBuffer.append("\nExecute runtime: ");
			// \\theBuffer.append("\"" + szCommand + "\"\n");
			// theBuffer.append("\nOutput message is: \n");
			// theBuffer.append("----------------------\n");
			// theBuffer.append("\n");
			while ((szLine = in.readLine()) != null)
				theBuffer.append(szLine + "\n");
			// theBuffer.append("----------------------\n");
			// theBuffer.append("\nError message is: ");
			while ((szLine = err.readLine()) != null)
				theBuffer.append(szLine + " ");

			Result = theBuffer.toString();
			// System.out.println(theBuffer.toString());
			// log().info(theBuffer.toString());
		} catch (IOException e) {
			System.out.println("Can't execute " + szCommand + " Exception: " + e.getMessage());
			// Result = "false";
			// log().error("Can't execute " + szCommand + " Exception: " + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException Ex) {
				Ex.printStackTrace();
			}
			try {
				if (err != null)
					err.close();
			} catch (IOException Ex) {
				Ex.printStackTrace();
			}
		}
		return Result;

	}

	/**
	 * Выполнить команду операционной системы
	 * 
	 * @param szCommand  - сама команда
	 * @param szEncoding - кодировка, в которой выводится сообщение в консоль ОС
	 * @return String - строка.
	 */
	public static String backstring(String szCommand, String szEncoding) {
		String Result = "";
		BufferedReader in = null;
		BufferedReader err = null;
		try {
			Process p = Runtime.getRuntime().exec(szCommand);
			in = new BufferedReader(new InputStreamReader(p.getInputStream(), szEncoding));
			err = new BufferedReader(new InputStreamReader(p.getErrorStream(), szEncoding));

			String szLine = null;
			StringBuilder theBuffer = new StringBuilder();
			// theBuffer.append("\nExecute runtime: ");
			// \\theBuffer.append("\"" + szCommand + "\"\n");
			// theBuffer.append("\nOutput message is: \n");
			// theBuffer.append("----------------------\n");
			// theBuffer.append("\n");
			while ((szLine = in.readLine()) != null)
				theBuffer.append(szLine + "\n");
			// theBuffer.append("----------------------\n");
			// theBuffer.append("\nError message is: ");
			while ((szLine = err.readLine()) != null)
				theBuffer.append(szLine + " ");

			Result = theBuffer.toString();
			// System.out.println(theBuffer.toString());
			// log().info(theBuffer.toString());
		} catch (IOException e) {
			System.out.println("ErrorWinCommandLine: Can't execute " + szCommand + " Exception: " + e.getMessage());
			Result = "false";
			// log().error("Can't execute " + szCommand + " Exception: " + e.getMessage());
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException Ex) {
				Ex.printStackTrace();
			}
			try {
				if (err != null)
					err.close();
			} catch (IOException Ex) {
				Ex.printStackTrace();
			}
		}
		return Result;
	}

	public static void icacls(String FolderFullPath, String UserName, String AccessRight) {

		String szCommand = "icacls.exe \"" + FolderFullPath + "\" /grant \"" + UserName + "\":" + AccessRight;
		String szEncoding = "cp1251";
		WinCommandLine.backstring(szCommand, szEncoding);
	}

	public static void folderFullAccess(String FolderFullPath, String UserName) {
		String AccessRight = "(OI)(CI)(F)";
		WinCommandLine.icacls(FolderFullPath, UserName, AccessRight);

	}

}