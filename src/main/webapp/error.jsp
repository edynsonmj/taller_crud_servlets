<%-- 
    Document   : error
    Created on : 24/01/2023, 4:11:43 a. m.
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
                    <h5>Error en la operacion</h5>
                </div>
                <div class="card-body text-center">
                    <h6> "${mensaje}" </h6>
                    <a href="<%=request.getContextPath()%>/${ruta}" class="btn btn-outline-success">Volver</a>
                </div>
            </div>
        </div>
    </body>
</html>
