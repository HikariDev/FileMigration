```
 ______ _ _      __  __ _                 _   _             
|  ____(_) |    |  \/  (_)               | | (_)            
| |__   _| | ___| \  / |_  __ _ _ __ __ _| |_ _  ___  _ __  
|  __| | | |/ _ \ |\/| | |/ _` | '__/ _` | __| |/ _ \| '_ \
| |    | | |  __/ |  | | | (_| | | | (_| | |_| | (_) | | | |
|_|    |_|_|\___|_|  |_|_|\__, |_|  \__,_|\__|_|\___/|_| |_|
                           __/ |                            
                          |___/
```

This utility was written to allow easy migration of files both locally and across the network.

## Command Line Arguments

| Short     | Long            | Purpose                                                     |
|-----------|-----------------|-------------------------------------------------------------|
| -h        | --help          | Displays the application help prompt.                       |
| -r        | --recursive     | Enables recursive file discovery and migration.             |
| -v        | --verbose       | Enables verbose logging output.                             |
| -t        | --test          | Disables filesystem changes for testing configurations.     |
| -c [file] | --config [file] | Specify a custom config path. Default: MigrationConfig.json |
| -l [file] | --log [file]    | Specify a custom log file path. Default: Migration.log      |

### Requires Java 16 or newer.

Author: Andrew Kroll \
Created: 2022-11-18