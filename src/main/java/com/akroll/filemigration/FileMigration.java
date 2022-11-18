package com.akroll.filemigration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FileMigration {
	
	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		MigrationConfig config = mapper.readValue(new File("MigrationConfig.json"), MigrationConfig.class);
		
		
		// Add logger close hook for application shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.getInstance().close();
		}));
		
	}
}
