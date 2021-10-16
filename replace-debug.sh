#!/bin/bash
./gradlew build
cp build/libs/*-all.jar debug/plugins/
