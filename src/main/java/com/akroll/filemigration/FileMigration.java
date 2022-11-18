package com.akroll.filemigration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

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
		if (!new File("MigrationConfig.json").exists()) {
			URL inputUrl = FileMigration.class.getResource(File.separator + "MigrationConfig.json");
			File dest = new File("MigrationConfig.json");
			try {
				FileUtils.copyURLToFile(inputUrl, dest);
			} catch (IOException e) {
				Logger.getInstance().write("ERROR: Unable to save default migration config: " + e.getMessage());
				throw new RuntimeException(e);
			}
			Logger.getInstance().write("A default configuration file has been created. " +
						"Please configure this application before running again!");
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		MigrationConfig config;
		// Load Migration Configuration
		try {
			config = mapper.readValue(new File("MigrationConfig.json"), MigrationConfig.class);
		} catch (IOException e) {
			Logger.getInstance().write("ERROR: Unable to load migration config: " + e.getMessage());
			throw new RuntimeException(e);
		}
		
		// Run migration jobs
		Logger.getInstance().write("---- Beginning File Migration ----");
		
		Arrays.stream(config.getJobs())
					.map(job -> new MigrationHandler(job))
					.forEach(MigrationHandler::run);
		
		Logger.getInstance().write("---- File Migration Finished ----");
		
		// Add logger close hook for application shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.getInstance().close();
		}));
		
	}
}
