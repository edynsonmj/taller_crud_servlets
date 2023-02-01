<%-- 
    Document   : form-product
    Created on : 24/01/2023, 12:23:37 a. m.
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
                    <h5>Nuevo producto</h5>
                </div>
                <div class="card-body">
                    <form action="<%=request.getContextPath()%>/insertProduct" method="post">
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" id="floatingInput" placeholder="nombre" name="name" required="required">
                        <label for="floatingInput" class="text-dark">Nombre</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" class="form-control" id="floatingInput" placeholder="precio"  step="0.01" name="price" required="required">
                        <label for="floatingInput" class="text-dark">Precio</label>
                    </div>
                    <button type="submit" class="btn btn-outline-success">Guardar</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
