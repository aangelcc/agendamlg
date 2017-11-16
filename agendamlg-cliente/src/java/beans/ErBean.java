package beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import servicios.Agendamlg_Service;
import servicios.Usuario;

@Named
@SessionScoped
public class ErBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    private int contarUsuarios() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.contarUsuarios();
    }
    
    public int getContarUsuarios() {
        return contarUsuarios();
    }
    
    public Usuario getJohn() {
        return service.getAgendamlgPort().buscarTodosLosUsuarios().get(0);
    }
    
}
