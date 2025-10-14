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

### 🖥️ Frontend
- **Framework** : React + TypeScript
- **Build** : Vite 
- **Gestion d’état** : React Query 
- **Style** : monitor-ui
- **Tests** : Vitest 
- **CI/CD** : GitHub Actions / Docker

### ⚙️ Backend
- **Langage** : Kotlin
- **Framework** : Spring Boot
- **Base de données** : PostgreSQL
- **ORM** : JPA / Hibernate
- **Tests** : JUnit5
- **Documentation API** : Swagger 
- **Build & Run** : Gradle
- **CI/CD** : GitHub Actions / Docker

### 🧩 Infrastructure
- **Conteneurisation** : Docker & Docker Compose
- **Base de données locale** : PostgreSQL via Docker
- **Secrets** : `.env` + gestion sécurisée (Gitlab Secrets)
- **Monitoring** : Portainer

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
- Séparation claire : `controller / service / repository / model / config`
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

Les versions sont taguées dans Git : v1.3.2



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

- CI/CD obligatoire (Gitlab Actions)
- Environnements :
  - `local` → développeurs
  - `int` → intégration / staging
  - `prod` → production
- Déploiement via Docker 
- Rollback possible mais privilégier le roll-forward correctif
- Migration DB versionnée (Flyway)

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



