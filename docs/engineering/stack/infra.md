# Infrastructure

L'hébergement et l'infrastructure de ce projet est gérée par le bureau NUM3 de la DAM-SI (Systemes d'Information de la Direction des Affaires Maritimes). Ils sont basés à Saint Malo.
Ils se chargent de toute la partie hébergement et fournissent plusieurs outils.

A terme, il est possible que RapportNav soit migré sur le réseau eco du ministère de l'écologie.

## Le RIE et les machines virtuelles

L'application RapportNav n'est accessible que dans un réseau privé appellé RIE (Réseau Interministériel).
Autrement dit, il n'est pas possible d'accéder à RapportNav et les MonitorFish/Env via l'internet "normal".

Pour ce faire, il faut se connecter au RIE via :
- une machine virtuelle
- un certificat
- ~~VPN (option indisponible)~~

Côté software, il faudra rajouter un proxy de sortie pour pouvoir envoyer/recevoir de la data de l'extérieur.

### Demande de machine virtuelle

Afin d'obtenir une machine virtuelle, l'intrapreneur.e devra effectuer une demande auprès de la DAM-SI/NUM3.
Ces VMs peuvent tourner sur Windows ou Ubuntu.

Ces machines sont très très lentes, il ne faut pas hésiter à demander plus de mémoire physique et/ou virtuelle.

### Demande de certificats

Grâce à un certificat d'accès, il est possible d'accéder depuis l'extérieur à certains sites du RIE. 
Cela a notamment été mis en place lors du covid.
Similairement aux machines virtuelles, l'intrapreneur.e devra en effectuer une demande. 


## Hébergement

La DAM-SI dispose d'un gitlab self-hosted et nous met à disposition un repository ainsi qu'une chaine de déploiement.

### Les environements

Trois environnements nous sont mis à disposition
- ~~dev - machine disponible mais non setup~~
- [integration](http://int-rapportnav-appli01.dsi.damgm.i2)
- [production](https://rapport-nav.din.developpement-durable.gouv.fr/)

Bien entendu, ces environnements ne sont accessible que via le RIE.

### Gitlab

Le repo [gitlab/rapportnav2](https://gitlab-sml.din.developpement-durable.gouv.fr/rapportnav-v2/rapportnav_v2) est accessible apès avoir :
- obtenu un compte Cerbere (demande auprès de l'intrapreneur.e)
- été invité par la DAM-SI et obtenu un token


### Mirroring the repo

Ce repo gitlab est un miroir de notre repo github.
Les deux repos doivent donc être synchronisé dans les 2 sens car les équipes RapportNav et DAM-SI peuvent ajouter des changements sur la codebase.


### Chaine de déploiement CI/CD

La DAM-SI nous fournit une chaine CI/CD pré-programmée via Ansible.

#### Configuration

Au sein du repo rapportnav2 se trouve un dossier `.gitlab-ci` dans lequel se trouvent :
- des configs nécessaires et spécifique au projet
- un template de Dockerfile

En effet, certaines configs sont vraiment spécifiques au projet comme par ex la manière de build le backend ou le frontend.

Les différentes pipelines sont accessibles [ici](https://gitlab-sml.din.developpement-durable.gouv.fr/rapportnav-v2/rapportnav_v2/-/pipelines) et sont déclenchées
- à la main en sélectionnant une branche
- automatiquement après un push sur la branche main

La configuration de la pipeline se trouve [ici](https://gitlab-sml.din.developpement-durable.gouv.fr/rapportnav-v2/rapportnav_v2/-/ci/editor?branch_name=main&tab=1)

#### Continuous Integration (CI)

Dans les grands principes, la Continuous Integration se chargera de build et analyser le projet

##### CI Build

Dans cette toute première étape, différents élements vont être build et recombinés :
- le frontend
- le backend
- le tout ensemble dans une image Docker

##### CI Analysis

Une fois le projet buildé, différentes étapes d'analyse de failles et de qualité de code sont effectuées :
- Analyse des dépendances via [dependency-check](https://owasp.org/www-project-dependency-check/)
- Analyse des images/containers Docker via [Trivy](https://trivy.dev/latest/)
- Analyse de qualité via [sonarqube](https://github.com/SonarSource/sonarqube)

Pour chacune de ces étapes, certaines configs, telles que la criticalité des failles, sont disponibles dans le fichier `.gitlab-ci.yml`.
Il conviendra d'en discuter avec la DAM-SI car in fine, ils ont la responsabilité de cette chaine CI. 


#### Continuous Deployment (CD)

Lors de cette étape, uniquement déclenchée manuellement via l'UI Gitlab, il est possible de choisir sur quel environement déployer (pour l'instant intégration ou production).
Ainsi l'image Docker sera taggée et déployée sur les machines correspondantes.



