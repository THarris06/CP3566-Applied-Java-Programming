package servlet_example;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/set_user_status")
public class SetUserStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uname = req.getParameter("uname");
        Data[] users = (Data[])getServletContext().getAttribute("users");
        int uid = -1;
        for (int i = 0; i<users.length; i++) {
            Data data = users[i];
            if(data.uname.equals(uname)){
                uid = i;
                break;
            }
        }
        if (uid != -1) {
            String statusString = req.getParameter("status");
            Status status = Status.valueOf(statusString);
            users[uid] = users[uid].changeStatus(status);

        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
