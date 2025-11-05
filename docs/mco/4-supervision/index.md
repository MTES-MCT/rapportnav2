## 4. Supervision et métrologie

### 4.1 Outils de supervision

#### Sentry

Sentry est mis à disposition via l'incubateur de la Fabrique Numérique.

Il est notre principale source de suivi des erreurs en permettant le suivi des erreurs frontend et backend.

#### Portainer

Portainer permet de visualiser les logs et autres stats pour différents containers, images Docker.

Pour l'instant, il faut demander à chaque fois l'accès à la DSI car les droits sont overwritten à chaque déploiement.

Portainer n'est accessible que via le RIE à l'url suivante: http://int-rapportnav-appli01.dsi.damgm.i2

Les logs du backend et de la database sont visibles dans les logs du container


#### Sonarqube

Sonarqube permet de mesurer la qualité du code selon plusieurs critères comme la couverture de tests, duplication de code, code smells, maintenabilité...

A chaque push sur main sur le Gitlab de la DSI, une analyse est lancée et les résultats sont updatés automatiquement.

Il est possible de voir le projet en suivant l'url: http://sonarqube.dsi.damgm.i2/projects

En fonction du résultat de la Quality Gate, un déploiement peut être bloqué.

#### Admin Panel

Nous possédons aussi notre propre admin panel dans lequel il est possible d'effectuer certaines supervisions, comme pour les clés API par exemple.


### 4.2 Indicateurs surveillés

Les indicateurs suivants sont surveillés :
- Disponibilité via Portainer
- Taux d’erreur / latence via Sentry et Portainer
- Ressources système (CPU, RAM, disque) via Portainer

### 4.3 Seuils et alertes

Au niveau applicatif, aucuns seuils et alertes ont été mis en place.
Au niveau machines, le suivi des alertes est à charge de l'hébergeur. 

### 4.4 Gestion des alertes et escalade

- Canaux d’alerte : 
  - email à l'intrapreneur.e RapportNav
  - contact via email, Mattermost ou téléphone è l'équipe de développement
- Escalade : 
  - ordre des contacts :
    - intrapreneur.e RapportNav
    - équipe de développement RapportNav
    - potentiellement hébergeur
  - délais :
    - immédiats si criticalité importante/urgente
    - max 72h pour les criticalités non impactantes pour les utilisateurs
