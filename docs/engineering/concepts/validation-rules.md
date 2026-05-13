# Regles de validation des champs requis

> **Fichier genere automatiquement** a partir du code source.
> Ne pas modifier manuellement. Lancer le generateur pour mettre a jour :
> `./gradlew generateValidationDocs`
>
> Derniere generation : 2026-05-13

Ces regles sont evaluees lorsque la mission est cloturee (groupe `ValidateWhenMissionFinished`).
Un champ en erreur apparait dans le panneau de completude.

## MissionGeneralInfoEntity

| Champ | Condition | Message d'erreur |
|-------|-----------|------------------|
| `distanceInNauticalMiles` | serviceType = PAM | La distance en milles nautiques est requise |
| `consumedGOInLiters` | serviceType = PAM | La consommation de GO en litres est requise |
| `consumedFuelInLiters` | serviceType = PAM | La consommation de carburant en litres est requise |
| `nbrOfRecognizedVessel` | serviceType = PAM | Le nombre de navires reconnus est requis |
| `interMinisterialServices` | serviceType = ULAM et isWithInterMinisterialService = true | Les services interministériels sont requis |

## MissionNavActionEntity

| Champ | Condition | Message d'erreur |
|-------|-----------|------------------|
| `id` | Toujours | L'identifiant est requis |
| `missionId` | Toujours | L'identifiant de mission est requis |
| `actionType` | Toujours | Le type d'action est requis |
| `startDateTimeUtc` | Toujours | La date de début est requise |
| `endDateTimeUtc` | actionType ∈ {ANTI_POLLUTION, BAAEM_PERMANENCE, CONTROL, RESCUE, VIGIMER, REPRESENTATION, PUBLIC_ORDER, ILLEGAL_IMMIGRATION, NAUTICAL_EVENT, CONDUCT_HEARING, COMMUNICATION, TRAINING, UNIT_MANAGEMENT_PLANNING, UNIT_MANAGEMENT_TRAINING, CONTROL_SECTOR, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL, RESOURCES_MAINTENANCE, MEETING, PV_DRAFTING, HEARING_CONDUCT, LAND_SURVEILLANCE, FISHING_SURVEILLANCE, UNIT_MANAGEMENT_OTHER, OTHER, MARITIME_SURVEILLANCE} | La date de fin est requise |
| `latitude` | actionType ∈ {RESCUE, ILLEGAL_IMMIGRATION, ANTI_POLLUTION} **OU** actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = GPS | La latitude est requise |
| `longitude` | actionType ∈ {RESCUE, ILLEGAL_IMMIGRATION, ANTI_POLLUTION} **OU** actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = GPS | La longitude est requise |
| `city` | actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = COMMUNE | La commune est requise |
| `zipCode` | actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = COMMUNE | Le code postal est requis |
| `portLocode` | actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = PORT **OU** actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType = LANDING_SITE | Le port est requis |
| `controlMethod` | actionType ∈ {CONTROL} | La méthode de contrôle est requise |
| `vesselIdentifier` | actionType ∈ {CONTROL} | L'identifiant du navire est requis |
| `vesselType` | actionType ∈ {CONTROL} | Le type de navire est requis |
| `vesselSize` | actionType ∈ {CONTROL} | La taille du navire est requise |
| `identityControlledPerson` | actionType ∈ {CONTROL} | L'identité de la personne contrôlée est requise |
| `nbOfInterceptedVessels` | actionType ∈ {ILLEGAL_IMMIGRATION} | Le nombre de navires interceptés est requis |
| `nbOfInterceptedMigrants` | actionType ∈ {ILLEGAL_IMMIGRATION} | Le nombre de migrants interceptés est requis |
| `nbOfSuspectedSmugglers` | actionType ∈ {ILLEGAL_IMMIGRATION} | Le nombre de passeurs suspectés est requis |
| `status` | actionType ∈ {STATUS} | Le statut est requis |
| `reason` | actionType = STATUS et status ∈ {DOCKED, UNAVAILABLE} | La raison est requise |
| `nbrOfHours` | actionType ∈ {INQUIRY} | Le nombre d'heures est requis |
| `trainingType` | actionType ∈ {TRAINING} | Le type de formation est requis |
| `unitManagementTrainingType` | actionType ∈ {UNIT_MANAGEMENT_TRAINING} | Le type de formation est requis |
| `resourceType` | actionType ∈ {RESOURCES_MAINTENANCE} | Le type de ressource est requis |
| `resourceId` | actionType ∈ {RESOURCES_MAINTENANCE} | L'identifiant de ressource est requis |
| `nbrOfControl` | actionType ∈ {CONTROL_NAUTICAL_LEISURE} | Le nombre de contrôles est requis |
| `nbrOfControlAmp` | actionType ∈ {CONTROL_NAUTICAL_LEISURE} | Le nombre de contrôles AMP est requis |
| `nbrOfControl300m` | actionType ∈ {CONTROL_NAUTICAL_LEISURE} | Le nombre de contrôles 300m est requis |
| `leisureType` | actionType ∈ {CONTROL_NAUTICAL_LEISURE} | Le type de loisir est requis |
| `sectorType` | actionType ∈ {CONTROL_SECTOR} | Le type de secteur est requis |
| `sectorEstablishmentType` | actionType ∈ {CONTROL_SECTOR} | Le type d'établissement est requis |
| `establishment` | actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType ∉ {FISH_AUCTION, LANDING_SITE} | L'établissement est requis |
| `fishAuction` | actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType = FISH_AUCTION | La criée est requise |
| `fishingGearType` | actionType ∈ {CONTROL_SLEEPING_FISHING_GEAR} | Le type d'engin de pêche est requis |
| `controlType` | actionType ∈ {OTHER_CONTROL} | Le type de contrôle est requis |
| `securityVisitType` | actionType ∈ {SECURITY_VISIT} | Le type de visite de sécurité est requis |
| `nbrSecurityVisit` | actionType ∈ {SECURITY_VISIT} | Le nombre de visites de sécurité est requis |
| `numberPersonsRescued` | actionType = RESCUE et isPersonRescue = true | Le nombre de personnes secourues est requis |
| `numberOfDeaths` | actionType = RESCUE et isPersonRescue = true | Le nombre de décès est requis |
| `nbOfVesselsTrackedWithoutIntervention` | actionType = RESCUE et isMigrationRescue = true | Le nombre de navires suivis sans intervention est requis |
| `nbAssistedVesselsReturningToShore` | actionType = RESCUE et isMigrationRescue = true | Le nombre de navires assistés retournant à terre est requis |

---

## Champs requis par type d'action

### ANTI_POLLUTION

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | - |
| `longitude` | - |

### BAAEM_PERMANENCE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### COMMUNICATION

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### CONDUCT_HEARING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### CONTROL

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | locationType = GPS |
| `longitude` | locationType = GPS |
| `city` | locationType = COMMUNE |
| `zipCode` | locationType = COMMUNE |
| `portLocode` | locationType = PORT |
| `controlMethod` | - |
| `vesselIdentifier` | - |
| `vesselType` | - |
| `vesselSize` | - |
| `identityControlledPerson` | - |

### CONTROL_NAUTICAL_LEISURE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | locationType = GPS |
| `longitude` | locationType = GPS |
| `city` | locationType = COMMUNE |
| `zipCode` | locationType = COMMUNE |
| `portLocode` | locationType = PORT |
| `nbrOfControl` | - |
| `nbrOfControlAmp` | - |
| `nbrOfControl300m` | - |
| `leisureType` | - |

### CONTROL_SECTOR

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `sectorType` | - |
| `sectorEstablishmentType` | - |
| `establishment` | sectorType = FISHING et sectorEstablishmentType ∉ {FISH_AUCTION, LANDING_SITE} |
| `fishAuction` | sectorType = FISHING et sectorEstablishmentType = FISH_AUCTION |
| `portLocode` | sectorType = FISHING et sectorEstablishmentType = LANDING_SITE |

### CONTROL_SLEEPING_FISHING_GEAR

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | locationType = GPS |
| `longitude` | locationType = GPS |
| `city` | locationType = COMMUNE |
| `zipCode` | locationType = COMMUNE |
| `portLocode` | locationType = PORT |
| `fishingGearType` | - |

### FISHING_SURVEILLANCE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### HEARING_CONDUCT

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### ILLEGAL_IMMIGRATION

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | - |
| `longitude` | - |
| `nbOfInterceptedVessels` | - |
| `nbOfInterceptedMigrants` | - |
| `nbOfSuspectedSmugglers` | - |

### INQUIRY

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `nbrOfHours` | - |

### LAND_SURVEILLANCE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### MARITIME_SURVEILLANCE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### MEETING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### NAUTICAL_EVENT

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### OTHER

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### OTHER_CONTROL

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | locationType = GPS |
| `longitude` | locationType = GPS |
| `city` | locationType = COMMUNE |
| `zipCode` | locationType = COMMUNE |
| `portLocode` | locationType = PORT |
| `controlType` | - |

### PUBLIC_ORDER

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### PV_DRAFTING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### REPRESENTATION

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### RESCUE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `latitude` | - |
| `longitude` | - |
| `numberPersonsRescued` | isPersonRescue = true |
| `numberOfDeaths` | isPersonRescue = true |
| `nbOfVesselsTrackedWithoutIntervention` | isMigrationRescue = true |
| `nbAssistedVesselsReturningToShore` | isMigrationRescue = true |

### RESOURCES_MAINTENANCE

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `resourceType` | - |
| `resourceId` | - |

### SECURITY_VISIT

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `securityVisitType` | - |
| `nbrSecurityVisit` | - |

### STATUS

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `status` | - |
| `reason` | status ∈ {DOCKED, UNAVAILABLE} |

### TRAINING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `trainingType` | - |

### UNIT_MANAGEMENT_OTHER

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### UNIT_MANAGEMENT_PLANNING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

### UNIT_MANAGEMENT_TRAINING

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |
| `unitManagementTrainingType` | - |

### VIGIMER

| Champ | Condition supplementaire |
|-------|-------------------------|
| `id` | - |
| `missionId` | - |
| `actionType` | - |
| `startDateTimeUtc` | - |
| `endDateTimeUtc` | - |

