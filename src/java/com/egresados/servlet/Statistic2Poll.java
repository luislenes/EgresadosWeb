/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoHistorialDeEncuestas;
import com.egresados.dao.DaoRespuesta;
import com.egresados.model.Encuesta;
import com.egresados.model.Opcion;
import com.egresados.model.Pregunta;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Gustavo Pacheco
 */
@WebServlet(name = "Statistic2Poll", urlPatterns = {"/estadisticas2"})
public class Statistic2Poll extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        String code = req.getParameter("code");
        
        if (code != null) {
            try {
                int nResponse = DaoHistorialDeEncuestas.getInstance().countHistoryByPoll(code);
                
                Encuesta find = DaoEncuesta.getInstance().find(code);
                
                JSONObject json = new JSONObject();
                json.put("code", find.getCodigo());
                json.put("name", find.getNombre());
                
                JSONArray questions = new JSONArray();
                for (Pregunta pregunta : find.getPreguntas()) {
                    JSONObject question = new JSONObject();
                    question.put("question", pregunta.getEnunciado());
                    JSONArray options = new JSONArray();
                    for (Opcion opcion : pregunta.getOpciones()) {
                        JSONObject option = new JSONObject();
                        option.put("option", opcion.getCuerpo());
                        int nOption = DaoRespuesta.getInstance().countAnswerByCodeOption(opcion.getCodigo());
                        option.put("per", (nOption / nResponse * 100) + "%");
                        options.add(option);
                    }
                    question.put("options", options);
                    questions.add(question);
                }
                json.put("questions", questions);
                
                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            } catch (SQLException ex) {
                Logger.getLogger(Statistic2Poll.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new NullPointerException("no se recibio ningun parametro");
        }
    }

}
