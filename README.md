# HUMAN RESOURCE MANAGEMENT
## 1. Requirements
- Install Java SDK 1.8 and set up environment
- Install Maven
- Install Redis
## 2. Setting for writing log to file
- Step 1: Access to application.properties file following path src/main/resources/application.properties
- Step 2: Add the sentences below into application.properties file.

    ```
    logging.level.root=warn
    ```
  
    ```
    logging.pattern.console= %d{yyyy-MMM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15} - %msg%n
    ```
  
    ```
    logging.file.name = log/log.log
    ```

   These configurations will write the status what is greater than or equal to WARN level to file. Change logging.level.root value to change this configuration.
   
   Change path of logging.file.name to change directory and file name of log file.
## 3. Install Redis on CentOS
- Step 1: Install the Redis package

    ```
    sudo yum install redis
    ```

- Step 2: Start the Redis service and enable it to start automatically on the boot

    ```
    sudo systemctl start redis
    ```
  
    ```
    sudo systemctl enable redis
    ```


- Step 3: Check the status of the service

    ```
    sudo systemctl status redis
    ```