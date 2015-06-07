<%@page import="com.egresados.model.TipoDeUsuario"%>
<%@page import="com.egresados.model.Usuario"%>
<%@page import="com.egresados.model.Administrador"%>
<%@page import="com.egresados.model.Persona"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.egresados.dao.DaoPrograma"%>
<%@page import="java.util.List"%>
<%@page import="com.egresados.model.Programa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect("./login.jsp");
    }

    boolean adv1 = false, adv2 = false, adv3 = false;
    String result = null;
    if (request.getParameter("adv1") != null) {
        adv1 = Boolean.parseBoolean(request.getParameter("adv1"));
    }
    if (request.getParameter("adv2") != null) {
        adv2 = Boolean.parseBoolean(request.getParameter("adv2"));
    }
    if (request.getParameter("adv3") != null) {
        adv3 = Boolean.parseBoolean(request.getParameter("adv3"));
    }
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
        <link rel="stylesheet" href="css/program.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="js/action-message.js"></script>
        <script src="js/action_program.js"></script>
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
                        <a href="program.jsp"><li class="item select">Programas Academicos</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="register_user.jsp"><li class="item">Registro de Usuarios</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="register_encuestas.jsp"><li class="item">Registro de Encuestas</li></a>
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
                <h2>Programas Academicos</h2>
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
                                        out.print("¡Se ha guardado perfectamente el programa acádemico!");
                                        break;
                                    case "remove":
                                        out.print("¡Se ha eliminado el programa acádemico perfectamente!");
                                        break;
                                    case "badAdd":
                                        out.print("¡Lo sentimos!, no pudimos agregar el programa acádemico.");
                                        break;
                                    case "badRemove":
                                        out.print("¡Lo sentimos!, no pudimos eliminar el programa acádemico.");
                                        break;
                                    default:
                                        throw new AssertionError();
                                }
                            %>
                        </p>
                    </div>
                    <button onclick="closeMessage()" class="btn btn-close icon-error right" title="Cerrar"></button>
                </span>
                <%                    }
                %>
                <section>
                    <span style="color: red" <%=(adv3 ? "" : "hidden=\"\"")%>>
                        Solo los tipo de usuario administrador pueden agregar programas academicos.
                    </span>
                    <p>Para agregar un programa digite la siguiente información:</p>
                    <form action="RegisterProgram" name="formProgram" method="POST" accept-charset="utf-8">
                        <input type="text" name="txt-id" maxlength="3" placeholder="Codigo de programa" required>
                        <input type="text" name="txt-name" maxlength="45" placeholder="Nombre de programa" required>
                        <input class="btn btn-addprogram" type="submit" value="Agregar" title="agregar programa">
                    </form>
                    <span <%=(adv1 ? "" : "hidden=\"\"")%>>
                        <div class="adv">El codigo digitado en esta casilla debe ser numerico.</div>
                        <div class="adv">El nombre del programa no contiene numeros o caracteres especiales.</div>
                    </span>
                </section>
                <section>
                    <p>En la siguiente tabla se muestran todos los programas academicos registrados actualmente.</p>
                    <span style="color: red" <%=(adv2 ? "" : "hidden=\"\"")%>>
                        El administrador de sistemas es el unico que puede eliminar programas academicos.
                    </span>
                    <table>
                        <thead>
                            <tr>
                                <td>Codigo</td>
                                <td>Nombre del programa</td>
                                <td></td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                try {
                                    List<Programa> list = DaoPrograma.getInstance().list();

                                    for (Programa p : list) {
                                        out.println("<tr>");
                                        out.println("<td class=\"user-codigo\">" + p.getId() + "</td>");
                                        out.println("<td class=\"user-name\">" + p.getNombre().toLowerCase() + "</td>");
                                        out.println("<td class=\"user-button\">");
                                        out.println("<form class='btn-delete' action='RemoveProgram' method='POST' accept-charset='utf-8'>");
                                        out.println("<input type='hidden' name='code' value=" + p.getId() + ">");
                                        out.println("<input type='button' onclick='removeProgram(this)' value='Eliminar' title='eliminar programa'>");
                                        out.println("</form>");
                                        out.println("</tr>");
                                    }
                                } catch (SQLException ex) {
                                    response.sendRedirect("404nofound.html");
                                }
                            %>
                        </tbody>
                    </table>
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
