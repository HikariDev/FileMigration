package com.akroll.filemigration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logging handler class for the FileMigration application.<p>
 * Author: Andrew Kroll<p>
 * Created: 2022-11-18<p>
 * Updated: 2022-11-18
 */
public class Logger {
	
	/**
	 * The current Logger instance to be reused upon request.
	 */
	private static Logger instance;
	
	/**
	 * An instance of a Buffered Writer used to append the existing or new log file.
	 */
	private final BufferedWriter writer;
	
	/**
	 * Creates a new instance of the Logger class.<p>
	 * Creates a new BufferedWriter and Migration.log file.
	 * <p>
	 * Destroys the current instance and throws a RuntimeException upon failure.
	 */
	private Logger() {
		try {
			writer = new BufferedWriter(new FileWriter(FileMigration.getLogFile(), true));
		} catch (IOException e) {
			instance = null;
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a new Logger instance if one does not exist.<p>
	 * Returns the Logger instance.
	 *
	 * @return the active Logger instance
	 */
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	/**
	 * Closes the BufferedWriter and terminates the Logger instance.
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		instance = null;
	}
	
	/**
	 * Appends an ISO-8601 timestamped message to the Migration.log file.<p>
	 * Writes the message to stdout.
	 *
	 * @param message the message to be written.
	 */
	public void write(String message) {
		try {
			String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			System.out.printf("%s >> %s\n", dateTime, message);
			writer.write(String.format("%s >> %s\n", dateTime, message));
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
