###Run app 

    mvn -pl backend spring-boot:run
    
###Deploy to Wildfly server running locally

    mvn -pl backend -P deploy wildfly:deploy
    
###Redeploy

    mvn -pl backend -P deploy wildfly:redeploy
    
###Deploy to production server

    $ ssh -L 9990:localhost:9990 root@hostname