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

### 5.3 Journalisation et audit

Statut des différents journaux :
- changelog : généré automatiquement, disponible sur github
- access logs : disponible sur les machines intégration/production
- incident logs : inexistant
- application logs : disponible via les outils mis à disposition par l'hébergeur

