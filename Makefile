SHELL := /bin/bash

MAKEFLAGS := --silent --no-print-directory

.DEFAULT_GOAL := help

help: ## Show the list of commands
	@echo "Please use 'make <target>' where <target> is one of"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z0-9\._-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

spigot.build: ## Retrieve the BuildTools.jar from Spigot and build required versions to the maven repository, usage: make spigot.build version=<version>
ifeq ($(and $(version)),)
	echo "You need to define a version parameter in order to run this command, usage: make spigot.build version=<version>"
	exit 1
endif
	mkdir -p build
	cd build \
		&& curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o BuildTools.jar \
		&& java -jar BuildTools.jar --rev ${version}

spigot.test: ## Test the plugin with a specified spigot revision
ifeq ($(and $(version)),)
        echo "You need to define a version parameter in order to run this command, usage: make spigot.build version=<version>"
        exit 1
endif
	mkdir -p spigot-test
	cd spigot-test
	echo "This command is still work in progress"

docker.run: ## Run an interactive shell to test the application in docker
	#docker run -itv ${CURDIR}:/app --workdir=/app -u 1000 openjdk-8 bash
	echo "This command is still work in progress"
