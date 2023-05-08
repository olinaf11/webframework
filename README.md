# webframework
  
  ### Requierement:
    - JDK 8 or later
    
  ### Steps
    - Put this code in web.xml
    	```
		for i in range(5):
		    print(i)
	```


	```		
		<servlet>
			<servlet-name>FrontServlet</servlet-name>
			<servlet-class>etu2028.framework.servlet.FrontServlet</servlet-class>
			<init-param>
				<param-name>packages</param-name>
				<param-value>YourPackage</param-value>
			</init-param>
		</servlet>
		<servlet-mapping>
			<servlet-name>FrontServlet</servlet-name>
			<url-pattern>/</url-pattern>
		</servlet-mapping>
	```
    
    - Put your models in package YourPackage
    - To access the servlet on your method follow this Exemples:
    ```Java
        import etu2028.framework.annotation.Url;

        @Url(name = "test-test") // bla/insert?a=4&&b=5
        public ModelView test() {
            ModelView modelView = new ModelView("test.jsp");
            return modelView;
        }
    ```
    
