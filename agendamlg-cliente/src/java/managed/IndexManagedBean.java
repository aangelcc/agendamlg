/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import java.util.ArrayList;
import java.util.Arrays;
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
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    @Inject
    private UsuarioManagedBean usuarioManagedBean;
    
    // Lista de eventos a mostrar en el index
    private List<Evento> eventos;
    
    // Una cadena que es una representacion de las categorias en las que el usuario
    // esta interesado
    private String categoriasInteresado = "";
    
    
    // Una cadena que describe las categorias que el usuario ha seleccionado
    private String categoriasSeleccionadasString = "";
    
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
    private Categoria[] seleccionCategorias;
   
    
    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        this.obtenerListaEventos();
        
        // Si el usuario ha iniciado sesion
        if(this.usuarioManagedBean.getId() != -1){
            List<Categoria> categoriasList = buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId()));
            
            for(Categoria categoria : categoriasList){
                this.categoriasInteresado += " <"+categoria.getNombre()+">";
            }
            
            if(this.seleccionCategorias != null){
            for(Categoria categoria: this.seleccionCategorias){
                this.categoriasSeleccionadasString += " <"+categoria.getNombre()+">";
            }
            }
        }
        
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
        this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), buscarUsuario(usuarioManagedBean.getId()));
    return null;
    }
    
    // Mostrar de categorias seleccionadas
    public String mostrarDeCategoriasSeleccionadas(){
        // Si no se han seleccionado categorias se muestran las preferencias de usuario
        Usuario usuarioSesion = buscarUsuario(usuarioManagedBean.getId());
        if(this.seleccionCategorias.length == 0){
        this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), usuarioSesion);
        }
        else{
            List<Categoria> listaCategoriasMetodo = new ArrayList<Categoria>(Arrays.asList(this.seleccionCategorias));
            this.eventos = buscarEventoCategorias(listaCategoriasMetodo, usuarioSesion);
        }
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

    public Categoria[] getSeleccionCategorias() {
        return seleccionCategorias;
    }

    public void setSeleccionCategorias(Categoria[] seleccionCategorias) {
        this.seleccionCategorias = seleccionCategorias;
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

    

    

    private java.util.List<servicios.Categoria> buscarPreferenciasUsuario(servicios.Usuario usuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarPreferenciasUsuario(usuario);
    }

    private Usuario buscarUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarUsuario(id);
    }

    public void setCategoriasInteresado(String categoriasInteresado) {
        this.categoriasInteresado = categoriasInteresado;
    }

    public String getCategoriasInteresado() {
        return categoriasInteresado;
    }

    private java.util.List<servicios.Evento> buscarEventoCategorias(java.util.List<servicios.Categoria> categorias, servicios.Usuario usuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventoCategorias(categorias, usuario);
    }

    public String getCategoriasSeleccionadasString() {
        return categoriasSeleccionadasString;
    }

    public void setCategoriasSeleccionadasString(String categoriasSeleccionadasString) {
        this.categoriasSeleccionadasString = categoriasSeleccionadasString;
    }
    
    
    
    
    
}
