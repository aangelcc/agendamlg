/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ejb;

import app.entity.Categoria;
import app.entity.Evento;
import app.entity.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author melchor9000
 */
@Stateless
public class EventoFacade extends AbstractFacade<Evento> {

    @PersistenceContext(unitName = "agendamlg-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventoFacade() {
        super(Evento.class);
    }
    
    public List<Evento> buscarEventosUsuario(int idUsuario){
        Query q = this.em.createQuery("select e from Evento e where e.creador.id=:id");
        q.setParameter("id", idUsuario);
        return (List) q.getResultList();
    }
    
    public List<Evento> buscarEventosNoCaducados(){
        Date ahora = new Date(System.currentTimeMillis());
        Query q = this.em.createQuery("select e from Evento e where e.fecha > :hoy");
        q.setParameter("hoy",ahora,TemporalType.TIMESTAMP);
        return (List) q.getResultList();
    }
    
    public List<Evento> buscarEventoCategorias(List<Categoria> categorias){
        
        Query q = this.em.createQuery("select e from Evento e join e.categoriaList c where c in :categorias");
        q.setParameter("categorias", categorias);
        return q.getResultList();
    }
    
    public void validarEvento(int idEvento){
        Evento evento = this.find(idEvento);
        evento.setValidado((short)1);
    }
}
