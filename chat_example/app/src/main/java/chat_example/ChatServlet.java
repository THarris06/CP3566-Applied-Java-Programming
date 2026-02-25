package chat_example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    String chatLog;
    private String htmlPart1;
    private String htmlPart2;
    private DateFormat formatter;


    private String getFractionalHTMLString(
        ServletConfig config,
        String path
    ) {
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(
                config.getServletContext().getResourceAsStream(path)
            )
        )) {
            return br.lines().reduce("", String::concat);
        } catch (IOException e) {
            System.err.println("Failed to open " + path);
            System.exit(1);
            return "";
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        chatLog = "";
        htmlPart1 = getFractionalHTMLString(config, "/WEB-INF/chat.p1.html");
        htmlPart2 = getFractionalHTMLString(config, "/WEB-INF/chat.p2.html");
        formatter = DateFormat.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        out.println(htmlPart1);
        out.println(chatLog);
        out.println(htmlPart2);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uname = req.getParameter("username");
        String message = req.getParameter("message");
        String timeStamp = formatter.format(Date.from(Instant.now()));

        String messageHTML = new StringBuilder()
            .append("<p><span class=\"uname\">")
            .append(uname)
            .append(" @ ")
            .append(timeStamp)
            .append("</span>: ")
            .append(message)
            .append("</p>\n")
            .toString();
        synchronized(chatLog) {
            chatLog += messageHTML;
        }

        PrintWriter out = res.getWriter();
        out.println(htmlPart1);
        out.println(chatLog);
        out.println(htmlPart2);
    }

}
