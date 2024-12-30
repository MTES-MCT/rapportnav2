# Co-saisie avec MonitorFish et MonitorEnv

Il y a un concept essentiel à assimiler ici : RapportNav n'est pas un outil standalone, il dépend de [MonitorFish et MonitorEnv](https://beta.gouv.fr/startups/monitorfish.html) pour exister. 
Ces 3 softwares sont intimement liés.

## Leur raison d'être 

Monitor est là pour optimiser l’effort de contrôle en permettant aux agents de mener des actions les plus efficientes possibles (notion de coût/efficacité de la mission), et ce en valorisant l’ensemble de leurs compétences et des moyens mis à leur disposition. Le projet vise à favoriser la réalisation des objectifs des plans de contrôle et améliorer le taux d’infraction des inspections. Le suivi de ces deux indicateurs-clés permet d’objectiver sur le long terme l’impact de la start-up.

La solution se décline en deux outils opérationnels : MonitorFish pour la partie “pêche professionnelle” (CNSP), MonitorEnv pour la partie “environnement marin” (CACEM).

Plusieurs axes de développement :
- centraliser toutes les informations importantes sur une carte
- développer des outils d’aide au ciblage (alertes en temps réel, facteur de risque…)
- faciliter le partage d’informations entre le CNSP, le CACEM et les agents de terrain
- outiller une montée en compétence en matière d’analyse de données

## Les liens RapportNav <> Monitor

En préambule, il est mentionné que RapportNav ne peut être considéré standalone car il dépend de Monitor. Pourquoi cela ?
Cela est dû à des raisons organisationnelles et techniques. 
Organisationelles car ces 3 softwares sont gérés par 3 startups d'état différentes, sur des budgets et timeline différentes.
Techniques car les Missions sont stockées a l'heure actuelle chez MonitorEnv, d'où la forte dépendance.


## La co-saisie

Avec RapportNav2, le concept de co-saisie a été intégré.
Il consiste à partager la saisie des rapports par différents acteurs afin que chacun puisse s'occuper des parties dont il a la plus fine connaissance tout au long de la chaine d'execution.
Chacun apporte sa pierre à l'édifice là où il est le plus compétent.

Ainsi, certaines informations seront saisies par les centres (ouverture d'un mission, saisie des moyens engagés, actions de controles ou surveillances...) 
alors que d'autres seront complétées par les unités terrains qui sont au plus proche de la réalité (détails des controles effectués, infractions, membres d'équipages...)

Ces informations sont synchronisées entre RapportNav et Monitor afin que les centres et les unités puissent les consulter.
