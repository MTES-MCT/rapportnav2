# Gestion des Erreurs

## Principes

La gestion des erreurs dans RapportNav suit les principes de la Clean Architecture :

1. **Le domain layer reste propre** - Pas de try-catch dans les use cases
2. **L'infrastructure gère les erreurs techniques** - Les repositories JPA et API encapsulent les exceptions techniques
3. **Les controllers s'appuient sur un handler global** - Pas de try-catch dans les controllers

## Types d'Exceptions

### BackendUsageException

Exception pour les **violations de règles métier** (erreurs utilisateur). Retourne un code HTTP **400 Bad Request**.

```kotlin
throw BackendUsageException(
    code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
    message = "CreateMission: Control Units or Service are falsy"
)
```

**Codes disponibles** (`BackendUsageErrorCode`) :
- `INVALID_PARAMETERS_EXCEPTION` - Paramètres invalides ou manquants
- `COULD_NOT_FIND_EXCEPTION` - Ressource non trouvée
- `COULD_NOT_DELETE_EXCEPTION` - Échec de suppression
- `COULD_NOT_SAVE_EXCEPTION` - Échec de sauvegarde

### BackendInternalException

Exception pour les **erreurs internes inattendues** (erreurs système). Retourne un code HTTP **500 Internal Server Error**.

```kotlin
throw BackendInternalException(
    message = "MonitorEnv API returned status=${response.statusCode()}"
)
```

## Architecture par Layer

### Domain Layer (Use Cases)

Les use cases **ne doivent pas** contenir de try-catch. Ils :
- Valident les paramètres et lancent `BackendUsageException` si invalides
- Appellent les repositories sans gérer les exceptions
- Laissent les exceptions remonter naturellement

```kotlin
@UseCase
class CreateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
) {
    fun execute(input: MissionAction): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        return MissionNavActionEntity.fromMissionActionModel(
            missionActionRepository.save(action.toMissionActionModel())
        )
    }
}
```

### Infrastructure Layer (Repositories JPA)

Les repositories JPA capturent les exceptions techniques et les encapsulent :

```kotlin
@Repository
class JPAMissionActionRepository(
    private val dbRepo: IDBMissionActionRepository
) : INavMissionActionRepository {

    override fun save(action: MissionActionModel): MissionActionModel {
        return try {
            dbRepo.save(action)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Invalid data for action: ${action.id}",
                cause = e
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to save action: ${action.id}",
                cause = e
            )
        }
    }

    override fun deleteById(id: UUID) {
        if (!dbRepo.existsById(id)) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Action not found: $id"
            )
        }
        try {
            dbRepo.deleteById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to delete action: $id",
                cause = e
            )
        }
    }
}
```

### Infrastructure Layer (Repositories API externes)

Les repositories API gèrent les erreurs HTTP :

```kotlin
@Repository
class APIEnvMissionRepositoryV2(
    private val mapper: ObjectMapper,
    private val apiClient: MonitorEnvApiClient
) {
    fun createMission(mission: CreateMissionInput): MissionEnvEntity {
        val response = apiClient.post("/missions", mapper.writeValueAsString(mission))

        if (response.statusCode() !in 200..299) {
            throw BackendInternalException(
                message = "MonitorEnv API returned status=${response.statusCode()} for POST mission"
            )
        }

        return mapper.readValue(response.body(), MissionEnvEntity::class.java)
    }
}
```

### Controllers

Les controllers **ne doivent pas** contenir de try-catch. Le `ControllersExceptionHandler` global gère toutes les exceptions :

```kotlin
@RestController
class ActionRestController(
    private val updateNavAction: UpdateNavAction
) {
    @PutMapping("{actionId}")
    fun updateAction(
        @PathVariable actionId: String,
        @RequestBody body: MissionAction
    ): MissionAction? {
        val response = when (body.source) {
            MissionSourceEnum.RAPPORTNAV -> updateNavAction.execute(actionId, body as MissionNavAction)
            else -> throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Unknown mission action source: ${body.source}"
            )
        }
        return MissionAction.fromMissionActionEntity(response)
    }
}
```

### Exception Handler Global

Le `ControllersExceptionHandler` intercepte toutes les exceptions et retourne les codes HTTP appropriés :

```kotlin
@ControllerAdvice
class ControllersExceptionHandler {

    @ExceptionHandler(BackendUsageException::class)
    fun handleBackendUsageException(ex: BackendUsageException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(code = ex.code.name, message = ex.message))
    }

    @ExceptionHandler(BackendInternalException::class)
    fun handleBackendInternalException(ex: BackendInternalException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(message = ex.message))
    }
}
```

## Flux d'une Erreur

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Controller │────>│   UseCase   │────>│ Repository  │────>│  Database   │
└─────────────┘     └─────────────┘     │   (JPA)     │     └─────────────┘
       │                   │            └─────────────┘            │
       │                   │                   │                   │
       │                   │                   │    SQLException   │
       │                   │                   │<──────────────────┘
       │                   │                   │
       │                   │   BackendInternal │
       │                   │   Exception       │
       │                   │<──────────────────┘
       │                   │
       │  Exception        │
       │  (propagée)       │
       │<──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ ControllersException    │
│ Handler                 │
└─────────────────────────┘
       │
       ▼
   HTTP 500 ou 400
```

## Bonnes Pratiques

### Ce qu'il faut faire

- Valider les paramètres en entrée des use cases et lancer `BackendUsageException`
- Encapsuler les exceptions techniques dans les repositories
- Utiliser `BackendUsageException` pour les erreurs prévisibles (validation, ressource non trouvée)
- Utiliser `BackendInternalException` pour les erreurs inattendues (base de données, API externe)
- Inclure un message descriptif pour faciliter le debugging

### Ce qu'il ne faut pas faire

- Ajouter des try-catch dans les use cases
- Ajouter des try-catch dans les controllers
- Retourner `null` pour masquer une erreur
- Utiliser `RuntimeException` ou `IllegalArgumentException` dans le code métier
- Logger une erreur puis la swallow (avaler sans la propager)

## Tests

### Test d'un Use Case

Les tests de use cases vérifient que les exceptions sont bien propagées :

```kotlin
@Test
fun `test throws exception when id different of action id`() {
    val updateNavAction = UpdateNavAction(
        missionActionRepository = missionActionRepository,
        processMissionActionTarget = processMissionActionTarget
    )

    val exception = assertThrows<BackendUsageException> {
        updateNavAction.execute(actionId, inputWithDifferentId)
    }
    assertThat(exception.message).isEqualTo("UpdateNavAction: action id mismatch")
}
```

### Test d'un Repository

Les tests de repositories vérifient l'encapsulation des exceptions :

```kotlin
@Test
fun `save should throw BackendInternalException on generic exception`() {
    whenever(dbRepo.save(any())).thenThrow(RuntimeException("DB error"))

    assertThrows<BackendInternalException> {
        repository.save(entity)
    }
}

@Test
fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
    whenever(dbRepo.save(any())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

    assertThrows<BackendUsageException> {
        repository.save(entity)
    }
}
```
