jar=$CLASSPATH:/home/fanilo/gson-2.8.2.jar

#compile du framework
javac -cp $jar -parameters -d ./webFramework/bin ./webFramework/src/etu2028/framework/*java

#Mampiditra anle bin ao anaty .jar files
cd ./webFramework/bin
jar -cvf ../../framework.jar ./etu2028

cd ../../
