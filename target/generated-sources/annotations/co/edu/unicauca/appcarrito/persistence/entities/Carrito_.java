package co.edu.unicauca.appcarrito.persistence.entities;

import co.edu.unicauca.appcarrito.persistence.entities.Producto;
import co.edu.unicauca.appcarrito.persistence.entities.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-02-05T12:20:31")
@StaticMetamodel(Carrito.class)
public class Carrito_ { 

    public static volatile SingularAttribute<Carrito, Usuario> idUsuario;
    public static volatile SingularAttribute<Carrito, Integer> id;
    public static volatile SingularAttribute<Carrito, Producto> idProducto;

}