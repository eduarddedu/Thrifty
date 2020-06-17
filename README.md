## 1. Serve 

Serve web app on the embedded Tomcat server: 

    mvn -pl backend -P dev spring-boot:run
    
Go to [http://localhost:8080/mythrifty](http://localhost:8080/mythrifty)
    
## 2. Test

Run JUnit tests: 

    mvn -pl backend -Dspring.profiles.active=dev test
    
## 3. Deploy 

Deploy to WildFly server running locally: 


    mvn -P dev -pl backend clean wildfly:deploy
    
Deploy app to remote WildFly server: 


    $ mvn -P production -pl backend clean wildfly:deploy

