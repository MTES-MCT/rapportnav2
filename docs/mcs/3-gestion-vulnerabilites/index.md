## 3. Gestion des vulnérabilités

### 3.1 Politique de veille et d’alerte
_(Suivi des bulletins de sécurité, flux CERT, CVE, etc.)_
La chaine CI/CD fournie par la DAMSI St Malo effectue des analyses :
- Analyse des dépendances via dependency-check
- Analyse des images/containers Docker via Trivy
- Analyse de qualité via sonarqube

Il est donc impossible de déployer de nouvelles versions si ces analyses échouent, cad si des criticités critiques ou majeures sont détectées.

Outre ces détections en continu, des tests d'intrusions programmés permettent de faire ressortir d'autres vulnérabilités.

### 3.2 Analyse et priorisation

Les règles de priorisation suivantes sont appliquées :
- critique : l'équipe interrompt immédiatement les tâches en cours afin de se focaliser sur la résolution le plus rapidement possible.
- majeure : la vulnérabilité sera traitée le jour même ou au plus tard le lendemain selon l'heure de détection
Les criticités plus faibles seront traitées avec plus faible priorité selon les disponibilités de l'équipe.


### 3.3 Suivi et traçabilité

Aucune procédure de suivi de traçabilité et de correctifs n'a été mis en place.
Un suivi peut être effectué en observants les commits sur le dépôt GitHub.
