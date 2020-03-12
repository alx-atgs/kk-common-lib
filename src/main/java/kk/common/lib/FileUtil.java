/*
 * Copyright WizTools.org
 * Licensed under the Apache License, Version 2.0:
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package kk.common.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common functions using the java.io.File object.
 * 
 * @author subwiz
 */
public final class FileUtil {
	private FileUtil() {
	}

	/**
	 * Each line in the file is returned as a element in the list.
	 * 
	 * @param f       The file that needs to be read.
	 * @param charset The charset to be used to read the file.
	 * @return The string-list containing lines in the file.
	 * @throws IOException
	 */
	public static List<String> getContentAsStringList(final File f, final Charset charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));) {
			List<String> out = new ArrayList<>();
			String line;
			while ((line = br.readLine()) != null) {
				out.add(line);
			}
			return out;
		}
	}

	/**
	 * Returns the mime-type of the file. Returns "content/unknown" when VM is not
	 * able to determine type.
	 * 
	 * @param f File for which mime-type needs to be determined.
	 * @return Mime-type as a String.
	 */
	public static String getMimeType(File f) {
		String type = null;
		URLConnection uc = null;
		try {
			URL u = f.toURI().toURL();
			uc = u.openConnection();
			type = uc.getContentType();
		} catch (Exception e) {
			// Do nothing!
			Logger.getLogger(FileUtil.class.getName()).log(Level.WARNING, e.getMessage(), e);
		} finally {
			if (uc != null) {
				// No method like uc.close() !!
			}
		}
		return type;
	}

	/**
	 * Writes the provided String to the file using the provided charset encoding.
	 * 
	 * @param f       The file the write the String to.
	 * @param content The content String to write.
	 * @param charset The charset encoding to use when writing.
	 * @throws IOException
	 */
	public static void writeString(final File f, final String content, final Charset charset) throws IOException {
		try (PrintWriter w = new PrintWriter(f, charset.name())) {
			w.print(content);
		}
	}

	/**
	 * Writes a byte[] to a file.
	 * 
	 * @param f     The file to write the byte[].
	 * @param bytes The content to write.
	 * @throws IOException
	 */
	public static void writeBytes(final File f, final byte[] bytes) throws IOException {
		try (OutputStream os = new FileOutputStream(f)) {
			os.write(bytes, 0, bytes.length);
		}
	}

	/**
	 * Copies content of directory/file. Note: will over-write any existing content.
	 * 
	 * @param source The source directory/file.
	 * @param dest   The destination directory/file.
	 * @throws IOException When IO error happens.
	 */
	public static void copy(final File source, final File dest) throws IOException {
		if (source.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}

			final String[] files = source.list();
			for (final String name : files) {
				copy(new File(source, name), new File(dest, name));
			}
		} else {
			if (!dest.exists()) {
				dest.createNewFile();
			}

			try (final FileChannel fcSource = new FileInputStream(source).getChannel();
					final FileChannel fcDest = new FileOutputStream(dest).getChannel();) {
				fcDest.transferFrom(fcSource, 0, fcSource.size());
			}
		}
	}

	/**
	 * Deletes directory with subdirs and subfolders
	 * 
	 * @author Cloud
	 * @param dir Directory to delete
	 * @return
	 */
	public static boolean deleteDirectory(File DirOrFile) {
		if (DirOrFile.isDirectory()) {
			String[] children = DirOrFile.list();
			for (int i = 0; i < children.length; i++) {
				// System.out.println(dir + File.separator + children[i]);
				deleteDirectory(new File(DirOrFile, children[i]));
			}
		}
		// если папка не пустая, то не удалит и будет false
		boolean success = DirOrFile.delete(); // The directory is empty now and can be deleted.
		// System.out.println("Удаленный файл или папка:\n" + dir.getAbsolutePath() + "
		// = " + success);
		return success;

	}

	/**
	 * Deletes files and subfolders
	 * 
	 * @param file
	 */
	public static void recursiveDelete(File file) {
		// до конца рекурсивного цикла
		if (!file.exists())
			return;

		// если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление
		// всего, что там есть
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				// рекурсивный вызов
				recursiveDelete(f);
			}
		}
		// вызываем метод delete() для удаления файлов и пустых(!) папок
		file.delete();
		System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
	}

	public static void del_mkdir_Folder(String FolderFullPath) {

		// есть ли папка ./data и если есть - удаляем ее
		if (Check.dir(FolderFullPath)) {
			if (!FileUtil.deleteDirectory(new File(FolderFullPath))) {
				System.out.println("Невозможно удалить папку: " + FolderFullPath);
				return;
			}
		} else {
			System.out.println("Папка \"" + FolderFullPath + "\" не существует.\nВыполнение программы завершено.");
			return;
		}
		// создаем папку ./data
		if (!new File(FolderFullPath).mkdir()) {
			System.out.println("Невозможно создать папку: " + FolderFullPath);
			return;
		}
	}

	public static void printFile(File file) throws IOException {

		if (file == null)
			return;
		try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
	}

}
