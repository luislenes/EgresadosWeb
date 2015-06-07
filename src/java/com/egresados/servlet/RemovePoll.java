/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.model.Administrador;
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
@WebServlet(name = "EliminarEncuesta", urlPatterns = {"/removePoll"})
public class RemovePoll extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject();

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Administrador) {
                
                String codigo = request.getParameter("code");
                
                if (codigo != null) {
                    try {
                        boolean delete = DaoEncuesta.getInstance().delete(codigo);
                        if (delete) {
                            json.put("message", "Se ha eliminado perfectamente la encuesta!");
                            json.put("icon", "icon-good");
                            json.put("state", "success");
                            json.put("success", codigo);
                        } else {
                            json.put("message", "Error en la conexión, no se "
                                    + "pudo establecer una conexion con la base "
                                    + "de datos y la operación colapso.");
                            json.put("icon", "icon-error");
                            json.put("state", "error");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    json.put("message", "Parametros no recibidos");
                    json.put("icon", "icon-error");
                    json.put("state", "error");
                }
            } else {
                json.put("message", "No puedes acceder a estas operaciones si "
                        + "no eres un administrador");
                json.put("icon", "icon-error");
                json.put("state", "error");
            }
        } else {
            json.put("message", "No puedes acceder a estas operaciones si no"
                    + " has iniciado sesión");
            json.put("icon", "icon-error");
            json.put("state", "error");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        }
    }

}
