## 6. Surveillance et détection

### 6.1 Supervision de la sécurité

#### 6.1.1 Supervision de l'app

Aucun outil **dédié** de supervision de la sécurité (type SIEM) n'est en place à ce jour. La surveillance repose sur plusieurs dispositifs complémentaires :

- **Détection de vulnérabilités** avant tout déploiement (analyses dépendances / images / qualité, cf. [§3](../3-gestion-vulnerabilites/index)).
- **Supervision des erreurs et performances** via **Sentry** (backend et frontend). Les données envoyées à Sentry sont **pseudonymisées par conception** : `sentry.send-default-pii=false`, seuls l'identifiant utilisateur et l'identifiant de service sont transmis — **ni e-mail ni nom** (minimisation RGPD).
- **Journaux d'audit** exploitables pour la détection a posteriori : `authentication_audit` (connexions utilisateurs) et `api_key_audit` (accès par clé API), consultables depuis l'admin panel (cf. [MCO → Traçabilité](../../mco/9-tracabilite/index)).

#### 6.1.2 Supervision des clés API

Via les logs d'accès sur les clés API, un suivi peut être effectué. 
Via les mécanismes de protection des clés (Rate limiting par ex), certaines clés au comportement jugé frauduleux sont immédiatement désactivées.
La supervision s'effectué via l'admin panel ou des clés peuvent être désactivées/rotationnées par les membres admin RapportNav

### 6.2 Détection d’incidents

Aucun outil de détection d'incidents a priori n'a été mis en place. La détection est à l'heure actuelle toujours a posteriori.

### 6.3 Gestion des alertes

Aucune gestion des alertes n'a été mis en place.
