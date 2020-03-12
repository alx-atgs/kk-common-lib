package kk.common.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtils {

	public static void main(String[] args) {

		System.out.println("================================================");
		System.out.println("Проверка работы метода getExternalCurrentIP:\n");
		System.out.println(getExternalCurrentIP());

		System.out.println("================================================");
		System.out.println("Проверка работы метода validIP:\n");
		String[] ips = { "1.2.3.4", "000.12.23.034", "121.234.9.1", "23.45.56.12", "255.255.255.255", "255.1.0.256",
				"00.11.22.33.44", "123.45", "Im.not.IP.address", null, "t.1.2.3" };

		for (String ip : ips) {
			System.out.printf("%20s: %b%n", ip, validIP(ip));
		}

		System.out.println("================================================");
		System.out.println("Проверка работы методов getIPall и getHostnameByIp\n");
		String hostname = "kuznetsov";
		// String hostname = "nbog";
		// String hostname = "kuznt-nb";
		System.out.printf("%20s : %s%n", "Список IP на хосте", "Hostname");
		System.out.println("------------------------------------");
		List<String> listIPall = NetUtils.getIPall(hostname);
		for (String ip : listIPall) {
			System.out.printf("%20s : %s%n", ip, getHostnameByIp(ip));
		}

		System.out.println("================================================");
		String[] hostnames = { "winscada141", "komarov", "kuznetsov", "asoduserver" };
		// вывод списка в одну строку
		System.out.println("Список элементов массива для проверок, следующих ниже:\n" + String.join(", ", hostnames));

		System.out.println("================================================");
		System.out.println("Проверка работы метода checkLifeHost (аналог Ping)\n");
		for (String hns : hostnames) {
			System.out.printf("%s %s : %b%n", "Check HostName", hns, NetUtils.checkLifeHost(hns));
		}

		System.out.println("================================================");
		System.out.println("Проверка работы метода checkIpbyHostname\n");
		String ipValid = "10.22.16.146";
		for (String hn : hostnames) {
			System.out.printf("%s %s %s %s: %b%n", "Check", ipValid, "by", hn, NetUtils.checkIpbyHostname(ipValid, hn));
		}

		System.out.println("================================================");
		System.out.println("Проверка работы метода getIP\n");
		for (String hns : hostnames) {
			System.out.printf("%s %s : %s%n", "Get IP address by", hns, NetUtils.getIP(hns));
		}
		System.out.println("------------------------------------");
		System.out.println("Get IP address by localhost = " + NetUtils.getLocalIp());
		System.out.println("Get HostHame by localhost = " + NetUtils.getLocalhostName());

	}

	public static boolean checkLifeHost(String hostname) {

		try {
			InetAddress.getByName(hostname).isReachable(2000);
			return true;
		} catch (UnknownHostException e) {
			// System.out.println(e.getMessage());
		} catch (IOException e) {
			// System.out.println(e.getMessage());
		}
		return false;

	}

	public static String getLocalIp() {
		String result = null;
		try {
			result = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			String errormsg = e.getMessage().toString();
			return errormsg;
		}
		return result;
	}

	public static String getLocalhostName() {
		String result = null;
		try {
			result = InetAddress.getLocalHost().getHostName().toString();
		} catch (UnknownHostException e) {
			String errormsg = e.getMessage().toString();
			return errormsg;
		}
		return result;
	}

	public static String getIP(String hostname) {

		String result = null;

		if (!checkLifeHost(hostname)) {
			return result;
		}
		try {
			result = InetAddress.getByName(hostname).getHostAddress();
		} catch (UnknownHostException e) {
			// System.out.println(e.getMessage());
			// String errormsg = e.getMessage().toString();
			// return errormsg;
		}
		return result;
	}

	public static List<String> getIPall(String hostname) {
		List<String> listIPall = new ArrayList<String>();
		try {
			InetAddress[] ipAll = InetAddress.getAllByName(hostname);
			for (InetAddress addr : ipAll) {
				listIPall.add(addr.getHostAddress());
			}
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage().toString());
		}
		return listIPall;
	}

	public static void printIPall(String hostname) {
		try {
			System.out.println("------------------------------------");
			System.out.println("список ip, привязанных к заданному PC: " + hostname);
			InetAddress[] ipAll = InetAddress.getAllByName(hostname);
			for (InetAddress addr : ipAll) {
				System.out.println(addr.getHostAddress());
			}
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage().toString());
		}
	}

	/**
	 * Проверка существования ip адреса у заданного компьютера. Count, счетчик, если
	 * 0, то не найдено, иначе - цифра сколько раз найдено
	 * 
	 * @param ipaddress - адрес, который нужно проверить
	 * @param hostname  - имя машины, у которой проверяем адрес
	 * @return boolean - если count = 0 возвращаем false, иначе true
	 * 
	 */
	public static boolean checkIpbyHostname(String ipaddress, String hostname) {

		int count = 0;
		try {
			InetAddress[] iparray = InetAddress.getAllByName(hostname);
			for (InetAddress addr : iparray) {
				// System.out.println(addr.getHostAddress());
				if (addr.getHostAddress().equals(ipaddress)) {
					count++;
				}
			}
		} catch (UnknownHostException e) {
			return false;
			// System.out.println(e.getMessage().toString());
		}
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String getHostnameByIp(String ipAddress) {
		String computerName = null;
		try {
			computerName = InetAddress.getByName(ipAddress).getHostName();
			if (computerName.equalsIgnoreCase("localhost")) {
				computerName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
			}
		} catch (UnknownHostException e) {
			System.out.println(e.getStackTrace().toString());
			String errormsg = e.getMessage().toString();
			return errormsg;
		}
		return computerName;
	}

	/**
	 * Validate IP Address with Java Regular Expression
	 * 
	 * 1)It must start with a number from 0 – 255. 2)It must be followed a dot
	 * 3)This pattern has to repeat for 4 times (eliminating the last dot…)
	 * 
	 * @param ip - IpAddress
	 * @return true or false
	 */

	public static boolean validIPvar1(String ip) {

		final String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

		final String IP_REGEXP = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;

		final Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);

		// Return true when *address* is IP Address
		// private boolean isValid(String address) {
		return IP_PATTERN.matcher(ip).matches();
		// }
	}

	/**
	 * Проверяет правильность строки в IPv4-адресе с помощью регулярного выражения.
	 * Классический вариант (метод validIP работает быстрей).
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static boolean validIPvar2(final String ipAddress) {

		try {
			if (ipAddress != null && !ipAddress.isEmpty()) {

				final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

				Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
				Matcher matcher = pattern.matcher(ipAddress);
				return matcher.matches();
			}
			return false;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * Проверяет правильность строки в IPv4-адресе с помощью регулярного выражения.
	 * Это решение примерно в 20 раз быстрее, чем вариант с регулярным выражением.
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean validIP(String ip) {

		if (ip == null || ip.length() < 7 || ip.length() > 15)
			return false;

		try {
			int x = 0;
			int y = ip.indexOf('.');

			if (y == -1 || ip.charAt(x) == '-' || Integer.parseInt(ip.substring(x, y)) > 255)
				return false;

			x = ip.indexOf('.', ++y);
			if (x == -1 || ip.charAt(y) == '-' || Integer.parseInt(ip.substring(y, x)) > 255)
				return false;

			y = ip.indexOf('.', ++x);
			return !(y == -1 || ip.charAt(x) == '-' || Integer.parseInt(ip.substring(x, y)) > 255
					|| ip.charAt(++y) == '-' || Integer.parseInt(ip.substring(y, ip.length())) > 255
					|| ip.charAt(ip.length() - 1) == '.');

		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Возвращает текущий внешний IP, под которым сейчас в Internet. Отправляем
	 * запрос на сайт myip.by и анализируем ответ на предмет ip адреса.
	 * 
	 * @return String
	 */

	private static String getExternalCurrentIP() {
		String result = null;
		try {
			BufferedReader reader = null;
			try {
				URL url = new URL("https://myip.by");
				InputStream inputStream = null;
				inputStream = url.openStream();
				reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder allText = new StringBuilder();
				char[] buff = new char[1024];

				int count = 0;
				while ((count = reader.read(buff)) != -1) {
					allText.append(buff, 0, count);
				}
				// Строка содержащая IP имеет следующий вид
				// <a href="whois.php?127.0.0.1">whois 127.0.0.1</a>
				Integer indStart = allText.indexOf("\">whois ");
				Integer indEnd = allText.indexOf("</a>", indStart);

				String ipAddress = new String(allText.substring(indStart + 8, indEnd));
				if (ipAddress.split("\\.").length == 4) { // минимальная (неполная)
					// проверка что выбранный текст является ip адресом.
					result = ipAddress;
				}
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
