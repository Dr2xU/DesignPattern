# 🛠️ Rapport TP1 - Refactorisation et Tests Unitaires

## 📌 Résumé des Mises à Jour du Projet

Ce document présente les principaux changements et améliorations apportés au code d'origine lors du processus de refactorisation et d'ajout de tests.

## 🔄 Refactorisation et Améliorations du Code

### 1. Séparation des Responsabilités

- La logique de traitement des arguments en ligne de commande a été extraite dans une classe dédiée `CommandLineProcessor`.
- Un modèle `CommandLineArgs` a été introduit pour encapsuler les options et arguments analysés.
- Une classe `CommandExecutor` a été créée pour gérer l'exécution des commandes.

### 2. Encapsulation de la Logique des Courses

- La classe `GroceryListManager` gère toutes les opérations liées aux courses (ajout, suppression, listing, sauvegarde/chargement).

### 3. Meilleure Gestion des Erreurs

- Des messages d'erreur clairs ont été ajoutés pour les commandes invalides, les arguments manquants ou les types erronés.
- La gestion des exceptions est centralisée dans la classe `Main`.

### 4. Gestion JSON Simplifiée

- La sérialisation/désérialisation JSON est simplifiée via une liste de chaînes (`"Lait: 3"`) au lieu d'une structure complexe.

## ✅ Améliorations des Tests

### 1. Ajout de Tests Unitaires

- `CommandLineArgsTest` : vérifie le bon fonctionnement du modèle d'arguments.
- `CommandLineProcessorTest` : teste le parsing et la validation des arguments.
- `CommandExecutorTest` : valide le comportement et la robustesse de l'exécution des commandes.
- `GroceryListManagerTest` : couvre les opérations de gestion de liste, la persistance de fichiers, et les cas limites.
- `MainTest` : teste le flux de commande complet, les erreurs et les codes de sortie.

### 2. Test de Validation Basique (Smoke Test)

- Test minimal (`SmokeTest`) pour s'assurer que le cadre de test fonctionne correctement.

### 3. Utilisation de Dossiers Temporaires

- Pour les tests dépendants du système de fichiers, `@TempDir` a été utilisé pour garantir l'isolation et le nettoyage automatique.

---

✍️ Contributeurs : AKIL Wael - BENMAMMAR Thanina

> Répertoire : `report/tp1`  
> Fichier : `REPORT.md`
