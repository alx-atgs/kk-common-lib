package kk.common.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class PropertiesFile {

	public File getFileFromResource(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}
	}

	public InputStream getStreamFromResource(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		return inputStream;
	}

	/**
	 * 
	 * Считываем содержимое из задаваемого файла Properties
	 * 
	 * @param PropsFileName - задаем имя файла
	 * @return Возвращаем набор ключей и значений, из которого потом можем брать
	 *         нужные.
	 * 
	 */
	public static Properties getProps(String PropsFileFullPath) {

		Properties defaultProps = new Properties();
		// создаем поток для чтения из файла
		FileInputStream input = null;
		try {
			input = new FileInputStream(PropsFileFullPath);
			// загружаем свойства
			defaultProps.load(input);
		} catch (IOException ex) {
			// ex.printStackTrace();
			ex.getMessage();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// e.printStackTrace();
					e.getMessage();
				}
			}
		}
		return defaultProps;
	}

	/**
	 * Return array by all keys from properties file.
	 * 
	 * @param PropsFileFullPath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getListKeys(String PropsFileFullPath) throws IOException {

		List<String> result = new LinkedList<>();
		// defining variable for assignment in loop condition part
		Properties defaultProps = PropertiesFile.getProps(PropsFileFullPath);
		Enumeration<?> e = defaultProps.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			// System.out.println("Key : " + key);
			result.add(key);
		}
		return result;
	}

	/**
	 * Return array from properties file. Array must be defined as "key.0=value0",
	 * "key.1=value1", ...
	 * 
	 * @throws IOException
	 */
	public static List<String> getStringProps(String PropsFileFullPath, String key) throws IOException {

		List<String> result = new LinkedList<>();
		// defining variable for assignment in loop condition part
		String value;
		Properties defaultProps = getProps(PropsFileFullPath);
		// next value loading defined in condition part
		for (int i = 0; (value = defaultProps.getProperty(key + "." + i)) != null; i++) {
			result.add(value);
		}
		return result;
	}

	/**
	 * Вывод всех ключей и их значений из файла Properties/
	 * 
	 * @param PropsFileName - задаем имя файла
	 */
	public static void printAllprops(String PropsFileFullPath) throws FileNotFoundException, IOException {

		Properties defaultProps = getProps(PropsFileFullPath);
		Enumeration<?> e = defaultProps.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = defaultProps.getProperty(key);
			System.out.println("Key : " + key + ", Value : " + value);
		}

	}

	/**
	 * Вывод списка всех переменных и их значений для текущей OS. Без сортировки
	 * (метод с сортировкой: printSystemProperties)
	 * 
	 */
	public static void printSystemProps() {

		System.out.println("Following are the JVM information of your OS :");
		System.out.println("");

		// Property Object
		Properties jvm = System.getProperties();
		jvm.list(System.out);
	}

	/**
	 * Вывод списка всех переменных и их значений для текущей OS. То же, что и
	 * printSystemProps, но с отсортированным выводом.
	 * 
	 * Use of getProperties() method System class refers to the JVM on which you are
	 * compling your JAVA code getProperty fetches the actual properties that JVM on
	 * your System gets from your Operating System
	 * 
	 */
	public static void printSystemProperties() {

		Properties p = System.getProperties();

		// Sort:
		Map<String, String> m = new TreeMap(p);

		// Print sorted system properties:
		for (String key : m.keySet()) {
			String value = p.getProperty(key);
			if ("\n".equals(value)) {
				value = "\\n";
			} else if ("\r".equals(value)) {
				value = "\\r";
			} else if ("\r\n".equals(value)) {
				value = "\\r\\n";
			}
			System.out.println(key + " = " + value);
		}

	}

	public static void getSystemPath() {

		System.out.println("Path information of current OS :");
		System.out.println("");

		String property = System.getProperty("java.library.path");
		StringTokenizer parser = new StringTokenizer(property, ";");
		while (parser.hasMoreTokens()) {
			System.err.println(parser.nextToken());
		}
	}

}
