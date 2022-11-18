package com.akroll.filemigration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class Logger {
	
	private static Logger instance;
	private final BufferedWriter writer;
	
	private Logger() {
		try {
			writer = new BufferedWriter(new FileWriter(new File("Migration.log")));
		} catch (IOException e) {
			instance = null;
			throw new RuntimeException(e);
		}
	}
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		instance = null;
	}
	
	public void write(String message) {
		try {
			writer.write(String.format("%s >> %s\n", Instant.now().toString(), message));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
