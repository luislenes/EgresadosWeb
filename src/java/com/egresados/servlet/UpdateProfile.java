/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoAdministrador;
import com.egresados.dao.DaoPersona;
import com.egresados.model.Administrador;
import com.egresados.model.Persona;
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
@WebServlet(name = "UpdateProfile", urlPatterns = {"/updateProfile"})
public class UpdateProfile extends HttpServlet {

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
        HttpSession session = request.getSession(false);

        JSONObject json = new JSONObject();
        json.put("title", "");

        if (session != null && session.getAttribute("usuario") != null) {
            if (session.getAttribute("usuario") instanceof Administrador) {
                Administrador admin = (Administrador) session.getAttribute("usuario");

                String name = new String(request.getParameter("txt-name").getBytes("ISO-8859-1"), "UTF-8");
                String mail = request.getParameter("txt-email");

                if (!"".equals(name) && !"".equals(mail)) {
                    admin.setNombre(name);
                    admin.setCorreo(mail);

                    try {
                        boolean update = DaoAdministrador.getInstance().update(admin);

                        if (update) {
                            json.put("message", "Se ha modificado perfectamente los datos del administrador.");
                            json.put("icon", "icon-good");
                        } else {
                            json.put("message", "Lo sentimos!, no pudimos realizar los cambios.");
                            json.put("icon", "icon-sad2");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(UpdateProfile.class.getName()).log(Level.SEVERE, null, ex);
                        json.put("message", ex);
                        json.put("icon", "icon-sad2");
                    }
                } else {
                    json.put("message", "El nombre y correo del administrador no deben estar vacios.");
                    json.put("icon", "icon-sad2");
                }
            } else if (session.getAttribute("usuario") instanceof Persona) {
                Persona persona = (Persona) session.getAttribute("usuario");

                String id = request.getParameter("txt-ide");
                String name = request.getParameter("txt-name");
                String secondname = request.getParameter("txt-secondname");
                String lastname = request.getParameter("txt-lastname");
                String email = request.getParameter("txt-email");
                String phone = request.getParameter("txt-phone");

                persona.setApellidos(lastname);
                persona.setCorreo(email);
                persona.setIdentificador(id);
                persona.setNombre(name);
                persona.setSegundoNombre(secondname);
                persona.setTelefono(phone);

                try {
                    boolean update = DaoPersona.getInstance().update(persona);

                    if (update) {
                        json.put("message", "Se ha modificado perfectamente los datos del usuario.");
                        json.put("icon", "icon-good");
                    } else {
                        json.put("message", "Lo sentimos!, no pudimos realizar los cambios.");
                        json.put("icon", "icon-sad2");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UpdateProfile.class.getName()).log(Level.SEVERE, null, ex);
                    json.put("message", ex);
                    json.put("icon", "icon-sad2");
                }
            } else {
                json.put("message", "Usuario no identificado.");
                json.put("icon", "icon-sad2");
            }
        } else {
            json.put("message", "Para acceder a este componente necesitas iniciar sesión");
            json.put("icon", "icon-sad2");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
