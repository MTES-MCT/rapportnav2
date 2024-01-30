# RapportNav

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MTES-MCT_rapportnav2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=MTES-MCT_rapportnav2)
[![[Build & Test] Frontend](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-frontend.yml/badge.svg)](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-frontend.yml)
[![[Build & Test] Backend](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-backend.yml/badge.svg)](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-backend.yml)

## What is it ?

RapportNav is a mission reporting tools developped by the French administration (https://mer.gouv.fr/).

## Stack

- Infra:
  - Docker
  - GitHub Actions
- Backend:
  - Gradle
  - Kotlin
  - Spring-boot
  - Flyway migrations
- Database:
  - PostgreSQL 15
- Frontend:
  - React (Vite)
  - [Monitor-ui](https://mtes-mct.github.io/monitor-ui/) design system

## Development process

### Before starting

Before trying to run the project, make sure you have:

- installed Postgres 15
- created the `rapportnavdb` database with the right users and passwords
  - on macOS: `createdb rapportnavdb`
  - use `dropdb rapportnavdb` if you wanna start clean
- created the role `postgres`: `createuser --interactive`
  - get into the database: `psql -d rapportnavdb -U postgres -h localhost`
  - check the role is present with the cmd: `\du`
- created a `metabase` schema" `CREATE SCHEMA metabase;` and verify with `\dn`

### Running the project

From the root dir, the following commands are available:

- backend
  - install dependencies: make back-clean-install
  - run local backend: make back-start-dev (http://localhost:80/)
- frontend
  - install dependencies: make front-install
  - run local frontend: make front-start (http://localhost:5173/)

## Security and Vulnerabilities analysis

The following checks are performed through Github Actions:

- dependencies:
  - frontend: [`npm audit`](https://docs.npmjs.com/auditing-package-dependencies-for-security-vulnerabilities)
  - backend: [OWASP Dependency-Check](https://mvnrepository.com/artifact/org.owasp/dependency-check-maven)
- vulnerabilities: [CodeQL from GitHub](https://codeql.github.com/)
- container scan: [Trivy](https://www.aquasec.com/products/trivy/)

## Deployment

### Pre-requisites

As we are mirroring our repo onto our hoster's repo, who can also provide changes and commits, _make sure to align the
two repos before deploying_:

- add mirror if you
  haven't: `git remote add mirror https://gitlab-sml.din.developpement-durable.gouv.fr/num3-exploitation/deploiement-continu/gitlab-ci/applications/rapportnav-v2/rapportnav-v2.git`
- fetch changes: `git fetch mirror`
  - username: use your @i-carre.net email
  - password: use token provided by other devs or devops at DSI
- pull changes if existing: `git pull mirror main`
- push to this repo if necessary

### Instructions

- Make sure you have the last changes from the mirror: so fetch & pull code from mirror
- Set version number in `build.gradle.kts`
- Set version number in `package.json` and `make front-ci` again to regen the `package-lock.json`
- Set version number in the variable `PROJECT_VERSION` in the file `.gitlab-ci.yml`
- Create a GitHub release with the same version number
- check the running `release` Action
- check the
  pipeline [here](https://gitlab-sml.din.developpement-durable.gouv.fr/num3-exploitation/deploiement-continu/gitlab-ci/applications/rapportnav-v2/rapportnav-v2/-/pipelines)
