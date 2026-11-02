# 🧭 Charte de Développement

## 1. Introduction

**Objectif :** Fournir une plateforme fiable, maintenable et évolutive, basée sur une architecture React + Kotlin Spring Boot + PostgreSQL.  
**Portée :**  
Ce document définit les principes, pratiques et standards à suivre pour tout développement, revue et déploiement du logiciel.

---

## 2. Principes directeurs

- **Lisibilité > performance prématurée**
- **Simplicité > complexité magique**
- **Automatisation > processus manuel**
- **Cohérence > individualité**
- **Sécurité et conformité dès la conception (Privacy by Design)**
- **Documentation vivante et à jour**

---

## 3. Stack technique

**Frontend** React + TypeScript + Vite (React Query, monitor-ui, Vitest) —
**Backend** Kotlin + Spring Boot + JPA/Hibernate (Gradle, JUnit5, Swagger) —
**Base de données** PostgreSQL — le tout conteneurisé avec Docker.

Le détail de la stack et les diagrammes d'architecture sont documentés dans
[Stack technique](../engineering/stack/index). Pour installer et lancer le projet,
voir [Installation & développement local](../engineering/getting-started/index).

- **CI / CD** : GitHub Actions pour le build, les tests et les analyses de sécurité ;
  GitLab CI pour le déploiement (voir §10).
- **Secrets** : variables de CI/CD GitLab ; configuration locale via `.env` et les
  fichiers d'`infra/configurations` (jamais de secret dans le code).
- **Monitoring** : Portainer.

---

## 4. Standards de code

### 4.1 Langage et style
#### Frontend
- TypeScript strict (`"strict": true` dans `tsconfig.json`)
- ESLint + Prettier obligatoires
- Nom de fichiers : `kebab-case`
- Composants fonctionnels uniquement
- Pas de logique métier dans les composants — utiliser des hooks dédiés

#### Backend
- Respect du style Kotlin (Kotlin Coding Conventions)
- Classes et packages clairement nommés
- **Architecture hexagonale** (ports & adapters) sous `fr.gouv.dgampa.rapportnav` :
  - `domain/` → cœur métier indépendant de tout framework : `entities/` (entités
    **pures**, sans annotation JPA ni dépendance Spring/Hibernate), `use_cases/`
    (logique applicative), `repositories/` (ports/interfaces), `validation/`,
    `exceptions/`
  - `infrastructure/` → adapters : `database/` (modèles JPA, mappers et
    implémentations des repositories), `api/` (contrôleurs, DTOs), intégrations
    externes (`monitorenv/`, `monitorfish/`, `cache/`…)
  - `config/` → configuration Spring
- **Règles d'or :** le domaine ne dépend jamais de l'infrastructure (inversion de
  dépendances via les ports) ; **le mapping et la persistance appartiennent à
  l'infrastructure** (modèles JPA), jamais au domaine ; un cas d'usage passe par les
  ports, pas directement par les modèles JPA.
- Configuration externalisée (`application-properties`)

---

## 5. Git & Workflow

### 5.1 Branches
- `main` → derniere version 
- `feature/*` → nouvelles fonctionnalités
- `fix/*` → corrections
- `chore/*` → maintenance, dépendances, outils

### 5.2 Commits
Utiliser la **convention Conventional Commits** :  

Types autorisés :
- `feat` : nouvelle fonctionnalité
- `fix` : correction de bug
- `docs` : documentation
- `style` : mise en forme, sans impact sur le code
- `refactor` : refonte du code sans changement fonctionnel
- `test` : ajout/modification de tests
- `chore` : maintenance, CI, dépendances

**Exemples :**
feat(api): add pagination for job listings
fix(frontend): prevent crash when user logs out
chore: upgrade Kotlin to 2.0.10



### 5.3 PRs
- Une PR = une fonctionnalité ou un correctif.
- Doit inclure :
  - Description claire du changement
  - Tests et linting passants
  - Revue obligatoire par un pair
- PR courtes (< 300 lignes si possible)

---

## 6. Versioning

Suivre le **Semantic Versioning (SemVer)** :
MAJOR.MINOR.PATCH
- `MAJOR` : changement incompatible (breaking change)
- `MINOR` : nouvelle fonctionnalité compatible
- `PATCH` : correction de bug ou ajustement mineur

Le versioning et le `CHANGELOG` sont **générés automatiquement par
`release-please`** à partir des Conventional Commits : un `feat` déclenche un bump
mineur, un `fix` un bump patch, un `feat!` / `BREAKING CHANGE` un bump majeur.
`release-please` ouvre une PR de release (`chore: release vX.Y.Z`) ; son merge crée
le tag Git (`vX.Y.Z`). Les commits doivent donc être correctement formatés, sous
peine de fausser la release.



---

## 7. Tests & Qualité

- **Frontend :**
  - Tests unitaires (Vitest) pour chaque composant logique
  - Tests E2E (Playwright) pour les parcours critiques

- **Backend :**
  - Tests unitaires (JUnit5)
  - Tests d’intégration sur les endpoints REST

- **CI/CD :**
  - Lint + tests + build doivent passer avant merge
  - Scan de sécurité automatisé (Dependabot, CodeQL)

- **Couverture minimale :** 50%

---

## 8. Sécurité & Données

- Jamais de secrets dans le code source
- Validation des entrées (frontend & backend)
- Nettoyage/sanitation des données affichées
- RGPD :
  - Consentement explicite avant tout tracking
  - Droit à l’effacement et à la portabilité respectés
- Accès DB : principe du moindre privilège
- Logs : pas de données personnelles en clair

---

## 9. Documentation

- README à jour pour chaque module
- Documentation API avec Swagger
- Pour chaque feature : description, endpoints, et payloads
- **ADR (Architecture Decision Records)** pour toute décision majeure
- Diagrammes d’architecture stockés dans `/docs/architecture`

---

## 10. Déploiement

- **Répartition CI/CD :**
  - **GitHub** = dépôt de référence et contrôles de PR. À chaque PR sur `main`,
    les workflows GitHub Actions exécutent build + tests (back & front) et les
    analyses de sécurité (CodeQL, Trivy, Dependency Review). `release-please` y
    gère les releases.
  - **GitLab CI** (`.gitlab-ci.yml`) = pipeline de déploiement (mirroir du dépôt) :
    `build → test → analyze (Sonar, Trivy, dependency-check) → deploy-recette →
    deploy-prod`, avec publication des images Docker.
- Environnements :
  - `local` → développeurs
  - `int` → intégration / staging
  - `prod` → production
- Déploiement via Docker 
- Rollback possible mais privilégier le roll-forward correctif
- Migration DB versionnée (**Flyway**), dans
  `backend/src/main/resources/db/migration` :
  - Nommage : `V1.AAAA.MM.JJ.HH.MM__description_en_snake_case.sql`
    (ex. `V1.2026.09.28.10.00__alter_action_type_add_control_sector_split.sql`)
  - Une migration est **immuable** une fois mergée : ne jamais modifier un fichier
    déjà livré, en créer une nouvelle
- **Suppressions / fusions de lignes : privilégier le soft-delete**
  (`deleted_at = now()`) plutôt qu'un `DELETE` physique, pour préserver l'historique
- Changements de calcul lourds **par étapes** : write + backfill d'abord,
  observation en production, bascule des lectures ensuite

---

## 11. Collaboration & Communication

- Discussions techniques sur GitHub / Mattermost
- Respect mutuel et bienveillance dans les reviews
- Préférer la transparence à la hiérarchie
- Décisions techniques documentées
- Code reviews centrées sur la qualité, pas le style personnel

---

## 12. Évolution de la charte

- Les modifications à cette charte se font via Pull Request
- Validation requise par au moins un Tech Lead
- Versionner la charte avec le projet (`docs/charte-dev/index.md`)

---



