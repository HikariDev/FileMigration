package com.akroll.filemigration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Main class for the FileMigration application.<p>
 * This application is intended for frequent migration of files from an indefinite number of source and destination directory pairs.<p>
 * Author: Andrew Kroll<p>
 * Created: 2022-11-18<p>
 * Updated: 2022-11-18<p>
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
	 * Flag for test mode.
	 */
	private static boolean test = false;
	
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
	 * Valid Arguments:<p>
	 * -h					--help						Displays application help.<p>
	 * -r 				--recursive				Enable recursive file discovery and migration.<p>
	 * -v 				--verbose					Enable verbose logging output.<p>
	 * -t         --test						Disable file removal and migration for testing configurations.<p>
	 * -c [file]	--config [file]		Use the specified config file. Defaults to MigrationConfig.json.<p>
	 * -l [file]  --log [file]      Use the specified log file. Defaults to Migration.log.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		// Handle CLI Arguments
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.matches("-\\w*h\\w*") || arg.equalsIgnoreCase("--help")) {
				Logger.getInstance().write("""
									
							  ______ _ _      __  __ _                 _   _            \s
							 |  ____(_) |    |  \\/  (_)               | | (_)           \s
							 | |__   _| | ___| \\  / |_  __ _ _ __ __ _| |_ _  ___  _ __ \s
							 |  __| | | |/ _ \\ |\\/| | |/ _` | '__/ _` | __| |/ _ \\| '_ \\\s
							 | |    | | |  __/ |  | | | (_| | | | (_| | |_| | (_) | | | |
							 |_|    |_|_|\\___|_|  |_|_|\\__, |_|  \\__,_|\\__|_|\\___/|_| |_|
							                            __/ |                           \s
							                           |___/                            \s
							Author: Andrew Kroll
							Created: 2022-11-18
							https://github.com/HikariDev/FileMigration
								
							Command Line Arguments:
								-h			--help				Displays this help prompt.
								-r			--recursive			Enables recursive file discovery and migration.
								-v			--verbose			Enables verbose logging output.
								-t 			--test				Disables file removal and migration for testing configurations.
								-c [file]	--config [file]		Uses the specified config file. Defaults to MigrationConfig.json.
								-l [file]	--log [file]		Uses the specified log file. Defaults to Migration.log.
								
							 Example Commands:
							 	java -jar FileMigration.jar -rvt
							  	java -jar FileMigration.jar -vl /var/log/migration.log
							 	java -jar FileMigration.jar -rtc C:\\test\\migration-config.json
							""");
			}
			if (arg.matches("-\\w*r\\w*") || arg.equalsIgnoreCase("--recursive")) {
				recursive = true;
			}
			if (arg.matches("-\\w*v\\w") || arg.equalsIgnoreCase("--verbose")) {
				verbose = true;
			}
			if (arg.matches("-\\w*t\\w*") || arg.equalsIgnoreCase("--test")) {
				test = true;
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
	 * Returns the status of test mode set by the -t flag.
	 *
	 * @return true if test, otherwise false
	 */
	public static boolean isTest() {
		return test;
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
