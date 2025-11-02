/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CardDAO;
import model.CardDTO;
import model.SetDAO;
import model.SetDTO;

/**
 *
 * @author bi
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

    private void processGetAllCard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            CardDAO cardDAO = new CardDAO();
            List<CardDTO> listCard = cardDAO.getAllCard();
            request.setAttribute("listC", listCard);

            SetDAO setDAO = new SetDAO();
            List<SetDTO> listSet = setDAO.getAllSet();

            request.setAttribute("listCater", listSet);

            request.getRequestDispatcher("home.jsp").forward(request, response);

        } catch (Exception e) {
            // Xử lý lỗi nếu có vấn đề khi truy cập DB
            response.getWriter().println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processGetAllCardBySet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String setID = request.getParameter("setID");
            CardDAO cardDAO = new CardDAO();
            List<CardDTO> listCard = cardDAO.getCardsBySetID(setID);
            request.setAttribute("listC", listCard);

            SetDAO setDAO = new SetDAO();
            List<SetDTO> listSet = setDAO.getAllSet();
            request.setAttribute("listCater", listSet);

            CardDTO staffPick = cardDAO.getMostExpensiveCard();
            request.setAttribute("p", staffPick);
            request.getRequestDispatcher("home.jsp").forward(request, response);

        } catch (Exception e) {
            // Xử lý lỗi nếu có vấn đề khi truy cập DB
            response.getWriter().println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void processViewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String destinationPage = "viewCard.jsp"; // <--- 1. Đã đổi sang file JSP mới
        
        try {
            String cardID = request.getParameter("cardID");
            CardDAO cardDAO = new CardDAO();
            SetDAO setDAO = new SetDAO();

            // 2. Lấy sản phẩm chính (đã bao gồm SetID từ Bước 2)
            CardDTO cardDetail = cardDAO.getCardByID(cardID);
            
            // 3. Lấy Set của lá bài (CHO DÒNG CHỮ ĐỎ)
            SetDTO cardSet = null;
            if (cardDetail != null) {
                // Dùng hàm getSetByID (từ Bước 3)
                cardSet = setDAO.getSetByID(cardDetail.getSetID());
            } else {
                // Xử lý nếu không tìm thấy card
                System.out.println("Card not found with ID: " + cardID);
            }
            
            // 4. Lấy danh sách Categories cho sidebar
            List<SetDTO> listSet = setDAO.getAllSet();
            
            // 5. Lấy sản phẩm cho "Staff's Pick" trong sidebar
            CardDTO staffPick = cardDAO.getMostExpensiveCard();

            // 6. Đặt các thuộc tính để gửi qua viewCard.jsp
            request.setAttribute("p", cardDetail);         // (Card chính)
            request.setAttribute("cardSet", cardSet);      // (Set của card đó)
            request.setAttribute("listCater", listSet);   // (Tất cả Set cho sidebar)
            request.setAttribute("last", staffPick);      // (Staff pick cho sidebar)
            
            request.getRequestDispatcher(destinationPage).forward(request, response);

        } catch (Exception e) {
            response.getWriter().println("An error occurred in ProductController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");

        if (txtAction == null) {
            txtAction = "login";
        }

        if (txtAction.equals("categoryProduct")) {
            processGetAllCardBySet(request, response);
        }
        else if (txtAction.equals("viewDetail")) {
        processViewDetail(request, response);
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
