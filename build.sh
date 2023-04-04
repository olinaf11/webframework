

#compile du framework
javac -d ./webFramework/bin ./webFramework/src/etu2028/framework/*java

#Mampiditra anle bin ao anaty .jar files
cd ./webFramework/bin
jar -cvf ../../framework.jar ./etu2028

#Compile des classes du test
cd ../../
cp framework.jar ./test/test/WEB-INF/lib
javac -cp ./test/test/WEB-INF/lib/framework.jar -d ./test/test/WEB-INF/classes ./test/src/*java

#archive du test en war et deplacer dans le fichier tomcat webapps
cd ./test/test
jar -cvf test_framework.war .
cp test_framework.war /home/fanilo/Documents/L2/apache-tomcat-10.0.22/webapps