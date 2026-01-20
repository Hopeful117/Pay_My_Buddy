# Pay my Buddy

Application web de transfert d'argent entre amis.
## Description
Pay my Buddy est une application web qui permet aux utilisateurs de transférer de l'argent entre amis de manière simple et sécurisée. Les utilisateurs peuvent créer un compte, ajouter des amis, et effectuer des transferts d'argent instantanés.

## Fonctionnalités
- Création de compte utilisateur
- Ajout et gestion des amis
- Transferts d'argent entre amis
- Historique des transactions
- Sécurité des données et des transactions

## Diagramme de la base de données
![MPD Pay my Buddy](docs/MPD.svg)

## Script SQL de création de la base de données
Le script SQL pour créer la base de données est disponible dans le fichier `docs/Schema.sql`

## Script SQL de peuplement de la base de données
Le script SQL pour peupler la base de données est disponible dans le fichier `docs/Peuplement.sql`

## Script SQL de test de la base de données
Le script SQL pour tester la base de données est disponible dans le fichier `docs/Script.sql`

## Script Bash de sauvegarde de la base de données
Le script Bash pour sauvegarder la base de données est disponible dans le fichier `docs/backup_db.sh`

## Script Bash de restauration de la base de données
Le script Bash pour restaurer la base de données est disponible dans le fichier `docs/restore_db.sh`

## Note
Pour exécuter les scripts nécessitant un mot de passe, assurez-vous de définir la variable d'environnement "MYSQL_PASSWORD" avec le mot de passe approprié avant d'exécuter les scripts.
Par exemple, dans un terminal bash, vous pouvez utiliser la commande suivante :
```bashexport MYSQL_PASSWORD="votre_mot_de_passe"```
Remplacez "votre_mot_de_passe" par le mot de passe réel que vous souhaitez utiliser.
