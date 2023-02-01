/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.appcarrito.persistence.jpa;

import co.edu.unicauca.appcarrito.persistence.entities.Carrito;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.unicauca.appcarrito.persistence.entities.Producto;
import co.edu.unicauca.appcarrito.persistence.entities.Usuario;
import co.edu.unicauca.appcarrito.persistence.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author edyns
 */
public class CarritoJpaController implements Serializable {

    public CarritoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carrito carrito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = carrito.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getCodigo());
                carrito.setIdProducto(idProducto);
            }
            Usuario idUsuario = carrito.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                carrito.setIdUsuario(idUsuario);
            }
            em.persist(carrito);
            if (idProducto != null) {
                idProducto.getCarritoList().add(carrito);
                idProducto = em.merge(idProducto);
            }
            if (idUsuario != null) {
                idUsuario.getCarritoList().add(carrito);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carrito carrito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrito persistentCarrito = em.find(Carrito.class, carrito.getId());
            Producto idProductoOld = persistentCarrito.getIdProducto();
            Producto idProductoNew = carrito.getIdProducto();
            Usuario idUsuarioOld = persistentCarrito.getIdUsuario();
            Usuario idUsuarioNew = carrito.getIdUsuario();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getCodigo());
                carrito.setIdProducto(idProductoNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                carrito.setIdUsuario(idUsuarioNew);
            }
            carrito = em.merge(carrito);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getCarritoList().remove(carrito);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getCarritoList().add(carrito);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getCarritoList().remove(carrito);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getCarritoList().add(carrito);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carrito.getId();
                if (findCarrito(id) == null) {
                    throw new NonexistentEntityException("The carrito with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrito carrito;
            try {
                carrito = em.getReference(Carrito.class, id);
                carrito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carrito with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = carrito.getIdProducto();
            if (idProducto != null) {
                idProducto.getCarritoList().remove(carrito);
                idProducto = em.merge(idProducto);
            }
            Usuario idUsuario = carrito.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getCarritoList().remove(carrito);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(carrito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carrito> findCarritoEntities() {
        return findCarritoEntities(true, -1, -1);
    }

    public List<Carrito> findCarritoEntities(int maxResults, int firstResult) {
        return findCarritoEntities(false, maxResults, firstResult);
    }

    private List<Carrito> findCarritoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrito.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Carrito findCarrito(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrito.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarritoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrito> rt = cq.from(Carrito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
