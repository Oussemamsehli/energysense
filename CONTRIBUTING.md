\# Guide de contribution — EnergySense



\## Stratégie de branches



\- `main` : toujours déployable, protégée (pas de push direct, seulement via Pull Request)

\- `develop` : branche d'intégration du travail en cours

\- `feature/<nom-court>` : une branche par fonctionnalité, créée depuis `develop`



Workflow :

`feature/xxx` → Pull Request vers `develop` → merge → puis `develop` → `main` quand une version est prête.



\## Convention de commits (Conventional Commits)



Format : `<type>(<scope optionnel>): <description>`



Types principaux :

\- `feat` : nouvelle fonctionnalité

\- `fix` : correction de bug

\- `chore` : tâche de maintenance, configuration, dépendances

\- `docs` : documentation uniquement

\- `test` : ajout ou modification de tests

\- `refactor` : changement de code sans changement de comportement observable



Exemples :

\- `feat(auth): ajoute l'endpoint de login avec génération JWT`

\- `fix(mqtt): corrige le parsing du timestamp`

\- `chore(ci): ajoute le workflow GitHub Actions`



\## Pull Requests



Chaque PR doit passer la CI avant d'être mergeable, et utiliser le template fourni.

