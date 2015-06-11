/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoPregunta;
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
 * @author Gustavo Pacheco
 */
@WebServlet(name = "OpenAnswer", urlPatterns = {"/respuestasPorTabular"})
public class OpenAnswer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String codePoll = request.getParameter("poll");
        String codeQuestion = request.getParameter("code");

        try {
            if (codePoll != null && codeQuestion != null) {
                List<Respuesta> list = DaoRespuesta.getInstance().list(codePoll, codeQuestion);
                JSONObject json = new JSONObject();
                json.put("codePoll", codePoll);
                json.put("namePoll", DaoEncuesta.getInstance().find(codePoll).getNombre());
                json.put("codeQuestion", codeQuestion);
                json.put("question", DaoPregunta.getInstance().find(codeQuestion).getEnunciado());

                JSONArray array = new JSONArray();
                for (Respuesta value : list) {
                    JSONObject resp = new JSONObject();
                    resp.put("answer", value.getContenido());
                    array.add(resp);
                }

                json.put("answers", array);

                try (PrintWriter out = response.getWriter()) {
                    out.print(json);
                }
            } else {
                throw new NullPointerException("No se recibieron los parametros, o por lo menos falta uno de los dos.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(OpenAnswer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
