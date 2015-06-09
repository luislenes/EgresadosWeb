<%@page import="com.egresados.model.TipoDeUsuario"%>
<%@page import="com.egresados.model.Administrador"%>
<%@page import="com.egresados.model.Persona"%>
<%@page import="com.egresados.model.Usuario"%>
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
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/edit.css">
        <link rel="stylesheet" href="css/components.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="js/action-message.js"></script>
        <script src="js/action_profile.js"></script>
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
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %>
                        <a href="encuestas.jsp"><li class="item">Encuestas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %>
                        <a href="historial.jsp"><li class="item">Historial de Encuestas</li></a>
                        <% } %>
                    </ul>
                </nav>
            </section><section class="content">
                <h2>Editar Informaci&oacute;n</h2>
                <span class="message">
                    <div class="icon-delete"></div>
                    <div>
                        <h4></h4>
                        <p></p>
                    </div>
                    <button onclick="closeMessage()" class="btn btn-close icon-error right" title="Cerrar"></button>
                </span>
                <article>
                    <p>Puede editar la siguiente informaci&oacute;n:</p>
                    <form class="form-change" name="updateProfile" method="POST" accept-charset="utf-8">
                        <h3>Informaci&oacute;n Personal</h3>
                        <%
                            if (usuario instanceof Administrador) {
                                Administrador admin = (Administrador) usuario;
                                if (admin != null) {
                        %>
                        <label>Nombre:</label>
                        <input id="txt-name" class="f-personal f-admin" type="text" name="txt-name" maxlength="45" value="<%=admin.getNombre()%>" placeholder="Nombre Completo" required>
                        <label>Correo:</label>
                        <input class="f-personal f-admin" type="email" name="txt-email" maxlength="45" value="<%=admin.getCorreo()%>" placeholder="example@server.com">
                        <%
                                }
                            } else {
                                Persona p = (Persona) usuario;
                                if (p != null) {
                        %>
                        <section>
                            <label>Identificaci&oacute;n:</label>
                            <input class="f-personal" type="text" name="txt-ide" maxlength="12" value="<%=p.getIdentificador()%>" placeholder="Identificación Personal" required>
                            <label>Nombre:</label>
                            <input id="txt-name" class="f-personal f-admin" type="text" name="txt-name" maxlength="45" value="<%=p.getNombre()%>" placeholder="Primer Nombre" required>
                            <label>Segundo nombre:</label>
                            <input class="f-personal" type="text" name="txt-secondname" maxlength="45" value="<%=p.getSegundoNombre()%>" placeholder="Segundo Nombre" required>
                        </section>
                        <section>
                            <label>Apellidos:</label>
                            <input class="f-personal" type="text" name="txt-lastname" maxlength="45" value="<%=p.getApellidos()%>" placeholder="Apellidos" required>
                            <label>Correo:</label>
                            <input class="f-personal f-admin" type="email" name="txt-email" maxlength="45" value="<%=p.getCorreo()%>" placeholder="example@server.com" required>
                            <label>Tel&eacute;fono:</label>
                            <input class="f-personal" type="tel" name="txt-phone" maxlength="10" value="<%=p.getTelefono()%>" placeholder="Teléfono">
                        </section>
                        <%
                                }
                            }
                        %>
                        <input type="submit" class="btn btn-save" value="Guardar" title="Guardar cambios en la información personal" >
                    </form>
                </article>
                <article>
                    <form class="form-change" name="changePass" method="POST" accept-charset="utf-8">
                        <h3>Cambio de contrase&ntilde;a</h3>
                        <input type="password" name="txt-actualpass" maxlength="16" placeholder="Digite su contrase&ntilde;a actual" required>
                        <input type="password" name="txt-newpass" maxlength="16" placeholder="Digite su nueva contrase&ntilde;a" required>
                        <input type="password" name="txt-confirmpass" maxlength="16" placeholder="Confirme su contrase&ntilde;a" required>
                        <input type="submit" value="Cambiar" class="btn btn-save" title="Guardar cambios de la contraseña">
                    </form>
                </article>
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
