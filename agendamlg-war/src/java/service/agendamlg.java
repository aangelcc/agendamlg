/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.ejb.CategoriaFacade;
import app.ejb.EventoFacade;
import app.ejb.UsuarioFacade;
import app.entity.Categoria;
import app.entity.Evento;
import app.entity.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author johncarlo
 */
@WebService(serviceName = "agendamlg")
public class agendamlg {
    @EJB
    private UsuarioFacade ejbRefUsuario;
    
    @WebMethod(operationName = "createUsuario")
    @Oneway
    public void createUsuario(@WebParam(name = "entity") Usuario entity) {
        ejbRefUsuario.create(entity);
    }
    @WebMethod(operationName = "editUsuario")
    @Oneway
    public void editUsuario(@WebParam(name = "entity") Usuario entity) {
        ejbRefUsuario.edit(entity);
    }

    @WebMethod(operationName = "removeUsuario")
    @Oneway
    public void removeUsuario(@WebParam(name = "entity") Usuario entity) {
        ejbRefUsuario.remove(entity);
    }

    @WebMethod(operationName = "findUsuario")
    public Usuario findUsuario(@WebParam(name = "id") Object id) {
        return ejbRefUsuario.find(id);
    }

    @WebMethod(operationName = "findAllUsuarios")
    public List<Usuario> findAllUsuarios() {
        return ejbRefUsuario.findAll();
    }

    @WebMethod(operationName = "findRangeUsuario")
    public List<Usuario> findRangeUsuario(@WebParam(name = "range") int[] range) {
        return ejbRefUsuario.findRange(range);
    }

    @WebMethod(operationName = "countUsuarios")
    public int countUsuarios() {
        return ejbRefUsuario.count();
    }
    
    @EJB
    private EventoFacade ejbRefEvento;

    @WebMethod(operationName = "createEvento")
    @Oneway
    public void createEvento(@WebParam(name = "entity") Evento entity) {
        ejbRefEvento.create(entity);
    }

    @WebMethod(operationName = "editEvento")
    @Oneway
    public void editEvento(@WebParam(name = "entity") Evento entity) {
        ejbRefEvento.edit(entity);
    }

    @WebMethod(operationName = "removeEvento")
    @Oneway
    public void removeEvento(@WebParam(name = "entity") Evento entity) {
        ejbRefEvento.remove(entity);
    }

    @WebMethod(operationName = "findEvento")
    public Evento findEvento(@WebParam(name = "id") Object id) {
        return ejbRefEvento.find(id);
    }

    @WebMethod(operationName = "findAllEventos")
    public List<Evento> findAllEventos() {
        return ejbRefEvento.findAll();
    }

    @WebMethod(operationName = "findRangeEvento")
    public List<Evento> findRangeEvento(@WebParam(name = "range") int[] range) {
        return ejbRefEvento.findRange(range);
    }

    @WebMethod(operationName = "countEvento")
    public int countEventos() {
        return ejbRefEvento.count();
    }
    
    @EJB
    private CategoriaFacade ejbRefCategoria;

    @WebMethod(operationName = "createCategoria")
    @Oneway
    public void createCategoria(@WebParam(name = "entity") Categoria entity) {
        ejbRefCategoria.create(entity);
    }

    @WebMethod(operationName = "editCategoria")
    @Oneway
    public void editCategoria(@WebParam(name = "entity") Categoria entity) {
        ejbRefCategoria.edit(entity);
    }

    @WebMethod(operationName = "removeCategoria")
    @Oneway
    public void removeCategoria(@WebParam(name = "entity") Categoria entity) {
        ejbRefCategoria.remove(entity);
    }

    @WebMethod(operationName = "findCategoria")
    public Categoria findCategoria(@WebParam(name = "id") Object id) {
        return ejbRefCategoria.find(id);
    }

    @WebMethod(operationName = "findAllCategorias")
    public List<Categoria> findAllCategorias() {
        return ejbRefCategoria.findAll();
    }

    @WebMethod(operationName = "findRangeCategoria")
    public List<Categoria> findRangeCategoria(@WebParam(name = "range") int[] range) {
        return ejbRefCategoria.findRange(range);
    }

    @WebMethod(operationName = "countCategorias")
    public int countCategorias() {
        return ejbRefCategoria.count();
    }
}
