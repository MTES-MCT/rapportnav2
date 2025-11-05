## 5. Sécurité des environnements et durcissement

### 5.1 Sécurisation du réseau

L'application RapportNav n'est accessible que via le RIE. 

La DAMSI St Malo s'occupe de la gestion du réseau, des pare-feux physiques et applicatifs. 

### 5.2 Sécurité applicative

Certaines bonnes pratiques de développements sont mises en places telles que :
- revues de code
- accès restreint ne permettant qu'aux équipes RapportNav de merger du nouveau code
- Analyses de qualité de code
- Analyses de dépendances
- Analyses de vulnérabilités des OS (Docker)

Deux tests d'intrusion sont prévus en 2025.

### 5.3 Sécurité clés API

Les mécanismes suivants ont été mis en place :
- Master password 64 chars stocké en sécurité dans un vault chez l'hébergeur
- Clés de 64 chars stockées hashées dans la base de données, via master password, visibles en clair qu'une seule fois
- Clé publique de 12 chars qui peut être partagée
- Rate limiting:
  - sur clé API, par minute et heure
  - sur IP entrante (60/minute)

### 5.4 Journalisation et audit

Statut des différents journaux :
- changelog : généré automatiquement, disponible sur github
- access logs : disponible sur les machines intégration/production
- incident logs : inexistant
- application logs : disponible via les outils mis à disposition par l'hébergeur
- API externe : logs d'audit sur les accès

