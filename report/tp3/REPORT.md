# Rapport TP3 – Liste de courses

## 1. Détails d’implémentation

Dans ce TP3, nous avons ajouté la gestion de la commande `info` dans l'application de gestion de liste de courses. Cette commande affiche des informations système telles que la date actuelle, le nom du système d’exploitation, et la version de Java.

### a. Fonctionnement de la commande `info`

- La commande `info` ne prend aucun argument, y compris les options `-s`, `-f`, et `-c`. Si un argument est fourni, il est ignoré.
- La commande `info` affiche des informations système telles que :
  - La date actuelle.
  - Le nom du système d’exploitation.
  - La version de Java.

### b. Mise à jour des classes

Les classes suivantes ont été modifiées pour intégrer la gestion de la commande `info` :

- **CommandExecutor** : La méthode `execute` a été mise à jour pour inclure la gestion de la commande `info`. Une nouvelle méthode, `displayInfo()`, a été ajoutée à la classe pour afficher les informations système. Cette modification permet à l'application de traiter la commande `info` et d'afficher les informations système correspondantes.
- **CommandLineProcessor** : Cette classe a été mise à jour pour valider correctement l'argument `info` et s’assurer qu’aucun argument n'est passé avec cette commande.

### c. Refactoring prévu pour la prochaine itération

- La méthode `execute()` dans la classe `CommandExecutor` sera refactorée en plusieurs classes dans la prochaine itération (TP suivant). Cela découplera les différentes commandes (`add`, `list`, `remove`, et `info`) et permettra une gestion plus modulaire des commandes.

## 2. Problèmes techniques rencontrés

### a. Difficultés rencontrées

- **Validation des arguments** : Au départ, la commande `info` acceptait des arguments, ce qui n'était pas conforme à la spécification. L'ajustement pour ignorer les arguments a nécessité une validation stricte.
- **Affichage des informations système** : Il fallait obtenir les informations système de manière fiable et les afficher dans un format convivial.

### b. Solutions apportées

- Mise à jour de la classe `CommandExecutor` pour ajouter le traitement de la commande `info` et affichage des informations système avec les propriétés système Java (`System.getProperty`).
- Ajout de la fonctionnalité d'affichage de la date actuelle avec le format `yyyy-MM-dd` à l’aide de `SimpleDateFormat`.

### c. Alternatives considérées

- Aucun autre outil ou bibliothèque n’a été envisagé pour obtenir les informations système car les méthodes `System.getProperty` sont suffisamment performantes pour ces besoins.

## 3. Schéma

Le diagramme de classes et le diagramme de séquence ont été mis à jour pour refléter les ajouts et les changements apportés au projet :

- Le **diagramme de classes** montre les interactions entre les différentes classes, en particulier l'ajout du traitement de la commande `info` dans la classe `CommandExecutor`.
  
  _Fichier fourni : `CLASS-DIAGRAM.png`_

- Le **diagramme de séquence** illustre le flux des actions lors de l'exécution de la commande `info`, en affichant les informations demandées par l’utilisateur.
  
  _Fichier fourni : `SEQUENCE-DIAGRAM.png`_

---

✍️ **Contributeurs** : AKIL Wael - BENMAMMAR Thanina

> **Répertoire** : `report/tp3`  
> **Fichier** : `REPORT.md`
