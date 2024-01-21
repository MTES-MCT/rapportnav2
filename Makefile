
#########################
# FRONTEND
#########################
#########################
FRONTEND_DIR := frontend

.PHONY: front-install front-start front-build front-test front-coverage front-lint front-visualize-bundle front-sourcemap

front-ci:
	cd $(FRONTEND_DIR) && npm ci

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

front-coverage:
	cd $(FRONTEND_DIR) && npm run test:coverage

front-sourcemap:
	cd $(FRONTEND_DIR) && npm run build:with-sourcemaps

front-visualize-bundle:
	cd $(FRONTEND_DIR) && npx vite-bundle-visualizer


#########################
# END FRONTEND
#########################


#########################
# BACKEND
#########################
#########################
BACKEND_DIR := backend
BACKEND_CONFIGURATION_FOLDER=$(shell pwd)/infra/configurations/backend/

.PHONY: back-version back-show-dependencies back-assemble back-build back-test back-local back-clean-archi

back-version:
	cd $(BACKEND_DIR) && ./gradlew properties | grep -w 'version' | awk -F': ' '{ if ($1 == "version") print $2 }'

back-show-dependencies:
	cd $(BACKEND_DIR) && ./gradlew dependencies

back-assemble:
	cd $(BACKEND_DIR) && ./gradlew clean assemble

back-build:
	cd $(BACKEND_DIR) && ./gradlew clean build

back-test:
	cd $(BACKEND_DIR) && ./gradlew test

back-start-local:
	cd $(BACKEND_DIR) && ./gradlew bootRun --args='--spring.profiles.active=local --spring.config.additional-location=$(BACKEND_CONFIGURATION_FOLDER)'

back-clean-archi:
	cd $(BACKEND_DIR)/tools && ./check-clean-architecture.sh


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




.PHONY: docker-build-app

docker-build-app:
	docker buildx build -f infra/docker/app/Dockerfile .  \
		-t rapportnav-app:$(VERSION) \
		-t rapportnav-app:latest \
		--load \
		--build-arg VERSION=$(VERSION) \
		--build-arg ENV_PROFILE=$(ENV_PROFILE) \
		--build-arg GITHUB_SHA=$(GITHUB_SHA)


docker-run-local:
	docker-compose -f ./infra/docker-compose.dev.yml up


