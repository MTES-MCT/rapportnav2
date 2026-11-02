## 9. Matrice des contrôles de sécurité

Cette page consolide, pour l'**homologation de sécurité**, l'ensemble des contrôles de sécurité
réellement mis en œuvre dans RapportNav, avec leur état et un pointeur vers le code source
(dépôt [github.com/MTES-MCT/rapportnav2](https://github.com/MTES-MCT/rapportnav2)).

**Légende :** ✅ en place · ⚠️ limitation connue · 🔜 évolution prévue

### 9.1 Authentification et sessions

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Sessions sans état | ✅ | `SessionCreationPolicy.STATELESS`, aucune session ni cookie serveur | `config/SecurityConfig.kt` |
| JWT signé | ✅ | HMAC-SHA256 (HS256), clé par `JWT_SECURITY_KEY`, validité 15 j | `domain/use_cases/auth/TokenService.kt`, `config/JwtEncodingConfig.kt` |
| Re-validation du jeton | ✅ | Utilisateur rechargé depuis la base à chaque requête ; rôles du jeton jamais utilisés pour l'autorisation | `TokenService.parseToken`, `infrastructure/api/filter/CustomAuthenticationFilter.kt` |
| Politique de mot de passe | ✅ | ≥ 16 car., majuscule/minuscule/chiffre/spécial | `domain/utils/PasswordValidator.kt` |
| Hachage des mots de passe | ✅ | BCrypt coût 10 | `domain/use_cases/auth/HashService.kt` |
| Anti-force-brute sur le login | ⚠️ | Pas de limitation de débit / verrouillage (journalisation seule) — cf. [§4.3](../4-gestion-acces/index) | — |
| ProConnect (OIDC) + 2FA | 🔜 | En développement (branche `pro-connect`) | — |

### 9.2 Autorisation (RBAC)

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Rôles applicatifs | ✅ | `ADMIN`, `API_USER`, `USER_PAM`, `USER_ULAM`, `MANAGER_PAM`, `MANAGER_ULAM` | `domain/entities/user/RoleTypeEnum.kt` |
| Autorisation au niveau des routes | ✅ | `/api/v2/admin/**`→ADMIN, `/api/v2/manage/**`→managers | `config/SecurityConfig.kt` |
| Autorisation au niveau des méthodes | ✅ | `@PreAuthorize` sur les endpoints sensibles (admin, metabase) | `infrastructure/api/admin/*Controller.kt` |
| Double vérification front + back | ✅ | Les contrôles de rôles frontend ne sont que du confort ; le backend fait foi | `frontend/.../auth/hooks/use-auth.tsx` |

### 9.3 Clés API

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Génération sûre | ✅ | `SecureRandom`, 48 octets → 64 car. Base64 | `domain/use_cases/apikey/CreateApiKey.kt` |
| Stockage haché | ✅ | BCrypt coût 12, clair affiché une seule fois | `CreateApiKey.kt`, `infrastructure/database/model/apikey/ApiKeyModel.kt` |
| Révocation / rotation | ✅ | Via admin panel ; clé désactivée refusée | `DisableApiKey.kt`, `RotateApiKey.kt` |
| Rate limiting | ✅ | 60/min, 1000/h par clé + garde-fou par IP → HTTP 429 | `domain/use_cases/apikey/ValidateApiKey.kt` |
| Clé maître durcie | ✅ | Ignorée si < 48 car., comparaison temps constant, usage tracé | `ValidateApiKey.kt` |
| Expiration automatique des clés | ⚠️ | Pas d'expiration ; révocation manuelle uniquement | — |

### 9.4 Durcissement HTTP et applicatif

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| CSP à nonce (HTML) | ✅ | Nonce aléatoire par requête (128 bits) ; `object-src 'none'`, `frame-ancestors 'none'` | `infrastructure/api/FrontendRoutesController.kt` |
| HSTS | ✅ | `max-age=63072000; includeSubDomains` | `config/SecurityConfig.kt` |
| X-Frame-Options / X-Content-Type-Options | ✅ | `DENY` / `nosniff` | `config/SecurityConfig.kt` |
| Referrer-Policy / Permissions-Policy | ✅ | `strict-origin-when-cross-origin` ; géoloc/caméra/micro désactivés | `config/SecurityConfig.kt` |
| CORS | ✅ | Liste blanche d'origines, chemins `/api/**`, sans credentials | `config/CorsConfig.kt` |
| CSRF | ✅ | Désactivé par conception (API sans état, jeton par en-tête) | `config/SecurityConfig.kt` |
| Protection injections SQL | ✅ | JPA, requêtes paramétrées uniquement | `infrastructure/database/repositories/**` |
| Validation des entrées | ✅ | Jakarta Bean Validation (back) + Yup (front) | `infrastructure/api/**`, `frontend/**` |
| Gestion des erreurs | ✅ | RFC 7807 ; stack traces désactivées en prod | `infrastructure/api/ControllersExceptionHandler.kt`, `application-prod.properties` |
| Swagger/OpenAPI | ✅ | Exposé hors production uniquement | `config/SecurityConfig.kt` |

### 9.5 Protection des données

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Chiffrement en transit | ✅ | TLS (reverse proxy DAMSI) + HSTS + `upgrade-insecure-requests` | `config/SecurityConfig.kt`, `FrontendRoutesController.kt` |
| Pseudonymisation Sentry | ✅ | `send-default-pii=false`, identifiants techniques uniquement | `config/SentryConfig.kt`, `infrastructure/api/filter/SentryUserContextFilter.kt`, `frontend/src/sentry.ts` |
| Pas de secret en clair dans le code | ✅ | Secrets via variables d'environnement | `application.properties` |
| Pas de journalisation de données sensibles | ✅ | En-tête `Authorization` exclu des logs ; pas de PII en console | `config/RequestLoggingConfig.kt` |
| Jeton JWT en `localStorage` (navigateur) | ⚠️ | Exposition XSS ; atténuée par CSP stricte | `frontend/src/features/auth/utils/token.ts` |

### 9.6 Journalisation, audit et supervision

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Audit des authentifications | ✅ | IP, user-agent, type d'événement, motif d'échec, horodatage | `authentication_audit` ; `domain/use_cases/auth/LogAuthenticationAudit.kt` |
| Audit des accès par clé API | ✅ | IP, chemin, statut, horodatage | `api_key_audit` ; `domain/use_cases/apikey/LogApiKeyAudit.kt` |
| Consultation des audits | ✅ | Admin panel (endpoints réservés `ROLE_ADMIN`) | `infrastructure/api/admin/*Controller.kt` |
| Supervision erreurs / perf | ✅ | Sentry (back + front) | `config/SentryConfig.kt`, `frontend/src/sentry.ts` |
| Outil dédié de supervision sécurité (SIEM) | ⚠️ | Absent ; détection a posteriori via journaux | — |
| Rétention des journaux d'audit | ⚠️ | Pas de politique de purge définie ; rétention des sauvegardes applicable | — |

### 9.7 Gestion des vulnérabilités (CI/CD — chaîne GitLab DAMSI)

Les analyses ci-dessous s'exécutent dans le pipeline de déploiement vers école / intégration.

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Analyse des dépendances | ✅ | Dependency-Check, seuil CVSS ≥ 8 | `.gitlab-ci/jobs/analyse_dependency-check.yml` |
| Analyse des images / IaC | ✅ | Trivy, sévérité CRITICAL | `.gitlab-ci/jobs/analyse_trivy.yml` |
| Qualité & sécurité du code | ✅ | SonarQube (Quality Gate) | `.gitlab-ci/jobs/analyse_sonar.yml` |
| SAST (miroir GitHub) | ✅ | CodeQL (push/PR + quotidien) | `.github/workflows/codeql.yml` |
| Blocage du déploiement sur échec d'analyse | ⚠️ | Jobs en `allow_failure: true` : analyses **informatives, non bloquantes** à ce jour | `.gitlab-ci/jobs/analyse_*.yml` |

### 9.8 Sauvegardes

| Contrôle | État | Détail | Référence |
|----------|------|--------|-----------|
| Sauvegardes VM & base | ✅ | Quotidiennes (gérées DAMSI) — voir [MCO §7](../../mco/7-sauvegardes/index) | — |
| Test de restauration | ⚠️ | Pas de processus formalisé | — |

### 9.9 Synthèse des limitations connues

Les points marqués ⚠️ ci-dessus constituent les limitations assumées à la date de l'homologation.
Les principales remédiations sont détaillées dans [§4.3 — Limitations connues et remédiation](../4-gestion-acces/index), la plus structurante étant la migration vers **ProConnect** (anti-force-brute et 2FA délégués au fournisseur d'identité de l'État).
