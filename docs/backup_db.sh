#!/bin/bash

# -----------------------------
# Script de sauvegarde MySQL sécurisé
# -----------------------------

# Variables à configurer avant l'exécution
DB_NAME="paymybuddy_db"
DB_USER="root"
BACKUP_DIR="./backupDB"
DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/backup_${DB_NAME}_$DATE.sql"

# Récupération du mot de passe depuis la variable d'environnement
if [ -z "$MYSQL_PASSWORD" ]; then
    echo "Erreur : veuillez définir la variable d'environnement MYSQL_PASSWORD"
    echo "Exemple : export MYSQL_PASSWORD='votre_mdp'"
    exit 1
fi

# Créer le dossier de backup si inexistant
mkdir -p "$BACKUP_DIR"

# Sauvegarde de la base
echo "Sauvegarde de la base $DB_NAME ..."
mysqldump -u $DB_USER -p$MYSQL_PASSWORD $DB_NAME > $BACKUP_FILE

# Vérification
if [ $? -eq 0 ]; then
    echo "Sauvegarde terminée avec succès : $BACKUP_FILE"
else
    echo "Erreur lors de la sauvegarde !"
    exit 1
fi

# Compression
gzip $BACKUP_FILE
echo "Fichier compressé : $BACKUP_FILE.gz"
