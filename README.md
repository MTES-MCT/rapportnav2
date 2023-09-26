# RapportNav

## What is it ?

RapportNav is a mission reporting tools developped by the French administration (https://mer.gouv.fr/).

## Stack

- Infra:
  - docker
- Backend:
  - Kotlin
  - Spring
- PostgreSQL 15 with postgis extension
- Frontend:
  - React (Vite)
  - Monitor-ui design system

## Development process

From the root dir, the following commands are available:

- backend
  - install dependencies: make back-clean-install
  - run local backend: make back-start-dev (http://localhost:8080/)
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

Pre-requisites:
As we are mirroring our repo onto our hoster's repo, who can also provide changes and commits, make sure to align the two repos before deploying:

- add mirror if you haven't: `git remote add mirror https://gitlab-sml.din.developpement-durable.gouv.fr/num3-exploitation/deploiement-continu/gitlab-ci/applications/rapportnav-v2/rapportnav-v2.git`
- fetch changes: `git fetch mirror`
- pull changes if existing: `git pull mirror main`
- push to this repo if necessary
- finally, prepare your release by creating a GitHub Release
- check the running `release` Action
