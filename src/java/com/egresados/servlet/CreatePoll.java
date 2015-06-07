/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoOpcion;
import com.egresados.dao.DaoPregunta;
import com.egresados.model.Administrador;
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
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Gustavo Pacheco
 */
@WebServlet(name = "CreatePoll", urlPatterns = {"/addPoll"})
public class CreatePoll extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        /*
         Este json va a estar conformado de la siguiente manera
         {
         message: -> mensaje de respuesta
         icon: -> icono correspondiente a la respuesta
         success: -> respuesta del servlet(IMPORTANTE: este atributo no aparecera si el el estado es diferente a SUCCESS)
         state: -> estado de la respuesta, tipos de estados (SUCCESS, ERROR) hasta ahora son esos dos
         }
         */
        JSONObject messageResponse = new JSONObject();
        
        HttpSession session = request.getSession(false);

        if (session != null) {
            if (session.getAttribute("usuario") instanceof Administrador) {
                Administrador admin = (Administrador) session.getAttribute("usuario");

                String jsonString = request.getParameter("doc");
                if (jsonString != null) {
                    JSONParser parser = new JSONParser();
                    try {
                        Object temp = parser.parse(jsonString);
                        if (temp instanceof JSONObject) {
                            JSONObject json = (JSONObject) temp;

                            Encuesta encuesta = new Encuesta(admin, json.get("name").toString());

                            JSONArray questions = (JSONArray) json.get("questions");
                            for (Object item : questions) {
                                JSONObject question = (JSONObject) item;

                                Pregunta pregunta = new Pregunta(
                                        question.get("content").toString(),
                                        encuesta.getCodigo(),
                                        Boolean.parseBoolean(question.get("multi").toString())
                                );

                                JSONArray options = (JSONArray) question.get("options");
                                for (Object o : options) {
                                    JSONObject option = (JSONObject) o;

                                    Opcion opcion = new Opcion(
                                            encuesta.getCodigo(),
                                            pregunta.getCodigo(),
                                            option.get("option").toString()
                                    );

                                    pregunta.addOpcion(opcion);
                                }

                                encuesta.addPregunta(pregunta);
                            }

                            DaoEncuesta.getInstance().insert(encuesta);
                            for (Pregunta pregunta : encuesta.getPreguntas()) {
                                DaoPregunta.getInstance().insert(pregunta);
                                for (Opcion opcion : pregunta.getOpciones()) {
                                    DaoOpcion.getInstance().insert(opcion);
                                }
                            }

                            messageResponse.put("message", "Se ha agregado la encuesta perfectamente!");
                            messageResponse.put("icon", "icon-good");
                            messageResponse.put("state", "success");
                            messageResponse.put("success", json);

                        } else {
                            messageResponse.put("message", "Error al parsear los parametros obtenidos.");
                            messageResponse.put("icon", "icon-error");
                            messageResponse.put("state", "error");
                        }
                    } catch (ParseException | SQLException ex) {
                        Logger.getLogger(CreatePoll.class.getName()).log(Level.SEVERE, null, ex);
                        messageResponse.put("message", ex.getMessage());
                        messageResponse.put("icon", "icon-error");
                        messageResponse.put("state", "error");
                    }
                } else {
                    messageResponse.put("message", "Parametros de entrada no reconocidos.");
                    messageResponse.put("icon", "icon-error");
                    messageResponse.put("state", "error");
                }
            } else {
                messageResponse.put("message", "Para acceder a esta operación debes ser un usuario administrador.");
                messageResponse.put("icon", "icon-error");
                messageResponse.put("state", "error");
            }
        } else {
            messageResponse.put("message", "Para acceder a esta operación necesitas estas loggeado.");
            messageResponse.put("icon", "icon-error");
            messageResponse.put("state", "error");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(messageResponse.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
