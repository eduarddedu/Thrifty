## 1. Serve Web App

Serve web app using spring-boot. 

    mvn -pl backend -P dev spring-boot:run
    
    
## 2. Test

Run JUnit tests.

    mvn -pl backend -P dev test
    

## 3. Deploy Web App

Upload `thrifty.war` to a local WildFly server. 


    mvn  -pl backend -P dev clean wildfly:deploy
    
Deploy to a remote WildFly server.


    $ mvn  -pl backend -P prod clean wildfly:redeploy

