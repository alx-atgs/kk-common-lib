package kk.common.lib;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

	/**
	 * Возвращаем текущую локальную дату в текстовом формате "yyyy-MM-dd"
	 * 
	 * @return String
	 */
	public static String getDateNow() {
		String DateNow = java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return DateNow;
	}

	public static long rrr() {
		long ttt = java.time.temporal.ChronoUnit.DAYS.between(null, null);
		System.out.println(ttt);
		return ttt;
	}

	public static LocalDateTime millsToLocalDateTime(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date;
	}

	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		sb.append(days);
		sb.append(" Days ");
		sb.append(hours);
		sb.append(" Hours ");
		sb.append(minutes);
		sb.append(" Minutes ");
		sb.append(seconds);
		sb.append(" Seconds");

		return (sb.toString());
	}

	/**
	 * Позволяет задержать выполнение других действий в программе на заданное
	 * количество секунд. Например, подождать пока запускается служба.
	 * 
	 * @param TimeInSecs - цифра в секундах
	 */
	public static void waitTime(int TimeInSecs) {

		try {
			TimeUnit.SECONDS.sleep(TimeInSecs);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

	}

}
