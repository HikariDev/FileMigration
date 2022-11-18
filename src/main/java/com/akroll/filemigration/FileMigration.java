package com.akroll.filemigration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Main class for the FileMigration application.
 * This application is intended for frequent migration of files from an indefinite number of source and destination directory pairs.
 * Author: Andrew Kroll
 * Created: 2022-11-18
 * Updated: 2022-11-18
 * https://github.com/HikariDev/FileMigration
 */
public class FileMigration {
	
	/**
	 * Main application function.
	 *
	 * @param args Unused
	 */
	public static void main(String[] args) {
		// Save default config if missing
		if (new File("MigrationConfig.json").exists()) {
			URL inputUrl = FileMigration.class.getResource("/MigrationConfig.json");
			File dest = new File("MigrationConfig.json");
			try {
				FileUtils.copyURLToFile(inputUrl, dest);
			} catch (IOException e) {
				Logger.getInstance().write("ERROR: Unable to save default migration config: " + e.getMessage());
				throw new RuntimeException(e);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		// Load Migration Configuration
		try {
			MigrationConfig config = mapper.readValue(new File("MigrationConfig.json"), MigrationConfig.class);
		} catch (IOException e) {
			Logger.getInstance().write("ERROR: Unable to load migration config: " + e.getMessage());
			throw new RuntimeException(e);
		}
		
		// Add logger close hook for application shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.getInstance().close();
		}));
		
	}
}
