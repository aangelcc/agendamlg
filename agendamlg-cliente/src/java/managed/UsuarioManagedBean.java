/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import servicios.Agendamlg_Service;
import servicios.Usuario;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import java.io.Serializable;

/**
 *
 * @author johncarlo
 */
@Named(value = "usuarioManagedBean")
@SessionScoped
public class UsuarioManagedBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    private String alias;
    private String mensajeDeError = null;
    // Es periodista? Se usa para saber los permisos que se tienen
    private boolean periodista = false;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String password;
    private int id = -1;
    /**
     * Creates a new instance of UsuarioManagedBean
     */
    public UsuarioManagedBean() {
    }

    public String iniciarSesion(){
        Usuario usuario = login(alias, password);
        if(usuario != null) {
            try {
                // asumismo que el periodista es el TIPO 3
                this.periodista = usuario.getTipo() == 3;
                mensajeDeError = null;
                id = usuario.getId();
                return "index";
            } catch(Exception e) {
                mensajeDeError = "Ha habido un error desconocido: " + e.getMessage() + ".";
            }
        } else {
            mensajeDeError = "El usuario no existe o la contraseña es incorrecta.";
        }
        return null;
    }
    
    public String cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        id = -1;
        // faces-redirect=true es necesario para actualizar adecuadamente la página
        // El funcionamiento https://www.mkyong.com/jsf2/jsf-page-forward-vs-page-redirect/
        return "/index.xhtml?faces-redirect=true";
    }
    
    private Usuario login(java.lang.String alias, java.lang.String password) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.login(alias, password);
    }

    public String getMensajeDeError() {
        return mensajeDeError;
    }

    public boolean getPeriodista() {
        return periodista;
    }

    public void setPeriodista(boolean periodista) {
        this.periodista = periodista;
    }
    
    
}
