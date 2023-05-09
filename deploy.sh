#Compile des classes du test
mkdir temp temp/WEB-INF temp/WEB-INF/classes temp/WEB-INF/lib
cp framework.jar ./temp/WEB-INF/lib/
cp ./test/web.xml ./temp/WEB-INF
cp ./test/view/*.jsp ./temp/

javac -cp ./temp/WEB-INF/lib/framework.jar -parameters -d ./temp/WEB-INF/classes ./test/src/*java

#archive du test en war et deplacer dans le fichier tomcat webapps
cd temp
jar -cvf fw.war .
cp fw.war /home/fanilo/Documents/L2/apache-tomcat-10.0.22/webapps
cd ../
