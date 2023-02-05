<%-- 
    Document   : form-user
    Created on : 24/01/2023, 6:52:39 a. m.
    Author     : edyns
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
       
    <body style="background-color: #3B3D3D">
        <jsp:include page="nav-admin.jsp" />
        
        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            HttpSession ses = request.getSession();
            if (ses.getAttribute("usuario") == null) {
                response.sendRedirect("login.jsp");
            }
        %>
        
        <div class="container mt-3 col-md-4">
            <div class="card text-bg-dark">
                <div class="card-header text-center">
                    <h5>Bienvenido <%= ses.getAttribute("usuario")%></h5>
                    <a href="<%=request.getContextPath()%>/listProduct" class="btn btn-outline-success">Ir a la tienda</a>
                </div>
                
            </div>
        </div>
    </body>
</html>

