# Completude des missions

## Vue d'ensemble

La completude d'une mission est calculee a trois niveaux :

1. **Informations generales** (`MissionGeneralInfoEntity2`)
2. **Actions** (`MissionActionEntity` et ses sous-classes)
3. **Donnees environnement** (`MissionEnvEntity`)

Le resultat global est agrege dans `MissionEntity.isCompleteForStats()`.

## Groupes de validation

Le systeme utilise Jakarta Bean Validation avec deux groupes :

| Groupe                       | Quand                  | Effet                                                                               |
|------------------------------|------------------------|-------------------------------------------------------------------------------------|
| `ValidateThrowsBeforeSave`   | A chaque sauvegarde    | Leve une exception si les dates sont invalides (fin avant debut, hors plage mission) |
| `ValidateWhenMissionFinished` | Au calcul de completude | Verifie que les champs requis sont remplis                                          |

## Regles de validation (`RequiredFieldsValidator`)

Les champs requis sont definis dans `RequiredFieldsValidator` sous forme de regles declaratives.
Chaque regle est associee a une entite et definit :
- le **champ** concerne
- la **condition** d'application (type d'action, type de service, etc.)
- le **message d'erreur**

Il existe trois types de regles :
- **Always** : le champ est toujours requis
- **ForActionTypes** : le champ est requis pour certains types d'action
- **Conditional** : le champ est requis selon une condition arbitraire (ex: `locationType = GPS` implique `latitude` et `longitude`)

La liste complete des regles est disponible dans [validation-rules.md](validation-rules.md) (fichier genere automatiquement).

Pour regenerer ce fichier :
```
cd backend && ./gradlew generateValidationDocs
```

## Completude des actions

Chaque action (`MissionNavActionEntity`, `MissionFishActionEntity`, `MissionEnvActionEntity`) herite de `MissionActionEntity` qui fournit la methode `computeValidityForStats()`.

Cette methode :
1. Execute la validation Jakarta avec les deux groupes (`ValidateThrowsBeforeSave` + `ValidateWhenMissionFinished`)
2. Stocke le resultat dans `completenessForStats` (statut + liste d'erreurs)
3. Attribue la source des erreurs (`RAPPORT_NAV`, `MONITORENV`, `MONITORFISH`)
4. Met a jour `isCompleteForStats`

Une action est consideree **complete** si :
- Aucune violation de champ requis
- Aucun controle manquant (`controlsToComplete` vide)

## Completude des informations generales

`MissionGeneralInfoEntity2` encapsule :
- `data` : les donnees generales (`MissionGeneralInfoEntity`)
- `crew` : l'equipage
- `services` : les services

La completude depend du **type de service** :

### PAM
- L'equipage ne doit pas etre vide
- Les champs `distanceInNauticalMiles`, `consumedGOInLiters`, `consumedFuelInLiters`, `nbrOfRecognizedVessel` doivent etre renseignes

### ULAM
- L'equipage ne doit pas etre vide
- Si `isWithInterMinisterialService = true`, les services interministeriels doivent etre renseignes

## Completude globale de la mission

`MissionEntity.isCompleteForStats()` agrege les trois niveaux :

```
isComplete = envDataComplete && generalInfoComplete
statut final = si isComplete alors actionsStatus sinon INCOMPLETE
```

**Cas particulier** : les missions inter-services (conjointes) ne verifient pas les informations generales.
