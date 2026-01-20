#!/bin/bash

# -----------------------------
# Script de restauration MySQL sécurisé
# -----------------------------

DB_NAME="paymybuddy_db"
DB_USER="root"
BACKUP_FILE="$1"

# Vérification du mot de passe
if [ -z "$MYSQL_PASSWORD" ]; then
    echo "Erreur : veuillez définir la variable d'environnement MYSQL_PASSWORD"
    echo "Exemple : export MYSQL_PASSWORD='votre_mdp'"
    exit 1
fi

# Vérification du fichier
if [ -z "$BACKUP_FILE" ]; then
    echo "Usage: ./restore_db.sh <chemin_fichier_backup.sql.gz>"
    exit 1
fi

if [ ! -f "$BACKUP_FILE" ]; then
    echo "Fichier de backup introuvable : $BACKUP_FILE"
    exit 1
fi

# Créer la base si nécessaire
mysql -u $DB_USER -p$MYSQL_PASSWORD -e "CREATE DATABASE IF NOT EXISTS $DB_NAME;"

# Restauration
echo "Restauration de la base $DB_NAME à partir de $BACKUP_FILE ..."

if [[ "$BACKUP_FILE" == *.gz ]]; then
    gunzip < "$BACKUP_FILE" | mysql -u $DB_USER -p$MYSQL_PASSWORD $DB_NAME
else
    mysql -u $DB_USER -p$MYSQL_PASSWORD $DB_NAME < "$BACKUP_FILE"
fi

if [ $? -eq 0 ]; then
    echo "Restauration terminée avec succès !"
else
    echo "Erreur lors de la restauration !"
    exit 1
fi
