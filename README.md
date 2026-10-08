# RapportNav

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MTES-MCT_rapportnav2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=MTES-MCT_rapportnav2)
[![[Build & Test] Frontend](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-frontend.yml/badge.svg)](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-frontend.yml)
[![[Build & Test] Backend](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-backend.yml/badge.svg)](https://github.com/MTES-MCT/rapportnav2/actions/workflows/build-and-test-backend.yml)

## What is it ?

RapportNav is a mission reporting tools developed by the French administration (https://mer.gouv.fr/).

The documentation can be found [here](https://mtes-mct.github.io/rapportnav2/#/).

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
  - TypeScript
  - React
  - Vite + Vitest
  - [Monitor-ui](https://mtes-mct.github.io/monitor-ui/) design system

Full stack details and architecture diagrams: [Stack](./docs/engineering/stack/index.md).

## Development process

Installation, local database setup, running the app and IntelliJ configuration are
documented on a single page: **[Installation & développement local](./docs/engineering/getting-started/index.md)**.

Contribution conventions (architecture, Git, tests…) live in the
**[Charte de développement](./docs/charte-dev/index.md)**. See also
[CONTRIBUTING.md](./CONTRIBUTING.md).

## Security and Vulnerabilities analysis

The following checks are performed through Github Actions:

- dependencies:
  - frontend: [`npm audit`](https://docs.npmjs.com/auditing-package-dependencies-for-security-vulnerabilities)
  - backend: [OWASP Dependency-Check](https://mvnrepository.com/artifact/org.owasp/dependency-check-maven)
- vulnerabilities: [CodeQL from GitHub](https://codeql.github.com/)
- container scan: [Trivy](https://www.aquasec.com/products/trivy/)

## Deployment

Deployment (GitLab mirror alignment, automated and manual procedures) is documented
here: [Déploiement](./docs/engineering/operations/deployment.md).

