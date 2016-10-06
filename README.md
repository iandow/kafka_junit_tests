
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

I recently developed some JUnit tests to find which Kafka configurations would maximize the speed at which I could publish messages into a Kafka stream. In fact, these unit tests don't so much test anything as produce speed data so that different configurations of Kafka producers can be adjusted to get optimal performance under different conditions. 

# How to I compile and run this project?

First install a JDK, maven, and optionally Rscript.

To compile and run, do this:

{% highlight bash %}
mvn -e -Dtest=TopicCountGridSearchTest,ThreadCountSpeedTest test
{% endhighlight %}

That will generate two csv files.  You can use the provided R script to visualize your results, like this:

{% highlight bash %}
Rscript draw-speed-graphs.r
{% endhighlight %}

Then open the two png files to see your results.
