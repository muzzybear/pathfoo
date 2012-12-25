#!/bin/bash
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
rm -rf builddir
mkdir builddir
javac -d builddir -classpath lwjgl.jar:colt.jar:. test.java && java -classpath lwjgl.jar:colt.jar:builddir test
