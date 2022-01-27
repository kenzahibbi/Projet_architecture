# archLogP2


For the data base, I use a docker container to configure it do that:


 sudo docker run --detach --name=sql3 --publish 3306:3306 --env="MYSQL_ROOT_PASSWORD=qwerty" mysql


sudo docker exec -it sql3 bash

mysql -uroot -p

passwd=’qwerty’

CREATE DATABASE classRessources;


While your docker container is running you have a database
