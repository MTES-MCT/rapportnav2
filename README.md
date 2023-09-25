# RapportNav

## What is it ?

RapportNav is a mission reporting tools developped by the French administration (https://mer.gouv.fr/).

## Stack

- Infra:
  - docker
- Backend:
  - Kotlin
  - Spring
- PostgreSQL
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
