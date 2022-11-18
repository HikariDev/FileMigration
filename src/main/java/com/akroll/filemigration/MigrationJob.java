package com.akroll.filemigration;

import java.io.File;
import java.util.List;

public class MigrationJob extends Thread {
	
	/**
	 * The absolute path to migrate files from.
	 */
	public String from;
	
	/**
	 * The absolute path to migrate files to.
	 */
	public String to;
	
	/**
	 * A list of regex string filters to apply to file names.
	 */
	public String[] filters;
	
	/**
	 * Runs the Migration Job
	 */
	public void run() {
		List<File> migrationFiles = discover(from); // Collect all files within the path
		migrationFiles = migrationFiles.stream().filter(file -> checkFilters(file.getName())).toList(); // Remove filtered files
		
	}
	
	/**
	 * Performs discovery on the passed directory and all subdirectories for files to migrate.
	 *
	 * @param path the absolute path to discover.
	 * @return a list of files which must be migrated.
	 */
	public List<File> discover(String path) {
		return null;
	}
	
	/**
	 * Checks for file conflicts in the destination directory.
	 * Files which would conflict, but are newer than the destination, will pass this check.
	 *
	 * @param file the path to the file relative to the source directory
	 * @return true if the file can be migrated, otherwise false
	 */
	public boolean checkConflict(String file) {
		return false;
	}
	
	/**
	 * Compares the passed file name to this job's regex filters.
	 *
	 * @param file the name of the file to check
	 * @return true if the file passes, otherwise false
	 */
	public boolean checkFilters(String file) {
		if (filters.length == 0) { // No filters present; accept everything.
			return true;
		}
		for (String filter : filters) { // Filters present
			if (file.matches(filter)) { // Accept only if a filter matches
				return true;
			}
		}
		return false;
	}
	
	
}
