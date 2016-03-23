To run, type mvn clean install exec:java -Dexec.mainClass="com.codurance.social.app.SocialApplication" 

The project uses Spring for Dependency Injection and 

an embedded Neo4J database. As such, messages are available after application restart.

Over the network functionality is disabled, though available

via spring configuration of the classes in package 

com.codurance.social.ha (which uses Hazelcast). 

This is an extension point that could be used 

to make interaction feasible oer the network, and rather cheaply in 

terms of effort. It would be necessary to make the model entities 

properly serialisable for message exchange to work in Hazelcast.

What would be interesting would be to make the storage network aware

(through Neo4J's rest API) and provide high availability through Hazelcast

listeners so that as soon as a message is posted, it is immediately published

to all interested user clients (consoles, browsers, etc)

 and the clients would not need to poll storage.