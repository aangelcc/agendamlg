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

/**
 *
 * @author johncarlo
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    @Inject
    private UsuarioManagedBean usuarioManagedBean;
    
    // Lista de eventos a mostrar en el index
    private List<Evento> eventos;
    
    // El usuario ha seleccionado ordenar por distancia?
    private boolean ordenarPorDistancia;
    
    // Distancia entrada por el usuario
    // Posicion x
    private int dx;
    // Posicion y
    private int dy;
    
    // Radio escogido para hacer la busqueda de eventos
    private int radio;
    
    // Categorias disponibles para ser mostradas en el formulario
    private List<Categoria> categorias;
    
    // Categorias seleccionadas para filtrar
    private List<Categoria> seleccionCategorias;
   
    
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
        this.eventos = this.buscarEventosTipoUsuario(this.usuarioManagedBean.getId());
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    private java.util.List<servicios.Evento> buscarEventosTipoUsuario(int idUsuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosTipoUsuario(idUsuario);
    }
    
    // Metodos que obedezcan al filtrado de eventos
    
    // Mostrar de mi interes
    public String mostrarDeMiInteres(){
        
        return null;
    }
    
    // Mostrar de categorias seleccionadas
    public String mostrarDeCategoriasSeleccionadas(){
        
        return null;
    }
    
    // Setters y getters para el filtrado de eventos

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public List<Categoria> getSeleccionCategorias() {
        return seleccionCategorias;
    }

    public boolean getOrdenarPorDistancia() {
        return this.ordenarPorDistancia;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }
    
    
    
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setOrdenarPorDistancia(boolean ordenarPorDistancia) {
        this.ordenarPorDistancia = ordenarPorDistancia;
    }

    public void setSeleccionCategorias(List<Categoria> seleccionCategorias) {
        this.seleccionCategorias = seleccionCategorias;
    }
    
    
}
