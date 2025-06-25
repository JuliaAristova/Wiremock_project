WIREMOCK

To start stand-alone jar:
java -jar wiremock-standalone-<version>.jar
java -jar wiremock-standalone-3.13.1.jar

To stop: Ctrl C

To kill the process (running instance of Wiremock - if error is displayed_"
To see PID:   netstat -ano | findstr :8080  
taskkill /F /PID <PID>    or  
netstat -ano | findstr :8080
