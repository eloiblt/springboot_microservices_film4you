# Film4you - Ansible

## Liste des variables d'environements



| Nom de la variable              | Utilité                                                      |
| ------------------------------- | ------------------------------------------------------------ |
| ANSIBLE_HOST_KEY_CHECKING=false | Permet d'éviter à Ansible de vérifier l'authenticité des hosts AWS. |
| AWS_ACCESS_KEY                  | Access key d'AWS présent sur le portail étudiant.            |
| AWS_SECRET_KEY                  | Secret key d'AWS présent sur le portail étudiant.            |
| AWS_SESSION_TOKEN               | Session token d'AWS présent sur le portail étudiant.         |
| DOCKER_USERNAME                 | Username du compte docker.                                   |
| DOCKER_PASSWORD                 | Mot de passe du compte docker.                               |



## Liste des commandes 



### Création des instances AWS

> Cette commande va permettre de générer les instances AWS nécessaire.



```bash
ansible-playbook -i hosts site.yml
```



**Il faut bien aller vérifier dans le fichier `roles/ec2-provision/vars` les varaibles que l'on souhaite utiliser.**



### Configuration des instances AWS



> Cette commande va se connecter en SSH et configurer les instances pour avoir un cluster `Swarm` prêt à l'emploi.



```bash
ansible-playbook -i dynamic_hosts/aws_ec2.yml cluster.yml
```



#### Liste des actions effectué 



- Installation de docker.
- Démarrage du service docker.
- Création du docker `swarm` sur le `Manager`.
- Ajout des nodes `Worker` au `swarm` du `Manager`.



### Lancement de l'application sur les instances 



> Cette commande va permettre de déployer la stack sur notre cluster docker `swarm`.



```bash
ansible-playbook -i dynamic_hosts/aws_ec2.yml film4you.yml
```



#### Liste des actions effectué 



- Copie du dossier `production` sur l'instance `Manager`.
- Connection au compte `docker` pour récupérer les images.
- Lancement de la stack depuis la dernière version du docker compose.

