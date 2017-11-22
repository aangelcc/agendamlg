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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 *
 * @author melchor9000
 */
@Stateless
public class EventoFacade extends AbstractFacade<Evento> {

    @EJB
    private CategoriaFacade categoriaFacade;

    @EJB
    private GmailBean gmailBean;

    @EJB
    private UsuarioFacade usuarioFacade;
    
    

    @PersistenceContext(unitName = "agendamlg-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventoFacade() {
        super(Evento.class);
    }

    public void enviarCorreoInteresados(Evento evento) {
        gmailBean.sendMail(usuarioFacade.buscarUsuariosPreferencias(evento.getCategoriaList()), "Hay un evento que te puede gustar", evento.getNombre() + " es un evento de tu preferencia");
    }
    
    public void enviarCorreoCreador(Evento evento, Usuario creador){
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(creador);
        gmailBean.sendMail(usuarios, "Tu evento ha sido publicado", "El evento "+evento.getNombre()+" ha sido publicado");
    }

    public void anadirCategoriaEvento(Evento evento, List<Categoria> categoriasEvento) {
        evento.setId(this.findLastId());
        evento.getCategoriaList().addAll(categoriasEvento);
        for(Categoria categoria: evento.getCategoriaList()){
            categoria = categoriaFacade.find(categoria.getId());
            categoria.getEventoList().add(evento);
        }
    }

    public void crearEventoTipoUsuario(Evento evento, List<Categoria> categoriasEvento) throws AgendamlgException {
        try {
            Usuario usuario = evento.getCreador();
            if (usuario == null) {
                throw new AgendamlgException("Usuario anónimo no puede crear eventos");
            } else if (usuario.getTipo() == 1) {
                evento.setValidado((short) 0);
                this.create(evento);
                this.anadirCategoriaEvento(evento, categoriasEvento);
            } else if (usuario.getTipo() > 1) {
                evento.setValidado((short) 1);
                this.create(evento);
                this.anadirCategoriaEvento(evento, categoriasEvento);
                this.enviarCorreoInteresados(evento);
            }
            //Tenemos que coger el id del evento que se acaba de añadir (yo no sé si esto es thread safe)
        } catch (ConstraintViolationException e) {
            throw new AgendamlgException("Hay campos invalidos", e);
        }
    }

    public int findLastId() {
        Query q = this.em.createQuery("select max(e.id) from Evento e");
        return (int) q.getSingleResult();
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

    @SuppressWarnings("unchecked")
    public List<Evento> buscarEventoCategorias(List<Categoria> categorias, Usuario usuario){
        /* Query q = this.em.createQuery("select distinct e from Evento e join e.categoriaList c where c in :categorias");
        q.setParameter("categorias", categorias);
        return q.getResultList(); */
        
        // Dado que una de las columnas es de tipo LONGVARCHAR, JPQL no permite usar
        // distinct para evitar obtener filas repetidas, de ahí que se
        // haga el procesamiento a mano para lograr esto
        List<Evento> listaEventos;
        
        Date ahora = new Date(System.currentTimeMillis());
        if (usuario != null && usuario.getTipo() == 3) {
            Query q = this.em.createQuery("select e from Evento e join e.categoriaList c where c in :categorias and e.fecha > :hoy ORDER BY e.fecha ASC");
            q.setParameter("categorias", categorias);
            q.setParameter("hoy", ahora, TemporalType.TIMESTAMP);
            listaEventos = q.getResultList();
        } else {
            Query q = this.em.createQuery("select e from Evento e join e.categoriaList c where c in :categorias and e.fecha > :hoy and e.validado = 1 ORDER BY e.fecha ASC");
            q.setParameter("categorias", categorias);
            q.setParameter("hoy", ahora, TemporalType.TIMESTAMP);
            listaEventos = q.getResultList();
        }
        
        // Remover duplicados
        List<Evento> duplicadosEliminados = new ArrayList<>();
        
        for(Evento evento : listaEventos){
            if(!duplicadosEliminados.contains(evento)){
                duplicadosEliminados.add(evento);
            }
        }
        
        return duplicadosEliminados;
    }

    public void validarEvento(Usuario usuario, int idEvento) throws AgendamlgException {
        if(usuario == null) {
            throw new AgendamlgException("Un usuario anónimo no puede crear eventos");
        } else if(usuario.getTipo() == 3) {
            Evento evento = this.find(idEvento);
            if (evento.getValidado() == 0) {
                evento.setValidado((short) 1);
                enviarCorreoInteresados(evento);
                enviarCorreoCreador(evento,evento.getCreador());
            } else {
                throw new AgendamlgException("El evento ya ha sido validado");
            }
        } else {
            throw new AgendamlgException("El usuario " + usuario.getAlias() + " no tiene permisos para realizar esta acción");
        }
    }

    public void borrarEvento(Usuario usuario, int idEvento) throws AgendamlgException {
        if(usuario == null) {
            throw new AgendamlgException("Un usuario anónimo no puede crear eventos");
        } else if(usuario.getTipo() == 3) {
            remove(find(idEvento));
        } else {
            throw new AgendamlgException("El usuario " + usuario.getAlias() + " no tiene permisos para borrar eventos");
        }
    }
}
