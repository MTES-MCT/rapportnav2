## 8. Sauvegardes et confidentialité des données

### 8.1 Protection des données sensibles
_(Chiffrement au repos/en transit, anonymisation, masquage.)_

Les mots de passe sont hashés en utilisant BCrypt provenant de Spring Security: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-bcrypt

Le chiffrement du token JWT suit la convention HmacSHA256.

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





