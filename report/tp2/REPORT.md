
# Rapport TP2 – Liste de courses

## 1. Détails d’implémentation

Dans ce projet, plusieurs **design patterns** ont été utilisés afin d’améliorer la structure, la maintenabilité et l’extensibilité du code :

- **DAO (Data Access Object)** : permet d’abstraire la persistance des données. Deux implémentations ont été créées : `JsonGroceryListDAO` pour le format JSON et `CsvGroceryListDAO` pour le format CSV.
- **Strategy Pattern** : le choix de l’implémentation DAO à utiliser est décidé dynamiquement en fonction du format de fichier (`json` ou `csv`) fourni par l’utilisateur.
- **Separation of Concerns** : chaque classe a une responsabilité claire : parsing de ligne de commande (`CommandLineProcessor`), exécution (`CommandExecutor`), logique métier (`GroceryListManager`), accès aux données (`GroceryListDAO`).

## 2. Problèmes techniques rencontrés

### a. Difficultés rencontrées

- **Désérialisation JSON** : Jackson échouait à convertir les anciennes chaînes `"Nom: Quantité"` en objets `GroceryItem`.
- **Incohérences dans les tests** : certaines classes ne respectaient pas le nouveau modèle basé sur `GroceryItem`, causant des échecs de tests.
- **Absence de prise en charge de la catégorie** : l’ajout par défaut ne prenait pas en compte la catégorie correctement via la ligne de commande.

### b. Solutions apportées

- Implémentation d’un **constructeur vide** et des **getters/setters** dans `GroceryItem` pour permettre la sérialisation.
- Réécriture des DAO pour fonctionner strictement avec des objets `GroceryItem`.
- Extension du parseur d’arguments pour accepter le flag `--category` (`-c`) et transmission au `CommandExecutor`.

### c. Alternatives considérées

- Utilisation de Jackson pour le CSV : rejeté car plus complexe pour la structure actuelle.
- Création d’une classe `GroceryList` pour encapsuler les opérations métier : jugée redondante avec `GroceryListManager`.

## 3. Schéma

Le diagramme de classes décrit les interactions entre les différentes classes du projet, ainsi que leurs relations telles que l'association, la composition et la dépendance, basées sur le comportement décrit dans le diagramme de séquence.

_Fichier fourni : `CLASS-DIAGRAM.png`_

Le diagramme de séquence, qui illustre le flux des actions entre les objets lors de l'exécution des commandes, a également été utilisé pour clarifier les relations de collaboration entre les classes pendant l'exécution des commandes.

_Fichier fourni : `SEQUENCE-DIAGRAM.png`_

---

✍️ Contributeurs : AKIL Wael - BENMAMMAR Thanina

> Répertoire : `report/tp2`  
> Fichier : `REPORT.md`
