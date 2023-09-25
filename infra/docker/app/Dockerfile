#####################
# Multi stage build #
#####################

ARG GITHUB_SHA=NO_COMMIT
ARG VERSION=NO_VERSION

########################################
# Build rapportnav backend with maven #
########################################
FROM maven:3.8.5-openjdk-17-slim as build-backend

ARG GITHUB_SHA
ARG VERSION

WORKDIR /tmp/backend
COPY backend/pom.xml pom.xml
RUN npm config set proxy http://172.27.229.197:8090
RUN npm config set https-proxy http://172.27.229.197:8090
RUN mvn dependency:go-offline -B

COPY backend/ /tmp/backend
COPY backend/pom.xml /tmp/backend/POM_WITH_ENV_VARS
RUN sed -e 's/COMMIT_TO_CHANGE/${GITHUB_SHA}/' \
  -e 's/VERSION_TO_CHANGE/${VERSION}/' \
  POM_WITH_ENV_VARS > pom.xml

RUN mvn clean package -DskipTests=true



########################################
# Build frontend                       #
########################################
# Stage 1: Build npm dependencies
FROM node:18 AS npm-dependencies
WORKDIR /tmp/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm config set proxy http://172.27.229.197:8090
RUN npm config set https-proxy http://172.27.229.197:8090
RUN npm ci

# Stage 2: Build the application
FROM node:18 AS build-frontend
WORKDIR /tmp/frontend
COPY --from=npm-dependencies /tmp/frontend/node_modules ./node_modules
COPY frontend .
RUN npm run build --verbose



########################################
# Run Application                      #
########################################
FROM azul/zulu-openjdk-debian:17-latest as build-all

WORKDIR /home/rapportnav

ARG VERSION
ENV VERSION=$VERSION

ARG ENV_PROFILE
ENV ENV_PROFILE=$ENV_PROFILE

ENV SPRING_PROFILES_ACTIVE=$ENV_PROFILE

# ENV REACT_APP_ENV=$ENV_PROFILE
ENV ENV_DB_URL=""

# copy backend related things
COPY --from=build-backend /tmp/backend/target/rapportnav-${VERSION}.jar /home/rapportnav
COPY infra/configurations/backend /home/rapportnav/configurations/


RUN mkdir /home/rapportnav/public
# COPY --from=buildSourcemaps /tmp/frontend/build /home/rapportnav/public/
COPY --from=build-frontend /tmp/frontend/dist /home/rapportnav/public/
# COPY --from=build-frontend /tmp/infra/docker/tmp/env.sh /home/rapportnav/

# Add logs folder to be mounted as volume
RUN mkdir /home/rapportnav/logs

# Set up environement variable that define the root folder use for serving static files
# It must point to the front (React) files
ENV STATIC_FILES_PATH=/home/rapportnav/public


# CMD exec java -jar "/home/rapportnav/rapportnav-${VERSION}.jar"
CMD exec java -Dspring.config.additional-location="/home/rapportnav/configurations/" -Dspring.profiles.active=$ENV_PROFILE -jar "/home/rapportnav/rapportnav-${VERSION}.jar"
