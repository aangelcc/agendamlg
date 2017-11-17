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
import javax.xml.ws.WebServiceRef;
import servicios.Agendamlg_Service;
import servicios.Evento;

/**
 *
 * @author johncarlo
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    private List<Evento> eventos;
    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        this.obtenerListaEventos();
    }
    
    public void obtenerListaEventos(){
        this.eventos = this.buscarEventosNoCaducados();
    }

    private java.util.List<servicios.Evento> buscarTodosLosEventos() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarTodosLosEventos();
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    private java.util.List<servicios.Evento> buscarEventosNoCaducados() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosNoCaducados();
    }
}
