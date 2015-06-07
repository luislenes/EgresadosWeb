/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoUsuario;
import com.egresados.model.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

/**
 *
 * @author Gustavo Pacheco
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/updatePassword"})
public class ChangePassword extends HttpServlet {

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

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        JSONObject json = new JSONObject();
        json.put("title", "");

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Usuario) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");

                String actualpass = request.getParameter("txt-actualpass");
                DaoUsuario dao = DaoUsuario.getInstance();

                try {
                    if (dao.access(usuario.getCodigo(), actualpass)) {
                        String newPass = request.getParameter("txt-newpass");
                        String confirmPass = request.getParameter("txt-confirmpass");

                        if (newPass.equals(confirmPass)) {
                            //change password
                            usuario.setPassword(newPass);
                            boolean update = dao.update(usuario);

                            if (update) {
                                json.put("message", "Hemos cambiado la contraseña satisfactoriamente.");
                                json.put("icon", "icon-good");
                            } else {
                                json.put("message", "Lo sentimos, no pudimos realizar los cambios.");
                                json.put("icon", "icon-sad2");
                            }
                        } else {
                            json.put("message", "Las contraseñas no coinciden, intentalo nuevamente.");
                            json.put("icon", "icon-sad2");
                        }
                    } else {
                        json.put("message", "La contraseña actual no es correcta.");
                        json.put("icon", "icon-sad2");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
                    json.put("message", "Ops! un error");
                    json.put("icon", "icon-sad2");
                }
            } else {
                json.put("message", "Error, no se reconoce el tipo de usuario");
                json.put("icon", "icon-sad2");
            }
        } else {
            json.put("message", "Esta acción solo se puede realizar si has iniciado sesión");
            json.put("icon", "icon-sad2");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
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
