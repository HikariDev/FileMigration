package com.akroll.filemigration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * Logging handler class for the FileMigration application.
 * Author: Andrew Kroll
 * Created: 2022-11-18
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
	 * Creates a new instance of the Logger class.
	 * Creates a new BufferedWriter and Migration.log file.
	 * <p>
	 * Destroys the current instance and throws a RuntimeException upon failure.
	 */
	private Logger() {
		try {
			writer = new BufferedWriter(new FileWriter(new File("Migration.log")));
		} catch (IOException e) {
			instance = null;
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a new Logger instance if one does not exist.
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
	 * Appends an ISO-8601 timestamped message to the Migration.log file.
	 * Writes the message to stdout.
	 *
	 * @param message the message to be written.
	 */
	public void write(String message) {
		try {
			System.out.println(String.format("%s >> %s\n", Instant.now().toString(), message));
			writer.write(String.format("%s >> %s\n", Instant.now().toString(), message));
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
