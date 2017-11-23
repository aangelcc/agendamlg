/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import servicios.AgendamlgException_Exception;
import servicios.Agendamlg_Service;
import servicios.Categoria;
import servicios.Evento;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceRef;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author antonio
 */
@Named(value = "editarEventoManagedBean")
// Es viewscoped para poder mantener adecuadamente los valores del formulario
@RequestScoped
public class editarEventoManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    // Inyecciones correspondientes
    @Inject
    private UsuarioManagedBean usuarioManagedBean;
    
    
    // Lista de categorias a las que asociar un evento, para mostrar en el formulario
    private List<Categoria> categorias;
    
    // En esta propiedad se almacenan las categorias seleccionadas para el evento
    private List<String> categoriasId;
    
    
    @PostConstruct
    public void init() {
       this.categorias = this.buscarTodasLasCategorias();
       this.categoriasId = new ArrayList<>();
       // Es necesario rellenar debidamente las categorias que el evento tiene asignadas
       // En primer lugar se obtienen las categorias de un evento
       List<Categoria> categoriasEvento = obtenerCategoriasEvento(usuarioManagedBean.getEventoEditado());
       
       // A continuación se recorrer la lista y se insertan los IDs en su debido sitio
       for(Categoria categoria : categoriasEvento){
           this.categoriasId.add(categoria.getId().toString());
       }
       
    }

    private java.util.List<servicios.Categoria> buscarTodasLasCategorias() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarTodasLasCategorias();
    }
    
    
    
    // A este método llama el command button del formulario de editar evento
    public String submitEvento() throws AgendamlgException_Exception{
        // Hay que convertir las ids de categorias en categorias para mandarlas al metodo adecuado
        List<Categoria> listaCategoriasSubmit = new ArrayList<>();
        
        for(String idCategoria : this.categoriasId){
            listaCategoriasSubmit.add(buscarCategoria(Integer.parseInt(idCategoria)));
        }
        
        // A continuacion se obtiene el usuario cuya id se ha seleccionado
        
        // Se procede a actualizar el evento
        actualizarEventoTipoUsuario(usuarioManagedBean.getEventoEditado(), listaCategoriasSubmit);
        // Se vuelve al index ya que se supone se ha editado correctamente
        // Y con redirección
        return "index?faces-redirect=true";
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<String> getCategoriasId() {
        return categoriasId;
    }

    public void setCategoriasId(List<String> categoriasId) {
        this.categoriasId = categoriasId;
    }

    

    private java.util.List<servicios.Categoria> obtenerCategoriasEvento(servicios.Evento evento) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.obtenerCategoriasEvento(evento);
    }

    private Categoria buscarCategoria(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarCategoria(id);
    }

    private void actualizarEventoTipoUsuario(servicios.Evento evento, java.util.List<servicios.Categoria> categoriasEvento) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        port.actualizarEventoTipoUsuario(evento, categoriasEvento, usuarioManagedBean.getUsuario());
    }
 
    public Date getEventoFecha() {
        Evento evento = usuarioManagedBean.getEventoEditado();
        return evento.getFecha() == null ? null : evento.getFecha().toGregorianCalendar().getTime();
    }

    public void setEventoFecha(Date date) throws DatatypeConfigurationException {
        if(date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            usuarioManagedBean.getEventoEditado().setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
        } else {
            usuarioManagedBean.getEventoEditado().setFecha(null);
        }
    }
    
}
