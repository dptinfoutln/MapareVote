# MapareVote

Application de vote.
Pour calculer le résultat du vote, les algorithmes suivant sont disponible pour l'instant:
- Choix majoritaire ("majority")
- Méthode Borda ("borda")
- Scrutin à vote unique transférable ("STV")

## Interface REST

### Liste des votes publics

	GET https://api.maparevote.siannos.fr/votes/public

**Format de la requête**

	Headers 
	Accept: application/json

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
maxChoices		|	Number		| Si le vote est de type STV, nombre de gagnants, sinon nombre de choix maximum que l'on peut sélectionner au moment du vote.
members		|	Array		| En cas de vote privé, liste des membres invités à voter. Même format que votemaker pour un membre.
pendingResult		|	Boolean		| Future proofing pour le multi-threading. Pas utilisé pour l'instant. 

**Exemple Curl**

	curl -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/public/votes"

### Détails d'un vote

	GET https://api.maparevote.siannos.fr/votes/{ID}
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie le détail d'un vote en particulier et calcule les résultats d'un vote si besoin.
	Format d'un vote: voir plus haut.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/46"
	
### Résultats d'un vote

	GET https://api.maparevote.siannos.fr/votes/{ID}/results
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie les résultats d'un vote si ceux ci existent.
	Les résultats ne sont pas générés avec cette requête. Pour générer les résultats voir plus haut.  
	Format d'un résultat:  
	{  
		"choice": {  
			"id": 13,  
			"names": [  
				"Vert"  
			]  
		},  
		"value": 236  
	},  
	{  
		"choice": {  
			"id": 12,  
			"names": [  
				"Blanc"  
			]  
		},  
		"value": 249  
	},  
	{  
		"choice": {  
			"id": 10,  
			"names": [  
				"Rouge"  
			]  
		},  
		"value": 286  
	},  
	{  
		"choice": {  
			"id": 11,  
			"names": [  
				"Bleu"  
			]  
		},  
		"value": 26  
	}

**Objet**

**Nom**				| **Type**	| **Description** 
--------------------|-----------|-------------------
choice		|	Array		| Choix pour le vote en question. Format d'un choix: id du choix, names: intitulé du choix, id du vote.
value		|	Number		| Si le vote est de type STV, nombre positif = gagnant, nombre négatif = perdant, sinon score total pour ce choix.


**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/46/results"

### Créer un vote publique

	POST https://api.maparevote.siannos.fr/votes/public

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json
	Content: application/json

	Payload  
	{  
		"label":"De quelle couleur sont les moutons?",  
		"startDate":[  
			2021,5,12
		],  
		"endDate":null,  
		"algo":"majority",  
		"anonymous":false,  
		"intermediaryResult":true,  
		"choices":[  
		{  
		"names":["Blanc"]},  
		{"names":["Beige"]}],  
		"maxChoices":"1"  
	}

	
**Paramètres**

**Nom**			| **Requis**| **Type** 	| **Valeur par défaut**	| **Description**																| **Valeur possible**
----------------|-----------|-----------|-----------------------|-------------------------------------------------------------------------------|----------------------
Authorization	| Oui 		| String	| Aucune 				| Authentification avec jeton JWT. 												| Bearer {JETON}
label	| Oui 		| String	| Aucune 				| Intitulé du vote en question. 												| Chaine de caractère quelconque.
startDate	| Oui 		| Date	| Aucune 				| Date à partir de laquelle le vote est ouvert. 												| La date doit être aujourd'hui ou plus tard.
endDate	| Non 		| Date	| null 				| Date de fermeture du vote (peut être nulle si vote a durée indéterminée). 												| Peut être nulle, date supérieure à la date de début requise.
algo	| Oui 		| String	| Aucune 				| Algorithme utilisé pour calculer les résultats. Voir plus haut pour liste d'algorithmes implantés. 												| majority/borda/STV.
anonymous	| Non 		| Boolean	| false 				| Indique si le vote est anonyme ou si la liste des personnes ayant voté sera publique. 												| true/false.
intermediaryResult	| Non 		| Boolean	| false 				| Indique si le vote laisse la possibilité de consulter les résultats avant la date de fin.													| true/false. Doit être vrai si pas de date de fin.
choices	| Oui 		| Array	| Aucune 				| Liste de choix pour le vote en question. Format d'un choix: id du choix, names: intitulé du choix, id du vote. 												| Au moins 2 choix.
maxChoices	| Oui sauf borda 		| Number	| Aucune 				| Si le vote est de type STV, nombre de gagnants, sinon nombre de choix maximum que l'on peut sélectionner au moment du vote. 												| Entier inférieur ou égal au nombre de choix total. Ignoré pour borda.

**Réponse**

	Renvoie le détail du vote créé avec les bons identifiants si tout est correct.
	Format d'un vote: voir plus haut.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"label":"Nomduvote","startDate":[2021,5,21],"endDate":null,"algo":"borda","anonymous":false,"intermediaryResult":true,"choices":[{"names":["choix 1"]},{"names":["choix 2"]}]}' "https://api.maparevote.siannos.fr/votes/public"


### Créer un vote privé

	POST https://api.maparevote.siannos.fr/votes/private

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json
	Content: application/json

	Payload  
	{  
		"label":"De quel couleur est le cheval blanc d'Henri IV?",  
		"startDate":[2021,5,29],  
		"endDate":null,  
		"algo":"borda",  
		"anonymous":false,  
		"intermediaryResult":true,  
		"choices":[  
			{"names":["Magenta"]},  
			{"names":["Champagne"]}  
		],  
		"maxChoices":1,  
		"members":[  
			{"email":"henri8@examplemail.com"},  
			{"email":"shakespeare@bill.com"}  
		]  
	}

	
**Paramètres**

**Nom**			| **Requis**| **Type** 	| **Valeur par défaut**	| **Description**																| **Valeur possible**
----------------|-----------|-----------|-----------------------|-------------------------------------------------------------------------------|----------------------
Authorization	| Oui 		| String	| Aucune 				| Authentification avec jeton JWT. 												| Bearer {JETON}
label	| Oui 		| String	| Aucune 				| Intitulé du vote en question. 												| Chaine de caractère quelconque.
startDate	| Oui 		| Date	| Aucune 				| Date à partir de laquelle le vote est ouvert. 												| La date doit être aujourd'hui ou plus tard.
endDate	| Non 		| Date	| null 				| Date de fermeture du vote (peut être nulle si vote a durée indéterminée). 												| Peut être nulle, date supérieure à la date de début requise.
algo	| Oui 		| String	| Aucune 				| Algorithme utilisé pour calculer les résultats. Voir plus haut pour liste d'algorithmes implantés. 												| majority/borda/STV.
anonymous	| Non 		| Boolean	| false 				| Indique si le vote est anonyme ou si la liste des personnes ayant voté sera publique. 												| true/false.
intermediaryResult	| Non 		| Boolean	| false 				| Indique si le vote laisse la possibilité de consulter les résultats avant la date de fin.													| true/false. Doit être vrai si pas de date de fin.
choices	| Oui 		| Array	| Aucune 				| Liste de choix pour le vote en question. Format d'un choix: id du choix, names: intitulé du choix, id du vote. 												| Au moins 2 choix.
maxChoices	| Oui sauf borda 		| Number	| Aucune 				| Si le vote est de type STV, nombre de gagnants, sinon nombre de choix maximum que l'on peut sélectionner au moment du vote. 												| Entier inférieur ou égal au nombre de choix total. Ignoré pour borda.
members	| Non 		| Array	| Aucune 				| Liste des emails de chaque membre. 												| Liste d'emails valides de personnes ayant un compte. Pas de liste = le seul membre est le créateur du vote.


**Réponse**

	Renvoie le détail du vote créé avec les bons identifiants si tout est correct.
	Format d'un vote: voir plus haut.

**Exemple Curl**

	curl -H "Authorization: Bearer {TOKEN}" -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"label":"De quel couleur est le cheval blanc d'Henri IV?","startDate":[2021,5,29],"endDate":null,"algo":"borda","anonymous":false,"intermediaryResult":true,"choices":[{"names":["Magenta"]},{"names":["Champagne"]}],"members":[{"email":"henri8@examplemail.com"},{"email":"shakespeare@bill.com"}]}' "http://localhost:5431/votes/private"
	
### Supression d'un vote

	DELETE https://api.maparevote.siannos.fr/votes/{ID}
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Confirme la suppression du vote.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X DELETE "https://api.maparevote.siannos.fr/votes/46"   
