# Variables d'environnement et configuration

## Configurations

Le frontend utilise des fichiers `.env`, ils sont été codé en
suivant [ce principe](https://github.com/MTES-MCT/monitorfish/blob/master/adrs/0001-frontend-runtime-env-var-injection.md)

Le backend utilise un fichier `application.properties` comme base sur laquelle d'autres fichiers de configs par
environnement
peuvent être ajoutées, comme `application-local.properties`.

## Variables d'environnement

Les variables d'environnement sont stockées dans GitLab est sont maintenues par la DSI.
Les développeurs RapportNav n'ont pas accès à cette interface de gestion des variables d'environnement.

Ces variables sont ensuite récupérées par la chaine CI et sont transmises au `docker-compose` via le fichier `.env.j2`.

Ces variables peuvent être dépendente de l'environnement donc elles auront une valeur différente selon `int` ou `prod`,
par exemple les mots de passe de base de données, ou URL des APIs de Monitor.
D'autres variables sont indépendentes de l'environnement donc une seule valeur suffit. Il s'agit par exemple de valeurs
avec des données confidentielles, telles que l'URL de Sentry.

## Configuration locale

### Variables d'environnement locales

Les variables d'environnement suivantes sont nécessaires pour la configuration de votre backend en local

- ENV_DB_URL=jdbc:postgresql://localhost:5432/rapportnavdb?user=postgres&password=postgres
- JWT_SECURITY_KEY=fG2hI6jK0lM4nO8pQ2rS6tU0vW4xY8zA2bC6dE0fG4hI8jK2lM6nO0pQ4rS8tU2
- MASTER_API_KEY=uV3wX7yZ1aB5cD9eF3gH7iJ1kL5mN9oP3qR7sT1uV5wX9yZ3aB7cD1eF5gH9iJ3
- MONITORFISH_API_KEY=fake-key
- MONITORFISH_HOST=http://localhost:8089
- MONITORENV_HOST=http://localhost:8089

### Configuration locale

Comme déjà mentionné, le backend utilise la config `application.properties` mais il est possible d'écraser ces valeurs
dans le fichier `application-local.properties`.
Le frontend utilise les fichiers `.env.local.defaults` et `.env.local`.
