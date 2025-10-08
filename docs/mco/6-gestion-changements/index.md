## 6. Gestion des changements

### 6.1 Typologie des changements

Les changements suivent la typologie proposée par [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/)

### 6.2 Processus de gestion des changements
RapportNav a pour chance d'avoir accès à ses utilisateurs très facilement. Ils sont donc inclus très tôt dans le processus de création et validation.

Les étapes suivantes constituent le socle du processus de développement :
- Recueil des besoins métiers
- Interviews utilisateurs
- Spécifications et maquettage
- Validation par les utilisateurs
- Développement informatique
- Tests internes
- Tests utilisateurs
- Mise en production
- Monitoring

### 6.3 Validation et communication

La validation finale des changements ainsi que la communication avec les utilisateurs est effectuée via l'entrepreneur.e de RapportNav.
Il n'y a pas à ce jour de communication automatique avec les utilisateurs.

### 6.4 Gestion des versions et déploiements

#### Méthodologie 

Grâce à la typologie conventional-commits, le versioning de l'application est automatisé et suit les convention de [semantinc versioning](https://semver.org/)

Dès qu'une nouvelle version est créée, la chaîne CI/CD est lancée avec les étapes suivantes :

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


#### Stratégies de déploiement :

Une stratégie de déploiement "rolling" a été mise en place par l'hébergeur, consistant à déployer un nouveau container Docker.

#### Traçabilité et rollback :

Un rollback peut être effectué par l'hébergeur à la demande de l'équipe RapportNav, bien que souvent, la stratégie du rollforward soit adoptée,
consistant en la mise en production d'un correctif.

