<%-- 
    Document   : login
    Created on : 1/02/2023, 5:27:38 p. m.
    Author     : juliana
--%>
<%@page import="java.util.List"%>
<%@page import="co.edu.unicauca.appcarrito.persistence.entities.Usuario"%>
<%@page import="co.edu.unicauca.appcarrito.persistence.jpa.UsuarioJpaController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
        <title>JSP Page</title>
    </head>

    <%
        UsuarioJpaController ctrl = new UsuarioJpaController();
        List<Usuario> data = ctrl.findUsuarioEntities();
        HttpSession sesion = request.getSession();

        if (request.getParameter("btnIngresar") != null) {
            String usuarioN = request.getParameter("usuario").toString();
            String contra = request.getParameter("clave").toString();
            for (Usuario us : data) {
                if (us.getUsuario().equals(usuarioN) && us.getClave().equals(contra)) {
                    sesion.setAttribute("usuario", usuarioN);
                    sesion.setAttribute("rol", us.getRol());
                    response.sendRedirect("index.jsp");
                }
            }
            out.print("<script>alert('Usuario o contraseña invalida');</script>");
        }
        if (request.getParameter("cerrar") != null) {
            session.invalidate();
        }
    %>

    <body style="background-color: #3B3D3D">
        <nav class="navbar navbar-expand-lg bg-dark" data-bs-theme="dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">AppWeb</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container mt-3 col-md-4">
            <div class="card text-bg-dark">
                <div class="card-header text-center">
                    <h5>Inicio de Sesión</h5>
                </div>
                <div class="card-body">
                    <form action="login.jsp" method="post">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="nombre" name="usuario" required="required">
                            <label for="floatingInput" class="text-dark">Usuario</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="password" class="form-control" id="floatingInput" placeholder="contraseña" name="clave" required="required">
                            <label for="floatingInput" class="text-dark">Contraseña</label>
                        </div>
                        <button type="submit" name="btnIngresar" class="btn btn-outline-success">Ingresar</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>


