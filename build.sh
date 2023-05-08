#compile du framework
javac -parameters -d ./webFramework/bin ./webFramework/src/etu2028/framework/*java

#Mampiditra anle bin ao anaty .jar files
cd ./webFramework/bin
jar -cvf ../../framework.jar ./etu2028

cd ../../
cp framework.jar ./test/test/WEB-INF/lib
