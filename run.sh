#!/bin/bash
export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
rm *.class
javac -classpath lwjgl.jar:lwjgl_util.jar:colt.jar:. test.java && java -classpath lwjgl.jar:colt.jar:. test
