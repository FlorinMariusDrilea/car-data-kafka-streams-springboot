# How to test it / run the streaming app and rest api app

First start zookeper server in kafka folder:

- zookeeper-server-start ./config/zookeeper.properties

Then start kafka app to detect input topic and create other topics if needed (output-vehicle-info)

- bin/kafka-server-start.sh config/server.properties

To ensure that the topics are correctly populated, or resetting it try to delete the topics if they exist, before testing, cause when you'll start the app,
it will automatically recreate both input and ouput topics

- ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --delete --topic input-vehicle-info
- ./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --delete --topic output-vehicle-info

After this - use a POSTMAN to send a json type format at http://localhost:8080/post

To monitorize input and output topics use the following commands:

- kafka-console-consumer --bootstrap-server=localhost:9092 --topic input-vehicle-info --from-beginning
- kafka-console-consumer --bootstrap-server=localhost:9092 --topic output-vehicle-info --from-beginning

## Producer Service REST API SPRING

This is a simple REST API using spring boot framework in order to send using POSTMAN a json file to http://localhost:8080/post , and send it into the input topic
which produces to the output topic on order to receive the desired output.

Input: 


{
    "forwardedAt":"2022-06-13T07:47:25.463Z",
    "subscriptionId":"12356-RRRRR-56744-AAAAAA",
    "inVehicleResponse":{
        "version":"1.0",
        "deliveredAt":"2022-06-13T07:47:25.215618311Z",
        "inVehicleData":[
            {
            "response":{
                "mileage":{
                    "dataPoint":{
                        "value":13249.0,
                        "unit":"km",
                        "timestamp":"2022-06-13T07:47:25.21Z"
                        }
                    }
                },
                "identifier":{
                    "type":"VIN",
                    "value":"IOOOXXFFFNKU9999"
                }
            }
        ]
    }
}

Output:

{"time":"2022-06-13T07:47:25.21Z","odometer":13249000,"unit":"km","source_id":"honda-IOOOXXFFFNKU9999","source_type":"honda","message_id":"d4908445-b4af-4390-b421-4dff7d314053","timestamp_millis":1678026501629}

Most of the work in this case is done in rest apis as we match the json format and filter the values so it can output a new object (VehicleOutput) with the desired output


### Producing message json in input topic and have an output, just with kafkastreaming app

Use:
- kafka-console-producer --bootstrap-server=localhost:9092 --topic input-vehicle-info
> {"forwardedAt":"2022-06-13T07:47:25.463Z","subscriptionId":"12356-RRRRR-56744-AAAAAA","inVehicleResponse":{"version":"1.0","deliveredAt":"2022-06-13T07:47:25.215618311Z","inVehicleData":[{"response":{"mileage":{"dataPoint":{"value":13249.0,"unit":"km","timestamp":"2022-06-13T07:47:25.21Z"}}},"identifier":{"type":"VIN","value":"IOOOXXFFFNKU9999"}}]}}} 

Output: 
- kafka-console-consumer --bootstrap-server=localhost:9092 --topic output-vehicle-info --from-beginning
{"time":"2022-06-13T07:47:25.21Z","odometer":13249000,"unit":"km","source_id":"honda-IOOOXXFFFNKU9999","source_type":"honda","message_id":"311acd3e-0a67-4456-8bdf-ae4ed655e01f","timestamp_millis":1655102845021}
