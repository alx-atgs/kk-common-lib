package kk.common.lib;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс для определения пути к каталогу, из которого запущен jar-файл.
 * 
 * String apppath = AppStartUpPath.getPath();
 */
public class AppStartUpPath {

	private static String outPath;

	public static void main(String[] args) {

		new AppStartUpPath();
		// AppStartUpPath startUpPath = new AppStartUpPath();
		System.out.println("\nПолучаем путь до каталога, откуда запущено приложение, используя getDir(): \n"
				+ AppStartUpPath.getDir());

		System.out.println("\nПолучаем путь до каталога, откуда запущено приложение, используя getDirStrOld(): \n"
				+ getDirStrOld());

		System.out.println("\nПолучаем путь до каталога, откуда запущено приложение, используя getDirPathNew(): \n"
				+ new AppStartUpPath().getDirPathNew());
	}

	public static String getDir() {
		outPath = "";
		outPath = new File("").getAbsolutePath();
		return outPath;
	}

	/**
	 * Вывод полного пути до папки(директории), откуда запущено приложение(класс)
	 * (где расположен jar-файл с классом AppStartUpPath)
	 * 
	 * @return String
	 */

	public static String getDirStrOld() {

		AppStartUpPath startUpPath = new AppStartUpPath();
		outPath = startUpPath.getDirPath().toString();
		return outPath;

	}

	public Path getDirPath() {
		URL startupUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
		Path path = null;
		try {
			// получаем путь до папки bin, откуда запущен класс
			path = Paths.get(startupUrl.toURI());
			// если ошибка, пробуем иначе
		} catch (URISyntaxException e) {
			try {
				path = Paths.get(new URL(startupUrl.getPath()).getPath());
				// если и тут ошибка, то
			} catch (MalformedURLException ipe) {
				path = Paths.get(startupUrl.getPath());
			}
		}

		// возвращаем родительский каталог
		path = path.getParent();
		return path;
	}

	/**
	 * Вывод полного пути до папки(директории), откуда запущено приложение(класс)
	 * (где расположен jar-файл с классом AppStartUpPath)
	 * 
	 * @return Path
	 */

	public Path getDirPathNew() {
		URL startupUrl = getClass().getProtectionDomain().getCodeSource().getLocation();

		String s = getClass().getName();
		int i = s.lastIndexOf(".");
		if (i > -1)
			s = s.substring(i + 1);
		s = s + ".class";
		System.out.println("name = " + s);
		URL ttt = this.getClass().getResource(s);

		// String path1 = s.class.getResource("").getPath();
		// File file = new File(path1);
		// System.out.println(file.getAbsolutePath());

		Path path = null;
		try {
			// получаем путь до папки bin, откуда запущен класс
			path = Paths.get(startupUrl.toURI());
			// String aaa = startupUrl.getHost();
			System.out.println("ttt = " + ttt);
			// если ошибка, пробуем иначе
		} catch (URISyntaxException e) {
			try {
				path = Paths.get(new URL(startupUrl.getPath()).getPath());
				// если и тут ошибка, то
			} catch (MalformedURLException ipe) {
				path = Paths.get(startupUrl.getPath());
			}
		}

		// возвращаем родительский каталог
		// path = path.getParent();
		path = path.getParent();
		return path;
	}
}