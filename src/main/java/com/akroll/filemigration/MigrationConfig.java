package com.akroll.filemigration;

/**
 * JSON Data class for the Migration Configuration file.<p>
 * Author: Andrew Kroll<p>
 * Created: 2022-11-18<p>
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
