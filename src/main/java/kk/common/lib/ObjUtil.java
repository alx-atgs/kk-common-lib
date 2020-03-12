package kk.common.lib;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Alexander Kuznetsov
 *
 */
public class ObjUtil {

	/**
	 * Функция позволяет вернуть файл в String. Используется быстрый в работе
	 * java.io.RandomAccessFile.
	 *
	 * При создании экземпляра RandomAccessFile, мы должны выбрать режим файла,
	 * например "r", если хотим прочитать данные из файла или "rw" — если не только
	 * читать, но и писать в файл.
	 * 
	 * Альтернатива одноименному методу из org.wiztools.commons.FileUtil, но этот
	 * удобней использовать, т.к. в том нужно будет использовать конструктор: File f
	 * = new File(pathname), а кодировку указывать в формате Charset charset =
	 * StandardCharsets.UTF_8;
	 * 
	 * 
	 * @param filePath - путь к файлу как строка, не нужен конструктор new File()
	 *                 подать сюда текст нельзя, будет ошибка
	 * @param charset  - кодировка в формате вида "utf-8"
	 * @return String - Возвращает строку.
	 */
	public static String getContentAsString(String filePath, String charset) {

		// if (!Check.file(filePath)) {
//			System.out.println("Error.ObjUtil.getContentAsString: Input file not founded or not file.");
		// return null;
		// }

		java.io.RandomAccessFile fileHandler = null;
		int l = 0;
		int offset = 0;
		String backString = null;
		try {
			fileHandler = new java.io.RandomAccessFile(filePath, "r");
			// берем длину файла
			l = (int) (fileHandler.length());
			byte[] data = new byte[l];
			// читаем каждый байт
			fileHandler.read(data, offset, l);
			// перекодирует прочитанный байт в указанную в charset кодировку
			backString = new String(data, offset, l, charset);

		} catch (java.io.FileNotFoundException e) {
			System.out.println("Error.ObjUtil.getContentAsString: " + e.getMessage());
			// return null;
		} catch (java.io.IOException e) {
			System.out.println("Error.ObjUtil.getContentAsString: " + e.getMessage());
			// e.printStackTrace();
			// return null;
		} finally {
			if (fileHandler != null)
				try {
					fileHandler.close();
				} catch (IOException e) {
					System.out.println("Error.ObjUtil.getContentAsString: " + e.getMessage());
				}
		}
		return backString;
	}

	/**
	 * This method save String to File.
	 * 
	 * @param inputstr - input String
	 * @param          - targetFilePath- path to target File
	 * @param charset  - chartset, example "utf-8"
	 * @return File
	 */
	public static File stringToFile(String inputstr, String targetFilePath, String charset) {

		File fname = null;
		Path targetPath = Paths.get(targetFilePath);
		try {
			byte[] bytes = inputstr.getBytes(charset);
			Path path = Files.write(targetPath, bytes,
					new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING });
			// return path;
			if (path.toFile().exists()) {
				fname = path.toFile();
			} else {
				System.out.println("Error.ObjUtil.stringToFile:  the check of filePath returned is false");
			}
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.stringToFile: " + e.getMessage());
			// e.printStackTrace();
			System.exit(2);
		}
		return fname;
	}

	/**
	 * Метод создает временный файл и возвращает путь до папки где он создался
	 * Используется для определения пути к папке Temp и наличие прав на запись
	 * 
	 * @return String - строка, содержит путь до папки, где можно создать временный
	 *         файл
	 */
	public static String tempPath() {
		try {
			File tempFile = File.createTempFile("temp-file", "tmp");
			String tempPath = tempFile.getParent();
			tempFile.deleteOnExit();
			return tempPath;
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.tempPath: " + e.getMessage());
			// Logger.getLogger(Result.class.getName()).log(Level.SEVERE, (String) null, e);
			return null;
		}
	}

	/**
	 * Получаем первую значимую строку из файла. Пустые строки сверху - игнорируются
	 * 
	 * @param FileFullPath - - полный путь к файлу
	 * @return String - возвращает строку, empty - если строка пустая или "false"
	 */
	public static String getFirstLine(String fileFullPath) {
		String line = "";

		if (!Check.file(fileFullPath)) {
			System.out.println("Error.ObjUtil.getFirstLine:  the check of filePath returned is false");
			// System.exit(2) - for Windows: The system cannot find the file specified.
			// ERROR_FILE_NOT_FOUND
			// System.exit(2);
			return null;
		}
		for (int i = 1; i < fileFullPath.length(); i++) {
			// System.out.println(line);
			line = getNumline(fileFullPath, i);
			if (line.trim().length() > 1) {
				break;
			} else {
				line = "empty";
			}
		}
		return line;
	}

	/**
	 * Позволяет получить из файла строку, номер которой, задан вторым аргументом.
	 * Возможно получение пустых строк.
	 * 
	 * @param String FileFullPath - полный путь к файлу
	 * @param        int numLine - число, номер строки
	 * @return String - возвращает строку или код ошибки
	 */
	public static String getNumline(String fileFullPath, int numLine) {

		String line = "";
		if (numLine < 1) {
			System.out.println("Error.ObjUtil.getNumline: number line not right. Current value: " + numLine);
			// System.exit(10) - for Windows: The environment is incorrect.
			// ERROR_BAD_ENVIRONMENT
			// System.exit(10);
			return null;
		}
		if (!Check.file(fileFullPath)) {
			System.out.println("Error.ObjUtil.getNumline: the check of filePath returned is false");
			// System.exit(2) - for Windows: The system cannot find the file specified.
			// ERROR_FILE_NOT_FOUND
			// System.exit(2);
			return null;
		}
		try {
			File file = new File(fileFullPath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (int i = 0; i < numLine; i++) {
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.getNumline:" + e.getMessage());
		}
		return line;
	}

	/**
	 * функция выводит в строку несколько последних строк из указанного файла
	 * количество нужных строк указывается в lines позволяет БЫСТРО выводить
	 * последние строки из ооочень больших файлов, т.к. не читает весь файл с
	 * начала, а указатель сразу прыгает в самый конец файла
	 * 
	 * @param filePath - путь к файлу
	 * @param lines    - количество строк
	 * @return String
	 */
	// добавление содержимого
	// fileHandler.write("Java Tutorial".getBytes());
	// установить указатель назад -8 символов
	// fileHandler.seek(fileHandler.getFilePointer() - 8);
	// пишем текст, где указатель
	// fileHandler.write("File Class Tutorial ".getBytes());
	// установить указатель на начало файла
	// fileHandler.seek(0);

	public static String tail(String filePath, int lines) {

		if (!Check.file(filePath)) {
			System.out.println("Error.ObjUtil.tail: the check of filePath returned is false");
			// System.exit(2) - for Windows: The system cannot find the file specified.
			// ERROR_FILE_NOT_FOUND
			// System.exit(2);
			return null;
		}

		java.io.RandomAccessFile fileHandler = null;
		try {
			fileHandler = new java.io.RandomAccessFile(filePath, "r");
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();
			int line = 0;

			// С помощью указателя файла мы можем читать из файла или записывать
			// данные в файл в любом месте. Чтобы получить текущий указатель
			// файла, используется метод getFilePointer().
			for (long filePointer = fileLength; filePointer != -1; filePointer--) {
				// установить индекс указателя файла seek(int i)
				fileHandler.seek(filePointer);
				int readByte = fileHandler.readByte();

				if (readByte == 0xA) {
					if (filePointer < fileLength) {
						line = line + 1;
					}
				} else if (readByte == 0xD) {
					if (filePointer < fileLength - 1) {
						line = line + 1;
					}
				}
				if (line >= lines) {
					break;
				}
				sb.append((char) readByte);
			}

			String lastLine = sb.reverse().toString();
			return lastLine;
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fileHandler != null)
				try {
					fileHandler.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * выводить на экран имя файла, метода и номер строки который в данный момент
	 * выполняется. Пример использования:
	 * System.out.println(ObjUtil.getCodePoint());
	 * 
	 * @return String
	 */
	public static String getCodePoint() {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		return ste.getFileName() + ":" + ste.getClassName() + ":" + ste.getMethodName() + ":" + ste.getLineNumber();
	}

	/**
	 * Удаляет три байта BOM из входящей строки/файла // проверяем на входе файл или
	 * строка if (Check.file(PathOrString)) { // открываем файл и переводим его в
	 * байты // т.к. ByteArrayInputStream требует байты b =
	 * Files.readAllBytes(Paths.get(PathOrString)); } else { }
	 * 
	 * 
	 * @param inputString - строка
	 * @return String - строка с удаленными символами
	 */
	public static String delBOM(String inputString) {

		String backString = null;

		// переводим строку в InputStream
		try (InputStream is = string2inputStream(inputString)) {
			// try (InputStream is = new ByteArrayInputStream(inputString.getBytes())) {
			// подвергаем is удалению BOM символов
			InputStream backInputStream = Check.checkForUtf8BOMAndDiscardIfAny(is);
			// переводим очищенный InputStream обратно в строку
			backString = inputStream2String(backInputStream, "utf-8");
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.delBOM: " + e.getMessage());
			// e.printStackTrace();
		}
		return backString;
	}

	public static String delBOM(File fileName) {

		String backString = null;
		// String inputString = input.getName().toString();

		// переводим строку в InputStream
		try (InputStream is = file2inputStream(fileName)) {
			// try (InputStream is = new ByteArrayInputStream(inputString.getBytes())) {
			// подвергаем is удалению BOM символов
			InputStream backInputStream = Check.checkForUtf8BOMAndDiscardIfAny(is);
			// переводим очищенный InputStream обратно в строку
			backString = inputStream2String(backInputStream, "utf-8");
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.delBOM: " + e.getMessage());
			// e.printStackTrace();
		}
		return backString;
	}

	/**
	 * Переводим InputStream в String оптимальным путем
	 * (https://habr.com/company/luxoft/blog/278233/), указывая нужную кодировку на
	 * выходе.
	 * 
	 * @param is      - InputStream
	 * @param charset - кодировка, например: "UTF-8"
	 * @return - строка в нужной кодировке
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is, String charset) {

		String backstring = null;
		try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[2048];
			int length;
			while ((length = is.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			backstring = result.toString(charset);
		} catch (IOException e) {
			System.out.println("Error.ObjUtil.isToString: " + e.getMessage());
			// e.printStackTrace();
		}
		return backstring;
	}

	public static String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream file2inputStream(File fileName) {

		InputStream backinputstream = null;
//		try (InputStream steam = new ByteArrayInputStream(Files.readAllBytes(Paths.get(inputString)))) {
		try (InputStream steam = new ByteArrayInputStream(Files.readAllBytes(fileName.toPath()))) {
			backinputstream = steam;
		} catch (Exception e) {
			System.out.println("Error.ObjUtil.stringToIs: " + e.getMessage());
		}
		return backinputstream;
	}

	/**
	 * 
	 * @param inputString
	 * @return
	 */
	public static InputStream string2inputStream(String inputString) {

		InputStream backinputstream = null;
		try (InputStream steam = new ByteArrayInputStream(inputString.getBytes())) {
			backinputstream = steam;
		} catch (Exception e) {
			System.out.println("Error.ObjUtil.stringToIs: " + e.getMessage());
		}
		return backinputstream;
	}

	/**
	 * НЕДОДЕЛАН
	 * 
	 * Как сделать запрос HTTP get в java и получить тело ответа как строку, а не
	 * поток?
	 * 
	 * Лучший способ обработки тела ответа в виде строки, будь то действительный
	 * ответ или ответ об ошибке для запроса HTTP POST:
	 * 
	 */
	public static String NewMethod() {
		BufferedReader reader = null;
		OutputStream os = null;
		String payload = "";
		try {
			URL url1 = new URL("YOUR_URL");
			HttpURLConnection postConnection = (HttpURLConnection) url1.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			os = postConnection.getOutputStream();
			// os.write(eventContext.getMessage().getPayloadAsString().getBytes());
			os.flush();

			String line;
			try {
				reader = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			} catch (IOException e) {
				if (reader == null)
					reader = new BufferedReader(new InputStreamReader(postConnection.getErrorStream()));
			}
			while ((line = reader.readLine()) != null)
				payload += line.toString();
		} catch (Exception ex) {
			// log.error("Post request Failed with message: " + ex.getMessage(), ex);
		} finally {
			try {
				reader.close();
				os.close();
			} catch (IOException e) {
				// log.error(e.getMessage(), e);
				return null;
			}
		}
		return payload;
	}
}
