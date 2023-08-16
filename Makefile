
#########################
# FRONTEND
#########################
#########################
FRONTEND_DIR := frontend

.PHONY: front-install front-start front-build front-test front-lint

front-install:
	cd $(FRONTEND_DIR) && npm install

front-start:
	cd $(FRONTEND_DIR) && npm start

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

back-build:
	docker compose build

back-start:
	docker-compose up

.PHONY: back-build back-start

.PHONY: docker-build-app 
docker-build-app:
	docker buildx build -f infra/docker/app/Dockerfile . -t rapportnav-app:$(VERSION) \
		--build-arg VERSION=$(VERSION) \
		--build-arg ENV_PROFILE=$(ENV_PROFILE) \
		--build-arg GITHUB_SHA=$(GITHUB_SHA) 


.PHONY: check-clean-archi back-start-dev back-build-mvn

check-clean-archi:
	cd $(BACKEND_DIR)/tools && ./check-clean-architecture.sh

# OK
back-build-mvn:
	cd $(BACKEND_DIR) \
	&& \
	mvn clean package -DskipTests=true

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




# CI commands - app
.PHONY: docker-tag-app docker-push-app run-infra-for-frontend-tests
docker-tag-app:
	docker tag rapportnav-app:$(VERSION) ghcr.io/MTES-MCT/rapportnav2/rapportnav-app:$(VERSION)
docker-push-app:
	docker push ghcr.io/MTES-MCT/rapportnav2/rapportnav-app:$(VERSION)





