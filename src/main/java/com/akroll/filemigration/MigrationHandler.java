package com.akroll.filemigration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Handler class for Migration Jobs.
 * Author: Andrew Kroll
 * Created: 2022-11-18
 * Updated: 2022-11-18
 */
public class MigrationHandler {
	
	/**
	 * The Migration Job that will be handled by the Migration Handler.
	 */
	private final MigrationJob job;
	
	/**
	 * Crates a new Migration Handler for the passed Migration Job.
	 *
	 * @param job the job to handle
	 */
	public MigrationHandler(MigrationJob job) {
		this.job = job;
	}
	
	/**
	 * Runs the Migration Job
	 */
	public void run() {
		Logger.getInstance().write("Beginning migration job " + job.title);
		
		// Replace slashes with system appropriate
		job.from = job.from.replaceAll("/\\\\", File.separator);
		job.to = job.to.replaceAll("/\\\\", File.separator);
		
		// Append trailing slash if missing
		if (!job.from.endsWith(File.separator)) {
			job.from += File.separator;
		}
		if (!job.to.endsWith(File.separator)) {
			job.to += File.separator;
		}
		
		Set<File> migrationFiles = discover(job.from); // Collect all files within the path
		migrationFiles.stream()
					.filter(file -> checkFilters(file.getName())) // Remove filtered files
					.filter(this::checkConflict) // Remove conflicting files
					.forEach(this::migrate);
		
		Logger.getInstance().write("Finished migration job " + job.title);
	}
	
	/**
	 * Performs recursive discovery on the passed target path for files to migrate.
	 *
	 * @param path the absolute path to discover.
	 * @return a set of files which must be migrated.
	 */
	public Set<File> discover(String path) {
		Set<File> files = new HashSet<>();
		
		File target = new File(path); // Starting path
		if (target.exists()) { // Is the starting path valid?
			if (target.isDirectory()) { // If the starting path is a directory, discover its children
				for (File file : target.listFiles()) {
					files.addAll(discover(file.getAbsolutePath()));
				}
			}
			else { // If the starting path is a file, add it to files
				files.add(target);
			}
		}
		
		// Return the discovered files
		return files;
	}
	
	/**
	 * Compares the passed file name to this job's regex filters.
	 *
	 * @param file the name of the file to check
	 * @return true if the file passes, otherwise false
	 */
	public boolean checkFilters(String file) {
		if (job.filters.length == 0) { // No filters present; accept everything.
			return true;
		}
		for (String filter : job.filters) { // Filters present
			if (Pattern.compile(filter).matcher(file).matches()) { // Accept only if a filter matches
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks for file conflicts in the destination directory.
	 * Files which would conflict, but are newer than the destination, will pass this check.
	 *
	 * @param file the path to the file relative to the source directory
	 * @return true if the file can be migrated, otherwise false
	 */
	public boolean checkConflict(File file) {
		String destination = job.to + file.getAbsolutePath().substring(job.from.length());
		File target = new File(destination);
		if (target.exists()) { // If the target exist in the destination
			if (target.lastModified() > file.lastModified()) { // If the target is newer than the source
				Logger.getInstance().write("Skipping outdated file: " + file.getAbsolutePath());
				return false; // Do not migrate
			}
		}
		return true; // Otherwise migrate
	}
	
	/**
	 * Migrates a file from the source directory to the destination directory.
	 * Removes the conflicting file one exists.
	 *
	 * @param file the file to move
	 */
	public void migrate(File file) {
		String destination = job.to + file.getAbsolutePath().substring(job.from.length());
		if (new File(destination).exists()) {
			Logger.getInstance().write(String.format("Removing existing file: %s", destination));
		}
		Logger.getInstance().write(String.format("Migrating: %s -> %s", file.getAbsolutePath(), destination));
		file.renameTo(new File(destination));
	}
}
