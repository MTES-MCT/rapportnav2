# 🧭 Architecture fonctionnelle (DAF)

## Objet

Ce document présente RapportNav du point de vue **fonctionnel / métier** (le *quoi* et le *pour qui*), en complément du [Dossier d'Architecture Technique (DAT)](../dat/index) qui traite du *comment* technique.

> **Principe : pas de duplication.** Cette page est un **point d'entrée** qui référence les contenus fonctionnels déjà maintenus ailleurs dans la documentation. Seuls la **cartographie fonctionnelle** (§4) et les **parcours utilisateurs** (§5) sont rédigés ici. Toute évolution métier se fait dans la page source référencée, pas ici.

## Historique du document
| Version | Date        | Auteur      | Commentaires     |
|---------|-------------|-------------|------------------|
| 1.0 | 05 Oct 2026 | Louis Hache | Version initiale (DAF par référence) |

---

## 1. Contexte et enjeux métier

Le Dispositif de Contrôle et de Surveillance (DCS) des Affaires Maritimes doit produire de multiples rapports, à des formats hétérogènes, pour plusieurs donneurs d'ordres. RapportNav unifie cette production en un **compte-rendu de mission unique** dont les statistiques sont ensuite extraites.

➜ Détail : [Présentation fonctionnelle](../dat/presentation-fonctionnelle) · [Description du système — MCO §2.1](../mco/2-description-systeme/index)

## 2. Périmètre fonctionnel

- **Applications** : RapportNav (rédaction, export, restitution).
- **Utilisateurs** : agents du DCS de la DGAMPA (unités PAM et ULAM) et encadrement.
- **Hors périmètre** : stockage des Missions (porté par MonitorEnv), ciblage pêche (MonitorFish) — voir la dépendance ci-dessous.

➜ Détail : [MCO §1 — Objet et périmètre](../mco/1-objet-et-perimetre/index) · [MCO §2.3 — Environnements](../mco/2-description-systeme/index)

## 3. Acteurs et rôles

Deux profils d'unités (**PAM** — patrouilleurs ; **ULAM** — unités littorales), un encadrement (managers) et l'administration.

➜ Détail des rôles applicatifs : [Présentation fonctionnelle — Utilisateurs & rôles](../dat/presentation-fonctionnelle) · [Les utilisateurs](../concepts/users)

---

## 4. Cartographie fonctionnelle

Les grands domaines fonctionnels de RapportNav :

| # | Domaine fonctionnel | Sous-fonctions | Acteurs principaux | Référence |
|---|---------------------|----------------|--------------------|-----------|
| F1 | **Gestion des missions** | Ouvrir, compléter, clôturer une mission ; informations générales ; unités & moyens engagés ; statut de complétude | Agents PAM / ULAM | [Mission](../features/mission) · [Concepts — Missions](../concepts/missions) |
| F2 | **Saisie des actions** | Contrôles pêche ; contrôles & surveillances environnement ; statut du navire (PAM) ; notes libres ; contacts centres ; actions spécifiques (assistance/sauvetage, anti-pollution, Vigimer, cérémonies…) | Agents PAM / ULAM | [Concepts — Entités](../concepts/main-entities) |
| F3 | **Contrôles & infractions** | Contrôles (administratif, navigation, sécurité, gens de mer) ; infractions ; qualification NATINF | Agents PAM / ULAM | [Concepts — Entités](../concepts/main-entities) · [Règles métier](../features/regles-metier) |
| F4 | **Co-saisie & synchronisation** | Partage de la saisie avec MonitorFish / MonitorEnv ; synchronisation des données communes (missions, contrôles) | Agents + systèmes Monitor | [Co-saisie Fish/Env](../concepts/fish-env) · [Flux externes](../dat/flux-donnees-externes) |
| F5 | **Gestion des effectifs / équipage** | Agents et équipage engagés sur la mission | Agents PAM / ULAM | [Mission](../features/mission) |
| F6 | **Exports documentaires** | Export des tableaux **AEM** ; export du **rapport de patrouille** (PAM uniquement) | Agents, encadrement | [AEM](../features/aem) · [Rapport de patrouille](../features/rapport-patrouille) |
| F7 | **Analyse & pilotage** | Tableaux de bord d'analyse d'activité (par périmètre PAM / ULAM) | Encadrement (managers), admins | [Présentation fonctionnelle](../dat/presentation-fonctionnelle) |
| F8 | **Administration** | Gestion des utilisateurs & habilitations ; gestion des clés API ; consultation des journaux d'audit | Admins RapportNav | [MCS §4 — Gestion des accès](../mcs/4-gestion-acces/index) · [MCO §9 — Traçabilité](../mco/9-tracabilite/index) |
| F9 | **Référentiels** | Navires, ports, codes NATINF, ressources des unités (fournis par MonitorFish/Env, mis en cache) | Transverse | [Flux externes](../dat/flux-donnees-externes) |

## 5. Parcours utilisateurs

### P1 — Agent de terrain : rédiger un rapport de mission

1. Authentification de l'agent (PAM ou ULAM).
2. Sélection ou ouverture de la **mission** (co-saisie avec Monitor en arrière-plan).
3. Saisie des **informations générales** (dates, unités, moyens engagés).
4. Ajout des **actions** au fil de la mission : contrôles, surveillances, statut navire, notes, actions spécifiques.
5. Pour une action de contrôle : saisie des **contrôles** puis, le cas échéant, d'une **infraction** et de ses **NATINF**.
6. Marquage de la mission comme **complète** ; les données sont synchronisées et comptabilisées en statistiques.

### P2 — PAM : produire les livrables d'une mission

1. À partir d'une mission complétée, génération de l'**export AEM** (tableaux XLSX / ODS).
2. Et/ou génération du **rapport de patrouille** (DOCX / ODT) — réservé aux unités **PAM**.
3. Téléchargement du document produit.

### P3 — Encadrement : piloter l'activité

1. Authentification d'un profil **MANAGER** (PAM ou ULAM) ou **ADMIN**.
2. Accès aux **tableaux de bord d'analyse** (restitution par périmètre).
3. Lecture des indicateurs d'activité pour orienter le ciblage des contrôles.

---

## 6. Objets métier et règles de gestion

- **Modèle conceptuel** (Mission → Actions → Contrôles → Infractions → NATINF) : [Concepts — Entités principales](../concepts/main-entities).
- **Règles de gestion** : [Règles métier](../features/regles-metier).

## 7. Flux fonctionnels

Échanges de données métier avec les systèmes externes (MonitorFish, MonitorEnv, Metabase, référentiels) : [Flux de données externes](../dat/flux-donnees-externes).

---

## 8. Correspondance avec les autres documents

| Thème | Document de référence |
|-------|-----------------------|
| Architecture **technique** (stack, applicative, déploiement) | [DAT](../dat/index) |
| Exploitation, supervision, sauvegardes | [MCO](../mco/index) |
| Sécurité et homologation | [MCS](../mcs/index) |
| Concepts métier détaillés | [Concepts généraux](../concepts/index) |
| Fonctionnalités détaillées | [Fonctionnalités utilisateur](../features/index) |
