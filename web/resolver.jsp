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
    if (request.getParameter("codigo") == null) {
        response.sendRedirect("./encuestas.jsp");
    }
%>
<!DOCTYPE html>
<html ng-app="EgresadosModule">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Plataforma de egresados de la Universidad de Cartagena</title>
        <link rel="stylesheet" href="css/components.css">
        <link rel="stylesheet" href="css/style.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>
        <script src="js/poll.js"></script>
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
                        <a href="encuestas.jsp"><li class="item select">Encuestas</li></a>
                        <% } %>
                        <% if (usuario != null && usuario.getTipo() == TipoDeUsuario.EGRESADO) { %>
                        <a href="historial.jsp"><li class="item">Historial de Encuestas</li></a>
                        <% } %>
                    </ul>
                </nav>
            </section><section  class="content" ng-controller="rePollController">
                <span class="message" ng-show="showMessage">
                    <div ng-class="message.icon"></div>
                    <div>
                        <h4></h4>
                        <p>{{ message.message }}</p>
                    </div>
                    <button ng-click="showMessage = false" class="btn btn-close icon-error right" title="Cerrar"></button>
                </span>
                <div ng-show="!showMessage">
                    <h2>{{poll.name}}</h2>
                    <p>Por favor responda a las siguientes preguntas</p>
                    <form method="post" accept-charset="utf-8" ng-submit="submitAnswersPoll()">
                        <article ng-repeat="question in poll.questions">
                            <h4>{{'Pregunta #' + ($index + 1)}}</h4>
                            <p>{{question.body}}</p>
                            <textarea name="respuesta" ng-if="question.options.length===0" ng-model="pollAnswers.answers[$index].content"></textarea>
                            <section ng-if="question.options.length!==0" >
                                <div ng-repeat="option in question.options">
                                    <label>
                                        <input type="radio" name="respuesta" ng-value="option.body" class="btn-radio" ng-click="changeOption(option,poll.questions.indexOf(question))">
                                        {{option.body}}
                                    </label>
                                </div>
                            </section>
                        </article>
                        <input type="submit" value="Enviar" class="btn btn-default right">
                    </form>
                </div>
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