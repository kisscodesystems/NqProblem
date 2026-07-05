#!/bin/bash

# 1. Compile the sources into a fresh output directory.
#    NqProblem is dependency-free; no external libraries are needed.
javac -d bin src/com/kisscodesystems/NqProblem/*.java

# 2. Package a runnable jar using the bundled manifest (it sets Main-Class).
cd bin && jar cvfm NqProblem.jar ../src/com/kisscodesystems/NqProblem/manifest.txt com/kisscodesystems/NqProblem/*.class

cp NqProblem.jar ../

echo ""
echo "You can now start your application by"
echo "java -jar NqProblem.jar i 8 q r a"
