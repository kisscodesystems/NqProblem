#!/bin/bash
#
# Runs the NqProblem regression tests.
#
# It compiles the current sources, then compiles and runs the JUnit test that
# checks that the original ("o 14") and improved ("i 14") modes both count 365596
# solutions for the 14 queens problem.
#
set -e
cd "$(dirname "$0")/.."
ROOT="$(pwd)"

SRC="src/com/kisscodesystems/NqProblem"
BUILD="$ROOT/build/testrun"
JARS="$ROOT/lib"
JUNIT="$JARS/junit-4.12.jar"
HAMCREST="$JARS/hamcrest-core-1.3.jar"

rm -rf "$BUILD"
mkdir -p "$BUILD/main_out" "$BUILD/test_out"

# 1. Compile the current sources.
javac -d "$BUILD/main_out" "$SRC"/*.java

# 2. Compile and run the tests.
CP="$BUILD/main_out:$JUNIT:$HAMCREST"
javac -cp "$CP" -d "$BUILD/test_out" test/com/kisscodesystems/NqProblem/NqProblemTest.java
java  -cp "$CP:$BUILD/test_out" org.junit.runner.JUnitCore com.kisscodesystems.NqProblem.NqProblemTest
