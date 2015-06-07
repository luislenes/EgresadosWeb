/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoAdministrador;
import com.egresados.dao.DaoEgresado;
import com.egresados.dao.DaoInvitado;
import com.egresados.dao.DaoUsuario;
import com.egresados.model.TipoDeUsuario;
import com.egresados.model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "AccessControl", urlPatterns = {"/login"})
public class AccessControl extends HttpServlet {

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
        
        String userName = request.getParameter("txt-user");
        String password = request.getParameter("txt-password");
        
        DaoUsuario control = DaoUsuario.getInstance();
        try {
            if (control.access(userName, password)) {
                HttpSession session = request.getSession();
                
                if (!session.isNew()) {
                    session.invalidate();
                    session = request.getSession();
                }
                
                Usuario temp = control.find(userName);
                Usuario user = null;
                
                if (temp.getTipo() == TipoDeUsuario.ADMINISTRADOR) {
                    user = DaoAdministrador.getInstance().find(userName);
                } else if (temp.getTipo() == TipoDeUsuario.EGRESADO) {
                    user = DaoEgresado.getInstance().find(userName);
                } else if (temp.getTipo() == TipoDeUsuario.INVITADO) {
                    user = DaoInvitado.getInstance().find(userName);
                }
                
                if (user == null) {
                    response.sendRedirect("404nofound.html");
                }
                
                session.setAttribute("usuario", user);
                
                response.sendRedirect("./");
            } else {
                response.sendRedirect("nologin.jsp?username=" + userName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccessControl.class.getName()).log(Level.SEVERE, null, ex);
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
