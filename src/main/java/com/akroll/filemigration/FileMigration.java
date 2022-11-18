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
	 * Flag for recursive file discovery and migration.
	 */
	private static boolean recursive = false;
	
	/**
	 * Flag for verbose log output.
	 */
	private static boolean verbose = false;
	
	/**
	 * Argument for custom configuration files.
	 */
	private static String config = "MigrationConfig.json";
	
	/**
	 * An argument for custom log files.
	 */
	private static String log = "Migration.log";
	
	
	/**
	 * Main application function.
	 * <p>
	 * Valid Arguments:
	 * -r 				--recursive				Enable recursive file migration
	 * -v 				--verbose					Enable verbose logging output
	 * -c [file]	--config [file]		Use the specified config file, rather than MigrationConfig.json
	 * -l [file]  --log [file]      Use the specified log file, rather than
	 *
	 * @param args Unused
	 */
	public static void main(String[] args) {
		// Handle CLI Arguments
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.matches("-\\w*r\\w*") || arg.equalsIgnoreCase("--recursive")) {
				recursive = true;
			}
			if (arg.matches("-\\w*v\\w") || arg.equalsIgnoreCase("--verbose")) {
				verbose = true;
			}
			if (arg.matches("-\\w*c\\w") || arg.equalsIgnoreCase("--config")) {
				if (i < args.length - 1) {
					config = args[i + 1];
				}
				else {
					Logger.getInstance().write("Error: custom configuration file path not specified!");
					return;
				}
			}
			if (arg.matches("-\\w*l\\w*") || arg.equalsIgnoreCase("--log")) {
				if (i < args.length - 1) {
					log = args[i + 1];
				}
				else {
					Logger.getInstance().write("Error: custom log file path not specified!");
					return;
				}
			}
		}
		
		// Save default config if missing
		if (!new File(config).exists()) {
			URL inputUrl = FileMigration.class.getResource("/MigrationConfig.json");
			File dest = new File(config);
			if (dest.getParentFile() != null) {
				dest.getParentFile().mkdirs();
			}
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
		MigrationConfig migrationConfig;
		// Load Migration Configuration
		try {
			migrationConfig = mapper.readValue(new File(config), MigrationConfig.class);
		} catch (IOException e) {
			Logger.getInstance().write("ERROR: Unable to load migration config: " + e.getMessage());
			throw new RuntimeException(e);
		}
		
		// Run migration jobs
		Logger.getInstance().write("---- Beginning File Migration ----");
		
		Arrays.stream(migrationConfig.getJobs())
					.map(job -> new MigrationHandler(job))
					.forEach(MigrationHandler::run);
		
		Logger.getInstance().write("---- File Migration Finished ----\n");
		
		// Add logger close hook for application shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.getInstance().close();
		}));
		
	}
	
	/**
	 * Returns the status of recursive discovery set by the -r flag.
	 *
	 * @return true if recursive, otherwise false
	 */
	public static boolean isRecursive() {
		return recursive;
	}
	
	/**
	 * Returns the status of verbose logging output set by the -v flag.
	 *
	 * @return true if verbose, otherwise false
	 */
	public static boolean isVerbose() {
		return verbose;
	}
	
	/**
	 * Returns the log file path set by the -l flag.
	 *
	 * @return the path to the log file
	 */
	public static String getLogFile() {
		return log;
	}
}
