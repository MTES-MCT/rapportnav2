
#########################
# FRONTEND
#########################
#########################
FRONTEND_DIR := frontend

.PHONY: front-install front-start front-build front-test front-lint

front-ci:
	cd $(FRONTEND_DIR) && npm ci --verbose

front-install:
	cd $(FRONTEND_DIR) && npm install

front-start:
	cd $(FRONTEND_DIR) && npm run dev

front-build:
	cd $(FRONTEND_DIR) && npm run build

front-lint:
	cd $(FRONTEND_DIR) && npm run lint

front-test:
	cd $(FRONTEND_DIR) && npm run test

#########################
# END FRONTEND
#########################


#########################
# BACKEND
#########################
#########################
BACKEND_DIR := backend
BACKEND_CONFIGURATION_FOLDER=$(shell pwd)/infra/configurations/backend/


.PHONY: back-clean-install back-check-dependencies back-test back-sonar
back-clean-install:
	cd $(BACKEND_DIR) && ./mvnw clean install

back-check-dependencies:
	cd $(BACKEND_DIR) && ./mvnw dependency-check:check

back-sonar:
	cd $(BACKEND_DIR) && ./mvnw clean install sonar:sonar \
	    -Dsonar.projectKey$(projectKey) \
            -Dsonar.organization=$(organization) \
            -Dsonar.host.url=$(url) \
            -Dsonar.token=$(token)
            -Dsonar.verbose=true

back-test:
	cd $(BACKEND_DIR) && ./mvnw test -Pci -Dmaven.main.skip=true


.PHONY: check-clean-archi back-start-dev back-build-mvn

check-clean-archi:
	cd $(BACKEND_DIR)/tools && ./check-clean-architecture.sh

# OK
back-build-mvn:
	cd $(BACKEND_DIR) \
	&& \
	./mvnw clean package -DskipTests=true

# OK
back-start-dev:
	cd $(BACKEND_DIR) \
	&& \
	./mvnw spring-boot:run \
		-Dspring-boot.run.arguments="--spring.config.additional-location="$(BACKEND_CONFIGURATION_FOLDER)"" \
		-Dspring-boot.run.profiles="dev"


#########################
# END BACKEND
#########################



#########################
# MAINTENANCE
#########################
#########################
.PHONY: docker-prune logs-backend

docker-prune:
	docker image prune -a
logs-backend:
	docker container logs -f rapportnav_backend


#########################
# END MAINTENANCE
#########################




.PHONY: docker-build-app docker-tag-app docker-push-app

docker-build-app:
	docker buildx build -f infra/docker/app/Dockerfile .  \
		-t rapportnav-app:$(VERSION) \
		-t rapportnav-app:latest \
		--load \
		--build-arg VERSION=$(VERSION) \
		--build-arg ENV_PROFILE=$(ENV_PROFILE) \
		--build-arg GITHUB_SHA=$(GITHUB_SHA)

# not used
docker-tag-app:
	docker tag rapportnav-app:latest ghcr.io/mtes-mct/rapportnav2/rapportnav-app:latest

# not used
docker-push-app:
	docker push ghcr.io/mtes-mct/rapportnav2/rapportnav-app:latest



docker-run-local:
	docker-compose -f ./infra/docker-compose.dev.yml up


