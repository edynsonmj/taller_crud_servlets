<%-- 
    Document   : list-product
    Created on : 23/01/2023, 10:55:39 p. m.
    Author     : edyns
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
    </head>
    <body style="background-color: #3B3D3D">
        <jsp:include page="nav-admin.jsp" />
        <!--barra de navegacion fin-->
        <div class="container mt-3">
            <h4 class="text-center text-white">Catalogo de productos</h4>
            <a href="<%=request.getContextPath()%>/newProduct" class="btn btn-outline-light">Nuevo producto</a>
            <table class="table table-sm table-dark table-striped table-hover mt-1">
                <thead>
                <th>codigo</th>
                <th>nombre</th>
                <th>precio</th>
                <th>acciones</th>
                </thead>
                <tbody class="table-group-divider">
                    <c:forEach var="producto" items="${listProduct}">
                        <tr>
                            <td>
                                <c:out value="${producto.codigo}" />
                            </td>
                            <td>

                                <c:out value="${producto.nombre}" />
                            </td>
                            <td>

                                <c:out value="${producto.precio}" />
                            </td>
                            <td>
                                <a href="editProduct?codigo=<c:out value='${producto.codigo}'/>" class="btn btn-outline-success">Editar</a>
                                <a href="deleteProduct?codigo=<c:out value='${producto.codigo}' />" class="btn btn-outline-danger">Eliminar</a>
                                <a href="addProduct?codigo=<c:out value='${producto.codigo}' />" class="btn btn-outline-info">Add carrito</a>
                            </td>
                        </tr> 
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
