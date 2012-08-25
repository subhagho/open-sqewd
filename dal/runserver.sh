#!/bin/bash

export BINDIR=./target/

CLASSPATH=$CLASSPATH:.

for JAR in `ls $BINDIR/*.jar`
do
	CLASSPATH=$CLASSPATH:$JAR
done
for JAR in `ls $BINDIR/lib/*.jar`
do
	CLASSPATH=$CLASSPATH:$JAR
done
export CLASSPATH
echo "Using classpath $CLASSPATH..."
/usr/bin/java -cp $CLASSPATH $@
