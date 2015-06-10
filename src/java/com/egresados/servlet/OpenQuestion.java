/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoPregunta;
import com.egresados.dao.DaoRespuesta;
import com.egresados.model.Pregunta;
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
@WebServlet(name = "OpenQuestion", urlPatterns = {"/preguntasAbiertas"})
public class OpenQuestion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String codigo = req.getParameter("codigo");
        DaoPregunta instance = DaoPregunta.getInstance();

        try {
            if(codigo != null){
                List<Pregunta> preguntas = instance.listByCode(codigo);
                List<Respuesta> respuestas = DaoRespuesta.getInstance().list(codigo);
                JSONArray json = new JSONArray();

                for (Pregunta item : preguntas) {
                    json.add(convert(item, respuestas));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }else{
                List<Pregunta> preguntas = instance.list();
                List<Respuesta> respuestas = DaoRespuesta.getInstance().list();
                JSONArray json = new JSONArray();

                for (Pregunta item : preguntas) {
                    json.add(convert(item, respuestas));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JSONObject convert(Pregunta pregunta, List<Respuesta> respuestas) throws SQLException {
        JSONObject json = new JSONObject();
        
        int r = 0;
        
        for (Respuesta respuesta : respuestas) {
            if (true) {
                
            }
        }

//        json.put("poll", historial.getCodigo());
//        json.put("graduates", historial.getEgresado().getCodigo());
//        json.put("date", historial.getFecha().toString().substring(0, 10));
//        json.put("namePoll", DaoEncuesta.getInstance().find(historial.getCodigo()).getNombre());

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
    }

}
