/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.model.Encuesta;
import com.egresados.model.Opcion;
import com.egresados.model.Pregunta;
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
 * @author Gustavo Pacheco
 */
@WebServlet(name = "Poll", urlPatterns = {"/encuesta"})
public class Poll extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String codigo = req.getParameter("codigo");
        DaoEncuesta instance = DaoEncuesta.getInstance();

        try {
            if (codigo != null) {
                Encuesta encuesta = instance.find(codigo);

                JSONObject json = convert(encuesta);

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            } else {
                List<Encuesta> encuestas = instance.list();
                JSONArray json = new JSONArray();

                for (Encuesta encuesta : encuestas) {
                    json.add(convert(encuesta));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JSONObject convert(Encuesta encuesta) {
        JSONObject json = new JSONObject();

        json.put("code", encuesta.getCodigo());
        json.put("name", encuesta.getNombre());

        JSONArray questions = new JSONArray();
        for (Pregunta pregunta : encuesta.getPreguntas()) {
            JSONObject question = new JSONObject();

            question.put("code", pregunta.getCodigo());
            question.put("body", pregunta.getEnunciado());

            JSONArray options = new JSONArray();
            for (Opcion opcion : pregunta.getOpciones()) {
                JSONObject option = new JSONObject();

                option.put("code", opcion.getCodigo());
                option.put("body", opcion.getCuerpo());
                options.add(option);
            }

            question.put("options", options);
            question.put("multiple", pregunta.isMultiple());
            questions.add(question);
        }

        json.put("questions", questions);
        json.put("creator", encuesta.getCreador().getNombre());
        json.put("date", encuesta.getFechaDeCreacion().toString());
        json.put("enable", encuesta.isHabilitado());

        return json;
    }

}
