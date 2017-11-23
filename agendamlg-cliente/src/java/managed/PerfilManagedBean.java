/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import servicios.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import java.util.List;

/**
 *
 * @author johncarlo
 */
@Named(value = "perfilManagedBean")
@RequestScoped
public class PerfilManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;
    
    @Inject
    private UsuarioManagedBean usuarioManagedBean;

    private Usuario usuario;
    private List<Evento> eventos;
    private List<Categoria> categorias;

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    /**
     * Creates a new instance of PerfilManagedBean
     */
    public PerfilManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        int id = usuarioManagedBean.getId();
        this.usuario = this.buscarUsuario(id);
        try {
            this.eventos = this.buscarEventosUsuario(id);
            this.categorias = this.buscarPreferenciasUsuario(this.usuario);
        } catch (AgendamlgException_Exception ignore) {
            throw new RuntimeException(ignore);
        }
    }

    private Usuario buscarUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarUsuario(id);
    }

    private java.util.List<servicios.Evento> buscarEventosUsuario(int id) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosUsuario(id);
    }

    private java.util.List<servicios.Categoria> buscarPreferenciasUsuario(servicios.Usuario usuario) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarPreferenciasUsuario(usuario);
    }
    
    
}
