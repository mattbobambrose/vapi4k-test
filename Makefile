VERSION=0.0.1

default: versioncheck

clean:
	./gradlew clean

compile:
	./gradlew build -x test

jar:
	./gradlew buildFatJar

run-jar: jar
	./gradlew runFatJar

versioncheck:
	./gradlew dependencyUpdates


# Assign your docker hub username here
IMAGE_NAME := docker_hub_username/myapp
PLATFORMS := linux/amd64,linux/arm64/v8

build-docker:
	docker build -t ${IMAGE_NAME}:${VERSION} .

run-docker:
	docker run --rm -p 8080:8080 ${IMAGE_NAME}:${VERSION}

push-docker:
	# prepare multiarch
	docker buildx use buildx 2>/dev/null || docker buildx create --use --name=buildx
	docker buildx build --platform ${PLATFORMS} --push -t ${IMAGE_NAME}:latest -t ${IMAGE_NAME}:${VERSION} .

release: clean compile jar build-docker push-docker

upgrade-wrapper:
	./gradlew wrapper --gradle-version=8.9 --distribution-type=bin
