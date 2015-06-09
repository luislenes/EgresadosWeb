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
<html ng-app="EgresadosModule">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Plataforma de egresados de la Universidad de Cartagena</title>
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/createpoll.css">
        <link rel="stylesheet" href="css/components.css">
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
            </section><section class="content" ng-controller="PollController">
                <h2>Creador de encuesta</h2>
                <a href="register_encuestas.jsp"><span class="btn btn-atras right">Atrás</span></a>
                <span class="message" ng-show="showMessage">
                    <div ng-class="message.icon"></div>
                    <div>
                        <h4></h4>
                        <p>{{ message.message }}</p>
                    </div>
                    <button ng-click="showMessage = false" class="btn btn-close icon-error right" title="Cerrar"></button>
                </span>
                <p>Se encuestra ubicado en el creador de encuestas, si quiere crear una encuesta por favor digite la siguiente información:</p>
                <form ng-submit="submitCreatePoll()" name="encuesta" method="POST" accept-charset="utf-8" autocomplete="off">
                    <input class="namePoll" type="text" name="txt-namepoll" maxlength="35" placeholder="Nombre de la encuesta" ng-model="poll.name" required>
                    <article class="question" ng-repeat="question in poll.questions">
                        <div class="title">{{ poll.questions.indexOf(question) + 1 }}</div>
                        <button class="btn btn-deletequestion icon-delete2 right" title="Eliminar esta pregunta" ng-hide="poll.questions.length === 1" ng-click="removeQuestion(question)" onclick="return false"></button>
                        <p>En el siguiente cuadro de texto defina el enunciado de su pregunta:</p>
                        <textarea name="txt-question" ng-model="question.content" required></textarea>
                        <p>Si quiere agregarle opciones a esta pregunta puede darle en el boton <span>(+)</span> que esta en la parte inferior de la pregunta.</p>
                        <div class="options">
                            <div class="option" ng-repeat="op in question.options">
                                <input type="text" name="txt-option" placeholder="Digite la opción" ng-model="op.option" required>
                                <button class="btn btn-removeoption icon-minus" title="Eliminar opción" ng-click="removeOption(question, op)" onclick="return false"></button>
                            </div>
                        </div>
                        <div class="right">
                            <input type="checkbox" ng-model="question.multi" ng-disabled="disabledCheck(question)"> Pregunta multiplataforma
                        </div>
                        <button class="btn btn-addoption icon-plus" title="agrega otra opción" ng-click="addOption(question)" onclick="return false">Agregar más opciones</button>
                    </article>
                    <button class="btn btn-add icon-plus right" title="Agregar otra pregunta" ng-click="addQuestion()" onclick="return false"></button>
                    <input type="submit" class="btn btn-save" value="Guardar Encuesta">
                </form>
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