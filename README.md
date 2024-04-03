# HttpServer

CRÉATION DU SERVEUR SOCKET EN JAVA
La classe est conçue pour fournir un moyen simple de démarrer et d'arrêter un serveur web qui peut gérer des connexions simultanées.
Au centre de la classe, nous avons un ServerSocket. Il écoute les connexions entrantes sur le port spécifié lors du démarrage du serveur.
Le serveur utilise le multithreading pour gérer plusieurs connexions simultanément. Chaque nouvelle connexion est gérée par un nouveau Thread qui exécute une instance de ClientTraitement.
Un booléen volatile nommé estEnFonction est utilisé pour contrôler l'exécution du serveur de manière thread-safe. L'utilisation du mot-clé volatile assure que la valeur de cette variable est toujours lue depuis la mémoire principale, et non à partir d'un cache local de thread, ce qui est crucial pour garantir la cohérence entre les différents threads.


GESTION DES THREADS : 
Pour gérer plusieurs connexions simultanées, le serveur utilise un modèle multithread. Chaque requête est traitée dans un thread séparé, permettant au serveur de continuer à écouter d'autres requêtes entrantes.
L'utilisation des threads dans notre projet de serveur web est particulièrement pertinente pour :

1. Gestion des connexions clients : Nous avons utilisé des threads pour gérer les connexions entrantes de manière individuelle. Cela permet à votre serveur de gérer plusieurs requêtes simultanément, sans bloquer les autres utilisateurs pendant le traitement d'une seule requête. Lorsqu'une nouvelle connexion est établie, nous pouvons lancer un nouveau thread pour traiter cette connexion

2. Amélioration du débit du serveur : L'implémentation d'un système de threads peut améliorer considérablement le débit de notre serveur web, car cela permet de traiter plusieurs requêtes en parallèle. Cette approche est particulièrement utile pour un serveur web destiné à gérer un trafic élevé.

La classe HttpServer est au cœur de notre application de serveur web en Java. Elle est responsable de démarrer le serveur, d'accepter les connexions entrantes et de gérer la logique d'arrêt. Voici une explication détaillée de sa conception et de son implémentation :


GESTION DES REQUÊTES HTTP
La gestion des requêtes est conçue pour être simple et conforme aux méthodes de base du protocole HTTP. Le serveur gère les requêtes GET pour servir des fichiers et des répertoires, et exécute des scripts Python si nécessaire.
Le document est une consigne de projet pour la mise en place d'un serveur web basique qui répond aux requêtes HTTP d'un client. Il doit pouvoir :
- retourner le contenu d'un fichier demandé 
- lister les répertoires
- retourner des pages d'erreur pour les ressources inexistantes ou les requêtes incorrectes. 
Des fonctionnalités optionnelles incluent : 
- des listages de répertoires avec liens hypertextes 
- exécution de fichiers Python avant de retourner le résultat. 
- Le projet doit respecter le protocole HTTP


INTERFACE UTILISATEUR GRAPHIQUE
L'interface graphique permet une interaction conviviale avec le serveur. Elle fournit des commandes pour démarrer et arrêter le serveur et ouvrir la page d'accueil dans un navigateur web.
La GUI est implémentée en utilisant la bibliothèque Swing de Java. La classe ServeurInterface crée la fenêtre principale et ses composants. Les boutons sont équipés de gestionnaires d'événements pour réagir aux clics de l'utilisateur.

LIENS UTILES
https://www.jetbrains.com/help/idea/adding-support-for-frameworks-and-technologies.html#disable-framework-auto-detection
https://github.com/paulitoo320/HttpServer
https://trello.com/b/Xd2taLtp/serverwebprojet
https://httpd.apache.org/


