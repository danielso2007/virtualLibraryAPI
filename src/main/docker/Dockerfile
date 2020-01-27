FROM openjdk:11.0.5-jdk-stretch
MAINTAINER danielso2007@gmail.com
RUN set -eux; \
	apt-get update; \
	apt-get install -y --no-install-recommends \
		ca-certificates \
		jq \
		numactl \
	; \
	if ! command -v ps > /dev/null; then \
		apt-get install -y --no-install-recommends procps; \
	fi; \
	rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]

ARG JAR_FILE

ADD @project.build.finalName@.jar /opt/app.jar
