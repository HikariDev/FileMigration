package com.akroll.filemigration;

/**
 * A JSON object for Migration Jobs.<p>
 * Author: Andrew Kroll<p>
 * Created: 2022-11-18<p>
 * Edited: 2022-11-18
 */
public class MigrationJob {
	
	/**
	 * The title of the migration job for reference.
	 */
	public String title;
	
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
}
