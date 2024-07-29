
FROM azul/zulu-openjdk-alpine:17.0.12-jre-headless AS build-all

WORKDIR /home/rapportnav

ARG VERSION
ENV VERSION=$VERSION

ARG ENV_PROFILE
ENV ENV_PROFILE=$ENV_PROFILE

ENV SPRING_PROFILES_ACTIVE=$ENV_PROFILE

# ENV REACT_APP_ENV=$ENV_PROFILE
ENV ENV_DB_URL=""

# copy backend related things
COPY backend/build/libs/rapportnav-${VERSION}.jar /home/rapportnav
COPY infra/configurations/backend /home/rapportnav/configurations/


RUN mkdir /home/rapportnav/public
# COPY --from=buildSourcemaps /tmp/frontend/build /home/rapportnav/public/
COPY frontend/dist /home/rapportnav/public/
# COPY --from=build-frontend /tmp/infra/docker/tmp/env.sh /home/rapportnav/

# Add logs folder to be mounted as volume
RUN mkdir /home/rapportnav/logs

# Set up environement variable that define the root folder use for serving static files
# It must point to the front (React) files
ENV STATIC_FILES_PATH=/home/rapportnav/public

CMD exec java -Dspring.config.additional-location="/home/rapportnav/configurations/" -Dspring.profiles.active="$ENV_PROFILE" -jar "/home/rapportnav/rapportnav-${VERSION}.jar"
