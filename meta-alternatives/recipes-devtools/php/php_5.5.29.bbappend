PACKAGECONFIG="sqlite3"
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 1}"
