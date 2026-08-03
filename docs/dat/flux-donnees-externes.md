3333333# Flux de données externes

Cette page décrit **comment RapportNav se connecte à d'autres sources de données**, pour quel usage et pour quels acteurs. C'est un point clé pour la reprise d'hébergement : le backend doit pouvoir joindre ces services depuis le RIE (via un **proxy de sortie**), et certaines connexions sont réalisées directement depuis le **navigateur** de l'utilisateur (frontend).

## Préambule : dépendance à Monitor

RapportNav n'est **pas standalone** : il co-saisit les missions avec **MonitorFish** et **MonitorEnv**, où sont notamment stockées les Missions. Voir [Co-saisie avec MonitorFish et MonitorEnv](../concepts/fish-env).

## Tableau des flux

| Source                | Sens (appelant)        | Protocole / Hôte                                  | Authentification            | Usage métier                                                       | Pour qui                          |
|-----------------------|------------------------|---------------------------------------------------|-----------------------------|--------------------------------------------------------------------|-----------------------------------|
| **MonitorEnv**        | Backend → Env (serveur↔serveur) | HTTPS, `MONITORENV_HOST`                  | Aucune clé                  | Missions environnement, unités de contrôle, administrations, codes NATINF | Agents environnement / ULAM       |
| **MonitorFish**       | Backend → Fish (serveur↔serveur) | HTTPS, `MONITORFISH_HOST`               | Clé API (`x-api-key`)       | Actions de contrôle pêche, référentiels navires & ports            | Agents pêche / PAM                |
| **Metabase**          | Backend → Metabase (embarqué)   | HTTPS, `METABASE_SITE_URL`               | JWT signé HMAC-SHA256       | Tableaux de bord d'analyse d'activité (PAM / ULAM)                 | Encadrement (managers) / admins   |
| **Sentry**            | Backend & Frontend → Sentry (sortant) | HTTPS, `sentry.incubateur.net`     | DSN                         | Suivi des erreurs et performances                                  | Équipe technique                  |
| **GeoPF / Géoplateforme** | **Frontend** → GeoPF        | HTTPS, `data.geopf.fr`                   | Aucune (API publique)       | Autocomplétion d'adresses lors de la saisie                        | Agents de terrain                 |
| **Recherche Entreprises** | **Frontend** → API gouv     | HTTPS, `recherche-entreprises.api.gouv.fr` | Aucune (API publique)     | Recherche d'entreprises pour la documentation des contrôles        | Inspecteurs environnement         |

> Les flux **backend** transitent par le proxy de sortie du RIE. Les flux **frontend** (GeoPF, Recherche Entreprises) partent du navigateur de l'agent et sont autorisés explicitement par la politique **CSP** (voir plus bas).

## Base de données

- **PostgreSQL 15**, connexion via `ENV_DB_URL` (identifiants `DB_USER` / `DB_PASSWORD`).
- Migrations appliquées automatiquement au démarrage par Flyway.

## Mise en cache des référentiels

Pour réduire la dépendance et la charge sur les APIs externes, les référentiels peu changeants sont mis en cache mémoire (**Caffeine**) :

- `vessels` (navires) et `ports` — depuis MonitorFish ;
- `natinfs` (codes d'infraction) et `resources` (ressources des unités de contrôle) — depuis MonitorEnv.

## Variables d'environnement de connexion

Les connexions ci-dessus sont configurées par variables d'environnement (**noms uniquement — aucune valeur secrète n'est stockée dans le code ni dans cette documentation**). La gestion des valeurs est décrite dans [Variables d'environnement](../engineering/concepts/env-vars).

| Variable             | Rôle                                             |
|----------------------|--------------------------------------------------|
| `ENV_DB_URL`         | URL JDBC de la base PostgreSQL                    |
| `DB_USER`            | Utilisateur de la base                            |
| `DB_PASSWORD`        | Mot de passe de la base                           |
| `MONITORFISH_HOST`   | Hôte de l'API MonitorFish                         |
| `MONITORFISH_API_KEY`| Clé API MonitorFish (`x-api-key`)                 |
| `MONITORENV_HOST`    | Hôte de l'API MonitorEnv                          |
| `JWT_SECURITY_KEY`   | Clé de signature des JWT (≥ 32 octets)            |
| `MASTER_API_KEY`     | Clé API maître (endpoints admin / M2M)            |
| `METABASE_SITE_URL`  | URL de l'instance Metabase                        |
| `METABASE_SECRET_KEY`| Clé de signature des jetons Metabase              |
| `SENTRY_DSN`         | Endpoint Sentry                                   |
| `SENTRY_ENABLED`     | Activation / désactivation de Sentry              |
| `PROXY_HOST`         | Hôte du proxy de sortie (accès APIs externes)     |
| `PROXY_PORT`         | Port du proxy de sortie                           |

## Sécurité des flux

- **CSP restrictive** : seuls les services externes en **liste blanche** peuvent être appelés depuis le frontend (`data.geopf.fr`, `recherche-entreprises.api.gouv.fr`, Sentry…).
- **Proxy de sortie configurable** (`PROXY_HOST` / `PROXY_PORT`) pour les appels sortants du backend depuis le RIE.
- Les clés et secrets sont fournis exclusivement par **variables d'environnement**, gérées côté chaîne de déploiement (jamais commités).

Voir aussi le [Maintien en Conditions de Sécurité (MCS)](../mcs/index) pour la gouvernance sécurité.
