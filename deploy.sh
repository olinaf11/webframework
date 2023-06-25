#all path to use
tomcat=/home/fanilo/Documents/L2/apache-tomcat-10.0.22/webapps
jar=*.jar
jarc=./temp/WEB-INF/lib/framework.jar:./temp/WEB-INF/lib/gson-2.8.2.jar
java=./test/src/*java
webxml=./test/web.xml
jsp=./test/view/*.jsp

#create directory temp
mkdir temp temp/WEB-INF temp/WEB-INF/classes temp/WEB-INF/lib

#copy all necessary in temp
cp $jar ./temp/WEB-INF/lib/
cp $webxml ./temp/WEB-INF
cp $jsp ./temp/

#Compile des classes du test
javac -cp ./temp/WEB-INF/lib/framework.jar -parameters -d ./temp/WEB-INF/classes $java

#archive du test en war et deplacer dans le fichier tomcat webapps
cd temp
jar -cvf fw.war .
cp fw.war $tomcat
cd ../

#remove all temporary files
rm -R temp
