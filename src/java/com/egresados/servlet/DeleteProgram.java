/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoPrograma;
import com.egresados.model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gustavo Pacheco
 */
@WebServlet(name = "DeleteProgram", urlPatterns = {"/RemoveProgram"})
public class DeleteProgram extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Usuario) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                 if ("admin".equals(usuario.getCodigo())) {
                    String id = request.getParameter("code");
                     if (id.matches("[0-9]*")) {
                        try {
                            boolean delete = DaoPrograma.getInstance().delete(id);
                            
                            if (delete) {
                                response.sendRedirect("program.jsp?result=remove");
                            } else {
                                response.sendRedirect("program.jsp?result=badRemove");
                            }
                        } catch (SQLException ex) {
//                            Logger.getLogger(DeleteProgram.class.getName()).log(Level.SEVERE, null, ex);
                            response.sendRedirect("404nofound.html");
                        }
                     } else {
                         response.sendRedirect("404nofound.html");
                     }
                } else {
                     response.sendRedirect("program.jsp?adv2=true");
                }
            } else {
                response.sendRedirect("404nofound.html");
            }
        } else {
            response.sendRedirect("404nofound.html");
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
        response.sendRedirect("404nofound.html");
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
