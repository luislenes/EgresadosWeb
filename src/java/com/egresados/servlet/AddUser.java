/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egresados.servlet;

import com.egresados.dao.DaoAdministrador;
import com.egresados.dao.DaoEgresado;
import com.egresados.dao.DaoInvitado;
import com.egresados.dao.DaoPrograma;
import com.egresados.model.Administrador;
import com.egresados.model.Egresado;
import com.egresados.model.Invitado;
import com.egresados.model.PeriodoAcademico;
import com.egresados.model.Programa;
import com.egresados.model.TipoDeUsuario;
import com.egresados.model.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gustavo Pacheco
 */
@WebServlet(name = "AddUser", urlPatterns = {"/RegisterUser"})
public class AddUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("usuario") instanceof Usuario) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");

                if (usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) {
                    try {
                        TipoDeUsuario tipo = TipoDeUsuario.valueOf(request.getParameter("slt-type").toUpperCase());
                        String codigo = request.getParameter("txt-code");

                        if (!codigo.matches("[0-9]*")) {
                            response.sendRedirect("404nofound.html");
                        }

                        boolean save = false;
                        
                        switch (tipo) {
                            case ADMINISTRADOR:
                                String nombreCompleto = new String(request.getParameter("txt-name").getBytes("ISO-8859-1"), "UTF-8");
                                String email = request.getParameter("txt-email");

                                Administrador administrador = new Administrador(nombreCompleto, email, codigo, codigo);

                                save = DaoAdministrador.getInstance().insert(administrador);
                                break;
                            case EGRESADO:
                                String identificacion = request.getParameter("txt-ide");

                                if (!identificacion.matches("[0-9]*")) {
                                    response.sendRedirect("404nofound.html");
                                }

                                String nombre = new String(request.getParameter("txt-name").getBytes("ISO-8859-1"), "UTF-8").toUpperCase();
                                String segundoNombre = new String(request.getParameter("txt-secondname").getBytes("ISO-8859-1"), "UTF-8").toUpperCase();
                                String apellidos = new String(request.getParameter("txt-lastname").getBytes("ISO-8859-1"), "UTF-8").toUpperCase();
                                String correo = request.getParameter("txt-email").toLowerCase();
                                String telefono = request.getParameter("txt-phone");

                                String nombrePrograma = new String(request.getParameter("txt-programa").getBytes("ISO-8859-1"), "UTF-8");
                                Programa programa = DaoPrograma.getInstance().find("nombre", nombrePrograma);

                                int yIngreso = new Integer(request.getParameter("txt-yingreso"));
                                int pIngreso = new Integer(request.getParameter("txt-pingreso"));
                                int ySalida = new Integer(request.getParameter("txt-ysalida"));
                                int pSalida = new Integer(request.getParameter("txt-psalida"));

                                Egresado egresado = new Egresado(
                                        new PeriodoAcademico(yIngreso, pIngreso),
                                        new PeriodoAcademico(ySalida, pSalida),
                                        programa, identificacion,
                                        nombre, segundoNombre, apellidos,
                                        correo, telefono, codigo, identificacion);

                                save = DaoEgresado.getInstance().insert(egresado);
                                break;
                            case INVITADO:
                                identificacion = request.getParameter("txt-ide");
                                nombre = new String(request.getParameter("txt-name").getBytes("ISO-8859-1"), "UTF-8");
                                segundoNombre = new String(request.getParameter("txt-secondname").getBytes("ISO-8859-1"), "UTF-8");
                                apellidos = new String(request.getParameter("txt-lastname").getBytes("ISO-8859-1"), "UTF-8");
                                correo = request.getParameter("txt-email");
                                telefono = request.getParameter("txt-phone");

                                String empresa = request.getParameter("txt-business");

                                Invitado invitado = new Invitado(empresa, identificacion,
                                        nombre, segundoNombre, apellidos, correo, telefono, codigo, identificacion);

                                save = DaoInvitado.getInstance().insert(invitado);
                                break;
                            default:
                                response.sendRedirect("404nofound.html");
                        }

                        if (save) {
                            response.sendRedirect("register_user.jsp?result=add");
                        } else {
                            response.sendRedirect("register_user.jsp?result=sadAdd");
                        }
                    } catch (SQLException ex) {
//                        Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
                        response.sendRedirect("404nofound.html");
                    }
                } else {
                    response.sendRedirect("404nofound.html");
                }
            } else {
                response.sendRedirect("404nofound.html");
            }
        } else {
            response.sendRedirect("404nofound.html");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("404nofound.html");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
