# Gestion des utilisateurs

## Utilisateurs

Les utilisateurs sont stockés dans la table `users` de la base de données.

Les mots de passe sont hashés a l'aide de [BCrypt](org.springframework.security.crypto.bcrypt.BCrypt) (`BCrypt.gensalt(10)`)

## Authentification

### Méthode

L'authentification email/password se fait à l'aide d'un JWT token ayant une validité de 30 jours.

Ce Bearer token est transmis avec les requêtes API entre le frontend et l'API.

### Gestion des mots de passe

Aucune gestion des mots de passe, comme redemander un mot de passe, n'a été mise en place.
