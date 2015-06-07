<%@page import="com.egresados.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession(false);
    if (session != null && !session.isNew() && session.getAttribute("usuario") != null) {
        response.sendRedirect("home.jsp");
    } else if (session != null) {
        session.invalidate();
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Plataforma de egresados de la Universidad de Cartagena</title>
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/login.css">
    </head>
    <body>
        <header>
            <section>
                <h1><a href="./">Plataforma de Egresados</a></h1>
                <div id="login">
                    <form action="login" method="POST" accept-charset="utf-8">
                        <input name="txt-user" type="text" placeholder="Codigo" maxlength="12" required>
                        <input name="txt-password" type="password" placeholder="Contraseña" required>
                        <input type="submit" value="login" title="iniciar sesión">
                    </form>
                </div>
                <div style="clear: both"></div>
            </section>
        </header>
        <div id="container">
            <!--contenido de la pagina-->
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
