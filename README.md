Serve app 


    mvn -pl backend -P dev spring-boot:run
    
Deploy to WildFly server running on localhost


    mvn -pl backend -P dev wildfly:deploy
    
Deploy the _production_ version of the app to the WildFly server running on codecritique.org


    $ mvn -pl backend -P prod wildfly:deploy