/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import servicios.AgendamlgException_Exception;
import servicios.Agendamlg_Service;
import servicios.Evento;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import servicios.Categoria;

/**
 *
 * @author johncarlo
 */
@Named(value = "eventoManagedBean")
@RequestScoped
public class EventoManagedBean implements Serializable{

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;
    
    @Inject
    private UsuarioManagedBean usuarioManagedBean;

    private String mensajeDeError = null;
    
    private Evento evento = new Evento();
    private List<Categoria> categorias;

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    /**
     * Creates a new instance of EventoManagedBean
     */
    public EventoManagedBean() {
    }
    
    public String ver(Evento evento){
        this.evento = evento;
        this.categorias = this.obtenerCategoriasEvento(evento);
        return "evento";
    }
    
    public String subirEvento(){
        this.evento=this.buscarEvento(this.evento.getId());
        this.categorias = this.obtenerCategoriasEvento(evento);
        try {
            this.validarEvento(this.evento.getId(), this.usuarioManagedBean.getId());
        } catch(AgendamlgException_Exception other) {
            mensajeDeError = other.getMessage() + ".";
        } catch(Exception e) {
            mensajeDeError = "Ha habido un error interno al validar el evento.";
        }
        return "evento";
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    
    

    private Evento buscarEvento(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEvento(id);
    }

    private void validarEvento(int idEvento, int idUsuario) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        port.validarEvento(idEvento, idUsuario);
    }
    
    // Borrar evento, se encarga de borrar un evento dado este
    public String borrarEvento(Evento evento){
        // Se llama a la debida operacion del servicio
        try {
            eliminarEvento(evento);
            // Asi nos aseguramos que aparece la lista actualizada
            return "index?faces-redirect=true";
        } catch (AgendamlgException_Exception e) {
            mensajeDeError = e.getMessage() + ".";
            return "index?faces-redirect=true";
        }
    }

    public String getMensajeDeError() {
        return mensajeDeError;
    }

    public String getFechaDeFormaBonita(Evento e) {
        XMLGregorianCalendar calendar = e.getFecha();
        GregorianCalendar gregorianCalendar = calendar.toGregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        sdf.setCalendar(gregorianCalendar);
        return sdf.format(gregorianCalendar.getTime());
    }

    private void eliminarEvento(servicios.Evento entity) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        port.eliminarEvento(usuarioManagedBean.getId(), entity);
    }

    private java.util.List<servicios.Categoria> obtenerCategoriasEvento(servicios.Evento evento) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.obtenerCategoriasEvento(evento);
    }

}
