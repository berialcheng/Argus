package org.argus.core.route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RouteUtils {

	private static String packageFolder = "org.argus.core.route";

	private static String routeFileName = "route.conf";

	public static Rule resolveRoute(String path) {
		String confFilePath = System.getProperty("user.home") + File.separator
				+ ".argus" + File.separator
				+ packageFolder.replaceAll("\\.", File.separator)
				+ File.separator + routeFileName;
		File file = new File(confFilePath);
		String fileContent = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				fileContent += str;
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		Gson gson = new Gson();
		List<Rule> rules = gson.fromJson(fileContent,
				new TypeToken<List<Rule>>() {
				}.getType());
		for (Rule rule : rules) {
			if (path.matches(rule.getPath())) {
				return rule;
			}
		}
		return null;
	}

	public static void initializeRouteConf() {
		String confFilePath = System.getProperty("user.home") + File.separator
				+ ".argus" + File.separator
				+ packageFolder.replaceAll("\\.", File.separator)
				+ File.separator + routeFileName;
		File file = new File(confFilePath);
		if (!file.exists()) {
			try {
				String confFolderPath = System.getProperty("user.home") + File.separator
						+ ".argus" + File.separator
						+ packageFolder.replaceAll("\\.", File.separator);
				File folder = new File(confFolderPath);
				folder.mkdirs();
				copyRuleFile(routeFileName, file.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void copyRuleFile(String filename, String dest) {
		try {
			InputStream is = RouteUtils.class.getClassLoader()
					.getResourceAsStream(filename);
			FileOutputStream fos = new FileOutputStream(dest);
			do {
				byte[] by = new byte[1024];
				int length = is.read(by);
				fos.write(by, 0, length);
			} while (is.available() > 0);
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		RouteUtils.initializeRouteConf();
	}
}
