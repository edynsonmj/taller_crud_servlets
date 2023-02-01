<%-- 
    Document   : product-form-edit
    Created on : 24/01/2023, 2:49:54 a. m.
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
                    <h5>Editar producto</h5>
                </div>
                <div class="card-body">
                    <form action="<%=request.getContextPath()%>/updateProduct" method="post">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="codigo" name="codigo" required="required" value="${producto.codigo}" readonly="true">
                            <label for="floatingInput" class="text-dark">Codigo</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="floatingInput" placeholder="nombre" name="name" required="required" value="${producto.nombre}">
                            <label for="floatingInput" class="text-dark">Nombre</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="number" class="form-control" id="floatingInput" placeholder="precio"  step="0.01" name="price" required="required" value="${producto.precio}">
                            <label for="floatingInput" class="text-dark">Precio</label>
                        </div>
                        <button type="submit" class="btn btn-outline-success">Guardar</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
