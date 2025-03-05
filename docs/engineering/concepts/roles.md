# Les rôles

## Types de rôles 

Nous avons 3 types d’utilisateurs:
- PAM 
- ULAM 
- ADMIN

Pour l’instant, la gestion des rôles est basique donc on peut être tout à la fois

## Restrictions en fonction des rôles

- Côté frontend, nous n’avons que des routes publiques/authentifiées, il n’y pas de gestion de routes par roles. 
- Coté backend, les restrictions d’authentifications et permissions sont plus avancées, voir `SecurityConfig.kt`
