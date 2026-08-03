# 🏛️ Dossier d'Architecture Technique (DAT)

## Objet du document

Ce dossier décrit l'architecture technique de **RapportNav** à destination d'un **nouvel hébergeur**.

Il a pour but de donner une vision autoportante et synthétique de :

- ce que fait l'application, **pour qui** et **par qui** elle est utilisée ;
- la **stack technique** complète (frontend, backend, base de données) et ses versions ;
- l'**architecture applicative** (hexagonale) et le mode d'exécution / déploiement ;
- la manière dont l'application **se connecte à d'autres sources de données externes**, pour quel usage et pour quels acteurs.

> Ce document ne traite pas du dimensionnement de l'infrastructure (CPU/RAM, stockage, sauvegardes). Pour l'exploitation et la sécurité opérationnelle, se reporter aux dossiers [MCO](../mco/index) et [MCS](../mcs/index).

## Contexte de la migration

RapportNav est aujourd'hui hébergé par le bureau **NUM3 de la DAM-SI** (Systèmes d'Information de la Direction des Affaires Maritimes), basé à Saint-Malo, qui fournit l'hébergement, la chaîne CI/CD (GitLab self-hosted + Ansible) et les environnements.

Le projet doit être **repris par une autre entité de l'État** (par exemple le réseau éco du ministère de l'Écologie). L'application restant destinée à des agents de l'État, le cadrage souverain actuel est conservé :

- accès uniquement via le **RIE** (Réseau Interministériel de l'État) ;
- authentification des développeurs / accès dépôt via **Cerbère** ;
- nécessité d'un **proxy de sortie** pour joindre les APIs externes depuis le RIE.

Le détail de l'infrastructure actuelle est documenté dans [Concepts techniques → Infrastructure](../engineering/stack/infra).

## Vue d'ensemble

RapportNav est une application web à **image applicative unique** : le frontend (React) est buildé puis embarqué dans le JAR du backend (Spring Boot), le tout packagé dans une seule image Docker déployée derrière un reverse proxy.

```
                         Réseau RIE
  ┌───────────────┐    ┌───────────────────────────────────────────┐
  │  Navigateur   │    │  Reverse proxy (X-Forwarded-*)             │
  │  agent État   │───▶│        │                                   │
  └───────────────┘    │        ▼                                   │
                       │  ┌───────────────────────────────────┐    │
                       │  │  Image Docker RapportNav (port 80) │    │
                       │  │  ┌─────────────┐  ┌──────────────┐ │    │
                       │  │  │ Frontend    │  │ Backend      │ │    │
                       │  │  │ React (dist)│◀▶│ Spring Boot  │ │    │
                       │  │  │ statique    │  │ (Kotlin/JVM) │ │    │
                       │  │  └─────────────┘  └──────┬───────┘ │    │
                       │  └─────────────────────────┼─────────┘    │
                       │                             │              │
                       │            ┌────────────────┼───────────┐ │
                       │            ▼                ▼            │ │
                       │      ┌───────────┐   ┌──────────────┐   │ │
                       │      │PostgreSQL │   │ Proxy sortie │───┼─┼──▶ APIs externes
                       │      │   15      │   └──────────────┘   │ │   (MonitorFish/Env,
                       │      └───────────┘                      │ │    Sentry…)
                       └───────────────────────────────────────────┘
```

Le détail est présenté dans les pages suivantes :

- [Présentation fonctionnelle](presentation-fonctionnelle) — l'application, ses utilisateurs et leurs rôles.
- [Stack technique](stack-technique) — technologies et versions.
- [Architecture applicative](architecture-applicative) — couches, packaging, exécution, authentification.
- [Flux de données externes](flux-donnees-externes) — connexions aux sources externes.

## Historique du document

| Version | Date        | Auteur      | Commentaires     |
|---------|-------------|-------------|------------------|
| 1.0     | 03 Août 2026 | Équipe RapportNav | Version initiale (reprise d'hébergement) |
