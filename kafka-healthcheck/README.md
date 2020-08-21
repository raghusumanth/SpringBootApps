# Kafka Template code for Json Producer/Consumer And Avro Producer

## Url:
http://localhost:9000/healthcheck/post?type=avro|json

## Topic to be created
json-topic
avro-topic

## Avro schema Used:
```
{
  "type": "record",
  "name": "AvroEvent",
  "namespace": "com.kafka.healthcheck.model",
  "fields": [
    {
      "name": "message",
      "type": "string"
    },
    {
      "name": "ids",
      "type": "int"
    },
    {
      "name": "hello",
      "type": "string"
    }
  ]
}
```

# Frequent Errors:
