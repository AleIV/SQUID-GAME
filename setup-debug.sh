#!/bin/bashS
set -x
# Variables
VERSION="1.16.5"
BUILD_NUMBER="1171"
DOWNLOAD_URL="https://api.pl3x.net/v2/purpur/${VERSION}/${BUILD_NUMBER}/download"
SERVER_JAR="server.jar"
# If debug dir doesn't exist, create it
if [ ! -d "debug" ]; then
    # Create the dir
    mkdir debug
    mkdir debug/plugins
fi
# Download the server jar if not already present
if [ ! -f "debug/${SERVER_JAR}" ]; then
    # Download the jar
    echo "Downloading ${DOWNLOAD_URL}"
    curl -o debug/${SERVER_JAR} ${DOWNLOAD_URL}
fi
# Accept eula if there isn't a file for eula.txt
if [ ! -f "debug/eula.txt" ]; then
    # Download the jar
    echo "Accepting eula"
    echo "eula=true" >debug/eula.txt
fi
