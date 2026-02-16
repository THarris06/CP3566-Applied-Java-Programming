package chatlog;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", value = "/app")
public class HelloServlet extends HttpServlet {
    private String greeting;

    public String getGreeting() {
        return greeting;
    }

    public void init() {
        greeting = "Hello from Servlet";
    }

    public void doGet(
        HttpServletRequest req,
        HttpServletResponse res
    ) throws ServletException, IOException {
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();
        out.println("<html><head></head><body>");
        out.println("<h1>"+ getGreeting() +"</h1>");
        out.println("</body></html>");
    }

    public void destroy(){

    }
}
