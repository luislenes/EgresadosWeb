<%@page import="com.egresados.model.TipoDeUsuario"%>
<%@page import="com.egresados.model.Usuario"%>
<%@page import="com.egresados.model.Administrador"%>
<%@page import="com.egresados.model.Persona"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.ADMINISTRADOR) { %>
                        <a href="program.jsp"><li class="item">Programas Academicos</li></a>
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
                        <a href="historial.jsp"><li class="item select">Historial de Encuestas</li></a>
                        <% } %>
                    </ul>
                </nav>
            </section><section  class="content">
                <!--contenido de la seccion-->
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
