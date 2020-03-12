/**
 * 
 */
package kk.common.lib;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alex
 *
 */
public class PropertiesFileTest {

	static String fileName = "config.properties";

	public static void main(String[] args) throws IOException {

		String PropsFileName = null;

		System.out.println("Вывод полного пути файла " + fileName + ":");

		// определяем путь для заданного файла в корне проекта
		String PropsFileRootPath = AppStartUpPath.getDir() + File.separator + fileName;
		// определяем путь для заданного файла из Resources
		String PropsFileResPath = new PropertiesFile().getFileFromResource(fileName).getPath();

		if (!Check.file(PropsFileRootPath)) {
			System.out.println("Файл в корне проекта не найден: " + PropsFileRootPath);
		} else {
			PropsFileName = PropsFileRootPath;
			System.out.println("Путь до файла в корне проекта: " + PropsFileName);
		}
		if (!Check.file(PropsFileResPath)) {
			System.out.println("Файл в папке Resources не найден: " + PropsFileResPath);
		} else {
			PropsFileName = PropsFileResPath;
			System.out.println("Путь до файла в папке Resources: " + PropsFileName);
		}
		System.out.println("------------");

		System.out.println("Вывод из файла " + PropsFileName + " списка всех ключей:");
		List<String> keys = PropertiesFile.getListKeys(PropsFileName);
		System.out.println(keys);
		for (String key : keys) {
			System.out.print(key + " ");
		}
		System.out.println("\n------------");

		System.out.println("Вывод из файла " + PropsFileName + " всех ключей и их значений:");
		PropertiesFile.printAllprops(PropsFileName);
		System.out.println("------------");

		System.out.println("Вывод заданного ключа из файла " + PropsFileName + ":");
		// String key1 = "xmldeclaration";
		String key1 = "ps.servicename.1";
		System.out.println(key1 + " = " + PropertiesFile.getProps(PropsFileName).getProperty(key1));
		System.out.println("------------");

		System.out.println("Вывод ключей и их значений из файла " + PropsFileName
				+ ", если в нем есть ключи вида: key0, key1... и т.д.");
		// String key = "xsd.schema";
		String key = "ps.ipserver";
		List<String> StringProps = PropertiesFile.getStringProps(PropsFileName, key);
		System.out.println("Всего элементов: " + StringProps.size());
		for (int i = 0; i < StringProps.size(); i++) {
			System.out.println(key + "." + i + " = " + StringProps.get(i));
		}
		System.out.println("------------");
		// Вывод в консоль содержимого файла
		// FileUtil.printFile(fn);

		System.out.println("Following are the JVM information of your OS :");
		// printSystemProperties();
		System.out.println("------------");

		String keylist = "ps.listvalue";
		String[] tt = PropertiesFile.getProps(PropsFileName).getProperty(keylist).split(",");
		for (int i = 0; i < tt.length; i++) {
			System.out.println("keylist = " + tt[i].trim());
		}

		// PropertiesFile.printSystemProps();
		// System.out.println("------------");
		// PropertiesFile.getSystemPath();

	}

}
