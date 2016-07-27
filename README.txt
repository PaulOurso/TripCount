Before launch server :
- npm install -g strongloop
- npm install

API use :
- GET /accounts/count
- POST	/accounts
- GET /groupes/count
- POST /accounts/{id}/groupes : Crée un groupe et l'ajoute à account
- PUT /accounts/{id}/groupes/rel/{fk} : Ajoute une liaison entre account et un groupe déjà existant id: id account, fk: id groupe
- GET accounts/{id}/?filter[include][groupes] : Récupère les groupes de account