To run, type mvn clean install exec:java -Dexec.mainClass="com.codurance.social.app.SocialApplication" 

The project uses Spring for dependency Injection and the Neo4J 

database. As such, messages are available after application restart.

Over the network functionality is disabled, though available

via spring configuration of the classes in package 

com.codurance.social.ha (which uses Hazelcast). 

This is an extension point that could be used 

to make interaction feasible oer the network, and rather cheaply in 

terms of effort. It would be necessary to make the model entities 

properly serialisable for message exchange to work in Hazelcast.