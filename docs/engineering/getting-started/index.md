# 🚀 Installation & développement local

Page de référence unique pour installer et lancer RapportNav en local.

> Pour les conventions (architecture, Git, tests…), voir la
> [Charte de développement](../../charte-dev/index). Pour la stack détaillée, voir
> [Stack](../stack/index).

## Prérequis

- Debian-based Linux ou macOS
- Docker v25 (avec Docker Compose v2)
- Java Development Kit (JDK) 25
- Node.js v26 (avec npm v12)
- PostgreSQL 15

Sur puce Apple, ajouter à votre `.bashrc` / `.zshrc` :

```sh
export DOCKER_DEFAULT_PLATFORM=linux/amd64
```

## Préparation de la base de données

Avant de lancer le projet, préparer PostgreSQL :

```sh
# créer la base (macOS) — utiliser `dropdb rapportnavdb` pour repartir de zéro
createdb rapportnavdb

# créer le rôle postgres
createuser --interactive

# se connecter et vérifier
psql -d rapportnavdb -U postgres -h localhost
# \du   -> vérifier la présence du rôle
# CREATE SCHEMA metabase;
# \dn   -> vérifier la présence du schéma metabase
```

## Premier setup

```sh
git clone https://github.com/MTES-MCT/rapportnav2.git
cd rapportnav2
cd backend && ./gradlew clean assemble
cd ../frontend && npm i
```

## Lancement en local

La méthode recommandée lance le backend et le frontend séparément, dans deux onglets.

**Backend** (depuis la racine du projet) :

```sh
./gradlew bootRun --args='--spring.profiles.active=local --spring.config.additional-location=$(BACKEND_CONFIGURATION_FOLDER)'
```

**Frontend** :

```sh
npm run dev
```

- Frontend disponible sur http://localhost:5173/
- Backend disponible sur http://localhost:80/

> Alternative tout-Docker (plus simple mais moins adaptée au debug) :
> `make docker-run-local` (lance `infra/docker-compose.local.yml`).

## Configuration dans IntelliJ

- Ajouter une configuration Kotlin et définir la classe `RapportNavApplication`.
- Définir / modifier les variables d'environnement :

  ```
  ENV_DB_URL=jdbc:postgresql://localhost:5432/rapportnavdb?user=postgres&password=postgres;MONITORFISH_API_KEY=fake-key;JWT_SECURITY_KEY=somelongrandomkeywhichisenoughtoalignwiththejwtspecification;MASTER_API_KEY=somelongrandomkeywhichisenoughtoalignwiththejwtspecification;
  ```

- Définir les VM options (adapter le chemin à votre checkout local) :

  ```
  -Dspring.config.additional-location="file:/path/to/rapportnav2/infra/configurations/backend/" -Dspring.profiles.active=local -Dsentry.environment=local
  ```

- Répertoire de travail : `/path/to/rapportnav2/backend`

## Variables d'environnement

Le détail des fichiers de configuration et des variables est documenté dans
[Configs et variables d'environnement](../concepts/env-vars).
