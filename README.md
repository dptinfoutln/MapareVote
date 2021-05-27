# MapareVote

## Interface REST

### Liste des votes publics

	GET https://api.maparevote.siannos.fr/votes/public

**Format de la requête**

	Headers 
	Authorization: bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNYXBhcmVWb3RlIiwiaWF0IjoxNjIyMTI5NDEwLCJzdWIiOiJ0ZXN0QG1haWwuY29tIiwiZmlyc3RuYW1lIjoiSmVhbiIsImxhc3RuYW1lIjoiVW4iLCJleHAiOjE2NTM2NjU0MTB9.UEYDXj1nO2GsxU815BeUE-HLzlAv8-OLRH9VXQ3BEPg
	Accept: application/json

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String	
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	bearer <jeton>

**Réponse**

	Renvoie la liste des votes publics sous forme de liste JSON.  
	Format d'un vote:  
	{  
  "id": 46,  
  "label": "Lettre de l'alphabet préférée",  
  "startDate": [  
    2021,  
    5,  
    14  
  ],  
  "endDate": [  
    2021,  
    5,  
    25  
  ],  
  "algo": "STV",  
  "anonymous": false,  
  "intermediaryResult": false,  
  "votemaker": {  
    "id": 1,  
    "email": "fanduw@gmail.com",  
    "lastname": "Watson",  
    "firstname": "William",  
    "confirmed": true,  
    "admin": false,  
    "banned": false  
  },  
  "choices": [  
    {  
      "id": 107,  
      "names": [  
        "La lettre W"  
      ],  
      "vote": 46  
    },  
    {  
      "id": 108,  
      "names": [  
        "La lettre X"  
      ],  
      "vote": 46  
    },  
    {  
      "id": 113,  
      "names": [  
        "La lettre D"  
      ],  
      "vote": 46  
    }  
  ],  
  "maxChoices": 1,  
  "members": [],  
  "pendingResult": false  
}


**Objet**

**Nom**				| **Type**	| **Description** 
--------------------|-----------|-------------------
label		|	String		| Intitulé du vote en question.
startDate		|	Date		| Date à partir de laquelle le vote est ouvert.
endDate		|	Date		| Date de fermeture du vote (peut être nulle si vote a durée indéterminée).
algo		|	String		| Algorithme utilisé pour calculer les résultats. Voir plus haut pour liste d'algorithmes implantés.
anonymous		|	Boolean		| Indique si le vote est anonyme ou si la liste des personnes ayant voté sera publique.
intermediaryResult		|	Boolean		| Indique si le vote laisse la possibilité de consulter les résultats avant la date de fin. (Vrai par défaut si pas de date de fin)
Votemaker		|	Objet		| Utilisateur créateur du vote en question. Voir format d'un utilisateur plus bas.
choices		|	Array		| Liste de choix pour le vote en question. Format d'un choix: id du choix, names: intitulé du choix, id du vote.
maxChoices		|	String		| Si le vote est de type STV, nombre de gagnants, sinon nombre de choix maximum que l'on peut sélectionner au moment du vote.
members		|	Array		| En cas de vote privé, liste des membres invités à voter. Même format que votemaker pour un membre.
pendingResult		|	Boolean		| Future proofing pour le multi-threading. Pas utilisé pour l'instant. 

**Exemple Curl**

	curl -H " Authorization: bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNYXBhcmVWb3RlIiwiaWF0IjoxNjIyMTI5NDEwLCJzdWIiOiJ0ZXN0QG1haWwuY29tIiwiZmlyc3RuYW1lIjoiSmVhbiIsImxhc3RuYW1lIjoiVW4iLCJleHAiOjE2NTM2NjU0MTB9.UEYDXj1nO2GsxU815BeUE-HLzlAv8-OLRH9VXQ3BEPg" -H "Accept: application/json" -X GET
	https://api.maparevote.siannos.fr/public/votes
