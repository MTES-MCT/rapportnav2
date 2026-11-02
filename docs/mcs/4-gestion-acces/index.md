## 4. Gestion des accès et des habilitations

## Accès à l'application

#### 4.1.1 Politique d’accès
_(Principe du moindre privilège, séparation des rôles, double validation.)_
Les accès à la plateforme RapportNav et la création de comptes et des habilitations sont entièrement gérés par l'équipe RapportNav,
suite aux communications entre l'administration SNC3 et les équipes du DCS (PAM/ULAM).

Il existe les rôles applicatifs suivants :
- `USER_PAM` / `MANAGER_PAM` (PAM, avec distinction utilisateur / manager)
- `USER_ULAM` / `MANAGER_ULAM` (ULAM, avec distinction utilisateur / manager)
- `ADMIN`
- `API_USER` (rôle machine pour les accès par clé API)

Ces rôles sont non exclusifs, cad qu'un utilisateur peut en cumuler plusieurs.
Ceci dit, le rôle `ADMIN` n'est utilisé que par les membres de la startup d'Etat RapportNav.

Les rôles sont gérés par les administrateurs de la startup d'Etat RapportNav. Un utilisateur ne peut pas changer de rôle par lui-même.

Il arrive cependant qu'un membre du DCS de la DGAMPA soit muté (par ex d'ULAM vers PAM), auquel cas l'équipe RapportNav ajustera les rôles.


#### 4.1.2 Cycle de vie des habilitations

La création, modification et suppression des utilisateurs et habilitations sont gérées entièrement par l'équipe RapportNav.

Une revue périodique et procédurale n'a pas encore mis en place, ces revues sont effectuées ad-hoc suite aux communications
entre les membres du DCS et l'équipe RapportNav.

#### 4.1.3 Authentification et autorisation

A terme, l'équipe RapportNav prévoit de migrer vers l'authentification **ProConnect** (OIDC) ainsi que la **double authentification (2FA)** ; ces évolutions sont en cours de développement (branche `pro-connect`, non encore déployées). En attendant, la gestion des mots de passe est assurée par une solution custom.

Les mots de passes sont hashés et saltés, cad qu'ils ne sont pas visibles en clair dans la base de données.
La politique de mots de passe est 16 caractères minimum avec minuscule, majuscule, chiffre et caractères spéciaux.
Ceci représente un total d'environ 94 caractères, soit 94^16 ≈ 6,1 × 10^31 combinaisons possibles, 
rendant une attaque en brute-force bien trop coûteuse en temps et puissance de calcul.

Il n'y a pas d'options de modification de mots de passe via l'interface, les utilisateurs doivent faire une demande à l'intrapreneur.e RapportNav.

**Mécanismes techniques mis en œuvre :**

- **Sessions sans état (stateless)** : aucune session serveur ni cookie de session (`SessionCreationPolicy.STATELESS`). L'authentification repose uniquement sur un **jeton JWT** transmis en en-tête `Authorization: Bearer`.
- **JWT signé** en HMAC-SHA256 (HS256), clé fournie par variable d'environnement (`JWT_SECURITY_KEY`), validité **15 jours**.
- **Re-validation du jeton à chaque requête** : le backend recharge l'utilisateur depuis la base à chaque appel et **n'accorde jamais de confiance aux rôles contenus dans le jeton** (ceux-ci ne servent qu'au confort d'affichage côté frontend). Les droits réels sont donc toujours à jour (un compte désactivé ou un rôle retiré prend effet immédiatement).
- **Mots de passe** hachés avec **BCrypt** (coût 10) ; politique de complexité (≥ 16 caractères, majuscule/minuscule/chiffre/spécial) vérifiée à la création et à la modification.
- **Autorisation en profondeur (RBAC)** : contrôle au niveau des routes (`/api/v2/admin/**` → `ROLE_ADMIN`, `/api/v2/manage/**` → managers) **et** au niveau des méthodes (`@PreAuthorize`) sur les endpoints sensibles.
- **CSRF désactivé par conception** : l'API étant sans état et le jeton transmis par en-tête (jamais par cookie), la falsification de requête inter-site n'est pas applicable.
- **Journalisation des authentifications** : chaque tentative (succès / échec, avec motif) est tracée (IP, user-agent, horodatage) dans la table `authentication_audit`.

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

### 4.2.2 Sécurité des clés API

- Clés générées de façon cryptographiquement sûre (`SecureRandom`, 48 octets → 64 caractères), **stockées uniquement hachées** (BCrypt coût 12) et affichées en clair **une seule fois** à la création.
- **Identifiant public** (12 caractères) permettant le suivi/l'audit sans exposer la clé.
- **Révocation / rotation** depuis l'admin panel ; une clé désactivée (`disabledAt`) est refusée à la validation suivante.
- **Rate limiting** : 60 req/min et 1000 req/h par clé, plus un garde-fou par IP entrante ; dépassement → **HTTP 429**.
- **Clé maître (`MASTER_API_KEY`)** : clé d'exploitation fournie par variable d'environnement, **durcie** — ignorée si absente/vide ou < 48 caractères, **comparaison en temps constant** (anti-timing), et utilisation tracée distinctement dans les journaux. À conserver forte et à faire tourner régulièrement.

### 4.3 Limitations connues et remédiation

| Limitation | Risque | Contrôles compensatoires actuels | Remédiation prévue |
|------------|--------|----------------------------------|--------------------|
| Pas de limitation de débit / verrouillage de compte sur l'endpoint de **connexion** (login) | Attaque par force brute / bourrage d'identifiants | Mot de passe fort (≥ 16 car.), messages d'erreur génériques (pas d'énumération d'utilisateurs), journalisation de toutes les tentatives (`authentication_audit`), désactivation manuelle de compte possible | Migration vers **ProConnect** (OIDC) : l'anti-force-brute et la **2FA** sont alors délégués au fournisseur d'identité de l'État |
| Jeton JWT stocké en `localStorage` côté navigateur | Exfiltration en cas de faille XSS | CSP stricte à nonce, en-têtes de sécurité durcis, pas de `unsafe-eval` | Migration ProConnect (flux OIDC) ; durcissement CSP continu |


