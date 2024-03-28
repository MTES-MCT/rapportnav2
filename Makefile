
#########################
# FRONTEND
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

# END FRONTEND
#########################


#########################
# BACKEND
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
	cd $(BACKEND_DIR) && ./gradlew test --debug

back-start-local:
	cd $(BACKEND_DIR) && ./gradlew bootRun --args='--spring.profiles.active=local --spring.config.additional-location=$(BACKEND_CONFIGURATION_FOLDER)'

back-clean-archi:
	cd $(BACKEND_DIR)/tools && ./check-clean-architecture.sh

# END BACKEND
#########################


#########################
# DOCKER
#########################

INFRA_DIR := infra

.PHONY: docker-prune docker-logs-backend

docker-prune:
	docker image prune -a
docker-logs-backend:
	docker container logs -f backend


.PHONY: docker-build-back docker-restart-back docker-build-front docker-run-local

docker-build-back:
	cd $(INFRA_DIR) && docker compose -f docker-compose.local.yml build backend

docker-restart-back:
	cd $(INFRA_DIR) && docker compose -f docker-compose.local.yml restart backend

docker-build-front:
	cd $(INFRA_DIR) && docker compose -f docker-compose.local.yml build frontend

docker-run-local:
	cd $(INFRA_DIR) && docker compose -f docker-compose.local.yml up -d

# END DOCKER
#########################


