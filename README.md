Finding the optimal set of configurations for Kafka in order to achieve the fastest possible throughput for real time/stream analytics can be a time-consuming process of trial and error. Automating that process with parametrized JUnit tests can be an excellent way to find optimal Kafka configurations without guess work and without wasting time.

This project contains some JUnit tests that I have found useful for tuning Kafka configurations.  Usage instructions are at the bottom of this README file.

## What factors impact Kafka performance?

[Apache Kafka](http://kafka.apache.org) is a distributed streaming platform. It lets you publish and subscribe to streams of data like a messaging system. You can also use it to store streams of data in a distributed cluster and process those streams in real-time. However, sometimes it can be challenging to publish or consume data at a rate that keeps up with real-time. Optimizing the speed of your producers or consumers involves knowing what specific values to use for a variety of performance related variables:

1. How many worker threads should my producers (or consumers) have?
2. How many topics should my producers send to?
2. How many partitions should my topics have?
3. Should I enable compression? If so, should I use the gzip, snappy, or lz4 codec?
4. How long should my producers wait to allow other records to be sent so that the sends can be batched together?
5. How large should I make those batches?
6. What's the smallest virtual machine configuration that I can use to achieve my real-time throughput requirements?

(By the way, from my experience I found that the first three factors are the most significant - number of threads per producer, number of topics they send to, and the number of partitions in each topic.  I didn't spend much time optimizing the parameters for batching, but my instinct tells me they don't matter as much).

One method of tuning these parameters is to just run a series of incremental unit tests designed to measure throughput over a range of values for a single parameter. JUnit provides an excellent means of performing parameterized unit tests. 

## What is JUnit?

[JUnit](https://en.wikipedia.org/wiki/JUnit) is a unit testing framework for the Java programming language and is by far the most popular framework for developing test cases in Java. 

# What is in this project?

This project includes JUnit tests designed to find which Kafka configurations will maximize the speed at which messages can be published to a Kafka stream. In fact, these unit tests don't so much test anything as produce speed data so that different configurations of Kafka producers can be adjusted to get optimal performance under different conditions. 

The following unit tests are included:

**MessageSizeSpeedTest* measures producer throughput for a variety of message sizes. This test will show how much throughput declines as message sizes increase.
 
**ThreadCountSpeedTest* measures producer throughput for a variety of topic quantities. This test will show how much throughput declines as the producer sends to an increasing quantity of topics.
  
**TopicCountGridSearchTest* explores the effect of number of output topics, buffer size, threading and so on.

**TypeFormatSpeedTest* measures how fast messages can be converted from POJO or JSON data format to Kafka's native byte array format. This is useful for illustrating the speed penalty you pay in Kafka serialization for using complex data types.

# How to I compile and run this project?

## Prerequisites

First install a JDK, maven, and Rscript if you plan on using the provided R script to graph test results.

Next, make sure Kafka and Zookeeper are started.

Update bootstrap.servers in src/test/resources/producer.props to point to the Kafka service.

## Compile and Run

To compile and run, just run, `mvn package`.

That will output test data to `size-count.csv`, `thread-count.csv`, and `topic-count.csv`. If you have Rscript installed then you can create performance graph images like this:

```Rscript src/test/R/draw-speed-graphs.r```

Open the resulting .png files to see your results.

# How to run on MapR

MapR Streams complies with the Kafka API, so these tests can be executed on a MapR cluster simply by loading the MapR Kafka client in pom.xml. Simply checkout the `mapr` branch of this repo and run the same maven command shown above, like this:

```
git checkout mapr
mvn package
```

## Caveats

Sometimes these tests require a lot of memory. You'll know when you run out of heap if you see a "queue full" exception. If that happens, edit the pom.xml and increase the JVM heap in the Xmx parameter.

Also, make sure you don't run out of disk space. In zookeeper.properties (under the config dir, where ever you installed Kafka) make sure dataDir is pointed to a drive with lots of space.