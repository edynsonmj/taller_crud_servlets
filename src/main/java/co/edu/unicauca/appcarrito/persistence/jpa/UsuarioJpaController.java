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
import co.edu.unicauca.appcarrito.persistence.entities.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getCarritoList() == null) {
            usuario.setCarritoList(new ArrayList<Carrito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carrito> attachedCarritoList = new ArrayList<Carrito>();
            for (Carrito carritoListCarritoToAttach : usuario.getCarritoList()) {
                carritoListCarritoToAttach = em.getReference(carritoListCarritoToAttach.getClass(), carritoListCarritoToAttach.getId());
                attachedCarritoList.add(carritoListCarritoToAttach);
            }
            usuario.setCarritoList(attachedCarritoList);
            em.persist(usuario);
            for (Carrito carritoListCarrito : usuario.getCarritoList()) {
                Usuario oldIdUsuarioOfCarritoListCarrito = carritoListCarrito.getIdUsuario();
                carritoListCarrito.setIdUsuario(usuario);
                carritoListCarrito = em.merge(carritoListCarrito);
                if (oldIdUsuarioOfCarritoListCarrito != null) {
                    oldIdUsuarioOfCarritoListCarrito.getCarritoList().remove(carritoListCarrito);
                    oldIdUsuarioOfCarritoListCarrito = em.merge(oldIdUsuarioOfCarritoListCarrito);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Carrito> carritoListOld = persistentUsuario.getCarritoList();
            List<Carrito> carritoListNew = usuario.getCarritoList();
            List<String> illegalOrphanMessages = null;
            for (Carrito carritoListOldCarrito : carritoListOld) {
                if (!carritoListNew.contains(carritoListOldCarrito)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carrito " + carritoListOldCarrito + " since its idUsuario field is not nullable.");
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
            usuario.setCarritoList(carritoListNew);
            usuario = em.merge(usuario);
            for (Carrito carritoListNewCarrito : carritoListNew) {
                if (!carritoListOld.contains(carritoListNewCarrito)) {
                    Usuario oldIdUsuarioOfCarritoListNewCarrito = carritoListNewCarrito.getIdUsuario();
                    carritoListNewCarrito.setIdUsuario(usuario);
                    carritoListNewCarrito = em.merge(carritoListNewCarrito);
                    if (oldIdUsuarioOfCarritoListNewCarrito != null && !oldIdUsuarioOfCarritoListNewCarrito.equals(usuario)) {
                        oldIdUsuarioOfCarritoListNewCarrito.getCarritoList().remove(carritoListNewCarrito);
                        oldIdUsuarioOfCarritoListNewCarrito = em.merge(oldIdUsuarioOfCarritoListNewCarrito);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carrito> carritoListOrphanCheck = usuario.getCarritoList();
            for (Carrito carritoListOrphanCheckCarrito : carritoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Carrito " + carritoListOrphanCheckCarrito + " in its carritoList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
