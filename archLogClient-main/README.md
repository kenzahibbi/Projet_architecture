# RessourcesManagement

Phase 1 Projet Architecture Logicielle



Il s'agit d'un client lourd permettant de gérer les salles d'une école.
On peut ainsi réserver des salles sur un créneau pour un responsable donné.

Les fonctionnalités sont :
- créer + supprimer des salles
- créer + supprimer des personnes
- créer + supprimer des créneaux
- créer + supprimer + modifier des réservations


Le nom de l'exécutable est clickme.jar.

## Documentation

Pour installer, voir les consignes dans le manuel d'installation. Pour savoir comment utiliser l'application, voir le manuel utilisateur. La documentation javadoc est dans le
dossier javadoc.

## Technologies

Java 11

Maven

JavaFX

JDBC

MySQL

### Notes

- Les modèles sont dans le paquet models
- Les controleurs sont dans le paquet controllers; pour chaque modèle on a un sous répertoire
- Les controleurs et les vues sont divisés en fonction des actions lire (index), créer (new), modifier (edit)
- Convention de nommage : les noms des classes des modèles sont au singulier mais les noms des paquets, les noms des vues et des controleurs sont au pluriel
- Le main() est dans la classe Main.java
- Pour ajouter une dépendance, aller sur `mvnrepository.com`, chercher la librairie, et ajouter le morceau de code xml dans pom.xml; ensuite ajouter un `requires`  dans le module-info.java
- Les vues sont dans les ressources; elles sont organisées en fonction du modèle concerné: people/, rooms/, timeslots/ (exception avec le dashboard)
- Dans Eclipse: ctrl shift f pour formatter/indenter un code sélectionné; ctrl shift o pour faire des importations automatiques
- Tutoriel javafx : riptutorial.com/javafx
- Les ressources (images, vues en fxml, fichiers de configuration) doivent être dans le dossier src/main/resources
- Ne pas supprimer la section Logo du README: l'utilisation du logo est gratuite à condition de préciser les auteurs
- Pour créer le jar: Clic droit sur le projet > Run As > Maven Install (le jar est dans le dossier target)
- Pour compiler le projet: Clic droit sur le projet > Run As > Maven Install (les fichiers compilés sont dans le dossier target)
- Supprimer les fichiers compilés et binaires : Clic droit sur le projet > Run As > Maven clean
- Le code est en anglais

### Logo

Icon made by Freepik from www.flaticon.com


