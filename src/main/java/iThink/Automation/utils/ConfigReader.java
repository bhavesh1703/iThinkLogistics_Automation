package iThink.Automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	
	private static Properties prop;

	static {
		try {
			FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
//			FileInputStream fis = new FileInputStream(ConfigReader.getProperty("testdataPath"));
			prop = new Properties();
			prop.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load configuration file", e);
		}
	}
	
	public static String getProperty(String key) {
		String value = prop.getProperty(key);
		
		if(value == null ) {
			throw new RuntimeException("Property '" +key+ "' is not found in config.properties file");
		}
		return value;
	}
}
