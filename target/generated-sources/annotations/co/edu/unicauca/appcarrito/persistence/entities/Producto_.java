package co.edu.unicauca.appcarrito.persistence.entities;

import co.edu.unicauca.appcarrito.persistence.entities.Carrito;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2023-01-24T10:06:20")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile ListAttribute<Producto, Carrito> carritoList;
    public static volatile SingularAttribute<Producto, Integer> codigo;
    public static volatile SingularAttribute<Producto, BigDecimal> precio;
    public static volatile SingularAttribute<Producto, String> nombre;

}