# Liste des missions : recombinaison, pagination et filtres

Ce document décrit comment la **liste des missions** (`GET /api/v2/missions`) est construite côté
backend : d'où viennent les missions, comment les deux sources sont fusionnées, comment la
pagination fonctionne, et où les filtres sont appliqués.

> Pour la notion de complétude d'une mission, voir [Complétude des missions](mission-completion.md).
> Pour l'objet Mission lui-même, voir [L'objet Mission](missions-and-actions.md).

## Les deux sources

Une liste de missions est toujours la **fusion de deux sources distinctes** :

| Source | Origine | Portée | Volume |
|---|---|---|---|
| **Missions env** | API MonitorEnv (HTTP) | les *unités de contrôle* de l'utilisateur | la majorité de la liste |
| **Missions nav** | base de données locale (table `mission`) | le *service* de l'utilisateur, missions nav-only | peu nombreuses par service |

Les missions « env » ne sont **pas** dans notre base : elles sont récupérées à chaque requête
auprès de MonitorEnv (la source de vérité). Les lignes « env-mirror » éventuellement présentes en
base sont exclues de la source nav (`serviceId` nul), pour ne pas dupliquer ce que MonitorEnv renvoie
déjà.

Le point d'entrée est `GetMissions.executePaginated` (`domain/use_cases/mission/v2/GetMissions.kt`),
appelé via `GetMissionList` puis `MissionRestController`.

## Recombinaison

1. On récupère les candidats **bruts** (non calculés) des deux sources.
2. On les fusionne en un seul flux trié par `startDateTimeUtc` **décroissant** (plus récent d'abord).
3. On parcourt ce flux à partir de `offset`, et pour **chaque candidat de la fenêtre** on effectue
   le *compute* (validation + synchronisation de la ligne locale via `getComputeEnvMission` /
   `getComputeNavMission`).

Le compute est l'opération coûteuse (upsert en base + validation des actions). Il n'est exécuté que
sur la fenêtre réellement renvoyée, jamais sur l'ensemble du flux.

```
GET /api/v2/missions?offset=0&limit=15&statuses=ENDED
        │
        ▼
 fetch env (MonitorEnv)      fetch nav (DB locale, tout le service)
        │                           │
        └───────────┬───────────────┘
                    ▼
        fusion + tri décroissant (startDateTimeUtc)
                    ▼
   parcours depuis offset → compute chaque candidat → applique le prédicat (filtre)
   jusqu'à `limit` correspondances OU fin du flux OU plafond de scan atteint
                    ▼
        { items, hasMore, nextOffset }
```

## Pagination

L'endpoint est en mode « charger plus » (pas de numéro de page classique) :

- `offset` : index de reprise dans le flux brut fusionné (0 au départ).
- `limit` : taille de page (15 par défaut).
- Réponse : `MissionListPage { items, hasMore, nextOffset }`
  (`infrastructure/api/bff/model/v2/MissionListPage.kt`).
  - `hasMore` : reste-t-il des candidats au-delà de cette page ?
  - `nextOffset` : `offset` à envoyer pour la page suivante.

**La coupe à `limit` se fait après la fusion**, dans la boucle de parcours — c'est le seul endroit où
la taille de page est imposée. Une page ne dépasse donc jamais `limit`.

### Optimisation du fetch env (hors filtre)

Quand **aucun filtre** n'est actif, le fetch env ne récupère pas tout : il demande seulement les
`offset + limit + 1` missions les plus récentes à MonitorEnv.

C'est suffisant et prouvé : le rang d'une mission env (seule) est toujours ≤ son rang dans le flux
fusionné env+nav (intercaler des lignes nav ne peut que la repousser). Toute mission de rang fusionné
`< offset + limit + 1` est donc présente — les lignes env via cette fenêtre, les lignes nav parce
qu'elles sont **toutes** récupérées. Le `+1` sert uniquement à détecter `hasMore`.

Quand un **filtre est actif**, le fetch env est au contraire **non fenêtré** (toutes les missions de
la plage), car le filtre ne peut être évalué qu'après calcul de chaque mission (voir ci-dessous).

## Filtres

Les filtres disponibles (`MissionListFilter`, `domain/use_cases/mission/v2/MissionListFilter.kt`) :

| Dimension | Où est-elle appliquée ? |
|---|---|
| `startDateTimeUtc` / `endDateTimeUtc` (plage de dates) | **à la source** (API MonitorEnv + requête DB) |
| `statuses` (à venir / en cours / terminée / indisponible) | **après fusion**, en mémoire |
| `completenessStatuses` (valide / invalide / incomplète) | **après fusion**, en mémoire |
| `reportTypes` (type de rapport) | **après fusion**, en mémoire |

Sémantique multi-select : **OU** à l'intérieur d'une dimension, **ET** entre dimensions ; une liste
nulle/vide = dimension non filtrée. Appliqué via un prédicat passé par `GetMissionList` et exécuté
*à l'intérieur* de la boucle de `executePaginated`.

### Pourquoi statut / complétude / type de rapport ne descendent pas en base

1. **Ce ne sont pas des colonnes fiables.** `status` et `reportType` ne sont pas stockés (ils sont
   calculés à la lecture). La complétude n'existe en base que sous forme d'un cache approximatif
   (`isCompleteForStats`, booléen 2 états contre une énumération 3 états) qui peut être périmé.
2. **La requête DB ne voit que les missions nav.** Les missions env viennent de l'API. Un filtre
   posé en SQL ne filtrerait que le sous-ensemble nav et laisserait passer les missions env non
   filtrées → une page incohérente.

Seul le prédicat post-fusion voit les deux sources, après calcul — c'est donc le bon niveau pour ces
trois dimensions.

## Plafond de scan (`maxScan`)

Avec un filtre sélectif sur une plage large, la boucle devrait sinon parcourir et calculer tout le
jeu de données pour remplir une seule page (jusqu'à ~1000 missions pour un service ULAM très actif
« depuis toujours »). Pour borner la latence par requête :

- `executePaginated` calcule **au plus `maxScan` candidats** par requête
  (`DEFAULT_MAX_SCAN = 150`, `GetMissions.kt`).
- Si le plafond est atteint avant d'avoir `limit` correspondances, la requête renvoie une page
  **courte (voire vide) avec `hasMore = true`**, et `nextOffset` pointe sur le premier candidat non
  scanné. Le scan **reprend** à la page suivante : il est découpé, jamais perdu ni recalculé.
- Le chemin **sans filtre** s'arrête à `limit` correspondances bien avant `maxScan` : le plafond y
  est sans effet.

Ordres de grandeur à 150 : un service ULAM ≈ 20–30 missions/mois (≈ 240–360 sur un an). Une vue
courte tient en une requête ; un filtre sélectif sur une plage large se découpe en quelques requêtes
(ULAM un an ≈ 2–3, « depuis toujours » ≈ 7). `maxScan` se règle à un seul endroit pour arbitrer
latence par requête ⇄ nombre d'allers-retours.

### Conséquence côté frontend : auto-avance

Comme une page peut revenir **vide avec `hasMore = true`**, le frontend ne doit pas conclure « aucune
mission » tant que le flux n'est pas réellement épuisé. Le hook `useMissionsQuery`
(`frontend/src/v2/features/common/services/use-missions.tsx`) **enchaîne automatiquement** la page
suivante tant que la liste cumulée est vide et que `hasNextPage` est vrai. L'état « aucun résultat »
(`mission-list-page-content-wrapper.tsx`) ne s'affiche donc qu'une fois le flux épuisé
(`hasMore = false`). Dès qu'au moins une correspondance est accumulée, le bouton « charger plus »
manuel reprend la main.

## Fichiers clés

**Backend**

- `domain/use_cases/mission/v2/GetMissions.kt` — orchestration, fusion, fenêtrage, plafond de scan.
- `domain/use_cases/mission/v2/GetMissionList.kt` — projection légère + construction du prédicat.
- `domain/use_cases/mission/v2/MissionListFilter.kt` — dimensions de filtre et leur emplacement.
- `domain/use_cases/mission/GetEnvMissions.kt` + `infrastructure/monitorenv/APIEnvMissionRepository.kt`
  — fetch env (MonitorEnv, `@Cacheable`).
- `infrastructure/database/repositories/interfaces/mission/IDBMissionRepository.kt`
  — `findNavMissionsForService` (liste paginée) et `findAllBetweenDates`.
- `infrastructure/api/bff/v2/MissionRestController.kt` — endpoint et paramètres.
- `infrastructure/api/bff/model/v2/MissionListPage.kt` — enveloppe de réponse.

**Frontend**

- `features/common/services/use-missions.tsx` — `useInfiniteQuery` + auto-avance.
- `features/common/components/layout/mission-list-page-content-wrapper.tsx` — état vide vs liste.
- `features/common/components/elements/mission-list-load-more.tsx` — bouton « charger plus ».
