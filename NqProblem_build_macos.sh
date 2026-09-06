#!/bin/bash

# This is the macos version. Nothing of this build is platform specific: javac, jar
# and the : separator of the classpath work the same way on linux and on macos, so
# this script is the very same as NqProblem_build_linux.sh.

# 1. Compile the sources into a fresh output directory.
#    NqProblem is dependency-free; no external libraries are needed.
javac -d bin src/com/kisscodesystems/NqProblem/*.java

# 2. Package a runnable jar using the bundled manifest (it sets Main-Class).
cd bin && jar cvfm NqProblem.jar ../src/com/kisscodesystems/NqProblem/manifest.txt com/kisscodesystems/NqProblem/*.class

cp NqProblem.jar ../

echo ""
echo "You can now start your application by"
echo "java -jar NqProblem.jar i 8 q r a"
