package service;

import app.ejb.CategoriaFacade;
import app.ejb.EventoFacade;
import app.ejb.UsuarioFacade;
import app.entity.Categoria;
import app.entity.Usuario;
import app.exception.AgendamlgException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "Agendamlg")
@Stateless()
public class Agendamlg {
    
    @EJB
    private CategoriaFacade categoriaFacade;

    @WebMethod(operationName = "crearCategoria")
    @Oneway
    public void crearCategoria(@WebParam(name = "entity") app.entity.Categoria entity) {
        categoriaFacade.create(entity);
    }

    @WebMethod(operationName = "editarCategoria")
    @Oneway
    public void editarCategoria(@WebParam(name = "entity") app.entity.Categoria entity) {
        categoriaFacade.edit(entity);
    }

    @WebMethod(operationName = "eliminarCategoria")
    @Oneway
    public void eliminarCategoria(@WebParam(name = "entity") app.entity.Categoria entity) {
        categoriaFacade.remove(entity);
    }

    @WebMethod(operationName = "buscarCategoria")
    public app.entity.Categoria buscarCategoria(@WebParam(name = "id") Object id) {
        return categoriaFacade.find(id);
    }

    @WebMethod(operationName = "buscarTodasLasCategorias")
    public List<app.entity.Categoria> buscarTodasLasCategorias() {
        return categoriaFacade.findAll();
    }

    @WebMethod(operationName = "buscarCategoriasPorRango")
    public List<app.entity.Categoria> buscarCategoriasPorRango(@WebParam(name = "range") int[] range) {
        return categoriaFacade.findRange(range);
    }

    @WebMethod(operationName = "contarCategorias")
    public int contarCategorias() {
        return categoriaFacade.count();
    }
    
    @WebMethod(operationName = "buscarPreferenciasUsuario")
    public List<app.entity.Categoria> buscarPreferenciasUsuario(@WebParam(name = "usuario") Usuario usuario) {
        return categoriaFacade.buscarPreferenciasUsuario(usuario);
    }
    
    @WebMethod(operationName = "obtenerCategoriasEvento")
    public List<Categoria> obtenerCategoriasEvento(@WebParam(name="evento") app.entity.Evento evento){
        return categoriaFacade.buscarCategoriasEvento(evento);
    }
    
    //////////////////////////////////////////////
    
    @EJB
    private EventoFacade eventoFacade;

    @WebMethod(operationName = "crearEvento")
    @Oneway
    public void crearEvento(@WebParam(name = "entity") app.entity.Evento entity) {
        eventoFacade.create(entity);
    }

    @WebMethod(operationName = "editarEvento")
    @Oneway
    public void editarEvento(@WebParam(name = "entity") app.entity.Evento entity) {
        eventoFacade.edit(entity);
    }

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

    @WebMethod(operationName = "buscarEventosPorRango")
    public List<app.entity.Evento> buscarEventosPorRango(@WebParam(name = "range") int[] range) {
        return eventoFacade.findRange(range);
    }

    @WebMethod(operationName = "contarEvento")
    public int contarEvento() {
        return eventoFacade.count();
    }
    
    @WebMethod(operationName = "buscarEventosUsuario")
    public List<app.entity.Evento> buscarEventosUsuario(@WebParam(name = "id") int id) {
        return eventoFacade.buscarEventosUsuario(id);
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
   
    @WebMethod(operationName = "actualizarEventoTipoUsuario")
    public void actualizarEventoTipoUsuario(@WebParam(name="evento") app.entity.Evento evento,@WebParam(name="categoriasEvento") List<app.entity.Categoria> categoriasEvento) throws AgendamlgException{
        if(evento.getCreador() != null) {
            Usuario usuario = usuarioFacade.find(evento.getCreador().getId());
            evento.setCreador(usuario);
        }
        eventoFacade.editarEventoTipoUsuario(evento,categoriasEvento);
    }
    
    //////////////////////////////////////////////
    
    @EJB
    private UsuarioFacade usuarioFacade;

    @WebMethod(operationName = "crearUsuario")
    @Oneway
    public void crearUsuario(@WebParam(name = "entity") app.entity.Usuario entity) {
        usuarioFacade.create(entity);
    }

    @WebMethod(operationName = "editarUsuario")
    @Oneway
    public void editarUsuario(@WebParam(name = "entity") app.entity.Usuario entity) {
        usuarioFacade.edit(entity);
    }

    @WebMethod(operationName = "eliminarUsuario")
    @Oneway
    public void eliminarUsuario(@WebParam(name = "entity") app.entity.Usuario entity) {
        usuarioFacade.remove(entity);
    }

    @WebMethod(operationName = "buscarUsuario")
    public app.entity.Usuario buscarUsuario(@WebParam(name = "id") Object id) {
        return usuarioFacade.find(id);
    }

    @WebMethod(operationName = "buscarTodosLosUsuarios")
    public List<app.entity.Usuario> buscarTodosLosUsuarios() {
        return usuarioFacade.findAll();
    }

    @WebMethod(operationName = "buscarUsuariosPorRango")
    public List<app.entity.Usuario> buscarUsuariosPorRango(@WebParam(name = "range") int[] range) {
        return usuarioFacade.findRange(range);
    }

    @WebMethod(operationName = "contarUsuarios")
    public int contarUsuarios() {
        return usuarioFacade.count();
    }

    @WebMethod(operationName = "login")
    public app.entity.Usuario login(@WebParam(name = "alias") String alias, @WebParam(name = "password") String password) {
        return usuarioFacade.login(alias, password);
    }
}
