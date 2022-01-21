# kafka-project

//kafka download

https://kafka.apache.org/downloads

//for starting the zookeeper

./zookeeper-server-start.sh ../config/zookeeper.properties

// for starting broker

./kafka-server-start.sh ../config/server.properties






curl -i \
-d '{"libraryEventId":null,"book":{"bookId":456,"bookName":"Kafka Using Spring Boot","bookAuthor":"Inaam Ishaque"}}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/libraryevent/asynccall
