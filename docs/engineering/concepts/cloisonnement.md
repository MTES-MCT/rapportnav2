# Cloisonnement des données (contrôle d'accès au niveau objet)

Au-delà de l'authentification et des rôles (voir [Les rôles](roles.md)), l'application applique un **cloisonnement par service / unité de contrôle** : un utilisateur authentifié ne peut accéder qu'aux missions et actions rattachées à son propre service.

> Ce contrôle répond à une observation **MAJEURE** de l'audit de code (« Absence de cloisonnement ») : un utilisateur pouvait, en appelant directement une route par identifiant, récupérer **n'importe quelle** mission ou action, quelle que soit son unité de rattachement (faille IDOR).
>
> Pour l'angle gouvernance / audit, voir [MCS §4.1.5](../../mcs/4-gestion-acces/index.md).

## Principe

- Un utilisateur n'accède qu'aux données de son service (et de ses **services liés**, voir `GetUserServiceScope`).
- Les utilisateurs **`ADMIN`** (équipe RapportNav) ne sont **pas** soumis au cloisonnement.
- Résultat d'un contrôle :
  - accès refusé → **HTTP 403** (`USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION`)
  - ressource inexistante → **HTTP 404** (`COULD_NOT_FIND_EXCEPTION`)
  - identifiant malformé → **HTTP 400** (`INVALID_PARAMETERS_EXCEPTION`)
- Le contrôle **échoue de manière fermée** (fail-closed) : un propriétaire nul ou hors périmètre est refusé.

## Modèle de propriété

La logique de référence vit dans le use case `AssertMissionAccess` (`domain/use_cases/mission/v2/`), et reflète le filtrage déjà appliqué aux routes de **liste** :

| Ressource        | Identifiant        | Propriété vérifiée |
|------------------|--------------------|--------------------|
| Mission **nav**  | `UUID`             | `serviceId` de la mission ∈ services de l'utilisateur |
| Mission **env**  | entier (externe)   | une des `controlUnits` de la mission ∈ unités de contrôle de l'utilisateur |
| **Inquiry**      | `UUID`             | `serviceId` de l'inquiry ∈ services de l'utilisateur (comme une mission nav) |

> Cas particulier : une mission nav qui « miroite » une mission env porte un `externalId` ; elle est alors cloisonnée via les unités de contrôle, pas via le `serviceId`.

## Règle d'or : déclaratif, pas impératif

On **n'appelle pas** `assertMissionAccess.execute(id)` à la main dans le handler. On **déclare** l'exigence d'accès avec l'annotation `@CheckMissionAccess` sur la méthode du contrôleur v2. Elle est appliquée **avant** le corps de la méthode par `MissionAccessAspect`.

```kotlin
// Mission / propriétaire par id de chemin
@CheckMissionAccess(param = "ownerId")
fun getActions(@PathVariable(name = "ownerId") ownerId: String): List<MissionAction?> { ... }

// Inquiry (modèle de propriété distinct)
@CheckMissionAccess(param = "inquiryId", resource = MissionAccessResource.INQUIRY)
fun update(@PathVariable(name = "inquiryId") inquiryId: String, @RequestBody body: Inquiry): Inquiry? { ... }

// Collection d'ids à l'intérieur d'un @RequestBody (chaque id est vérifié)
@CheckMissionAccess(param = "request.missionIds", collection = true)
fun exportMissionReports(@Valid @RequestBody request: ExportBodyRequest): MissionExportEntity { ... }

// Ressource imbriquée : vérifier que l'enfant (actionId) appartient bien au propriétaire (ownerId)
@CheckMissionAccess(param = "ownerId", child = "actionId", childResource = MissionChildResource.NAV_ACTION)
fun deleteAction(@PathVariable actionId: String, @PathVariable ownerId: String) { ... }
```

Paramètres de l'annotation :

- `param` — où lire l'id : soit le nom d'un paramètre du handler (`@PathVariable` / `@RequestParam`), soit un chemin pointé `"<param>.<propriété>"` pour atteindre un champ dans un `@RequestBody`.
- `resource` — `MISSION` (défaut) ou `INQUIRY`.
- `collection` — `true` quand la valeur résolue est une `Collection` d'ids, chacun vérifié individuellement (ex. export).
- `child` / `childResource` — contrôle de **containment** : nomme l'id enfant qui doit appartenir au propriétaire résolu par `param`. Ferme l'IDOR « propriétaire autorisé + enfant étranger » qu'un contrôle sur `param` seul ne détecte pas.

## Architecture (hexagonale)

Le dispositif respecte l'[architecture hexagonale](missions-and-actions.md) : la logique d'autorisation reste dans un use case pur, l'infrastructure ne fait que câbler.

- `infrastructure/api/security/`
  - `CheckMissionAccess` — l'annotation déclarative.
  - `MissionAccessAspect` — l'aspect AOP : résout l'id déclaré et **délègue** la décision à `AssertMissionAccess`. Il n'ajoute aucune logique d'autorisation propre, donc un refus lève exactement la même exception qu'un appel inline.
  - `ChildAccessGuard` / `NavActionAccessGuard` — résolvent le propriétaire d'une ressource enfant pour le contrôle de containment.
- `domain/use_cases/mission/v2/AssertMissionAccess` — la décision de cloisonnement, pure, testable, sans dépendance au web.

> **Note AOP / CGLIB.** Spring AOP proxie via CGLIB, qui ne peut pas surcharger des membres `final`. Cela ne fonctionne que parce que le build applique `kotlin("plugin.spring")` (all-open), qui ouvre les classes/méthodes `@RestController`. **Ne pas retirer ce plugin.** `aspectjweaver` + `spring-aop` sont déjà au classpath (pas de nouvelle dépendance).

## Ajouter un nouvel endpoint by-id

1. Ajouter `@CheckMissionAccess(...)` sur le handler.
2. **Couvrir chaque `@PathVariable` d'identifiant** : le propriétaire via `param`, tout id enfant via `child`. Un path variable d'id non couvert = un IDOR potentiel.
3. Si l'id est porté dans le `@RequestBody` (ex. export), utiliser le chemin pointé `param = "request.xxx"` et couvrir le cas par un test de contrôleur dédié (voir ci-dessous).

## Filet de sécurité (fail-closed)

`MissionAccessCoverageTest` (`infrastructure/api/security/`) **casse le build** si un handler v2 prenant un `@PathVariable` n'est pas entièrement couvert :

- le handler doit porter `@CheckMissionAccess`, **et**
- chaque path variable d'id doit être couvert par `param` ou `child`.

Les exceptions délibérées sont inscrites dans une **allowlist** avec justification (ex. catalogues globaux `VesselRestController#getVesselById`, `AdministrationController#get` ; profil utilisateur `UserRestController#getUserById` qui applique un cloisonnement inline self / même-service / admin).

> **Limite volontaire** : le test ne scanne que les **path variables**. Un endpoint qui porte l'id dans un `@RequestBody` (ex. export) n'est pas attrapé automatiquement — il faut l'annoter **et** le couvrir par son propre test de contrôleur.

## Tests en tranche `@WebMvcTest`

Les tranches `@WebMvcTest` n'enregistrent pas les aspects `@Component` par défaut. Pour que `MissionAccessAspect` se déclenche dans ces tests, ajouter `MissionAccessTestConfig::class` au `@ContextConfiguration` de la tranche.
