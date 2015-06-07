<%@page import="com.egresados.model.TipoDeUsuario"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.List"%>
<%@page import="com.egresados.model.Programa"%>
<%@page import="com.egresados.model.Programa"%>
<%@page import="com.egresados.dao.DaoPrograma"%>
<%@page import="com.egresados.model.Administrador"%>
<%@page import="com.egresados.model.Persona"%>
<%@page import="com.egresados.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="404nofound.html" %>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect("./login.jsp");
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
        <link rel="stylesheet" href="css/add_user.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="js/action_adduser.js"></script>
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
                    } else if (usuario instanceof Administrador){
                        Administrador admin = (Administrador) usuario;
                        fullname = admin.getNombre();
                        email = admin.getCorreo();
                    }
                %>
                <div class="c-profile">
                    <h2 class="user-name"><%= fullname.toLowerCase() %></h2>
                    <span>Usuario: <%= usuario != null ? usuario.getCodigo() : "no code"%></span>
                    <span><%= email %></span>
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
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %><a href="program.jsp"><li class="item">Programas Academicos</li></a><% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %><a href="register_user.jsp"><li class="item select">Registro de Usuarios</li></a><% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %><a href="register_encuestas.jsp"><li class="item">Registro de Encuestas</li></a><% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %><a href="encuestas.jsp"><li class="item">Encuestas</li></a><% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %><a href="historial.jsp"><li class="item">Historial de Encuestas</li></a><% } %>
                    </ul>
                </nav>
            </section><section class="content">
                <h2>Nuevo Usuario</h2>
                <p>Para crear un nuevo usuario se debe dar la siguiente informaci&oacute;n.</p>
                <section>
                    <form class="user-form" action="RegisterUser" method="POST" accept-charset="utf-8">
                        <h3>Informaci&oacute;n B&aacute;sica</h3>
                        <label>Que tipo de usuario desea crear:</label>
                        <select id="slt-type" name="slt-type" title="selecctione el tipo de usuario que desea crear.">
                            <option value="administrador">ADMINISTRADOR</option>
                            <option value="egresado">EGRESADO</option>
                            <option value="invitado">INVITADO</option>
                        </select>
                        <label>Codigo de usuario:</label>
                        <input id="txt-code" type="text" name="txt-code" maxlength="12" placeholder="Ejemplo: 0635214987" required>
                        <span id="code-message"></span>
                        <h3>Informaci&oacute;n Personal</h3>
                        <section>
                            <label>Identificaci&oacute;n:</label>
                            <input class="f-personal" type="text" name="txt-ide" maxlength="12" placeholder="Identificación Personal">
                            <label>Nombre:</label>
                            <input id="txt-name" class="f-personal f-admin" type="text" name="txt-name" maxlength="45" placeholder="Primer Nombre">
                            <label>Segundo nombre:</label>
                            <input class="f-personal" type="text" name="txt-secondname" maxlength="45" placeholder="Segundo Nombre">
                        </section>
                        <section>
                            <label>Apellidos:</label>
                            <input class="f-personal" type="text" name="txt-lastname" maxlength="45" placeholder="Apellidos">
                            <label>Correo:</label>
                            <input class="f-personal f-admin" type="email" name="txt-email" maxlength="45" placeholder="example@server.com">
                            <label>Tel&eacute;fono:</label>
                            <input class="f-personal" type="tel" name="txt-phone" maxlength="10" placeholder="Teléfono">
                        </section>
                        <article id="capaEgresado">
                            <h3>Informaci&oacute;n de Egresado</h3>
                            <section>
                                <label>Programa Ac&aacute;demico:</label>
                                <input class="f-egresado" type="list" list="list-programas" name="txt-programa" maxlength="45" placeholder="Programa Acádemico">
                                <datalist id="list-programas">
                                    <%
                                        try {
                                            List<Programa> programas = DaoPrograma.getInstance().list();

                                            for (Programa p : programas) {
                                    %><option value="<%=p.getNombre()%>" /><%
                                            }
                                        } catch (SQLException ex) {
                                            response.sendRedirect("404nofound.html");
                                        }

                                        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
                                    %>
                                </datalist>
                                <label>Año de ingreso:</label>
                                <input class="f-egresado" type="number" name="txt-yingreso" min="1980" max="<%=actualYear%>" value="<%=(actualYear - 5)%>" placeholder="Ejemplo: 2015">
                                <label>Periodo de ingreso:</label>
                                <input class="f-egresado" type="number" name="txt-pingreso" min="1" max="2" value="1" placeholder="Opciones: 1 ó 2">
                            </section>
                            <section>
                                <label>Año de salida:</label>
                                <input class="f-egresado" type="number" name="txt-ysalida" min="1980" max="<%=actualYear%>" value="<%=actualYear%>" placeholder="Ejemplo: 2015">
                                <label>Periodo de salida:</label>
                                <input class="f-egresado" type="number" name="txt-psalida" min="1" max="2" value="1" placeholder="Opciones: 1 ó 2">
                            </section>
                        </article>
                        <article id="capaEmpresa">
                            <h3>Informaci&oacute;n de Empresa</h3>
                            <label>Empresa:</label>
                            <input class="f-invitado" type="text" name="txt-business" maxlength="45" placeholder="Nombre de la empresa">
                        </article>
                        <input class="btn-create" type="submit" value="Crear" title="crear usuario">
                    </form>
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
