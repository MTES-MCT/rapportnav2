
FROM bellsoft/liberica-openjdk-alpine:17 AS build-all

WORKDIR /home/rapportnav

ARG PROXY
ENV PROXY=$PROXY

ARG VERSION
ENV VERSION=$VERSION

ARG ENV_PROFILE
ENV ENV_PROFILE=$ENV_PROFILE

ARG MONITORFISH_API_KEY
ENV MONITORFISH_API_KEY=$MONITORFISH_API_KEY

ARG JWT_SECURITY_KEY
ENV JWT_SECURITY_KEY=$JWT_SECURITY_KEY

ENV SPRING_PROFILES_ACTIVE=$ENV_PROFILE

ENV ENV_DB_URL=""

# Set proxy environment variables
ENV http_proxy=${PROXY}
ENV https_proxy=${PROXY}

# Install LibreOffice using apk with proxy settings
RUN apk update && \
    apk add --no-cache libreoffice && \
    rm -rf /var/cache/apk/*

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
