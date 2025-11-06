## 4. Gestion des accès et des habilitations

## Accès à l'application

#### 4.1.1 Politique d’accès
_(Principe du moindre privilège, séparation des rôles, double validation.)_
Les accès à la plateforme RapportNav et la création de comptes et des habilitations sont entièrement gérés par l'équipe RapportNav,
suite aux communications entre l'administration SNC3 et les équipes du DCS (PAM/ULAM).

Il existe trois rôles utilisateurs :
- PAM
- ULAM
- ADMIN

Ces rôles sont non exclusifs, cad qu'un utilisateur peut être tout à la fois.
Ceci dit, le rôle ADMIN n'est utilisé que par les membres de la startup d'Etat RapportNav.

Les rôles sont gérés par les administrateurs de la startup d'Etat RapportNav. Un utilisateur ne peut pas changer de rôle par lui-même.

Il arrive cependant qu'un membre du DCS de la DGAMPA soit muté (par ex d'ULAM vers PAM), auquel cas l'équipe RapportNav ajustera les rôles.


#### 4.1.2 Cycle de vie des habilitations

La création, modification et suppression des utilisateurs et habilitations sont gérées entièrement par l'équipe RapportNav.

Une revue périodique et procédurale n'a pas encore mis en place, ces revues sont effectuées ad-hoc suite aux communications
entre les membres du DCS et l'équipe RapportNav.

#### 4.1.3 Authentification et autorisation

A terme, l'équipe RapportNav souhaite implémenter l'authentification Cerbère mais en attendant, la gestion des mots de passe est gérée par une solution custom.

Les mots de passes sont hashés et saltés, cad qu'ils ne sont pas visibles en clair dans la base de données.
La politique de mots de passe est 16 caractères minimum avec minuscule, majuscule, chiffre et caractères spéciaux.
Ceci représente un total d'environ 94 caractères, soit 94^16 ≈ 6,1 × 10^31 combinaisons possibles, 
rendant une attaque en brute-force bien trop coûteuse en temps et puissance de calcul.

Il n'y a pas d'options de modification de mots de passe via l'interface, les utilisateurs doivent faire une demande à l'intrapreneur.e RapportNav.

#### 4.1.4 Comptes à privilèges

Il existe un type de rôle administrateur à l'heure actuelle, seulement utilisé par certains membres de l'équipe RapportNav.
Il n'est pas prévu d'accès temporaires.

Certaines parties de l'application (création de comptes utilisateurs, page admin dans l'interface) ne sont autorisées que 
pour les administrateurs, avec une double vérification effectuée dans le frontend et le backend.

## 4.2 Accès à l'API

### 4.2.1 Quelles APIs et quels accès

Voici la matrice des APIs et de leur accès :

| API           | Sécurité requise     | Missions principales |
|---------------|----------------------|----------------------|
| API interne   | authentification jwt |----------------------|
| API publique  | api-key              |----------------------|
| API analytics | api-key              |----------------------|
| API SATI      | api-key              |----------------------|


