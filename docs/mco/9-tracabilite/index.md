## 9. Documentation et traçabilité

### 9.1 Gestion documentaire

Le code est :
- hébergé sur [github.com/MTES-MCT/rapportnav2](https://github.com/MTES-MCT/rapportnav2) - visible open-source
- mirroré sur le Gitlab self-hosté de l'hébergeur - privé

La documentation est :
- hébergée avec le code dans le même dépôt
- déployée automatiquement sur https://mtes-mct.github.io/rapportnav2/#/

### 9.2 Journal d’exploitation

Statut des différents journaux :
- changelog : généré automatiquement, disponible sur github
- access logs : disponible sur les machines intégration/production
- incident logs : inexistant
- application logs : disponible via les outils mis à disposition par l'hébergeur

### 9.3 Traçabilité des données

Pour chaque ligne de la base de données, sont stockés :
- created_at
- created_by
- last_modified_at
- last_modified_at

Ces données permettent de savoir qui/quand a été créé la donnée et qui/quand l'a modifié en dernier.
Un log des opérations intermédiaires n'est pas disponible à l'heure actuelle.
