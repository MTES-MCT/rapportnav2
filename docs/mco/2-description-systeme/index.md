## 2. Description du système

### 2.1 Vue d’ensemble fonctionnelle

Rédiger simplement les compte-rendus de missions des affaires maritimes

RapportNav est l’application de rédaction de rapport qui vise à remplacer la multiplicité des outils et formats de rapport par un unique compte rendu de mission. L’ensemble des statistiques en sont ensuite extraites. L’agent est guidé pour rédiger son rapport au fil de sa mission. Cet outil présente encore des problèmes de développement et agrandit son périmètre d'intervention (outil utilisable par les ULAM, les patrouilleurs, et les agents Navpro).

Lien vers la page Produit : https://beta.gouv.fr/startups/rapportnav.html
Contexte

Le Dispositif de Contrôle et de Surveillance (DCS) des Affaires Maritimes englobe l’ensemble des moyens des Affaires Maritimes dédiés aux contrôles des pêches, de l’environnement marin, du travail et de la navigation maritime. Ces unités interviennent sur plusieurs politiques publiques avec, pour chacune, plusieurs donneurs d’ordres et centres de coordination nationale, chacun exigeant la transmission d’un rapport à un format spécifique avec les données le concernant. Problème

Nous listons plusieurs problèmes:

- La multiplication des demandes de rapports avec des modalités et formats différents aux unités est fortement chronophage pour elles. Les modalités de comptabilisation et de rapportage variant au niveau local, il est complexe de construire une vision nationale de l’action de l’État.
- L’hétérogénéité de la qualité des données collectées ne permet pas une analyse pertinente de la situation sur le terrain Des outils de rapportage et de ciblage comme Monitor Fish et Monitor Env se développent sur le périmètre de la pêche et de l’environnement marin, mais n’adressent qu’une partie du périmètre d’activités des unités concernées

Solution

RapportNav est initialement une application de rédaction de rapport qui vise à remplacer la multiplicité des outils et formats de rapport par un unique compte-rendu de mission. L’ensemble des statistiques en sont ensuite extraites. Nous souhaitons refondre l’expérience utilisateur du RapportNav existant afin d’optimiser son usage par les unités terrain, et ainsi la qualité des données saisies. L’engagement des bureaux de la DGAMPA dans l’analyse de ces données permettra d’améliorer les orientations et la coordination des actions des unités sur le terrain, et de maximiser leur impact auprès des citoyens. Les périmètres où nous souhaitons améliorer l’impact de l’action de l’État : la pêche, la protection de l’environnement marin, les conditions de travail des gens de mer, la navigation maritime, la sécurité des navires. Étapes

La 1ere version de Rapport Nav a eu plusieurs objectifs:

- Unifier la rédaction des rapports de missions pour les ULAM : le produit s’est d’abord focalisé sur la suppression de rapports existants et redondants rédigés par les agents des Unités Littorales des Affaires Maritimes (ULAM).
- Extension aux Patrouilleurs des Affaires Maritimes (PAM) : l’inclusion des Patrouilleurs des Affaires Maritimes (PAM) comme bénéficiaires du produit permet d’aller plus loin dans l’unification du rapportage.

Sur la base de cet existant, une réflexion pour faire de Rapport Nav une start up d’état a été mise en œuvre. À cette occasion, nous avons retravaillé la trajectoire du produit mais aussi son impact. Impact

- Faciliter la saisie des informations pour les unités sur le terrain, et donc maximiser la qualité des données collectées
- Optimiser la visibilité des activités terrain mais aussi l’utilisation des moyens mis à disposition des unités par l’Etat.
- Faciliter le partage et la diffusion d’information sur les contrôles effectués par les affaires maritimes, au sein des affaires maritimes ainsi qu’avec les autres administrations.
- Permettre aux bureaux concernés de définir des grandes orientations sur les missions de surveillance et contrôle, et de constater les résultats de ces actions
- Permettre aux unités de mieux cibler leurs actions sur le terrain
- le cas échéant, améliorer la réglementation pour la rendre plus applicable
- Mieux protéger la population halieutique, l’environnement nautique, les gens de mer dans l’exercice de leur activité, les individus pratiquant des activités nautiques de plaisance

RapportNav a un double objectif :
- collecter des données qualifiées afin de mieux orienter le ciblage des contrôles
- faire gagner du temps aux unités en ne remplissant plus qu'un seul rapport au lieu de plusieurs

Ainsi RapportNav permet aux unités de :
- compléter leur rapports de mission
- exporter les tableaux AEM
- exporter les rapports de patrouille (PAM uniquement)
- consulter leur données sous formes de dashboards d'analyses


### 2.2 Architecture technique

Veuillez vous reporter à la documentation sur la [stack technique](https://mtes-mct.github.io/rapportnav2/#/engineering/stack/index)


### 2.3 Environnements
| Environnement | Usage principal                 | Hébergement      | Particularités |
|---------------|---------------------------------|------------------|----------------|
| Local         | dev local                       | Machines locales |                |
| Développement | inutilisée                      | DAMSI St. Malo   |                |
| Intégration   | tests avant mise en prod        | DAMSI St. Malo   | iso-prod       |
| Production    | version livrée aux utilisateurs | DAMSI St. Malo   |                |

### 2.4 Dépendances externes
- API externes :
  - MonitorEnv : connexion https serveur<>serveur
  - MonitorFish : connexion https serveur<>serveur avec clé API
- Services cloud :
  - Chaine CI/CD et hébergement à la DAMSI St. Malo
