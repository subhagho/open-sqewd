#!/bin/bash

export DIRS=`echo Zql dal-api dal`
if [ "$1" == "" ];
then
        echo "Usage : $0 <maven target>"
        exit -1
fi

function run
{
        echo "Building $PWD..."
        mvn clean
        mvn $1
}

for dir in `echo $DIRS`
do
	cd $dir
	run $1
	cd -
done
