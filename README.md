# ProjetSysD

Apres avoir rentré son adresse ip, port et pseudo un **ChordPeer** est créé.  
Il lance un thread qui contient un **ServerSocket** sur le port précisé.  
Si un autre utilisateur souhaite se connecté a celui ci,  
il envoie un message au format json de type :  
`{
    "type" = "****",  
    "****" = "****", ...
}`  
Chaque message envoyer sera composé d'un type qui pourra être :  
"find", "found", "join", "msg", "salon", "leave", "pred", "succ".  
Si le message envoyer contient "find", le **ChordPeer** interrogé, s'il n'est pas le successeur de la clé,   
fera passer le message a son successeur et ainsi de suite jusqu'à trouver la place de la clé envoyer dans le reseau chord.  
Une fois trouvez, le port, l'ip et le pseudo du successeru lui sont renvoyer avec le message "found".  
Il créé ainsi son _succ_ et bind son _sSock_ au socket vers celui ci.  
Il envoie ensuite un message "join" qui contient ces information.  
Le sont successeur lui envoie alors les information de son predecesseur avec comme type "pred",  
ferme le thread et le socket de son predecesseur et remplie sont _pred_ par les info reçut.  
Il bind également sont _pSocket_ et lance un nouveau thread.  
Le client fait donc de meme et renvoie ses informtations a son predecesseur avec comme type "succ".  
Pour envoyer un message sur le réseau chord, il faut envoyer un message de type "msg" à sont successeur,  
qui le recevra, fera un notify (obervable), et renverra le message a son successeur si celui ci est différent de l'exp du message.  
Les informations autour des salons sont de type "salon" et on un "goal" qui peut être :
 * "info" pour recevoir les salons qui existent ainsi que les clients qui y sont lié.
 * "join" pour rejoindre un salon existant
 * "create" pour creé un nouveau salon
 * "msg" pour envoyer un message au personne sur son salon
 * "leave" pour quitter son salon  

Toutes les messages lié au salon (sauf info) sont retransmisent a tous le réseaux chord,  
pour que toutes les clients ai leurs information a jour au niveau des salons.
Enfin pour quitté le réseau chord il suffit d'envoyer un message de typoe "leave" au successeur,  
avec les informations du predecesseur dedant ce qui lui permettra de créé un nouveau socket sur celui ci,  
et d'envoyer un message de type "succ" à celui ci de façon à avoir un reseau chord complet.