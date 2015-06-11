/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoHistorialDeEncuestas;
import com.egresados.dao.DaoUsuario;
import com.egresados.model.Encuesta;
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
@WebServlet(name = "StatisticPoll", urlPatterns = {"/estadisticas"})
public class StatisticPoll extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            JSONArray array = new JSONArray();
            int nUser = DaoUsuario.getInstance().countEgresados();
            List<Encuesta> list = DaoEncuesta.getInstance().list();
            
            for (Encuesta encuesta : list) {
                JSONObject json = new JSONObject();
                json.put("code", encuesta.getCodigo());
                json.put("name", encuesta.getNombre());
                int countHistoryByPoll = DaoHistorialDeEncuestas.getInstance().countHistoryByPoll(encuesta.getCodigo());
                json.put("per", (countHistoryByPoll / nUser * 100) + "%");
                
                array.add(json);
            }
            
            try (PrintWriter out = resp.getWriter()) {
                out.print(array.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatisticPoll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
