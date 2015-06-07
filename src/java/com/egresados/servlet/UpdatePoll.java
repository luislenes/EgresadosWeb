/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.model.Administrador;
import com.egresados.model.Encuesta;
import com.egresados.model.Opcion;
import com.egresados.model.Pregunta;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "modificarEncuesta", urlPatterns = {"/updatePoll"})
public class UpdatePoll extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject();

        HttpSession session = req.getSession(false);
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Administrador) {
                try {
                    String jsonDoc = req.getParameter("doc");
                    JSONParser parser = new JSONParser();
                    JSONObject request = (JSONObject) parser.parse(jsonDoc);

                    //reading object poll
                    String codigo = request.get("code").toString();
                    String nombre = request.get("name").toString();
                    boolean habilitado = Boolean.valueOf(request.get("enable").toString());

                    List<Pregunta> preguntas = new ArrayList<>();
                    JSONArray array1 = (JSONArray) request.get("questions");
                    for (Object item1 : array1) {
                        JSONObject question = (JSONObject) item1;
                        String enunciado = question.get("body").toString();
                        String id = question.get("code").toString();
                        boolean multiple = Boolean.valueOf(question.get("multiple").toString());

                        List<Opcion> opciones = new ArrayList<>();
                        JSONArray array2 = (JSONArray) question.get("options");
                        for (Object item2 : array2) {
                            JSONObject option = (JSONObject) item2;
                            String cuerpo = option.get("body").toString();
                            String code = option.get("code").toString();

                            Opcion opcion = new Opcion();
                            opcion.setCodigo(code);
                            opcion.setCodigoPregunta(id);
                            opcion.setCuerpo(cuerpo);

                            opciones.add(opcion);
                        }

                        Pregunta pregunta = new Pregunta(id, enunciado, opciones, codigo, multiple);
                        preguntas.add(pregunta);
                    }

                    Encuesta encuesta = new Encuesta(codigo, nombre, null, preguntas, null);
                    encuesta.setHabilitado(habilitado);

                    boolean update = DaoEncuesta.getInstance().update(encuesta);
                    if (update) {
                        json.put("message", "Se ha modificado perfectamemte la encuesta!");
                        json.put("success", request);
                        json.put("state", "success");
                        json.put("icon", "icon-good");
                    } else {
                        json.put("message", "Error en la conexión, no se pudo modificar la encuesta, lo sentimos :(");
                        json.put("state", "error");
                        json.put("icon", "icon-error");
                    }
                } catch (ParseException | SQLException ex) {
                    Logger.getLogger(UpdatePoll.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                json.put("message", "Para acceder a esta operacion necesitas ser"
                        + " administrador");
                json.put("state", "error");
                json.put("icon", "icon-error");
            }
        } else {
            json.put("message", "Para acceder a esta operacion necesitar acceder"
                    + " a tu cuenta");
            json.put("state", "error");
            json.put("icon", "icon-error");
        }

        try (PrintWriter out = resp.getWriter()) {
            out.print(json.toString());
        }
    }

}
