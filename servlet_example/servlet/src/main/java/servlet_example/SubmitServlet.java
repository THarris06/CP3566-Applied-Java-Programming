package servlet_example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet()
public class SubmitServlet extends HttpServlet{

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        Data[] users = {
            new Data("scotty", Status.OFFLINE),
            new Data("jimmy", Status.OFFLINE),
            new Data("chucky", Status.OFFLINE),
        };
        context.setAttribute("users", users);
    }

    public void doGet(
        HttpServletRequest req,
        HttpServletResponse res
    ) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("   <p>Set User " + req.getParameter("uname") + "'s status to: " + req.getParameter("status") + "</p>");
    }

    public void doPost(
        HttpServletRequest req,
        HttpServletResponse res
    ) throws ServletException, IOException {
        doGet(req, res);
    }

    public void doPut(
        HttpServletRequest req,
        HttpServletResponse res
    ) throws ServletException, IOException {
        doGet(req, res);
    }
}
