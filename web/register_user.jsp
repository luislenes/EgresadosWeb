<%@page import="com.egresados.model.TipoDeUsuario"%>
<%@page import="com.egresados.model.Usuario"%>
<%@page import="com.egresados.model.Persona"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.egresados.dao.DaoInvitado"%>
<%@page import="com.egresados.model.Invitado"%>
<%@page import="com.egresados.dao.DaoEgresado"%>
<%@page import="com.egresados.model.Egresado"%>
<%@page import="com.egresados.dao.DaoAdministrador"%>
<%@page import="com.egresados.model.Administrador"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect("./login.jsp");
    }

    String type = "administrador";
    if (request.getParameter("type") != null) {
        type = request.getParameter("type").toLowerCase();
    }

    String result = null;
    if (request.getParameter("result") != null) {
        result = request.getParameter("result");
    }

    Usuario usuario = (Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Plataforma de egresados de la Universidad de Cartagena</title>
        <link rel="stylesheet" href="css/components.css">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/user.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="js/action_registeruser.js"></script>
        <script src="js/action-message.js"></script>
    </head>
    <body>
        <header>
            <section>
                <h1><a href="./">Plataforma de Egresados</a></h1>
            </section>
        </header>
        <div id="container">
            <section class="profile">
                <%
                    String fullname = "";
                    String email = "";
                    if (usuario instanceof Persona) {
                        Persona p = (Persona) usuario;
                        fullname = p.getNombreCompleto();
                        email = p.getCorreo();
                    } else if (usuario instanceof Administrador) {
                        Administrador admin = (Administrador) usuario;
                        fullname = admin.getNombre();
                        email = admin.getCorreo();
                    }
                %>
                <div class="c-profile">
                    <h2 class="user-name"><%= fullname.toLowerCase()%></h2>
                    <span>Usuario: <%= usuario != null ? usuario.getCodigo() : "no code"%></span>
                    <span><%= email%></span>
                </div>
                <div class="logout right">
                    <form action="logout" method="POST" accept-charset="utf-8">
                        <input type="submit" value="Logout" title="cerrar sesión" class="btn btn-logout">
                    </form>
                </div>
            </section><section class="menu">
                <nav>
                    <ul>
                        <a href="edit_profile.jsp"><li class="item">Editar Información</li></a>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="program.jsp"><li class="item">Programas Academicos</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="register_user.jsp"><li class="item">Registro de Usuarios</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="register_encuestas.jsp"><li class="item">Registro de Encuestas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="close.jsp"><li class="item">Cerrar Respuestas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="estadisticas.jsp"><li class="item">Ver estadisticas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %>
                        <a href="encuestas.jsp"><li class="item">Encuestas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %>
                        <a href="historial.jsp"><li class="item">Historial de Encuestas</li></a>
                        <% } %>
                    </ul>
                </nav>
            </section><section class="content">
                <h2>Usuarios</h2>
                <%
                    if (result != null) {
                %>
                <span class="message">
                    <div class="<%
                        switch (result) {
                            case "add":
                                out.print("icon-good");
                                break;
                            case "remove":
                                out.print("icon-delete");
                                break;
                            case "badRemove":
                                out.print("icon-sad2");
                                break;
                            case "badAdd":
                                out.print("icon-sad2");
                                break;
                            default:
                                throw new AssertionError();
                        }
                         %>"></div>
                    <div>
                        <h4></h4>
                        <p>
                            <%
                                switch (result) {
                                    case "add":
                                        out.print("¡Se ha agregado un nuevo usuario!");
                                        break;
                                    case "remove":
                                        out.print("¡Se ha eliminado un usuario perfectamente!");
                                        break;
                                    case "badAdd":
                                        out.print("¡Lo sentimos!, no pudimos registrar el nuevo usuario.");
                                        break;
                                    case "badRemove":
                                        out.print("¡Lo sentimos!, no pudimos eliminar el usuario especificado.");
                                        break;
                                    default:
                                        throw new AssertionError();
                                }
                            %>
                        </p>
                    </div>
                    <button onclick="closeMessage()" class="btn btn-close icon-error right" title="Cerrar"></button>
                </span>
                <%
                    }
                %>
                <section>
                    <p>Para agregar nuevos usuarios dar en el siguiente boton.</p>
                    <a href="add_user.jsp" class="btn btn-new-user">Nuevo Usuario</a>
                </section>
                <section class="view-table">
                    <p>En la siguiente tabla se mostraran los usuarios de tipo <span><%=type.toLowerCase()%></span> registrados.</p>
                    <p>Tipo de usuario a mostrar:</p>
                    <form id="form-typeuser" method="GET">
                        <select id="slt-type" name="type" title="selecctione el tipo de usuario que desea ver." onchange="changeTypeUser()">
                            <option value="administrador">ADMINISTRADOR</option>
                            <option value="egresado">EGRESADO</option>
                            <option value="invitado">INVITADO</option>
                        </select>
                    </form>
                    <table>
                        <thead>
                            <tr>
                                <%
                                    if ("administrador".equals(type)) {
                                        out.println("<td>Codigo</td>");
                                        out.println("<td>Nombre Completo</td>");
                                        out.println("<td>Correo</td>");
                                        out.println("<td></td>");
                                    } else if ("egresado".equals(type)) {
                                        out.println("<td>Codigo</td>");
                                        out.println("<td>Nombre Completo</td>");
//                                        out.println("<td>Correo</td>");
//                                        out.println("<td>Teléfono</td>");
                                        out.println("<td>Programa</td>");
                                        out.println("<td></td>");
                                    } else if ("invitado".equals(type)) {
                                        out.println("<td>Codigo</td>");
                                        out.println("<td>Nombre Completo</td>");
//                                        out.println("<td>Correo</td>");
//                                        out.println("<td>Teléfono</td>");
                                        out.println("<td>Empresa</td>");
                                        out.println("<td></td>");
                                    } else {
                                        response.sendRedirect("404nofound.html");
                                    }
                                %>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    if ("administrador".equals(type)) {
                                        List<Administrador> admins = DaoAdministrador.getInstance().list();

                                        for (Administrador a : admins) {
                                            out.println("<tr>");
                                            out.println("<td class='user-code'>" + a.getCodigo() + "</td>");
                                            out.println("<td class='user-name'>" + a.getNombre().toLowerCase() + "</td>");
                                            out.println("<td class='user-mail'>" + a.getCorreo() + "</td>");
                                            out.println("<td class='user-button'>");
                                            out.println("<form class='btn-delete'>");
                                            out.println("<input type='hidden' name='code' value='" + a.getCodigo() + "'>");
                                            out.println("<input type='submit' value='Eliminar' title='Eliminar Administrador'>");
                                            out.println("<form>");
                                            out.println("</form>");
                                            out.println("</td>");
                                            out.println("</tr>");
                                        }
                                    } else if ("egresado".equals(type)) {
                                        List<Egresado> egresados = DaoEgresado.getInstance().list();

                                        for (Egresado e : egresados) {
                                            out.println("<tr>");
                                            out.println("<td class='user-code'>" + e.getCodigo() + "</td>");
                                            out.println("<td class='user-name'>" + e.getNombreCompletoAbreviado().toLowerCase() + "</td>");
//                                            out.println("<td class='user-mail'>" + e.getCorreo() + "</td>");
//                                            out.println("<td class='user-phone'>" + e.getTelefono() + "</td>");
                                            out.println("<td class='user-prog'>" + e.getPrograma().toString().toLowerCase() + "</td>");
                                            out.println("<td class='user-button'>");
                                            out.println("<form class='btn-delete'>");
                                            out.println("<input type='hidden' name='code' value='" + e.getCodigo() + "'>");
                                            out.println("<input type='submit' value='Eliminar' title='Eliminar Egresado'>");
                                            out.println("<form>");
                                            out.println("</form>");
                                            out.println("</td>");
                                            out.println("</tr>");
                                        }
                                    } else if ("invitado".equals(type)) {
                                        List<Invitado> invitados = DaoInvitado.getInstance().list();

                                        for (Invitado i : invitados) {
                                            out.println("<tr>");
                                            out.println("<td class='user-code'>" + i.getCodigo() + "</td>");
                                            out.println("<td class='user-name'>" + i.getNombreCompletoAbreviado().toLowerCase() + "</td>");
//                                            out.println("<td class='user-mail'>" + i.getCorreo() + "</td>");
//                                            out.println("<td class='user-phone'>" + i.getTelefono() + "</td>");
                                            out.println("<td class='user-business'>" + i.getEmpresa().toLowerCase() + "</td>");
                                            out.println("<td class='user-button'>");
                                            out.println("<form class='btn-delete'>");
                                            out.println("<input type='hidden' name='code' value='" + i.getCodigo() + "'>");
                                            out.println("<input type='submit' value='Eliminar' title='Eliminar Invitado'>");
                                            out.println("<form>");
                                            out.println("</form>");
                                            out.println("</td>");
                                            out.println("</tr>");
                                        }
                                    } else {
                                        response.sendRedirect("404nofound.html");
                                    }
                                } catch (SQLException ex) {
                                    response.sendRedirect("404nofound.html");
                                }
                            %>
                        </tbody>
                    </table>
                    <div class="footer-table-page">
                        <a href=""><span>anterior</span></a>
                        <a href=""><span>1</span></a>
                        <a href=""><span>2</span></a>
                        <a href=""><span class="selected">3</span></a>
                        <a href=""><span>4</span></a>
                        <a href=""><span>5</span></a>
                        <a href=""><span>siguiente</span></a>
                    </div>
                </section>
            </section>
        </div>
        <footer>
            <nav>
                <a href="">item1</a>
                <a href="">item2</a>
                <a href="">item3</a>
                <a href="">item4</a>
                <a href="">item5</a>
            </nav>
            <section>2015 &copy; Todos los derechos reservados.</section>
        </footer>
    </body>
</html>
