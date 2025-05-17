# Rapport TP4 – Liste de courses

## 1. Détails d’implémentation

Dans ce TP4, nous avons intégré deux fonctionnalités majeures à notre application de gestion de liste de courses : la commande `info` et la commande `web`. Ce TP marque également une phase importante de **refactoring** pour améliorer l'extensibilité et la maintenabilité de l'application en s’appuyant sur les patterns introduits précédemment.

### a. Commande `info`

- **But** : Afficher les informations système (date, système d’exploitation, version Java).
- **Implémentation** :
  - Création de la classe `InfoCommand` implémentant l’interface `Command`.
  - Récupération des propriétés système (`os.name`, `java.version`) et de la date (`LocalDate.now()`).
  - Affichage formaté dans la console.
- **Raison** : Grâce à notre architecture orientée commande, l’ajout s’est limité à une nouvelle classe sans modifier le reste du code.

### b. Commande `web` et interface Web

- **But** : Lancer un serveur web pour interagir via une interface utilisateur moderne.
- **Implémentation** :
  - Intégration de la librairie `grocery-web` (Javalin + Alpine.js).
  - Création de l’adaptateur `WebGroceryShop` implémentant `MyGroceryShop`.
  - Méthodes :
    - `getGroceries()` retourne la liste sous format `WebGroceryItem`.
    - `addGroceryItem(...)`, `removeGroceryItem(...)`, `getRuntime()`.
  - Dépend du `GroceryService` partagé avec le CLI.
- **Raison** : Réutiliser la logique centrale tout en évitant la duplication du code I/O.

### c. Refactorisation pour extensibilité

- Mise en place de `GroceryService` pour centraliser la logique métier.
- Tous les `Command` utilisent ce service, ce qui isole les responsabilités.
- Séparation nette entre :
  - **Modèle** : `GroceryItem`
  - **Contrôleur** : commandes CLI / `WebGroceryShop`
  - **Vue** : console ou UI web

## 2. Patterns et principes SOLID appliqués

### a. Command Pattern

- Toutes les commandes implémentent l’interface `Command`.
- Ajout d’une nouvelle commande = nouvelle classe + ajout dans la `CommandFactory`.
- Respect du principe **Open/Closed**.

### b. Strategy Pattern (DAO)

- Interface `GroceryListDAO`.
- Implémentations : `CsvGroceryListDAO`, `JsonGroceryListDAO`.
- Sélection dynamique via l’option `-f`.
- Ajout possible : `XmlGroceryListDAO`, `DatabaseDAO`.

### c. Adapter Pattern

- `WebGroceryShop` fait l’adaptation entre `MyGroceryShop` (librairie) et `GroceryService`.
- Aucun changement nécessaire dans la logique existante.

### d. MVC (structure implicite)

- Modèle : `GroceryItem` + DAO
- Contrôleur : commandes CLI, web adapter
- Vue : console ou interface web (HTML)

### e. Single Responsibility

- Chaque classe a un rôle précis :
  - `AddCommand` → validation + délégation au service
  - `JsonGroceryListDAO` → gestion I/O JSON
  - `GroceryService` → logique métier

## 3. Problèmes techniques rencontrés

### a. Intégration de la librairie Web

- Compréhension de `MyGroceryShop` et conversion de formats.
- Problèmes de concurrence entre CLI et web → décision : un seul point d’entrée à la fois.

### b. Compatibilité ascendante

- Fichiers JSON/CSV des anciens TPs encore valides.
- Parsing tolérant aux champs manquants.

### c. Testabilité

- Difficulté à tester l’interface web → tests manuels.
- Utilisation de `FakeDAO` pour les tests unitaires CLI.

## 4. Extensibilité démontrée

### a. Ajouter une nouvelle commande

- Créer `SearchCommand`, l’enregistrer dans la `CommandFactory`.
- Aucune modification des autres commandes.

### b. Ajouter un nouveau format de stockage

- Implémenter une nouvelle classe `XmlGroceryListDAO` (ex.).
- Aucun changement dans `GroceryService` ou les `Command`.

### c. Ajouter un nouveau champ (ex. nom de magasin)

- Modifier `GroceryItem`, DAO, `AddCommand`.
- Aucune propagation indésirable.

## 5. Limites et améliorations futures

- Pas de gestion multi-utilisateur ou verrouillage de fichiers.
- Fonctionnalités Web limitées (pas de modification in-place).
- Pas de commande `search` ou filtre CLI.
- Couverture de test à améliorer (notamment côté web).

## 6. Conclusion

Grâce à l’utilisation des **design patterns** et à une architecture claire (SRP, MVC, DI), nous avons pu :

- Ajouter deux nouvelles fonctionnalités majeures.
- Garder une compatibilité avec l’existant.
- Réduire le couplage entre composants.
- Améliorer l’extensibilité.

Cette base est désormais solide pour accueillir des évolutions futures comme l’édition des articles, la synchronisation avec une base de données ou une expérience utilisateur enrichie côté web.

## 7. Schéma

Le diagramme de classes a été mis à jour pour refléter les ajouts et les changements apportés au projet.
  
  _Fichier fourni : `CLASS-DIAGRAM.png`_
  
---

✍️ **Contributeurs** : AKIL Wael - BENMAMMAR Thanina  

> **Répertoire** : `report/tp4`  
> **Fichier** : `REPORT.md`
