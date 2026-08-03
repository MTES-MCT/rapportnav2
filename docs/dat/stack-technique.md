# Stack technique

Cette page synthétise les technologies et versions utilisées. Le détail par couche est disponible dans [Concepts techniques → Stack](../engineering/stack/index) (frontend / backend / database / infra).

## Frontend

- **React 19** avec **TypeScript** (5.9), build via **Vite 7**.
- **React Query** (@tanstack) pour la gestion des données serveur, avec persistance client (PWA / Workbox).
- **Formik** + **Yup** pour les formulaires et la validation.
- **Styled Components** pour le style, **Monitor-UI** (design system @mtes-mct) et **RSuite** pour les composants.
- **Axios** pour les appels HTTP, **date-fns** pour les dates.
- **Node.js >= 24** requis pour le build.
- Tests : **Vitest**, **Testing Library**, **MSW** (Mock Service Worker).

## Backend

- **Kotlin 2.x** sur **JVM 25**, build via **Gradle 9**.
- **Spring Boot 4** : Web, Data JPA, Data REST, **GraphQL**, **Security** (OAuth2 Resource Server), Cache, Flyway, Log4j2.
- **Caffeine** pour le cache mémoire.
- Génération de documents bureautiques : **LibreOffice** (via jodconverter) + **Apache POI** (Excel / DOCX).
- Suivi d'erreurs : **Sentry** (spring-boot + log4j2).
- Tests : **JUnit 5**, **MockK** / Mockito-Kotlin, **Testcontainers** (PostgreSQL), **WireMock** (mock HTTP).

## Base de données

- **PostgreSQL 15**.
- Migrations gérées par **Flyway** (exécutées au démarrage). Hibernate est en mode `ddl-auto=validate` : le schéma n'est **jamais** créé automatiquement par l'ORM, il est piloté par les scripts de migration versionnés.

Voir [Base de données](../engineering/stack/database) pour le détail.

## Observabilité

- **Sentry** côté backend et frontend (suivi des erreurs et des performances). Voir [Flux de données externes](flux-donnees-externes).

## Tableau de synthèse

| Composant       | Techno                         | Version | Rôle                                    |
|-----------------|--------------------------------|---------|-----------------------------------------|
| Frontend        | React + TypeScript + Vite      | 19 / 5.9 / 7 | Interface web (SPA / PWA)          |
| Backend         | Kotlin + Spring Boot           | 2.x / 4 | API applicative (REST + GraphQL)        |
| Runtime         | JVM (Liberica OpenJDK)         | 25      | Exécution du backend                    |
| Base de données | PostgreSQL                     | 15      | Persistance des données                 |
| Migrations      | Flyway                         | —       | Versionnement du schéma SQL             |
| Cache           | Caffeine                       | —       | Cache mémoire des référentiels externes |
| Documents       | LibreOffice + Apache POI       | —       | Génération d'exports (AEM, patrouille)  |
| Observabilité   | Sentry                         | —       | Suivi d'erreurs (front + back)          |
