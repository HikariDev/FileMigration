package com.akroll.filemigration;

/**
 * JSON Data class for the Migration Configuration file.
 * Author: Andrew Kroll
 * Created: 2022-11-18
 * Updated: 2022-11-18
 */
public class MigrationConfig {
	
	/**
	 * A list of Migration Jobs from the FileMigration.json file.
	 */
	public MigrationJob[] jobs;
	
	/**
	 * Retrieves an array of registered Migration Jobs.
	 *
	 * @return an array of Migration Jobs
	 */
	public MigrationJob[] getJobs() {
		return jobs;
	}
	
}
