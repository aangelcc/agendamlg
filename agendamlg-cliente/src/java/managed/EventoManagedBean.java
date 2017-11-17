/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import servicios.Agendamlg_Service;
import servicios.Evento;

/**
 *
 * @author johncarlo
 */
@Named(value = "eventoManagedBean")
@RequestScoped
public class EventoManagedBean implements Serializable{

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;
    
    private Evento evento;

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
    
    @PostConstruct
    public void init(){
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        int eventoId = Integer.parseInt(params.get("eventoId"));
        this.evento = this.buscarEvento(eventoId);
    }

    private Evento buscarEvento(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEvento(id);
    }
    
}
