#!/bin/bash

export CLASSPATH=classes
for file in `ls lib`; do export CLASSPATH=$CLASSPATH:lib/$file; done

java -cp $CLASSPATH Main $@