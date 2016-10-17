png(file="topics.png", width=800, height=500, pointsize=16)
x = read.csv("topic-count.csv")
boxplot(batchRate/1e6 ~ topicCount + batchSize, x, 
        xlab=c("Topics"), ylab="Millions of Messages / second", ylim=c(0,2),
        col=rainbow(3)[ceiling((1:16)/4)], xaxt='n')
axis(1,labels=as.character(rep(c(100,300,1000,2000),4)), at=(1:16), las=3)
legend(x=10,y=1.9,legend=c(0,16384,65536), col=rainbow(3), fill=rainbow(3), title="batch.size")
abline(v=4.5, col='lightgray')
abline(v=8.5, col='lightgray')
dev.off()



png(file="thread.png", width=800, height=500, pointsize=16)
x = read.csv("thread-count.csv")
boxplot(batchRate/1e6 ~ topicCount + threadCount, x, ylim=c(0,2.1), 
        ylab="Millions of messages / second", xlab="Topics",
        col=rainbow(4)[ceiling((1:30)/6)], xaxt='n')
axis(1,labels=as.character(rep(c(50,100,200,500,1000,2000),6)), at=(1:36), las=3)
legend(x=32,y=2.1,legend=c(1,2,5,10,15,20), col=rainbow(4), fill=rainbow(4), title="Threads")
abline(v=6.5, col='lightgray')
abline(v=12.5, col='lightgray')
abline(v=18.5, col='lightgray')
abline(v=24.5, col='lightgray')
abline(v=30.5, col='lightgray')
dev.off()



png(file="size-count.png", width=800, height=500, pointsize=16)
x = read.csv("size-count.csv")
boxplot(batchRate/1e6 ~ topicCount + messageSize, x, ylim=c(0,1.0),
        ylab="Millions of messages / second", xlab="Message Size (bytes)",
        col=rainbow(6)[ceiling((1:36)/6)], xaxt='n')
axis(1,labels=as.character(rep(c(100,500,1000,2000,10000,50000,100000),7)), at=(1:49), las=3)
dev.off()
