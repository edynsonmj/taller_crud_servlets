<%-- 
    Document   : list-usuario
    Created on : 24/01/2023, 4:53:34 a. m.
    Author     : edyns
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body style="background-color: #3B3D3D">
        <jsp:include page="nav-admin.jsp" />
        <div class="container mt-3">
            <h4 class="text-center text-white">lista de usuarios</h4>
            <a href="<%=request.getContextPath()%>/newUser" class="btn btn-outline-light">Nuevo usuario</a>
            <table class="table table-sm table-dark table-striped table-hover mt-1">
                <thead>
                    <th>Id</th>
                    <th>Nombre</th>
                    <th>Usuario</th>
                    <th>Clave</th>
                    <th>Rol</th>
                    <th>acciones</th>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${listUsuarios}">
                        <tr>
                            <td>
                                <c:out value="${usuario.getId()}" />
                            </td>
                            <td>
                                <c:out value="${usuario.getNombre()}" />
                            </td>
                            <td>
                                <c:out value="${usuario.getUsuario()}" />
                            </td>
                            <td>
                                <c:out value="${usuario.getClave()}" />
                            </td>
                            <td>
                                <c:out value="${usuario.getRol()}" />
                            </td>
                            <td>
                                <a href="editUser?codigo=<c:out value='${usuario.getId()}'/>" class="btn btn-outline-success">Editar</a>
                                <a href="deleteUser?codigo=<c:out value='${usuario.getId()}' />" class="btn btn-outline-danger">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
