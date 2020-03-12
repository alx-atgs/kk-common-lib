package kk.common.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class Check {

	static String FileFullPath = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/good FOR.xml";
	static String DirFullPath = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML";

	public static void main(String[] args) {

		System.out.println("1. " + FileFullPath + " как файл  = " + file(FileFullPath));
		System.out.println("2. " + FileFullPath + " как папка  = " + dir(FileFullPath));
		System.out.println("3. " + DirFullPath + " как файл = " + file(DirFullPath));
		System.out.println("4. " + DirFullPath + " как папка = " + dir(DirFullPath));

		System.out.println("================");
		File with_BOM = new File("D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/file_with_bom.xml");
		File without_BOM = new File("D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/file_without_BOM.xml");
		System.out.println("Проверка работы метода Check.foundForUtf8BOM:\n");
		String xmlstring1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		String xmlstring2 = ObjUtil.getContentAsString(with_BOM.getAbsolutePath().toString(), "UTF-8");
		// try {
		InputStream is1 = ObjUtil.file2inputStream(with_BOM);
		InputStream is2 = ObjUtil.file2inputStream(without_BOM);
		InputStream is3 = ObjUtil.string2inputStream(xmlstring1);
		InputStream is4 = ObjUtil.string2inputStream(xmlstring2);
		// InputStream is5 = ObjUtil.string2inputStream(with_BOM);
		System.out.println("Проверка файла file_with_BOM: " + Check.foundForUtf8BOM(is1));
		System.out.println("Проверка файла file_without_BOM: " + Check.foundForUtf8BOM(is2));
		System.out.println("Проверка заданной явно строки: " + Check.foundForUtf8BOM(is3));
		System.out.println("Проверка строки из метода getContentAsString: " + Check.foundForUtf8BOM(is4));
		try {
			is1.close();
			is2.close();
			is3.close();
			is4.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("================");
		System.out.println(DelSpaceSimbols("   dsgdfhg  fdsafs   "));
	}

	/**
	 * Проверка на существование файла
	 * 
	 * @param filename - путь к файлу, как строка
	 * @return True - если файл существует, иначе - false
	 */
	public static boolean file(String filename) {
		File file = new File(filename);
		return file.exists() && file.isFile();
	}

	/**
	 * Проверка на существование директории (папки, каталога)
	 * 
	 * @param path - путь к директории, как строка
	 * @return True - если файл существует, иначе - false
	 */
	public static boolean dir(String path) {
		File dir = new File(path);
		return dir.exists() && dir.isDirectory();
	}

	/**
	 * Удаление трех байт, составляющих BOM из InputStream.
	 * 
	 * @param is - входящий InputStream
	 * @return - InputStream, очищенный от байт BOM, если они в нем были, иначе
	 *         исходный InputStream
	 */
	public static InputStream checkForUtf8BOMAndDiscardIfAny(InputStream is) {

		try {
			PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(is), 3);

			byte[] bom = new byte[3];
			if (pushbackInputStream.read(bom) != -1) {
				if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
					pushbackInputStream.unread(bom);
				}
				return pushbackInputStream;
			}
		} catch (IOException e) {
			System.out.println("checkForUtf8BOMAndDiscardIfAny: \n" + e.getMessage());
			System.exit(1);
		}
		return is;
	}

	/**
	 * Проверка наличия в InputStream трех байт, составляющих BOM
	 * 
	 * @param is - входящий InputStream
	 * @return - true, если найдены, иначе - false
	 */
	public static boolean foundForUtf8BOM(InputStream is) {

		try {
			PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(is), 3);
			byte[] bom = new byte[3];
			if (pushbackInputStream.read(bom) != -1) {
				if ((bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("foundForUtf8BOM: \n" + e.getMessage());
			System.exit(1);
		}
		return false;
	}

	/**
	 * В приведенном ниже коде регулярное выражение (^\\s+) используется для поиска
	 * всех пробелов в начале строки. Символ '^' указывает на начало строки с
	 * последующим '\\s+', который указывает на пропуски, пока первый символ не
	 * встретится.
	 * 
	 * Во второй части регулярного выражения используется (\\s+$) для поиска
	 * пробелов начиная с последнего символа. Объединяет эти два условия поиска в
	 * одном регулярном выражении знак '|'.
	 * 
	 * @param inputString
	 * @return String - строка, в которой удалены пробелы в начале и в конце
	 */
	private static String DelSpaceSimbols(String inputString) {

		String outputString = inputString.replaceAll("^\\s+|\\s+$", "");

		return outputString;
	}
}
