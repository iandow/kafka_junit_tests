

This project contains JUnit tests for tuning Kafka configurations.  

# What is the purpose of this project?

[Apache Kafka](http://kafka.apache.org) is a distributed streaming platform. It lets you publish and subscribe to streams of data like a messaging system. You can also use it to store streams of data in a distributed cluster and process those streams in real-time. However, sometimes it can be challenging to publish or consume data at a rate that keeps up with real-time. Optimizing the speed of your producers or consumers involves knowing what specific values to use for a variety of performance related variables.

One method of tuning these parameters is to just run a series of incremental unit tests designed to measure throughput over a range of values for a single parameter.  However, determining which configurations produce the best possible Kafka performance can be a time-consuming process of trial and error. Automating that process with parametrized JUnit tests is an excellent way to optimize Kafka without guess work and without wasting time.

## What is JUnit?

[JUnit](https://en.wikipedia.org/wiki/JUnit) is a unit testing framework for the Java programming language and is by far the most popular framework for developing test cases in Java. 

# What is in this project?

This project includes JUnit tests designed to find which Kafka configurations will maximize the speed at which messages can be published to a Kafka stream. In fact, these unit tests don't so much test anything as produce speed data so that different configurations of Kafka producers can be adjusted to get optimal performance under different conditions. 

The following unit tests are included:

1. *MessageSizeSpeedTest* measures producer throughput for a variety of message sizes. This test will show how much throughput declines as message sizes increase.
 
2. *ThreadCountSpeedTest* measures producer throughput for a variety of topic quantities. This test will show how much throughput declines as the producer sends to an increasing quantity of topics.
  
3. *TopicCountGridSearchTest* explores the effect of number of output topics, buffer size, threading and so on.

4. *TypeFormatSpeedTest* measures how fast messages can be converted from POJO or JSON data format to Kafka's native byte array format. This is useful for illustrating the speed penalty you pay in Kafka serialization for using complex data types.

# How do I compile and run this project?

## Prerequisites

Download and run this code on a Kafka or MapR cluster.
 
Install a JDK and maven if you haven't already.
 
If you want to graph your test results, install Rscript, too.

Start Kafka and Zookeeper services.

Update bootstrap.servers in src/test/resources/producer.props to point to the Kafka service.

## Compile and Run

To compile and run, just cd to the root directory  of the project and run `mvn package`.

That will output test data to `size-count.csv`, `thread-count.csv`, and `topic-count.csv`. 

You can graph performance results like this:

```Rscript src/test/R/draw-speed-graphs.r```

Open the resulting .png image files to see your results.  Here is an example of performance data graphed from the TopicCountGridSearchTest test:

![Producer Throughput on a Kafka 3 node cluster](thread.png?raw=true "Producer Throughput on a Kafka 3 node cluster")

# How to run on MapR

MapR Streams complies with the Kafka API, so these tests can be executed on a MapR cluster, too. Simply checkout the `mapr` branch and run maven, like this:

```
git checkout mapr
mvn package
```

## Caveats

Sometimes these tests require a lot of memory. You'll know when you run out of heap if you see a "queue full" exception. If that happens, edit the pom.xml and increase the JVM heap in the Xmx parameter.

Also, make sure you don't run out of disk space. In zookeeper.properties (under the config dir, where ever you installed Kafka) make sure dataDir is pointed to a drive with lots of space.