package service;

import app.ejb.CategoriaFacade;
import app.ejb.EventoFacade;
import app.ejb.UsuarioFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

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
    @Oneway
    public void eliminarEvento(@WebParam(name = "entity") app.entity.Evento entity) {
        eventoFacade.remove(entity);
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
    
}
