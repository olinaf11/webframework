package etu2028.framework.servlet.webframework;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "*", value = "/*")
public class FrontServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        HttpServletMapping mapping = request.getHttpServletMapping();

        String uri = request.getRequestURI();
        out.println("<html><body>");
        out.println("<h1>"+uri+"</h1>");
        out.println("<h1>"+request.getRequestURL()+"</h1>");
        out.println("<h1>"+request.getQueryString()+"</h1>");
        out.println("<h1>"+mapping.getMatchValue()+"</h1>");
        out.println("<h1>"+mapping.getPattern()+"</h1>");
        out.println("<h1>"+request.getContextPath()+"</h1>");
        out.println("</body></html>");
    }
}
