/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.services;

import com.egresados.dao.DaoEncuesta;
import com.egresados.dao.DaoUsuario;
import com.egresados.model.Usuario;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

/**
 *
 * @author Gustavo Pacheco
 */
@Path("service")
public class Service {
    
    @GET
    @Path("codeCheckAvailability")
    @Produces(MediaType.APPLICATION_XML)
    public String codeCheckAvailability(@QueryParam("code") String code) {
        if (code == null || "".equals(code)) {
            return "<availability>false</availability>";
        }
        try {
            Usuario user = DaoUsuario.getInstance().find(code);
            return String.format("<availability>%b</availability>", user == null);
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<availability>false</availability>";
    }
    
    @GET
    @Path("getCountPoll")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountPoll() {
        JSONObject response = new JSONObject();
        int count = -1;
        try {
            count = DaoEncuesta.getInstance().count();
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.put("count", count);
        return response.toString();
    }
    
}
