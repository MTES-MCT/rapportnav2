## 5. Sécurité des environnements et durcissement

### 5.1 Sécurisation du réseau

L'application RapportNav n'est accessible que via le RIE. 

La DAMSI St Malo s'occupe de la gestion du réseau, des pare-feux physiques et applicatifs. 

### 5.2 Sécurité applicative

**Bonnes pratiques de développement :**
- revues de code
- accès restreint ne permettant qu'aux équipes RapportNav de merger du nouveau code
- Analyses de qualité de code
- Analyses de dépendances
- Analyses de vulnérabilités des OS (Docker)

**Durcissement à l'exécution (mesures mises en œuvre dans le backend) :**

- **En-têtes de sécurité HTTP :**
  - `Content-Security-Policy` : **CSP à nonce** (généré aléatoirement à chaque requête, 128 bits) pour les réponses HTML ; `default-src 'self'`, `object-src 'none'`, `frame-ancestors 'none'`, `base-uri 'self'`, `form-action 'self'`, liste blanche stricte de `connect-src`/`frame-src`.
  - `Strict-Transport-Security` : `max-age=63072000; includeSubDomains` (HSTS 2 ans).
  - `X-Frame-Options: DENY` et `frame-ancestors 'none'` (anti-clickjacking).
  - `X-Content-Type-Options: nosniff`.
  - `Referrer-Policy: strict-origin-when-cross-origin`.
  - `Permissions-Policy` : géolocalisation, caméra et micro désactivés.
- **CORS** : liste blanche d'origines (configurable par environnement), restreinte aux chemins `/api/**`, sans `allow-credentials`.
- **CSRF** : désactivé **par conception** (API sans état, jeton par en-tête et non par cookie — cf. [Gestion des accès](../4-gestion-acces/index)).
- **Protection contre les injections SQL** : accès aux données exclusivement via **Spring Data JPA** avec requêtes **paramétrées** (aucune requête SQL native concaténée).
- **Validation des entrées** : Jakarta Bean Validation (`@Valid`, `@NotEmpty`, …) côté backend et **Yup** côté frontend.
- **Gestion des erreurs** : réponses normalisées **RFC 7807** (`application/problem+json`) ; les **stack traces sont désactivées en production** (`server.error.include-stacktrace=never`), évitant toute fuite d'information technique.

### 5.3 Sécurité clés API

Les mécanismes suivants ont été mis en place :
- Master password 64 chars stocké en sécurité dans un vault chez l'hébergeur
- Clés de 64 chars stockées hashées dans la base de données, via master password, visibles en clair qu'une seule fois
- Clé publique de 12 chars qui peut être partagée
- Rate limiting:
  - sur clé API, par minute et heure
  - sur IP entrante (60/minute)
- Clé maître (`MASTER_API_KEY`) **durcie** : ignorée si absente/vide ou < 48 caractères, comparaison en **temps constant** (anti-timing), usage tracé distinctement.

> Détail des clés API (génération, hachage BCrypt coût 12, révocation/rotation, HTTP 429) : voir [Gestion des accès → 4.2.2](../4-gestion-acces/index).

### 5.4 Journalisation et audit

Statut des différents journaux :
- changelog : généré automatiquement, disponible sur github
- access logs : disponible sur les machines intégration/production
- incident logs : inexistant
- application logs : disponible via les outils mis à disposition par l'hébergeur
- API externe : logs d'audit sur les accès

