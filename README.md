# MapareVote

Application de vote.
Pour calculer le résultat du vote, les algorithmes suivant sont disponible pour l'instant:
- Choix majoritaire ("majority")
- Méthode Borda ("borda")
- Scrutin à vote unique transférable ("STV")

## Interface REST

### Authentification

	GET https://api.maparevote.siannos.fr/auth/signin

**Format de la requête**

	Headers 
	Authorization: Basic {AUTHORISATION}

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification pour recevoir le token JWT. {AUTHORISATION} consiste en la concaténation "Username:Password" convertie en base 64.  
**Valeur possible**	Basic {AUTHORISATION}

**Réponse**

	Renvoie le jeton d'authentification {JETON} utilisé dans la majorité des commandes plus bas.

**Exemple Curl**

	curl -H "Authorization: Basic {AUTHORISATION}" -X GET "https://api.maparevote.siannos.fr/auth/signin"

### Créer un compte utilisateur

	POST https://api.maparevote.siannos.fr/users

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json
	Content: application/json

	Payload  
	{  
		"email":"adresse@mail.com",  
		"lastname":"Nomdefamille",  
		"firstname":"Prenom",  
		"password":"mdp"  
	}

	
**Paramètres**

**Nom**			| **Requis**| **Type** 	| **Valeur par défaut**	| **Description**																| **Valeur possible**
----------------|-----------|-----------|-----------------------|-------------------------------------------------------------------------------|----------------------
Authorization	| Oui 		| String	| Aucune 				| Authentification avec jeton JWT. 												| Bearer {JETON}
email	| Oui 		| String	| Aucune 				| Adresse email de la personne. 												| Email valide unique.
lastname	| Oui 		| String	| Aucune 				| Nom de famille de la personne. 												| Chaine de caractère.
firstname	| Oui 		| String	| Aucune 				| Prénom de la personne. 												| Chaine de caractère.
password	| Oui 		| String	| Aucune 				| Mot de passe du compte. Il n'est pas stocké en dur. 												| Chaine de caractère.

**Réponse**

	Renvoie le détail du compte utilisateur avec le même format que celui envoyé et des identifiants en plus.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"email":"adresse@mail.com","lastname":"Nomdefamille","firstname":"Prenom","password":"mdp"}' "https://api.maparevote.siannos.fr/users"

### Détails de son propre compte

	GET https://api.maparevote.siannos.fr/users/me

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

	Renvoie les détails de son propre compte, y compris les tokens des votes sur lesquels on a voté pour vérification.
	Format d'un utilisateur:
	{
		"id": 73,
		"email": "monemail@host.com",
		"lastname": "DELON",
		"firstname": "Alain",
		"confirmed": true,
		"admin": false,
		"banned": false,
		"startedVotes": [
			{
				"id": 2,
				"maxChoices": 1,
				"pendingResult": false
			},
		],
		"privateVoteList": [],
		"votedVotes": []
	}

**Objet**

**Nom**				| **Type**	| **Description** 
--------------------|-----------|-------------------
email		|	String		| Adresse email liée au compte.
lastname		|	String		| Nom de famille.
firstname		|	String		| Prénom.
confirmed		|	Boolean		| Indique si l'email a été vérifié ou non.
admin		|	Boolean		| Indique si le compte est un compte administrateur.
banned		|	Boolean		| Indique si le compte est banni.
startedVotes		|	Array		| Liste des votes créés par l'utilisateur.
privateVoteList		|	Array		| Liste des votes privés pour lesquels l'utilisateur a été invité.
votedVotes		|	Array		| Liste des identifiants des votes pour lesquels l'utilisateur a voté et les tokens associés pour vérifier que le vote est bien pris en compte.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/users/me"

### Confirmation de l'email

	GET https://api.maparevote.siannos.fr/users/{ID}/validate/{VALIDATION}
	Avec {ID} le nombre identifiant de l'utilisateur et {VALIDATION} le token de validation de l'email.

**Format de la requête**

	No Headers

**Réponse**

	Renvoie "Ok" si tout s'est bien passé. Le compte utilisateur sera alors validé.

**Exemple Curl**

	curl -X GET "https://api.maparevote.siannos.fr/users/33/validate/a37d43b3-b991-4ceb-955c-56d4256f4681"
	

### Suppression d'un utilisateur

	DELETE https://api.maparevote.siannos.fr/users/{ID}
	Avec {ID} le nombre identifiant de l'utilisateur.

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

	Confirme la suppression du compte si l'utilisateur supprime son propre compte ou est admin.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -X DELETE "https://api.maparevote.siannos.fr/users/79"


### Liste des votes publics

	GET https://api.maparevote.siannos.fr/votes/public?{FILTER/SORT}
	Avec {FILTER/SORT} les options de filtrage.

**Format de la requête**

	Headers 
	Accept: application/json

**Options de filtrage**
	
**page_num** Numéro de page (pagination).  
**page_size** Nombre de votes par page (pagination).  
**name_like** Cherche les votes avec la chaine de caractères donnée dans leur titre.  
**starts_with** Même chose mais spécifiquement au début du titre du vote.  
**ends_with** Même chose mais spécifiquement à la fin du titre du vote.  
**algo** Filtre les votes par type d'algorithme (valeurs possibles au dessus).  
**open** true/false. Faux par défaut. Si vrai retire les votes déjà fermés ou pas encore ouvert à la date courante.  
**sort** Trie les résultats. name = ordre alphabétique, votes = par nombre de bulletin postés pour ce vote, startdate = par date d'ouverture du vote.  
**order** Lié à sort, détermine l'ordre croissant ou décroissant (asc et desc respectivement).

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
				"names":[  
					"Blanc"  
				]  
			},  
			{  
				"names":[  
					"Beige"
				]  
			}  
		],  
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
		"startDate":[  
			2021,5,29  
		],  
		"endDate":null,  
		"algo":"borda",  
		"anonymous":false,  
		"intermediaryResult":true,  
		"choices":[  
			{  
				"names":[  
					"Magenta"  
				]  
			},  
			{  
				"names":[  
					"Champagne"  
				]  
			}  
		],  
		"maxChoices":1,  
		"members":[  
			{  
				"email":"henri8@examplemail.com"  
			},  
			{  
				"email":"shakespeare@bill.com"  
			}  
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
	
### Suppression d'un vote

	DELETE https://api.maparevote.siannos.fr/votes/{ID}
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}

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

	curl -H "Authorization: Bearer {JETON}" -X DELETE "https://api.maparevote.siannos.fr/votes/46"

### Déposer un bulletin pour un vote

	POST https://api.maparevote.siannos.fr/votes/{ID}/ballots
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json
	Content: application/json

	Payload  
	{  
		"choices":[  
			{  
				"choice":{  
					"id":1,  
				},  
				"weight":9,   
			}  
		]  
	}

	
**Paramètres**

**Nom**			| **Requis**| **Type** 	| **Valeur par défaut**	| **Description**																| **Valeur possible**
----------------|-----------|-----------|-----------------------|-------------------------------------------------------------------------------|----------------------
Authorization	| Oui 		| String	| Aucune 				| Authentification avec jeton JWT. 												| Bearer {JETON}
choices	| Oui 		| Array	| Aucune 				| Liste des choix 												| Liste avec un nombre de choix cohérent avec le vote en question.
choice	| Oui 		| Objet	| Aucune 				| Choix sélectionné 												| En cas de Borda ou STV tous les choix doivent figurer
id	| Oui 		| Number	| Aucune 				| Le nombre identifiant du choix. 												| Doit correspondre à un choix spécifique associé au vote en question.
weight	| Oui sauf majority		| Number	| Aucune 				| Score pour le choix en question pour ce bulletin en particulier. 												| de 1 jusqu'au nombre de choix totaux du vote pour Borda/STV, avec 1 = premier choix pour STV, etc. Ignoré pour vote majoritaire.


**Réponse**

	Renvoie le bulletin bien formé avec le même format que l'envoi et des champs en plus de type id.

**Exemple Curl**

	curl -H "Authorization: Bearer {TOKEN}" -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"choices":[{"choice":{"id":1},"weight":9}]}' "http://localhost:5431/votes/37/ballots"
	
### Obtenir la liste des votes privés pour lesquels on est invité

	GET https://api.maparevote.siannos.fr/votes/private/invited?{FILTER/SORT}
	Avec {FILTER/SORT} les options de filtrage.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Options de filtrage**
	
**page_num** Numéro de page (pagination).  
**page_size** Nombre de votes par page (pagination).  
**name_like** Cherche les votes avec la chaine de caractères donnée dans leur titre.  
**starts_with** Même chose mais spécifiquement au début du titre du vote.  
**ends_with** Même chose mais spécifiquement à la fin du titre du vote.  
**algo** Filtre les votes par type d'algorithme (valeurs possibles au dessus).  
**open** true/false. Faux par défaut. Si vrai retire les votes déjà fermés ou pas encore ouvert à la date courante.  
**sort** Trie les résultats. name = ordre alphabétique, votes = par nombre de bulletin postés pour ce vote, startdate = par date d'ouverture du vote.  
**order** Lié à sort, détermine l'ordre croissant ou décroissant (asc et desc respectivement).

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie la liste des votes concernés. Voir plus haut pour le format d'un vote.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/private/invited"

### Obtenir le bulletin posté pour un vote spécifique.

	GET https://api.maparevote.siannos.fr/votes/{ID}/myballot
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

	Renvoie le bulletin concerné. Voir plus haut pour le format d'un bulletin. Marche seulement si le vote n'est pas anonyme.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/46/myballot"
	
### Obtenir la liste des votes que l'on a créé soi-même

	GET https://api.maparevote.siannos.fr/votes/startedvotes?{FILTER/SORT}
	Avec {FILTER/SORT} les options de filtrage.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Options de filtrage**
	
**page_num** Numéro de page (pagination).  
**page_size** Nombre de votes par page (pagination).  
**name_like** Cherche les votes avec la chaine de caractères donnée dans leur titre.  
**starts_with** Même chose mais spécifiquement au début du titre du vote.  
**ends_with** Même chose mais spécifiquement à la fin du titre du vote.  
**algo** Filtre les votes par type d'algorithme (valeurs possibles au dessus).  
**open** true/false. Faux par défaut. Si vrai retire les votes déjà fermés ou pas encore ouvert à la date courante.  
**sort** Trie les résultats. name = ordre alphabétique, votes = par nombre de bulletin postés pour ce vote, startdate = par date d'ouverture du vote.  
**order** Lié à sort, détermine l'ordre croissant ou décroissant (asc et desc respectivement).

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie la liste des votes concernés. Voir plus haut pour le format d'un vote.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/startedvotes"

### Obtenir la liste des votes pour lesquels on a déposé un bulletin

	GET https://api.maparevote.siannos.fr/votes/votedvotes?{FILTER/SORT}
	Avec {FILTER/SORT} les options de filtrage.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}
	Accept: application/json

**Options de filtrage**
	
**page_num** Numéro de page (pagination).  
**page_size** Nombre de votes par page (pagination).  
**name_like** Cherche les votes avec la chaine de caractères donnée dans leur titre.  
**starts_with** Même chose mais spécifiquement au début du titre du vote.  
**ends_with** Même chose mais spécifiquement à la fin du titre du vote.  
**algo** Filtre les votes par type d'algorithme (valeurs possibles au dessus).  
**open** true/false. Faux par défaut. Si vrai retire les votes déjà fermés ou pas encore ouvert à la date courante.  
**sort** Trie les résultats. name = ordre alphabétique, votes = par nombre de bulletin postés pour ce vote, startdate = par date d'ouverture du vote.  
**order** Lié à sort, détermine l'ordre croissant ou décroissant (asc et desc respectivement).

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie la liste des votes concernés. Voir plus haut pour le format d'un vote.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/votes/votedvotes"

### Obtenir la liste des tokens pour un vote spécifique

	GET https://api.maparevote.siannos.fr/votes/{ID}/tokens
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie la liste des tokens pour le vote concerné. Cela permet de vérifier que le bulletin que l'on a déposé est bien pris en compte.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -X GET "https://api.maparevote.siannos.fr/votes/46/tokens"

### Obtenir la liste des utilisateurs

	GET https://api.maparevote.siannos.fr/users

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

	Renvoie la liste des utilisateurs. Voir plus haut pour le format d'un utilisateur. Seuls les comptes administrateurs peuvent faire cette requête.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/users"
	
### Obtenir les détails d'un utilisateur spécifique

	GET https://api.maparevote.siannos.fr/users/{ID}
	Avec {ID} le nombre identifiant de l'utilisateur.

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

	Renvoie l'utilisateur concerné. Voir plus haut pour le format d'un utilisateur. Seuls les comptes administrateurs peuvent faire cette requête.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -H "Accept: application/json" -X GET "https://api.maparevote.siannos.fr/users/49"

### Bannir un utilisateur

	PATCH https://api.maparevote.siannos.fr/users/{ID}/ban
	Avec {ID} le nombre identifiant du vote.

**Format de la requête**

	Headers 
	Authorization: Bearer {JETON}

**Paramètres**

**Nom**	Authorization  
**Requis**	Oui  
**Type**	String  
**Valeur par défaut**	Aucune  
**Description**	Authentification avec jeton JWT  
**Valeur possible**	Bearer {JETON}

**Réponse**

	Renvoie 0 si tout s'est bien passé. Seuls les comptes administrateurs peuvent faire cette requête.

**Exemple Curl**

	curl -H "Authorization: Bearer {JETON}" -X PATCH "https://api.maparevote.siannos.fr/users/57/ban"
	
