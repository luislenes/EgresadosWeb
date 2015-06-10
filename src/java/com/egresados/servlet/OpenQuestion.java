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

        if (codigo != null) {
            try {
                JSONArray response = new JSONArray();
                List<Pregunta> listQuestions = instance.listByCode(codigo);
                for (Pregunta pregunta : listQuestions) {
                    JSONObject json = new JSONObject();
                    json.put("code", pregunta.getCodigo());
                    json.put("question", pregunta.getEnunciado());
                    
                    DaoRespuesta dao = DaoRespuesta.getInstance();
                    json.put("answers", dao.countAnswerByCodeQuestion(pregunta.getCodigo()));
                    
                    response.add(json);
                }
                
                try (PrintWriter writer = resp.getWriter()) {
                    writer.print(response.toString());
                }
            } catch (SQLException ex) {
                Logger.getLogger(OpenQuestion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new NullPointerException("no se recibio el parametro codigo");
        }
        
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
