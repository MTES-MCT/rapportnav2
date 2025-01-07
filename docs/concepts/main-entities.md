# Les concepts à maîtriser


## Missions

La Mission est le concept le plus important car il réside tout en haut de la pyramide.

Au niveau d'une mission seront stockées :
- un identifiant
- les dates de début/fin
- les unités impliquées
- les moyens impliqués
- qui a ouvert la mission (CNSP, CACEM, unité)
- des observations
- si le rapport est complet
- tout un tas d'autres infos

Cet objet est commun à RapportNav + MonitorFish + MonitorEnv.


## Actions

Au sein d'une mission, plusieurs actions peuvent être ajoutées.

Au niveau d'une action seront stockées :
- un identifiant
- les dates de début/fin
- des observations
- potentiellement des contrôles
- potentiellement des infractions
- tout un tas d'autres infos relatives au type d'action

Certaines Actions sont communes avec MonitorFish/Env mais pas toutes. Celles en commun sont :
- MonitorFish
  - Contrôles
- MonitorEnv
  - Contrôles
  - Surveillance
  
Les autres actions sont seulement disponibles pour RapportNav.


### Types d'actions

Il existe environ une trentaine d'actions différentes :
- Les actions en co-saisie
  - Contrôles Pêche pro
  - Contrôles environement marin
  - Surveillances environement marin
- Le statut du navire (PAM)
- Des notes libres 
- Les contacts avec les centres
- Des actions spécifiques telles que :
  - assistances et sauvetages
  - lutte anti pollution
  - lutte anti immigration illégale
  - permanence Vigimer
  - cérémonies et événements
  - ...


A l'exception des notes libres, toutes les actions sont comptabilisées en stats.

### Contrôles

Pour les actions de contrôle, selon les cas, plusieurs types de contrôles peuvent être effectués tels que :
- Contrôles administratifs
- Contrôles règles de navigation
- Contrôles respect des règles de sécurité
- Contrôles Gens de Mer

Une action peut donc avoir 0 ou plusieurs contrôles.

Il est important de ne pas confondre une action de contrôle (control pêche ou env par ex) et un contrôle au sein d'une action (généralement de contrôle).

### Infractions

Un contrôle peut déboucher sur une infraction. Un contrôle peut donc avoir 0 ou 1 infraction.
Une infraction peut avoir plusieurs [NATINF](https://www.justice.gouv.fr/documentation/ressources/liste-infractions-vigueur-nomenclature-natinf).

