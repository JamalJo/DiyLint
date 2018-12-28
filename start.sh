#!/bin/bash
./gradlew jar
mv lintjar/build/libs/lintjar.jar  ~/.android/lint/lint.jar
./gradlew lint