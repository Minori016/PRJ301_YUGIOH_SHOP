/*
         * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
         * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CardDTO;
import model.UserDTO;
import model.UserDAO;
import utils.EmailUtils;


@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String txtUsername = request.getParameter("user");
        String txtPassword = request.getParameter("pass");

        UserDAO userDAO = new UserDAO();
        String url = "";

        url = userDAO.login(txtUsername, txtPassword)
                ? "home.jsp" : "login.jsp";
        UserDTO user = null;

        String msg = "";
        if (!userDAO.login(txtUsername, txtPassword)) {
            msg = "Username or Password incorrect";
        } else {
            model.CardDAO cardDAO = new model.CardDAO();
            model.SetDAO setDAO = new model.SetDAO();

            java.util.List<model.CardDTO> listC = cardDAO.getAllCard();
            java.util.List<model.SetDTO> listCater = setDAO.getAllSet();
            CardDTO staffPick = cardDAO.getMostExpensiveCard();

            request.setAttribute("listC", listC);
            request.setAttribute("listCater", listCater);
            request.setAttribute("p", staffPick);

            url = "home.jsp"; // <-- đúng file JSP bạn muốn
            user = userDAO.getUserById(txtUsername);
              if (user == null) {
        user = userDAO.getUserByEmail(txtUsername);
    }
        }

        HttpSession session = request.getSession();

        session.setAttribute("user", user);
        request.setAttribute("username", txtUsername);
        request.setAttribute("msg", msg);
        request.getRequestDispatcher(url).forward(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("login.jsp");
    }

    private void processSearchUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("txtName");
        UserDAO userDAO = new UserDAO();
        ArrayList<UserDTO> listOfUsers = new ArrayList<>();
        if (keyword == null || keyword.trim().length() == 0) {
            //            listOfUsers = userDAO.getAllUsers();
        } else {
            //            listOfUsers = userDAO.getAllUsersByName(keyword);
        }
        request.setAttribute("listOfUsers", listOfUsers);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private void processAddUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String txtUsername = request.getParameter("txtUsername");
        String txtPassword = request.getParameter("txtPassword");
        String txtFullName = request.getParameter("txtFullName");
        String txtRole = request.getParameter("txtRole");
        String txtStatus = request.getParameter("txtStatus");
        boolean status = (txtStatus != null && txtStatus.equals("1")) ? true : false;

        UserDTO user = new UserDTO(txtUsername, txtRole, txtPassword, txtFullName, txtRole, status, txtRole, LocalDateTime.MIN);

        String error_userName = "";
        String error_password = "";
        String error_fullName = "";
        String error_role = "";

        boolean hasError = false;

        if (txtUsername == null || txtUsername.trim().isEmpty()) {
            error_userName = "Username can't left empty";
            hasError = true;
        } else {
            if (userDAO.getUserById(txtUsername) != null) {
                error_userName = "Username is already existed!";
                hasError = true;
            }
        }

        String regex = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$";
        if (!txtPassword.matches(regex) || txtPassword == null) {
            error_password = "Password must have atleast 8 letters, at least 1 special letter!";
            hasError = true;
        }

        if (txtFullName == null || txtFullName.trim().isEmpty()) {
            error_fullName = "Full Name can't left empty";
            hasError = true;
        }

        if (txtRole == null || txtRole.trim().isEmpty()) {
            error_role = "Role can't left empty";
            hasError = true;
        }

        String error = "";
        if (!hasError && !userDAO.insert(user)) {
            error = "Can't add new User!";
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("u", user);
            request.setAttribute("error_userName", error_userName);
            request.setAttribute("error_password", error_password);
            request.setAttribute("error_fullName", error_fullName);
            request.setAttribute("error_role", error_role);
            request.setAttribute("error", error);
            request.getRequestDispatcher("userForm.jsp").forward(request, response);
        }

        processSearchUser(request, response);
    }

    private void processCallUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String username = request.getParameter("username");
        UserDTO user = userDAO.getUserById(username);
        if (user != null) {
            request.setAttribute("update", true);
            request.setAttribute("u", user);
        }
        request.getRequestDispatcher("userForm.jsp").forward(request, response);
    }

    private void processOurCollection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        model.CardDAO cardDAO = new model.CardDAO();
        model.SetDAO setDAO = new model.SetDAO();

        java.util.List<model.CardDTO> listC = cardDAO.getAllCard();
        java.util.List<model.SetDTO> listCater = setDAO.getAllSet();
        CardDTO staffPick = cardDAO.getMostExpensiveCard();

        request.setAttribute("listC", listC);
        request.setAttribute("listCater", listCater);
        request.setAttribute("p", staffPick);

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
    private void processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "login.jsp"; // Trang mặc định nếu có lỗi
        try {
            String username = request.getParameter("user");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("pass");
            String rePassword = request.getParameter("repass");
            
            UserDAO userDAO = new UserDAO();
            String msg = "";
            boolean hasError = false;

            // 1. Kiểm tra mật khẩu có khớp không
            if (!password.equals(rePassword)) {
                msg = "Passwords do not match!";
                hasError = true;
            } 
            // 2. Kiểm tra Username đã tồn tại chưa
            else if (userDAO.getUserById(username) != null) {
                msg = "Username '" + username + "' already exists!";
                hasError = true;
            }
            // 3. Kiểm tra Email đã tồn tại chưa
            else if (userDAO.getUserByEmail(email) != null) {
                msg = "Email '" + email + "' already registered!";
                hasError = true;
            }

            if (hasError) {
                request.setAttribute("msg", msg);
                // Giữ lại dữ liệu người dùng đã nhập để họ không phải gõ lại
                request.setAttribute("username", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
            } else {
                // Tạo DTO và insert vào DB
                // Gán role "USER" và status "true" (hoạt động)
                // Bạn có thể đổi "USER" thành "US" nếu CSDL của bạn quy định vậy
                UserDTO newUser = new UserDTO(username, email, password, fullName, "", true, "USER", java.time.LocalDateTime.now());
                
                // Phương thức insert trong UserDAO sẽ tự động hash mật khẩu
                boolean checkInsert = userDAO.insert(newUser); 
                
                if (checkInsert) {
                    
                    try {
                   
                    String toEmail = email; 
                    
                    // Gọi hàm sendRegistrationEmail đã cung cấp
                   
                    EmailUtils.sendRegistrationEmail(toEmail, fullName, username);
                    
                } catch (Exception e) {
                    log("Error sending registration email: " + e.getMessage());
                    // Không cần chặn người dùng nếu gửi mail lỗi, chỉ cần log lại
                    e.printStackTrace();
                }
                    
                    
                    // Dùng sendRedirect để xóa dữ liệu form sau khi đăng ký thành công
                    // và hiển thị msg trên trang login
                    response.sendRedirect("login.jsp?msg=Registration successful! Please sign in.");
                    return; // Dừng thực thi sau khi redirect
                } else {
                    request.setAttribute("msg", "An error occurred during registration. Please try again.");
                }
            }
        } catch (Exception e) {
            log("Error at processRegister: " + e.toString());
            request.setAttribute("msg", "An unexpected error occurred.");
            e.printStackTrace(); // In lỗi ra console server
        }
        
        // Chỉ forward nếu có lỗi
        request.getRequestDispatcher(url).forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String txtAction = request.getParameter("txtAction");

        if (txtAction == null) {
            txtAction = "login";
        }

        if (txtAction.equals("login")) {
            processLogin(request, response);
        } else if (txtAction.equals("logout")) {
            processLogout(request, response);
        } else if (txtAction.equals("searchUser")) {
            processSearchUser(request, response);
        } else if (txtAction.equals("addUser")) {
            processAddUser(request, response);
        } else if (txtAction.equals("callUpdateUser")) {
            processCallUpdateUser(request, response);
        } else if (txtAction.equals("ourCollection")) {
            processOurCollection(request, response);
        }
        else if (txtAction.equals("signup")) {
            processRegister(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
