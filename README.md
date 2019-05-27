# sbt-kafka-compose-example

A sample KStreams app with an integration test, showing the usage of sbt-kafka-compose plugin. Note that, we are not using any effects / purely functional libraries here for the streaming app as we are primarly focused on the plugin demo. 

Run:

```
sbt kafkaComposeIntegrationTest

```

Refer to IntegrationTest in `it` folder to see the integration test.
Refer to build.sbt and plugins.sbt on how to configure sbt-kafka-compose.


# Troubleshooting
Explicitly try `sbt kafkaComposeDown` and then start `sbt kafkaComposeIntegrationTest`
