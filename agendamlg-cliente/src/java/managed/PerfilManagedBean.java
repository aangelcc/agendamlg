/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import servicios.Agendamlg_Service;
import servicios.Categoria;
import servicios.Evento;
import servicios.Usuario;

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
    
    private int id;
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
    public void init(){
        this.id = usuarioManagedBean.getId();
        this.usuario = this.buscarUsuario(this.id);
        this.eventos = this.buscarEventosUsuario(this.id);
        this.categorias = this.buscarPreferenciasUsuario(this.usuario);
    }

    private Usuario buscarUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarUsuario(id);
    }

    private java.util.List<servicios.Evento> buscarEventosUsuario(int id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosUsuario(id);
    }

    private java.util.List<servicios.Categoria> buscarPreferenciasUsuario(servicios.Usuario usuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarPreferenciasUsuario(usuario);
    }
    
    
}
