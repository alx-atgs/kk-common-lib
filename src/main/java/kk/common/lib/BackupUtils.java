/**
 * 
 */
package kk.common.lib;

import java.io.File;
import java.io.IOException;

/**
 * @author Alex
 *
 */
public class BackupUtils {

	/**
	 * Создаем копию папки, указанной в первом аргументе, в папку, указанной во
	 * втором. Если папки, указанной во втором аргументе нет, то создаем ее. И
	 * далее, создаем папку с именем, полученным из текущей даты. Пример:
	 * ./BackUp/2019-03-11
	 * 
	 * @param FullPathPgData
	 * @param FullPathBackUpFolder
	 */
	public static void copyFolder(String FullPathSourceFolder, String FullPathBackUpFolder) {

		if (!Check.dir(FullPathSourceFolder)) {
			System.out.println("BackUpUtils.CopyFolder(). Папка: \"" + FullPathSourceFolder
					+ "\" , указанная в качестве входящего аргумента, не существует.\nВыполнение программы завершено.");
			System.exit(2);
		}
		if (!Check.dir(FullPathBackUpFolder)) {
			if (!new File(FullPathBackUpFolder).mkdir()) {
				System.out.println("Невозможно создать папку \"" + FullPathBackUpFolder
						+ "\" для создания BackUp на текущем сервере.\nВыполнение программы завершено.");
				System.exit(2);
			}
		}

		// получим имя папки из первого аргумента
		String NamePgData = new File(FullPathSourceFolder).getName();
		// получим текущую дату
		String DateNow = DateTimeUtils.getDateNow();
		// создаем папку из текущей даты
		String BackUpFolderCur = FullPathBackUpFolder + File.separator + DateNow;
		if (!Check.dir(BackUpFolderCur)) {
			if (!new File(BackUpFolderCur).mkdir()) {
				System.out.println("Невозможно создать папку \"" + BackUpFolderCur
						+ "\" для создания BackUp на текущем сервере.\nВыполнение программы завершено.");
				System.exit(2);
			}
		}
		// создаем папку c окончательным полным путем
		String FullBackUpFolderCur = BackUpFolderCur + File.separator + NamePgData;
		if (!Check.dir(FullBackUpFolderCur)) {
			if (!new File(FullBackUpFolderCur).mkdir()) {
				System.out.println("Невозможно создать папку \"" + FullBackUpFolderCur
						+ "\" для создания BackUp на текущем сервере.\nВыполнение программы завершено.");
				System.exit(2);
			}
		}
		// установим права к папке и подпапкам FullPathBackUpFolder из второго аргумента
		// для службы NetworkService
		String szCommand = "icacls.exe \"" + FullPathBackUpFolder
				+ "\" /grant \"NT AUTHORITY\\NetworkService\":(OI)(CI)(F)";
		String szEncoding = "cp1251";
		WinCommandLine.backstring(szCommand, szEncoding);

		// копируем папку из первого аргумента в папку, которую собрали выше на
		// основании указанного второго аргумента, заодно применяем счетчик времени
		try {
			System.out.println("Операция по созданию BackUp стартовала. Ожидайте ...\n");
			long start = System.currentTimeMillis();
			FileUtil.copy(new File(FullPathSourceFolder), new File(FullBackUpFolderCur));
			long time = System.currentTimeMillis() - start;
			String time2str = DateTimeUtils.getDurationBreakdown(time);
			System.out.println("Создание BackUp завершено. Время выполнения: " + time2str);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Копия папки из первого аргумента в папку из второго аргумента, но в
	 * дополнительный подкаталог FirstBackUp Если папки [второй
	 * аргумент]/FirstBackUp нет, то создаем ее.
	 * 
	 * @param FullPathSourceFolder
	 * @param FullPathBackUpFolder
	 */
	public static void firstBackup(String FullPathSourceFolder, String FullPathBackUpFolder) {

		String FirstBackUpFolder = FullPathBackUpFolder + File.separator + "FirstBackUp";
		if (!Check.dir(FirstBackUpFolder)) {
			if (!new File(FirstBackUpFolder).mkdir()) {
				System.out.println("Невозможно создать папку \"" + FirstBackUpFolder
						+ "\" для создания BackUp на текущем сервере.\nВыполнение программы завершено.");
				System.exit(2);
			}
		}
		BackupUtils.copyFolder(FullPathSourceFolder, FirstBackUpFolder);
	}
}
