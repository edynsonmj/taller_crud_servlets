/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.appcarrito.persistence.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.unicauca.appcarrito.persistence.entities.Carrito;
import co.edu.unicauca.appcarrito.persistence.entities.Producto;
import co.edu.unicauca.appcarrito.persistence.jpa.exceptions.IllegalOrphanException;
import co.edu.unicauca.appcarrito.persistence.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author edyns
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getCarritoList() == null) {
            producto.setCarritoList(new ArrayList<Carrito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carrito> attachedCarritoList = new ArrayList<Carrito>();
            for (Carrito carritoListCarritoToAttach : producto.getCarritoList()) {
                carritoListCarritoToAttach = em.getReference(carritoListCarritoToAttach.getClass(), carritoListCarritoToAttach.getId());
                attachedCarritoList.add(carritoListCarritoToAttach);
            }
            producto.setCarritoList(attachedCarritoList);
            em.persist(producto);
            for (Carrito carritoListCarrito : producto.getCarritoList()) {
                Producto oldIdProductoOfCarritoListCarrito = carritoListCarrito.getIdProducto();
                carritoListCarrito.setIdProducto(producto);
                carritoListCarrito = em.merge(carritoListCarrito);
                if (oldIdProductoOfCarritoListCarrito != null) {
                    oldIdProductoOfCarritoListCarrito.getCarritoList().remove(carritoListCarrito);
                    oldIdProductoOfCarritoListCarrito = em.merge(oldIdProductoOfCarritoListCarrito);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getCodigo());
            List<Carrito> carritoListOld = persistentProducto.getCarritoList();
            List<Carrito> carritoListNew = producto.getCarritoList();
            List<String> illegalOrphanMessages = null;
            for (Carrito carritoListOldCarrito : carritoListOld) {
                if (!carritoListNew.contains(carritoListOldCarrito)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carrito " + carritoListOldCarrito + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Carrito> attachedCarritoListNew = new ArrayList<Carrito>();
            for (Carrito carritoListNewCarritoToAttach : carritoListNew) {
                carritoListNewCarritoToAttach = em.getReference(carritoListNewCarritoToAttach.getClass(), carritoListNewCarritoToAttach.getId());
                attachedCarritoListNew.add(carritoListNewCarritoToAttach);
            }
            carritoListNew = attachedCarritoListNew;
            producto.setCarritoList(carritoListNew);
            producto = em.merge(producto);
            for (Carrito carritoListNewCarrito : carritoListNew) {
                if (!carritoListOld.contains(carritoListNewCarrito)) {
                    Producto oldIdProductoOfCarritoListNewCarrito = carritoListNewCarrito.getIdProducto();
                    carritoListNewCarrito.setIdProducto(producto);
                    carritoListNewCarrito = em.merge(carritoListNewCarrito);
                    if (oldIdProductoOfCarritoListNewCarrito != null && !oldIdProductoOfCarritoListNewCarrito.equals(producto)) {
                        oldIdProductoOfCarritoListNewCarrito.getCarritoList().remove(carritoListNewCarrito);
                        oldIdProductoOfCarritoListNewCarrito = em.merge(oldIdProductoOfCarritoListNewCarrito);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getCodigo();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carrito> carritoListOrphanCheck = producto.getCarritoList();
            for (Carrito carritoListOrphanCheckCarrito : carritoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Carrito " + carritoListOrphanCheckCarrito + " in its carritoList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
