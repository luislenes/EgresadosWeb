/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoHistorialDeEncuestas;
import com.egresados.model.HistorialDeEncuestas;
import com.egresados.model.Usuario;
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
@WebServlet(name = "PollHistory", urlPatterns = {"/historialEncuesta"})
public class PollHistory extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String codigo = ((Usuario)req.getSession().getAttribute("usuario")).getCodigo();
        String codigoEn = req.getParameter("codigoEn");
        DaoHistorialDeEncuestas instance = DaoHistorialDeEncuestas.getInstance();

        try {
            if (codigo != null && codigoEn != null) {
                HistorialDeEncuestas historial = instance.find(codigo,codigoEn);

                JSONObject json = convert(historial);

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            } else if(codigo != null){
                List<HistorialDeEncuestas> historial = instance.list(codigo);
                JSONArray json = new JSONArray();

                for (HistorialDeEncuestas item : historial) {
                    json.add(convert(item));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }else{
                List<HistorialDeEncuestas> historial = instance.list();
                JSONArray json = new JSONArray();

                for (HistorialDeEncuestas item : historial) {
                    json.add(convert(item));
                }

                try (PrintWriter out = resp.getWriter()) {
                    out.print(json.toString());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Poll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JSONObject convert(HistorialDeEncuestas historial) throws SQLException {
        JSONObject json = new JSONObject();

        json.put("poll", historial.getCodigo());
        json.put("graduates", historial.getEgresado().getCodigo());
        json.put("date", historial.getFecha().toString().substring(0, 10));
        json.put("namePoll", DaoEncuesta.getInstance().find(historial.getCodigo()).getNombre());

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
