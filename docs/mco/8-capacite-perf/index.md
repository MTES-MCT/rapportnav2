## 8. Gestion de la capacité et des performances

### 8.1 Suivi des ressources

Le suivi des ressources est effectué via l'application Portainer fournie par l'hébergeur.
Portainer permet par environnement le suivi d'indicateurs tels que :
- CPU
- RAM
- Stockage
- Réseau

L'hébergeur est alerté lorsque ces capteurs passent dans le rouge, auquel cas une discussion avec l'équipe RapportNav débute.

### 8.2 Planification de la montée en charge

La volumétrie de RapportNav est très faible et à charge constante, une montée en charge n'est pas à prévoir avant plusieurs années.

### 8.3 Optimisation des performances

Plusieurs optimisations ont été mises en places :
  - compression gzip des assets
  - assets cachées et servies hors-ligne via PWA
  - mode hors-ligne et optimisations de bande passante

Certains travaux restent encore à effectuer tels que :
- audit des index de base de données
- audit des relations objets de base de données


