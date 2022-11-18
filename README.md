# File Migration

This utility was written to allow easy migration of files both locally and across the network.

## Command Line Arguments

| Short     | Long            | Purpose                                                     |
|-----------|-----------------|-------------------------------------------------------------|
| -h        | --help          | Displays the application help prompt.                       |
| -r        | --recursive     | Enables recursive file discovery and migration.             |
| -v        | --verbose       | Enables verbose logging output.                             |
| -c [file] | --config [file] | Specify a custom config path. Default: MigrationConfig.json |
| -l [file] | --log [file]    | Specify a custom log file path. Default: Migration.log      |

### Requires Java 16 or newer.

Author: Andrew Kroll \
Created: 2022-11-18