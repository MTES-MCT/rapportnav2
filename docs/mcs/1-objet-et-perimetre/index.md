## 1. Objet et périmètre

### 1.1 Objectif du document
Décrire les mesures, procédures et responsabilités permettant d’assurer le maintien en conditions de **sécurité** du système d’information tout au long de son cycle de vie.

### 1.2 Périmètre du MCS
- Applications : RapportNav 
- Environnements concernés : Production 
- Données traitées : Données de rapports de patrouilles des agents du DCS de la DGAMPA
- Utilisateurs et rôles : Membres du DCS (PAM, ULAM)  

### 1.3 Références et documents associés
- Dossier d’homologation SSI :
- Politique de sécurité (PSSI / PSSI-SI) :
- Document MCO : https://mtes-mct.github.io/rapportnav2/#/mcs/index
- Journal des vulnérabilités :
- Procédures internes de sécurité :

### 1.4 Inventaire des services et des données à protéger

Cet inventaire établit la liste de l'ensemble des **activités, services et données** du système d'information à protéger. Il est **maintenu à jour a minima annuellement**, ainsi qu'à chaque **changement significatif** (nouveau service, nouvelle donnée traitée, évolution d'architecture). Il est piloté par le **Référent sécurité applicative** (cf. [§2.1](../2-gouvernance/index)) ; chaque révision est tracée dans l'[historique du document](../index) et dans l'historique Git du dépôt `docs/`.

> **Principe : pas de duplication.** Cet inventaire est un point de consolidation sécurité ; le détail fonctionnel est maintenu dans la [cartographie fonctionnelle du DAF (§4)](../../daf/index) et le détail technique dans le [DAT](../../dat/index).

#### 1.4.1 Services et activités à protéger

| Réf. | Service / activité | Nature | Exposition | Référence |
|------|--------------------|--------|------------|-----------|
| F1 | Gestion des missions (ouverture, complétude, clôture) | Fonctionnel | Agents authentifiés | [DAF §4](../../daf/index) |
| F2 | Saisie des actions (contrôles, surveillances, notes, actions spécifiques) | Fonctionnel | Agents authentifiés | [DAF §4](../../daf/index) |
| F3 | Contrôles & infractions (qualification NATINF) | Fonctionnel | Agents authentifiés | [DAF §4](../../daf/index) |
| F4 | Co-saisie & synchronisation avec MonitorFish / MonitorEnv | Fonctionnel | Agents + systèmes Monitor | [Flux externes](../../dat/flux-donnees-externes) |
| F5 | Gestion des effectifs / équipage | Fonctionnel | Agents authentifiés | [DAF §4](../../daf/index) |
| F6 | Exports documentaires (AEM, rapport de patrouille) | Fonctionnel | Agents, encadrement | [DAF §4](../../daf/index) |
| F7 | Analyse & pilotage d'activité | Fonctionnel | Encadrement, admins | [DAF §4](../../daf/index) |
| F8 | Administration (utilisateurs, habilitations, clés API, audits) | Fonctionnel | Admins | [§4 Gestion des accès](../4-gestion-acces/index) |
| F9 | Référentiels (navires, ports, NATINF, ressources unités) | Fonctionnel | Transverse | [Flux externes](../../dat/flux-donnees-externes) |
| T1 | API backend (Kotlin / Spring Boot) | Technique | Interne (derrière proxy) | [DAT — Stack technique](../../dat/stack-technique) |
| T2 | Application frontend (SPA React) | Technique | Exposé Internet (via proxy TLS) | [DAT](../../dat/index) |
| T3 | Base de données **PostgreSQL** | Technique | Interne | [DAT](../../dat/index) |
| T4 | Reverse proxy / terminaison TLS (DAMSI) | Technique | Exposé Internet | [§9.5](../9-matrice-controles/index) |
| T5 | Intégrations externes (MonitorFish, MonitorEnv, Metabase, Sentry) | Technique | Sortant | [Flux externes](../../dat/flux-donnees-externes) |

#### 1.4.2 Données à protéger

Classification selon les critères **DICT** (Disponibilité, Intégrité, Confidentialité, Traçabilité), sur une échelle qualitative *Faible / Modéré / Fort*.

| Donnée | Description | D | I | C | T | Observations |
|--------|-------------|---|---|---|---|--------------|
| Rapports de mission / patrouille | Données métier produites par les agents du DCS | Modéré | Fort | Modéré | Modéré | Cœur de métier ; sauvegardes quotidiennes ([§8](../8-sauvegardes/index)) |
| Comptes utilisateurs & mots de passe | Identifiants des agents ; mots de passe hachés BCrypt | Modéré | Fort | Fort | Fort | Haché BCrypt coût 10 ([§9.1](../9-matrice-controles/index)) |
| Clés API | Secrets d'accès machine, hachés BCrypt | Modéré | Fort | Fort | Fort | Clair affiché une seule fois ([§9.3](../9-matrice-controles/index)) |
| Journaux d'audit (`authentication_audit`, `api_key_audit`) | Traces d'authentification et d'accès par clé API | Modéré | Fort | Modéré | Fort | Valeur probatoire ([§9.6](../9-matrice-controles/index)) |
| Infractions / NATINF | Qualifications d'infractions liées aux contrôles | Modéré | Fort | Modéré | Modéré | Donnée métier sensible |
| Référentiels (navires, ports, NATINF, ressources) | Données de référence fournies par MonitorFish/Env, mises en cache | Modéré | Modéré | Faible | Faible | Données reconstituables depuis la source |

> ⚠️ Les mesures de protection associées à ces données sont détaillées dans [§8 — Sauvegardes et confidentialité](../8-sauvegardes/index) et [§9.5 — Protection des données](../9-matrice-controles/index).
