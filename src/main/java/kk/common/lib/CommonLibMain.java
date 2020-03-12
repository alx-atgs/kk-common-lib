/**
 * 
 */
package kk.common.lib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Alex
 *
 */
public class CommonLibMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("kk-common-lib version: " + getVersion());

	}

	/**
	 * Получаем значение из version.properties, который создается автоматически в
	 * /resources. После создания jar к этому файлу нет доступа как к файлу, поэтому
	 * используем здесь getStreamFromResource и тогда имеем возможность получить
	 * значения
	 * 
	 * @return String
	 */
	public static String getVersion() {

		String strVersion = null;
		String VersionPropsFileName = "version.properties";

		InputStream inputStream = new PropertiesFile().getStreamFromResource(VersionPropsFileName);

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

}
