# Gestion des équipages


## Gestion équipages PAM

La gestion des équipages PAM est un sujet récurrent étant donné que plusieurs changements se produiront par an et par bordée.

A l'heure actuelle, ce processus est effectué par les développeurs à l'aide de migrations de base de donnéees.

Pour conserver les données historiques, il est important de ne pas supprimer de rows dans la table `agent_service`.

### Operations

#### Agents

Voici les opérations disponibles:
- create: ajouter une role dans la table `agent`
- update: 
- suppression: pas vraiment nécessaire mais possibilité de rajouter un timestamp dans la colonne `deletedAt`

#### Agents <> Service

Voici les opérations disponibles:
- ajouter un agent dans un service: 
  - ajouter une row dans `agent_service`
- changer un agent de service:
  - mettre `disabledAt` dans la row de l'ancien service dans `agent_service`
  - ajouter une row dans `agent_service` pour le nouveau service
- supprimer un agent dans un service :
  - - mettre `disabledAt` dans la row correspondante dans `agent_service`
