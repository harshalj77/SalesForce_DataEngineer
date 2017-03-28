# Salesforce_DataEngineer

Please find the Slide1.jpg to see the flow of the system

Dependencies:: 
redis-server --port 6379

How to run the Application::

1. Start Zookeeper: bin/zkServer.sh start on localhost://2181
2. Start Kafka:: bin/kafka-server-start.sh config/server.properties
3. Storm :: bin/storm nimbus bin/storm supervisor
4. POST localhost:8000?url=<URL>
Eg::<URL> = "https://www.amazon.com/gp/product/B00IB1BTWI/"
