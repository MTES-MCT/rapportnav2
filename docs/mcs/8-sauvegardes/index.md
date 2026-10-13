## 8. Sauvegardes et confidentialité des données

### 8.1 Protection des données sensibles
_(Chiffrement au repos/en transit, anonymisation, masquage.)_

**Au repos :**
- Les mots de passe sont hachés avec **BCrypt** (Spring Security) : https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-bcrypt
- Les **clés API** sont stockées uniquement hachées (BCrypt coût 12), jamais en clair.
- Le jeton JWT est signé en **HMAC-SHA256**.

**En transit :**
- Terminaison **TLS** assurée par le reverse proxy de la DAMSI St Malo ; l'application impose **HSTS** (`max-age` 2 ans, `includeSubDomains`) et, en HTTPS, `upgrade-insecure-requests`.
- Accès exclusivement via le **RIE**.

**Minimisation / pseudonymisation :**
- Les données transmises à **Sentry** sont pseudonymisées (`send-default-pii=false` ; identifiants techniques uniquement, pas d'e-mail ni de nom).
- Les en-têtes `Authorization` ne sont pas journalisés ; aucune donnée sensible n'est imprimée en console.

**Secrets :**
- Aucun secret n'est stocké en clair dans le dépôt : toutes les clés/identifiants proviennent de **variables d'environnement** fournies par la chaîne de déploiement.

### 8.2 Gestion des secrets et certificats

**Certificats :**

Les certificats sont gérés par la DAMSI St Malo avec un rythme de rotation compris entre 4 et 12 fois par an.
Les équipes de développement RapportNav ne sont pas engagées dans ce processus.

**Coffres-forts à secrets :**

Aucun mot de passe n'est stocké en clair dans le dépôt de code.
Les mots de passes sont gérer par la DAMSI dans leur dépôt GitLAb, self-hosté et uniquement accessible via RIE et invitation.

Seuls les administrateurs systèmes de DAMSI St Malo ont le pouvoir de changer les mots de passe.

**Outils de partage des secrets :**

Les mots de passes entre les équipes RapportNAv, Monitor et DAMSI sont échangés via l'application VaultWarden.


**Rotation des mots de passes**

La rotation des mots de passe s'effectue ad-hoc si des failles ont été détectées.
La DAMSI St Malo applique ensuite sa propre politique de rotation périodique.





