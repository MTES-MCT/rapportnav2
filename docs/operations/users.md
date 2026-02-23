# Gestion des utilisateurs

## Ajout d'utilisateurs

Pour ajouter des utilisateurs, il faut :
- être superadmin
- se rendre sur la page de [signup](https://rapport-nav.din.developpement-durable.gouv.fr/signup)
- renseigner les infos, le service rattaché ainsi que le rôle

Ce processus est pour l'instant très manuel et effectué par les développeurs eux-mêmes.

A terme, il conviendra de rajouter une meilleure page d'admnistration des utilisateurs.

## Gestion des mots de passe

Aucune gestion des mots de passe, comme redemander un mot de passe, n'a été mise en place.
Il faut que les utilisateurs nous demandent et nous leur en fournissons un nouveau.

## Impersonation (mode service)

Cette fonctionnalité permet aux administrateurs de voir l'application comme s'ils étaient un service spécifique.
C'est utile pour diagnostiquer des problèmes ou vérifier ce qu'un service peut voir.

### Prérequis

- Avoir le rôle **ADMIN**

### Utilisation

1. **Activer le mode service** : Dans l'en-tête de l'application, cliquer sur le dropdown "Voir comme un service..." et sélectionner le service souhaité
2. **Identifier le mode actif** : Un tag jaune "Mode service : [nom du service]" s'affiche dans l'en-tête pour indiquer que le mode est actif
3. **Quitter le mode** : Cliquer sur le bouton "Quitter" à côté du tag jaune

### Comportement technique

- Les requêtes sont effectuées au nom du service sélectionné
- Le rôle ADMIN est temporairement retiré pendant l'impersonation (l'admin voit exactement ce que le service voit)
- Toutes les actions effectuées en mode impersonation sont journalisées pour l'audit

