# Présentation fonctionnelle

## À quoi sert RapportNav

RapportNav est l'application de **rédaction des comptes-rendus de mission des Affaires Maritimes**. Elle vise à remplacer la multiplicité des outils et formats de rapport par un **unique compte-rendu de mission**, dont l'ensemble des statistiques est ensuite extrait.

Elle s'inscrit dans le **Dispositif de Contrôle et de Surveillance (DCS)** des Affaires Maritimes, qui regroupe les moyens dédiés au contrôle des pêches, de l'environnement marin, du travail et de la navigation maritime.

RapportNav poursuit un double objectif :

- **collecter des données qualifiées** afin de mieux orienter le ciblage des contrôles ;
- **faire gagner du temps aux unités** en ne remplissant plus qu'un seul rapport au lieu de plusieurs.

Concrètement, l'application permet aux unités de :

- compléter leurs rapports de mission ;
- exporter les tableaux AEM ;
- exporter les rapports de patrouille (PAM uniquement) ;
- consulter leurs données sous forme de tableaux de bord d'analyse.

> Le détail métier est décrit dans [Concepts généraux](../concepts/index) et dans la [description du système (MCO)](../mco/2-description-systeme/index).

## Une application non standalone

Point important pour l'hébergeur : **RapportNav n'est pas un outil autonome**. Il dépend fonctionnellement et techniquement de **MonitorFish** et **MonitorEnv** (deux autres startups d'État), notamment parce que les Missions sont aujourd'hui stockées chez MonitorEnv.

Ces liens reposent sur un principe de **co-saisie** : les centres et les unités de terrain se partagent la saisie des rapports, chacun renseignant les parties dont il a la connaissance la plus fine, les informations étant ensuite synchronisées entre RapportNav et Monitor.

Voir [Co-saisie avec MonitorFish et MonitorEnv](../concepts/fish-env) et la page [Flux de données externes](flux-donnees-externes).

## Utilisateurs & rôles

L'application est utilisée par des agents de l'État. Deux grands profils d'unités :

- **PAM** — Patrouilleurs des Affaires Maritimes (navires : Jeanne Barret, Themis, Iris, Gyptis).
- **ULAM** — Unités Littorales des Affaires Maritimes (unités côtières, réparties par département).

Les rôles applicatifs (issus de `RoleTypeEnum` / `AuthoritiesEnum`) sont :

| Rôle          | Description                                                              |
|---------------|--------------------------------------------------------------------------|
| `ADMIN`       | Administrateurs (équipe RapportMav / DGAMPA) : gestion des utilisateurs et accès à tous les périmètres. |
| `MANAGER_PAM` | Encadrement du service PAM : gestion des missions et données PAM, tableau de bord PAM. |
| `MANAGER_ULAM`| Encadrement du service ULAM : gestion des missions et données ULAM, tableau de bord ULAM. |
| `USER_PAM`    | Agent de terrain PAM : création / consultation des missions PAM.          |
| `USER_ULAM`   | Agent de terrain ULAM : création / consultation des missions ULAM.        |
| `API_USER`    | Accès machine-à-machine (backend-à-backend), authentifié par clé API.     |

> Le détail des utilisateurs figure dans [Les utilisateurs de RapportNav](../concepts/users) et des rôles techniques dans [Les rôles](../engineering/concepts/roles).
