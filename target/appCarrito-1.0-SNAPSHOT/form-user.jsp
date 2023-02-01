<%-- 
    Document   : form-user
    Created on : 24/01/2023, 6:52:39 a. m.
    Author     : edyns
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body style="background-color: #3B3D3D">
        <jsp:include page="nav-admin.jsp" />
        <div class="container mt-3 col-md-4">
            <div class="card text-bg-dark">
                <div class="card-header text-center">
                    <h5>Nuevo usuario</h5>
                </div>
                <div class="card-body">
                    <form action="<%=request.getContextPath()%>/insertUser" method="post">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="nombre" name="name" required="required">
                            <label for="floatingInput" class="text-dark">Nombre</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="usuario" name="user" required="required">
                            <label for="floatingInput" class="text-dark">Usuario</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="clave" name="pass" required="required">
                            <label for="floatingInput" class="text-dark">Clave</label>
                        </div>
                        <select class="form-select mb-3" name="rol" required>
                            <option value="no-admin">no-admin</option>
                            <option value="admin">admin</option>
                        </select>
                        <button type="submit" class="btn btn-outline-success">Guardar</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
