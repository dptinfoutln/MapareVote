# MapareVoteApp

Application Android qui utilise l'API du serveur REST.
	  
## Serveur REST
Pour lier votre propre serveur REST avec l'application mobile, il faut mettre l'url de votre serveur dans le `res/values/strings.xml`` et changer la valeur de API_URL à la ligne 32:

	<string name="API_URL">https://api.maparevote.siannos.fr</string>
	
## Application WEB
Pour lier votre propre application web avec l'application mobile, il faut mettre l'url de votre application  dans le `AndroidManifest.xml` à la ligne 31 pour rendre compatible le fait d'ouvrir l'application (si installé) au lieu de se diriger sur le site:

	<data android:scheme="https"  
	  android:host="maparevote.siannos.fr"  
	  android:pathPrefix="/" />