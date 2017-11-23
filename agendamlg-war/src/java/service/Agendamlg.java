package service;

import app.ejb.CategoriaFacade;
import app.ejb.EventoFacade;
import app.ejb.UsuarioFacade;
import app.entity.Usuario;
import app.exception.AgendamlgException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "Agendamlg")
@Stateless()
public class Agendamlg {
    
    @EJB
    private CategoriaFacade categoriaFacade;

    @WebMethod(operationName = "buscarCategoria")
    public app.entity.Categoria buscarCategoria(@WebParam(name = "id") Object id) {
        return categoriaFacade.find(id);
    }

    @WebMethod(operationName = "buscarTodasLasCategorias")
    public List<app.entity.Categoria> buscarTodasLasCategorias() {
        return categoriaFacade.findAll();
    }

    @WebMethod(operationName = "buscarPreferenciasUsuario")
    public List<app.entity.Categoria> buscarPreferenciasUsuario(@WebParam(name = "usuario") Usuario usuario) throws AgendamlgException {
        if(usuario != null) usuario = usuarioFacade.find(usuario.getId());
        return categoriaFacade.buscarPreferenciasUsuario(usuario);
    }
    
    //////////////////////////////////////////////
    
    @EJB
    private EventoFacade eventoFacade;

    @WebMethod(operationName = "eliminarEvento")
    public void eliminarEvento(@WebParam(name = "usuarioQueElimina") int usuarioId, @WebParam(name = "entity") app.entity.Evento entity) throws AgendamlgException {
        eventoFacade.borrarEvento(usuarioFacade.find(usuarioId), entity.getId());
    }

    @WebMethod(operationName = "buscarEvento")
    public app.entity.Evento buscarEvento(@WebParam(name = "id") Object id) {
        return eventoFacade.find(id);
    }

    @WebMethod(operationName = "buscarTodosLosEventos")
    public List<app.entity.Evento> buscarTodosLosEventos() {
        return eventoFacade.findAll();
    }

    @WebMethod(operationName = "buscarEventosUsuario")
    public List<app.entity.Evento> buscarEventosUsuario(@WebParam(name = "id") int id) throws AgendamlgException {
        return eventoFacade.buscarEventosUsuario(usuarioFacade.find(id));
    }
    
    @WebMethod(operationName = "buscarEventosTipoUsuario")
    public List<app.entity.Evento> buscarEventosTipoUsuario(@WebParam(name = "idUsuario")int idUsuario){
        if(idUsuario==-1){
            return eventoFacade.buscarEventosTipoUsuario(null);
        }else{
            Usuario usuario = this.usuarioFacade.find(idUsuario);
            return eventoFacade.buscarEventosTipoUsuario(usuario);
        }
    }
    
    @WebMethod(operationName = "validarEvento")
    public void validarEvento(@WebParam(name = "idEvento")int idEvento,@WebParam(name = "idUsuario")int idUsuario) throws AgendamlgException {
        Usuario usuario = this.usuarioFacade.find(idUsuario);
        eventoFacade.validarEvento(usuario, idEvento);
    }
    
    @WebMethod(operationName = "crearEventoTipoUsuario")
    public void crearEventoTipoUsuario(@WebParam(name="evento") app.entity.Evento evento,@WebParam(name="categoriasEvento") List<app.entity.Categoria> categoriasEvento) throws AgendamlgException{
        if(evento.getCreador() != null) {
            Usuario usuario = usuarioFacade.find(evento.getCreador().getId());
            evento.setCreador(usuario);
        }
        eventoFacade.crearEventoTipoUsuario(evento,categoriasEvento);
    }
    
    @WebMethod(operationName = "buscarEventoCategorias")
    public List<app.entity.Evento> buscarEventoCategorias(@WebParam(name = "categorias")List<app.entity.Categoria> categorias, @WebParam(name = "usuario")app.entity.Usuario usuario, @WebParam(name = "filtrarCategorias")boolean filtroCercania, @WebParam(name = "coordenadaX")double x, @WebParam(name = "coordenadaY")double y, @WebParam(name = "radio")double radio){
        if(usuario != null) usuario = usuarioFacade.find(usuario.getId());
        return eventoFacade.buscarEventoCategorias(categorias, usuario, filtroCercania, x, y, radio);
    }
    
    //////////////////////////////////////////////
    
    @EJB
    private UsuarioFacade usuarioFacade;

    @WebMethod(operationName = "buscarUsuario")
    public app.entity.Usuario buscarUsuario(@WebParam(name = "id") Object id) {
        return usuarioFacade.find(id);
    }

    @WebMethod(operationName = "buscarTodosLosUsuarios")
    public List<app.entity.Usuario> buscarTodosLosUsuarios() {
        return usuarioFacade.findAll();
    }

    @WebMethod(operationName = "login")
    public app.entity.Usuario login(@WebParam(name = "alias") String alias, @WebParam(name = "password") String password) {
        return usuarioFacade.login(alias, password);
    }
}
