\# EnergySense



Plateforme cloud-native de monitoring énergétique/HVAC : capteurs simulés, ingestion temps réel via MQTT, API REST sécurisée par JWT, dashboard, et alerting automatique basé sur des seuils.



\## Architecture (vue d'ensemble)



\- \*\*simulator\*\* — simule des capteurs (température, humidité, énergie) et publie les lectures sur MQTT \*(à venir)\*

\- \*\*core-api\*\* — service Spring Boot : écoute MQTT, stocke les lectures, expose l'API REST, gère l'authentification JWT et l'alerting

\- \*\*frontend\*\* — dashboard Angular \*(à venir)\*

\- \*\*Mosquitto\*\* — broker MQTT

\- \*\*TimescaleDB\*\* (PostgreSQL) — stockage des séries temporelles



\## Pipeline DevOps



\- \*\*GitHub Actions\*\* : feedback rapide à chaque push/PR

\- \*\*Jenkins\*\* : pipeline complet (SonarQube, Docker, Trivy, déploiement)

\- \*\*ArgoCD\*\* : déploiement GitOps sur Kubernetes



\## Statut actuel



En cours de construction, étape par étape.

