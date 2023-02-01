/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package co.edu.unicauca.appcarrito.control;

import co.edu.unicauca.appcarrito.persistence.jpa.ProductoJpaController;
import co.edu.unicauca.appcarrito.persistence.entities.Producto;
import co.edu.unicauca.appcarrito.persistence.entities.Usuario;
import co.edu.unicauca.appcarrito.persistence.entities.Carrito;
import co.edu.unicauca.appcarrito.persistence.jpa.CarritoJpaController;
import co.edu.unicauca.appcarrito.persistence.jpa.UsuarioJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author edyns
 */
@WebServlet("/")
public class ServletAppProduct extends HttpServlet {
    private ProductoJpaController productJPA;
    private UsuarioJpaController userJPA;
    private CarritoJpaController carJPA;
    private final static String PU = "co.edu.unicauca_appCarrito_war_1.0-SNAPSHOTPU";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void init() throws ServletException{
        super.init();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU);
        productJPA = new ProductoJpaController(emf);
        userJPA = new UsuarioJpaController(emf);
        carJPA = new CarritoJpaController(emf);
        List<Producto> listaProductos = productJPA.findProductoEntities();
        /*for(Producto producto: listaProductos){
            System.out.println("nombre: "+producto.getNombre()+" codigo: "+producto.getCodigo()+"  precio: "+producto.getPrecio());
        }*/
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getServletPath();
        try{
            switch(action){
                case "/addProduct":
                    addProduct(request, response);
                    break;
                case "/newProduct":
                    showNewFormProduct(request, response);
                    break;
                case "/insertProduct":
                    createProduct(request, response);
                    break;
                case "/deleteProduct":
                    deleteProduct(request, response);
                    break;
                case "/editProduct":
                    showEditFormProduct(request, response);
                    break;
                case "/updateProduct":
                    updateProduct(request, response);
                    break;
                case "/listUser":
                    readUser(request, response);
                    break;
                case "/newUser":
                    showNewFormUser(request, response);
                    break;
                case "/insertUser":
                    createUser(request, response);
                    break;
                case "/deleteUser":
                    deleteUser(request, response);
                    break;
                case "/editUser":
                    showEditFormUser(request, response);
                    break;
                case "/updateUser":
                    updateUser(request, response);
                    break;
                case "/listCar":
                    readCar(request, response);
                    break;
                case "/newCar":
                    //showNewFormCar(request, response);
                    break;
                case "/insertCar":
                    //createProduct(request, response);
                    break;
                case "/deleteCar":
                    deleteCar(request, response);
                    break;
                case "/editCar":
                    //showEditFormCar(request, response);
                    break;
                case "/updateCar":
                    updateProduct(request, response);
                    break;
                default:
                    readProduct(request, response);
                    break;
            }
        }
        catch(Exception e){
            throw new ServletException(e);
        }
    }
    
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int codigo = Integer.parseInt(request.getParameter("codigo"));
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        Usuario usuario = new Usuario();
        usuario.setId(6);
        Carrito car = new Carrito();
        car.setIdProducto(producto);
        car.setIdUsuario(usuario);
        carJPA.create(car);
        response.sendRedirect("list");
    }
    
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        String name = request.getParameter("name");
        BigDecimal precio = new BigDecimal(request.getParameter("price"));
        System.out.println("numero:"+precio);
        Producto producto = new Producto();
        producto.setNombre(name);
        producto.setPrecio(precio);
        productJPA.create(producto);
        System.out.println("cayo a insert");
        response.sendRedirect("list");
    }
    
    private void readProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        List<Producto> listaProductos = productJPA.findProductoEntities();
        request.setAttribute("listProduct", listaProductos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list-product.jsp");
        dispatcher.forward(request, response);
    }
    
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int codigo = Integer.parseInt(request.getParameter("codigo"));
        String name = request.getParameter("name");
        BigDecimal precio = new BigDecimal(request.getParameter("price"));
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(name);
        producto.setPrecio(precio);
        //si se pasan la lista de carrito normal, sera reescrita.
        List carrito = new ArrayList<Carrito>();
        producto.setCarritoList(carrito);
        try{
            productJPA.edit(producto);
        }catch(Exception e){
            Logger.getLogger(ServletAppProduct.class.getName()).log(Level.ALL.SEVERE, null, e);
        }
        response.sendRedirect("list");
    }
    
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("codigo"));
        try{
            productJPA.destroy(id);
        }catch(Exception e){
            System.out.println("llave foranea");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            String mensaje = "no puedes eliminar el producto, se encuentra en uso por almenos un usuario";
            String ruta = "list";
            request.setAttribute("ruta", ruta);
            request.setAttribute("mensaje", mensaje);
            dispatcher.forward(request, response);
        }
        response.sendRedirect("list");
    }
    
    private void showNewFormProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher("form-product.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEditFormProduct(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int codigo = Integer.parseInt(request.getParameter("codigo"));
        Producto producto = productJPA.findProducto(codigo);
        RequestDispatcher dispatcher = null;
        if( producto != null){
            dispatcher = request.getRequestDispatcher("product-form-edit.jsp");
            request.setAttribute("producto", producto);
        }else{
            dispatcher = request.getRequestDispatcher("list-product.jsp");
        }
        dispatcher.forward(request, response);
    }
    //
    private void createUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        String name = request.getParameter("name");
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String rol = request.getParameter("rol");
        Usuario usuario = new Usuario();
        usuario.setNombre(name);
        usuario.setUsuario(user);
        usuario.setClave(pass);
        usuario.setRol(rol);
        userJPA.create(usuario);
        response.sendRedirect("listUser");
    }
    
    private void readUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        List<Usuario> listaUsuarios = userJPA.findUsuarioEntities();
        request.setAttribute("listUsuarios", listaUsuarios);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list-usuario.jsp");
        dispatcher.forward(request, response);
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int codigo = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String user = request.getParameter("user");
        String clave = request.getParameter("pass");
        String rol = request.getParameter("rol");
        String esAdmin = request.getParameter("esAdmin");
        Usuario usuario = new Usuario();
        usuario.setId(codigo);
        usuario.setNombre(name);
        usuario.setUsuario(user);
        usuario.setClave(clave);
        usuario.setRol(rol);
        List carrito = new ArrayList<Carrito>();
        usuario.setCarritoList(carrito);
        System.out.println(usuario.getId());
        System.out.println(usuario.getNombre());
        System.out.println(usuario.getUsuario());
        System.out.println(usuario.getClave());
        System.out.println(usuario.getRol());
        try{
            userJPA.edit(usuario);
        }catch(Exception e){
            Logger.getLogger(ServletAppProduct.class.getName()).log(Level.ALL.SEVERE, null, e);
        }
        response.sendRedirect("listUser");
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("codigo"));
        try{
            userJPA.destroy(id);
        }catch(Exception e){
            System.out.println("llave foranea");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            String mensaje = "no puedes eliminar el usuario, se encuentra en uso";
            request.setAttribute("mensaje", mensaje);
            String ruta = "listUser";
            request.setAttribute("ruta", ruta);
            dispatcher.forward(request, response);
        }
        response.sendRedirect("listUser");
    }
    
    private void showNewFormUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher("form-user.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showEditFormUser(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int codigo = Integer.parseInt(request.getParameter("codigo"));
        Usuario usuario = userJPA.findUsuario(codigo);
        RequestDispatcher dispatcher = null;
        if( usuario != null){
            dispatcher = request.getRequestDispatcher("usuario-form-edit.jsp");
            request.setAttribute("usuario", usuario);
        }else{
            dispatcher = request.getRequestDispatcher("list-usuario.jsp");
        }
        dispatcher.forward(request, response);
    }
    
    
    private void readCar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        List<Carrito> listaCarrito = carJPA.findCarritoEntities();
        request.setAttribute("listCar", listaCarrito);
        RequestDispatcher dispatcher = request.getRequestDispatcher("list-carrito.jsp");
        dispatcher.forward(request, response);
    }
    
    private void deleteCar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("codigo"));
        try{
            carJPA.destroy(id);
        }catch(Exception e){
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            String mensaje = "no puedes eliminar el producto del carrito, el elemento esta siendo usado en otra referencia";
            request.setAttribute("mensaje", mensaje);
            String ruta = "listCar";
            request.setAttribute("ruta", ruta);
            dispatcher.forward(request, response);
        }
        response.sendRedirect("listCar");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
