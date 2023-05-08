#Compile des classes du test
javac -cp ./test/test/WEB-INF/lib/framework.jar -parameters -d ./test/test/WEB-INF/classes ./test/src/*java

#archive du test en war et deplacer dans le fichier tomcat webapps
cd ./test/test
jar -cvf test_framework.war .
cp test_framework.war /home/fanilo/Documents/L2/apache-tomcat-10.0.22/webapps
