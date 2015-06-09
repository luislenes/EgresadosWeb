/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoRespuesta;
import com.egresados.model.Respuesta;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Luis
 */
@WebServlet(name = "Answer", urlPatterns = {"/respuesta"})
public class Answer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String codigo = req.getParameter("codigo");
        DaoRespuesta instance = DaoRespuesta.getInstance();

        try {
            if (codigo != null) {
                List<Respuesta> respuestas = instance.list(codigo);

                JSONArray json = new JSONArray();

                for (Respuesta respuesta : respuestas) {
                    json.add(convert(respuesta));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            } else {
                List<Respuesta> respuestas = instance.list();
                JSONArray json = new JSONArray();

                for (Respuesta respuesta : respuestas) {
                    json.add(convert(respuesta));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JSONObject convert(Respuesta respuesta) throws SQLException {
        JSONObject json = new JSONObject();

        json.put("poll", respuesta.getEncuesta());
        json.put("graduates", respuesta.getEgresado());
        json.put("question", respuesta.getPregunta());
        json.put("content", respuesta.getContenido());
        json.put("option", respuesta.getOpcion());

        return json;
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
