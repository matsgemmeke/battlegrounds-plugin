SHELL := /bin/bash

MAKEFLAGS := --silent --no-print-directory

.DEFAULT_GOAL := help

help: ## Show the list of commands
	@echo "Please use 'make <target>' where <target> is one of"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z0-9\._-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

spigot.build: ## Retrieve the BuildTools.jar from Spigot and build required versions to the maven repository
	mkdir -p build
	cd build \
		&& curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o BuildTools.jar \
		&& java -jar BuildTools.jar --rev 1.8.8 \
        && java -jar BuildTools.jar --rev 1.9.2 \
        && java -jar BuildTools.jar --rev 1.9.4 \
        && java -jar BuildTools.jar --rev 1.10.2 \
        && java -jar BuildTools.jar --rev 1.11.2 \
        && java -jar BuildTools.jar --rev 1.12.2 \
        && java -jar BuildTools.jar --rev 1.13 \
        && java -jar BuildTools.jar --rev 1.13.1
