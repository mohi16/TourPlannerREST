#psql...
. ./prepare.sh

psql -h $DB_HOST -p $DB_PORT -f sql/create_db.sql
