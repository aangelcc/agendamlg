/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.ejb;

import app.entity.Categoria;
import app.entity.Evento;
import app.entity.Usuario;
import app.exception.AgendamlgException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolationException;

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

    public void crearEventoTipoUsuario(Evento evento) throws AgendamlgException {
        try {
            Usuario usuario = evento.getCreador();
            if (usuario == null) {
                throw new AgendamlgException("Usuario anónimo no puede crear eventos");
            } else if (usuario.getTipo() == 1) {
                evento.setValidado((short) 0);
                this.create(evento);
            } else if (usuario.getTipo() > 1) {
                evento.setValidado((short) 1);
                this.create(evento);
            }
        }catch(ConstraintViolationException e){
            throw new AgendamlgException("Hay campos invalidos",e);
        }

    }

    public List<Evento> buscarEventosUsuario(int idUsuario) {
        Query q = this.em.createQuery("select e from Evento e where e.creador.id=:id");
        q.setParameter("id", idUsuario);
        return (List) q.getResultList();
    }

    public List<Evento> buscarEventosTipoUsuario(Usuario usuario) {
        Date ahora = new Date(System.currentTimeMillis());
        if (usuario != null && usuario.getTipo() == 3) {
            Query q = this.em.createQuery("select e from Evento e where e.fecha > :hoy");
            q.setParameter("hoy", ahora, TemporalType.TIMESTAMP);
            return (List) q.getResultList();
        } else {
            Query q = this.em.createQuery("select e from Evento e where e.fecha > :hoy and e.validado = 1");
            q.setParameter("hoy", ahora, TemporalType.TIMESTAMP);
            return (List) q.getResultList();
        }
    }
    
    public List<Evento> buscarEventoCategorias(List<Categoria> categorias){
        
        Query q = this.em.createQuery("select e from Evento e join e.categoriaList c where c in :categorias");
        q.setParameter("categorias", categorias);
        return q.getResultList();
    }

    public void validarEvento(Usuario usuario, int idEvento) throws AgendamlgException {
        if (usuario.getTipo() == 3) {
            Evento evento = this.find(idEvento);
            if (evento.getValidado() == 0) {
                evento.setValidado((short) 1);
            } else {
                throw new AgendamlgException("El evento ya ha sido validado");
            }
        } else {
            throw new AgendamlgException("El usuario " + usuario.getAlias() + " no tiene permisos para realizar esta acción");
        }
    }
}
