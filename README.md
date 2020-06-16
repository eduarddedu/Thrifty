## Serve web app 

Using embedded Tomcat server: 

    mvn -pl backend -P dev spring-boot:run
    
Go to [http://localhost:8080/mythrifty](http://localhost:8080/mythrifty)
    
## Run Junit tests

    mvn -pl backend -P dev -Dspring.profiles.active=dev test
    
## Deploy 

To local WildFly server: 


    mvn -P dev -pl backend clean wildfly:deploy
    
To remote WildFly server: 


    $ mvn -P production -pl backend clean wildfly:deploy