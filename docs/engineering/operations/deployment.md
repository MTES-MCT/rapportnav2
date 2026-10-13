# Déploiement

Le dépôt GitHub est mirroré sur le dépôt GitLab de l'hébergeur, qui exécute le
pipeline de déploiement (voir la [Charte de développement](../../charte-dev/index),
§10). Ce dépôt hôte peut lui aussi recevoir des commits : **penser à aligner les
deux dépôts avant tout déploiement.**

## Pré-requis

- Ajouter le mirror si ce n'est pas déjà fait :
  `git remote add mirror https://gitlab-sml.din.developpement-durable.gouv.fr/rapportnav-v2/rapportnav_v2.git`
- Récupérer les changements : `git fetch mirror`
  - identifiant : votre email `@i-carre.net`
  - mot de passe : token fourni par les autres devs ou la devops à la DSI
- Pull des changements éventuels : `git pull mirror main`
- Pousser sur ce dépôt si nécessaire

## Instructions

### Job automatisé (cassé)

- S'assurer d'avoir les derniers changements du mirror (fetch & pull depuis le mirror)
- Définir le numéro de version dans `build.gradle.kts`
- Définir le numéro de version dans `package.json` puis relancer `make front-ci`
  pour régénérer le `package-lock.json`
- Définir le numéro de version dans la variable `PROJECT_VERSION` du fichier
  `.gitlab-ci.yml`
- Créer une release GitHub avec le même numéro de version
- Vérifier l'exécution de l'Action `release`
- Vérifier le
  [pipeline](https://gitlab-sml.din.developpement-durable.gouv.fr/num3-exploitation/deploiement-continu/gitlab-ci/applications/rapportnav-v2/rapportnav-v2/-/pipelines)

### Déploiement manuel

- Cloner le dépôt GitLab :
  `git clone https://gitlab-sml.din.developpement-durable.gouv.fr/rapportnav-v2/rapportnav_v2.git`
  - identifiant : votre email `@i-carre.net`
  - mot de passe : token fourni par les autres devs ou la devops à la DSI
- Ajouter le dépôt GitHub comme mirror :
  `git remote add mirror https://github.com/MTES-MCT/rapportnav2.git`
- Récupérer les changements : `git fetch`
- Aligner votre branche locale sur la remote main : `git pull`
- Récupérer le mirror : `git fetch mirror`
- Pull des changements distants : `git pull mirror main`
- Pousser les changements pour démarrer le déploiement : `git push`
