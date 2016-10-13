package com.random.imei;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String filePath =
		// "C:\\Users\\Administrator\\Desktop\\apks\\init.androVM.sh";
		if (args != null && args.length == 1) {
			doFile(args[0]);
		}
	}

	/**
	 * @desc 处理init.androVM.sh文件 生成一个随机IMEI值
	 * @param filePath
	 */
	private static void doFile(String filePath) {
		try {
			// read file content from file
			StringBuffer sb = new StringBuffer("");

			FileReader reader = new FileReader(filePath);
			BufferedReader br = new BufferedReader(reader);

			String str = null;
			String imei = getRandomIMEI();
			while ((str = br.readLine()) != null) {
//				System.out.println(str);
				if (str.contains("genyd.device.id")) {
					str = "setprop genyd.device.id \"" + imei + "\"";
				}
				sb.append(str + "\n");
			}

			br.close();
			reader.close();

			// write string to file
			FileWriter writer = new FileWriter(filePath);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(sb.toString());
			bw.flush();
			bw.close();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @desc 随机生成一个IMEI
	 */
	private static String getRandomIMEI() {
		// http://en.wikipedia.org/wiki/Reporting_Body_Identifier
		Random r = new Random();
		// String[] rbi = new String[] { "00", "01", "02", "03", "04", "05",
		// "06", "07", "08", "09", "10", "30", "33",
		// "35", "44", "45", "49", "50", "51", "52", "53", "54", "86", "91",
		// "98", "99" };
		// String imei = rbi[r.nextInt(rbi.length)];
		// China 默认是86
		String imei = "86";
		while (imei.length() < 14)
			imei += Character.forDigit(r.nextInt(10), 10);
		imei += getLuhnDigit(imei);
		return imei;
	}

	private static char getLuhnDigit(String x) {
		// http://en.wikipedia.org/wiki/Luhn_algorithm
		int sum = 0;
		for (int i = 0; i < x.length(); i++) {
			int n = Character.digit(x.charAt(x.length() - 1 - i), 10);
			if (i % 2 == 0) {
				n *= 2;
				if (n > 9)
					n -= 9; // n = (n % 10) + 1;
			}
			sum += n;
		}
		return Character.forDigit((sum * 9) % 10, 10);
	}

}
