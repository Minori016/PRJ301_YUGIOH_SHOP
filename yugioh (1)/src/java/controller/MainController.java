package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController" ,"/"})
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");
        String url = "login.jsp";

        String[] userActions = {"login", "logout", "searchUser", "addUser", "callUpdateUser", "ourCollection" , "signup"};
        String[] productActions = {"categoryProduct", "deleteProduct","viewDetail"};

        if (Arrays.asList(userActions).contains(txtAction)) {
            url = "UserController";
        } else if (Arrays.asList(productActions).contains(txtAction)) {
            url = "ProductController";
        }
        request.getRequestDispatcher(url).forward(request, response);

    }

    // Các phương thức doGet/doPost giữ nguyên
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
