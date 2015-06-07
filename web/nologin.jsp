<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession(false);
    if (session != null && !session.isNew() && session.getAttribute("usuario") != null) {
        response.sendRedirect("./");
    } else if (session != null) {
        session.invalidate();
    }

    if (request.getParameter("username") == null) {
        response.sendRedirect("./login.jsp");
    }

    String username = request.getParameter("username");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Plataforma de egresados de la Universidad de Cartagena</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/nologin.css">
    </head>
    <body>
        <header>
            <section>
                <h1><a href="./">Plataforma de Egresados</a></h1>
            </section>
        </header>
        <div id="container">
            <h2>Inicia Sesión en la Plataforma de Egresados</h2>
            <section>
                <form action="login" method="POST" accept-charset="utf-8">
                    <input name="txt-user" type="text" placeholder="Usuario" maxlength="12" value="<%=username%>" required>
                    <input name="txt-password" type="password" placeholder="Contraseña" required>
                    <input type="submit" value="login">
                </form>
            </section>
            <p>Si no te acuerdas de tu contraseña contactate con cualquier administrador de la página</p>
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
