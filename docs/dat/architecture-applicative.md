33# Architecture applicative

## Architecture hexagonale

Le backend suit une **architecture hexagonale** (clean architecture) avec une séparation stricte des responsabilités :

- **`domain`** — le cœur métier, indépendant de toute technologie :
  - entités métier **pures** ;
  - `use_cases` (orchestration des règles métier) ;
  - `repositories` = **contrats** (interfaces) que l'infrastructure doit implémenter ;
  - règles de validation et exceptions métier.
- **`infrastructure`** — les détails techniques, qui dépendent du domaine :
  - `api` — points d'entrée HTTP / GraphQL (admin, bff, public_api, auth, filters, adapters) ;
  - `database` — repositories JPA et modèles (mapping vers la base) ;
  - `cache` — implémentation du cache (Caffeine) ;
  - `monitorfish` / `monitorenv` — adapters vers les APIs externes ;
  - `utils` — utilitaires (dont génération de documents bureautiques).
- **`config`** — configuration Spring (Security/JWT, CORS, JPA, Flyway, logging, Sentry, sérialisation JSON géo-spatiale).

> Principe structurant : **les entités du domaine restent pures**, la logique de mapping vit dans la couche infrastructure (modèles JPA). Voir les schémas `hexa-archi.png` et `rapportnav-archi.png` dans [Concepts techniques](../engineering/index).

## Packaging & exécution

- Le **frontend** est buildé en fichiers statiques (`dist`) puis **embarqué dans le JAR** Spring Boot et servi par le backend (`spring.web.resources.static-locations`).
- L'ensemble est packagé dans une **image Docker unique** (base **Liberica OpenJDK 25**, incluant **LibreOffice** pour la conversion de documents).
- L'application **expose le port 80**.
- Elle est prévue pour tourner **derrière un reverse proxy** : la stratégie `forward-headers` est activée pour traiter les en-têtes `X-Forwarded-*` (schéma, hôte, IP réelle).

## Profils & configuration

- Profils Spring : **`local`**, **`int`** (intégration), **`prod`**.
- Configuration via `application.properties` surchargé par `application-{profil}.properties`, complété par des **variables d'environnement** (voir [Flux de données externes](flux-donnees-externes) pour la liste et [Variables d'environnement](../engineering/concepts/env-vars) pour la gestion).

## Authentification & gestion des accès

- Authentification par **JWT** : jeton Bearer transmis avec les requêtes API, validité **30 jours**.
- Mots de passe stockés hachés avec **BCrypt** (salt 10).
- Endpoints d'administration et accès machine-à-machine authentifiés par **clé API**.
- Autorisation basée sur les rôles applicatifs (voir [Présentation fonctionnelle](presentation-fonctionnelle)).
- Les tentatives de connexion (succès / échec) sont **auditées** (IP, user-agent).

Détails : [Gestion des utilisateurs](../engineering/concepts/auth) et [Les rôles](../engineering/concepts/roles).

## Note pour la reprise d'hébergement

- L'application est destinée à rester accessible **via le RIE** uniquement.
- Un **proxy de sortie** doit être prévu pour permettre au backend de joindre les APIs externes (MonitorFish/Env, Sentry) depuis le réseau interne. Les variables `PROXY_HOST` / `PROXY_PORT` sont prévues à cet effet.
- Le reverse proxy en frontal doit propager les en-têtes `X-Forwarded-*` et assurer la terminaison **TLS**.

Voir [Infrastructure](../engineering/stack/infra) pour le contexte de l'hébergement actuel.
