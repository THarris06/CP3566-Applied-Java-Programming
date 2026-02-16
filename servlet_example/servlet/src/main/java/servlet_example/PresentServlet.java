package servlet_example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = {"/present", "/present/"},
    initParams = {
        @WebInitParam(name = "param1", value = "Some string"),
        @WebInitParam(name = "param2", value = "Some other config string"),
    }
)
public class PresentServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher presentFormDis = req.getRequestDispatcher("/present_form");
        RequestDispatcher getUserDis = req.getRequestDispatcher("/present_form");
        RequestDispatcher setUserStatus  =req.getRequestDispatcher("/set_user_status");
        ServletConfig config = getServletConfig();
        PrintWriter out = res.getWriter();
        out.println("<html><head></head></html>");
        setUserStatus.include(req, res);
        presentFormDis.include(req, res);
        getUserDis.include(req, res);
        out.println("</body></html>");
        out.println("<!-- Comment added by PresentServlet with:");
        out.println("   param1:" + config.getInitParameter("param1"));
        out.println("   param2:" + config.getInitParameter("param2"));
        out.println("-->");
    }
}
