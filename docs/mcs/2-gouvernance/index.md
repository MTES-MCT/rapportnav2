## 2. Gouvernance et responsabilités

### 2.1 Organisation de la sécurité
| Rôle | Responsable               | Missions principales |
|------|---------------------------|----------------------|
| Responsable SSI | DGAMPA / SNC3             | |
| Administrateur système | DAMSI St Malo             | |
| Référent sécurité applicative | Louis Hache Startup d'Etat RapportNav          | |
| Équipe de développement | Startup d'Etat RapportNav | |
| Exploitant / DevOps | DAMSI St Malo        | |

### 2.2 Analyse de conformité réglementaire et normative

Cette analyse recense les exigences **réglementaires et normatives** applicables au système d'information et identifie les **écarts** avec les mesures effectivement mises en œuvre. Elle est maintenue à jour selon la cadence définie en **§2.3** ci-dessous.

**Légende :** ✅ conforme · ⚠️ partiellement conforme · 🔜 conformité prévue · ⬜ non applicable

| Exigence / référentiel | Applicable | État | Écart identifié | Mesure / référence |
|------------------------|------------|------|-----------------|--------------------|
| **RGPD** — protection des données personnelles (agents, journaux d'audit) | Oui | ⚠️ | Politique de rétention / purge des journaux non définie | Pseudonymisation Sentry, pas de PII en logs ([§9.5](../9-matrice-controles/index)) ; rétention à définir ([§9.6](../9-matrice-controles/index)) |
| **RGS** — Référentiel Général de Sécurité (authentification, chiffrement) | Oui | ✅ | — | Authentification JWT, TLS + HSTS ([§9.1](../9-matrice-controles/index), [§9.5](../9-matrice-controles/index)) |
| **Guide d'hygiène ANSSI / PSSI-E (PSSI de l'État)** | Oui | ⚠️ | Alignement avec la PSSI de la DGAMPA à confirmer _(à confirmer)_ | Durcissement HTTP, RBAC, gestion des accès ([§4](../4-gestion-acces/index), [§9.2](../9-matrice-controles/index), [§9.4](../9-matrice-controles/index)) |
| **ISO/IEC 27001** | Référentiel d'inspiration | ⬜ | Non certifié ; principes appliqués sans démarche de certification | Mesures reflétées dans la [matrice §9](../9-matrice-controles/index) |
| **SecNumCloud** — qualification de l'hébergement | Portée hébergeur | 🔜 | Porté par l'infrastructure DAMSI _(à confirmer avec la DAMSI)_ | Hébergement et terminaison TLS gérés par la DAMSI St Malo |

La **preuve détaillée** des contrôles mis en œuvre figure dans la [§9 — Matrice des contrôles de sécurité](../9-matrice-controles/index) ; la liste des écarts assumés à la date de l'homologation est consolidée en [§9.9 — Synthèse des limitations connues](../9-matrice-controles/index).

### 2.3 Revue des politiques et procédures de sécurité

Les politiques et procédures liées au système d'information sont **vérifiées a minima annuellement** afin de confirmer qu'elles sont **à jour et pertinentes**. Une revue est également déclenchée en cas d'**évolution majeure** :
- de la **menace** (nouvelle vulnérabilité structurante, évolution du contexte de cybersécurité) ;
- du contexte **métier** (nouveau service, nouvelle donnée traitée) ;
- du contexte **technique** (changement d'architecture, de dépendances, d'hébergement) ;
- du contexte **organisationnel** (changement de responsabilités, de prestataire).

**Périmètre de la revue :** le présent MCS (toutes sections), le [MCO](../../mco/index), l'alignement avec la PSSI, les procédures opérationnelles (gestion des accès [§4](../4-gestion-acces/index), des vulnérabilités [§3](../3-gestion-vulnerabilites/index), des incidents [§7](../7-gestion-incidents/index), des sauvegardes [§8](../8-sauvegardes/index)), ainsi que l'[inventaire des services et données (§1.4)](../1-objet-et-perimetre/index) et l'**analyse de conformité (§2.2)** ci-dessus.

**Responsables :** le **Référent sécurité applicative** pilote la revue, en lien avec le **Responsable SSI (RSSI)** (cf. table **§2.1** ci-dessus).

**Traçabilité :** chaque revue est consignée dans la table « Historique du document » de chaque page MCS concernée et dans l'historique Git du dépôt `docs/`. Le journal des revues ci-dessous en donne la synthèse.

| Date | Périmètre revu | Résultat / mises à jour | Responsable |
|------|----------------|-------------------------|-------------|
| 05 Oct 2026 | MCS complet | Version initiale de l'inventaire (§1.4), de l'analyse de conformité (§2.2) et de la présente procédure de revue (§2.3) | Louis Hache |
| _(à compléter)_ | | | |
