/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoHistorialDeEncuestas;
import com.egresados.dao.DaoRespuesta;
import com.egresados.model.Egresado;
import com.egresados.model.HistorialDeEncuestas;
import com.egresados.model.Respuesta;
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
 * @author Luis
 */
@WebServlet(name = "SaveAnswers", urlPatterns = {"/SaveAnswers"})
public class SaveAnswers extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        JSONObject messageResponse = new JSONObject();
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Egresado) {
                Egresado egresado = (Egresado) session.getAttribute("usuario");
                String jsonString = request.getParameter("doc");
                if (jsonString != null) {
                    JSONParser parser = new JSONParser();
                    try {
                        Object temp = parser.parse(jsonString);
                        if (temp instanceof JSONObject) {
                            JSONObject json = (JSONObject) temp;
                            
                            HistorialDeEncuestas historial = new HistorialDeEncuestas((String)json.get("codePoll"), egresado);
                            
                            JSONArray answers = (JSONArray) json.get("answers");
                            for (Object item : answers) {
                                JSONObject answer = (JSONObject) item;
                                
                                Respuesta respuesta;
                                
                                if ((String)answer.get("option")!=null && !"".equals((String)answer.get("option"))) {
                                    respuesta = new Respuesta(
                                            (String) answer.get("codeQuestion"), (String) json.get("codePoll"), (String) answer.get("content"),
                                            egresado.getCodigo(), (String)answer.get("option"));
                                }else{
                                    respuesta = new Respuesta(
                                            (String) answer.get("codeQuestion"), (String) json.get("codePoll"), (String) answer.get("content"),
                                            egresado.getCodigo());
                                }

                                historial.getRespuestas().add(respuesta);
                            }
                            
                            DaoHistorialDeEncuestas.getInstance().insert(historial);
                            for (Respuesta respuesta : historial.getRespuestas()) {
                                DaoRespuesta.getInstance().insert(respuesta);
                            }

                            messageResponse.put("message", "Se ha enviado la encuesta correctamente!");
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
            
                }else {
                    messageResponse.put("message", "Parametros de entrada no reconocidos.");
                    messageResponse.put("icon", "icon-error");
                    messageResponse.put("state", "error");
                }
            }else {
                messageResponse.put("message", "Para acceder a esta operación debes ser un usuario egresado.");
                messageResponse.put("icon", "icon-error");
                messageResponse.put("state", "error");
            }
        }else {
            messageResponse.put("message", "Para acceder a esta operación necesitas estas loggeado.");
            messageResponse.put("icon", "icon-error");
            messageResponse.put("state", "error");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(messageResponse.toString());
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
