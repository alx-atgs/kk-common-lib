package kk.common.lib;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CharacterDetectorTest {

	static String fileName1 = "codeutf8.txt";
	static String fileName2 = "code1251.txt";
	static String fileName3 = "code866.txt";

	public static void main(String args[]) throws IOException {

		System.out.println("Вывод кодировки из файлов в корне проекта с разной кодировкой текста:");

		String asup = AppStartUpPath.getDir() + File.separator;

		java.io.FileInputStream fis1 = new java.io.FileInputStream(asup + fileName1);
		java.io.FileInputStream fis2 = new java.io.FileInputStream(asup + fileName2);
		java.io.FileInputStream fis3 = new java.io.FileInputStream(asup + fileName3);

		String fn1 = CharacterDetector.detect(fis1);
		String fn2 = CharacterDetector.detect(fis2);
		String fn3 = CharacterDetector.detect(fis3);
		System.out.println(fn1 + "\n" + fn2 + "\n" + fn3);
		System.out.println("------------");

		System.out.println("Вывод кодировки из файлов в папке Resources с разной кодировкой текста:");

		PropertiesFile pf = new PropertiesFile();

		InputStream fis4 = pf.getStreamFromResource(fileName1);
		InputStream fis5 = pf.getStreamFromResource(fileName2);
		InputStream fis6 = pf.getStreamFromResource(fileName3);

		String fn4 = CharacterDetector.detect(fis4);
		String fn5 = CharacterDetector.detect(fis5);
		String fn6 = CharacterDetector.detect(fis6);

		System.out.println(fn4 + "\n" + fn5 + "\n" + fn6);
		System.out.println("------------");

		String b_str = "проверка кодировки";
		System.out.println("Исходная фраза: \"" + b_str + "\" в разных кодировках:");

		String b_str1 = new String(b_str.getBytes("UTF-8"));
		String b_str2 = new String(b_str.getBytes("Windows-1251"));
		String b_str3 = new String(b_str.getBytes("cp866"));

		System.out.println(b_str1 + "\n" + b_str2 + "\n" + b_str3);
		System.out.println("------------");

		System.out.println("Результыт попытки определения кодировки:");

		InputStream is1 = new ByteArrayInputStream(b_str1.getBytes("cp1251"));
		InputStream is2 = new ByteArrayInputStream(b_str2.getBytes("utf-8"));
		InputStream is3 = new ByteArrayInputStream(b_str3.getBytes("cp1251"));

		String str1 = CharacterDetector.detect(is1);
		String str2 = CharacterDetector.detect(is2);
		String str3 = CharacterDetector.detect(is3);

		System.out.println(str1 + "\n" + str2 + "\n" + str3);

		// File tempFile = File.createTempFile("temp-file", "tmp");
		// ObjUtil.stringToFile(b_str1, tempFile);
		// tempFile.deleteOnExit();

	}

	// get file from classpath, resources folder

}
