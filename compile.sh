#!/bin/bash

echo "---------------------------- Creating java file from jflex file -------------------------------"
java -jar lib/jflex-1.6.1.jar src/ctds.jflex

echo "---------------------------- Creating java file from cup file -------------------------------"
java -jar lib/java-cup-11b.jar -destdir src/ src/ctds.cup 


echo "---------------------------------- Compiling java files ---------------------------------------"
mkdir -p classes
javac -cp lib src/*.java -d classes/