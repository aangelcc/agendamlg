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
import java.util.ArrayList;
import java.util.List;

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
    private double dx;
    // Posicion y
    private double dy;

    // Radio escogido para hacer la busqueda de eventos
    private double radio;

    // Categorias disponibles para ser mostradas en el formulario
    private List<Categoria> categorias;

    // Categorias seleccionadas para filtrar
    private List<String> seleccionCategorias;

    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
    }

    @PostConstruct
    public void init() {
        this.obtenerListaEventos();

        // Si el usuario ha iniciado sesion
        if (this.usuarioManagedBean.getId() != -1) {
            try {
                List<Categoria> categoriasList = buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId()));

                for (Categoria categoria : categoriasList) {
                    this.categoriasInteresado += " <" + categoria.getNombre() + ">";
                }
            } catch(AgendamlgException_Exception ignore) {} //No va a pasar, es un usuario que ha iniciado sesion
        }

    }

    public void obtenerListaEventos() {
        this.eventos = this.buscarEventosTipoUsuario(this.usuarioManagedBean.getId());
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
    
    public String mostrarTodosLosEventos(){
        this.eventos = this.buscarEventosTipoUsuario(this.usuarioManagedBean.getId());
        return null;
    }

    private java.util.List<servicios.Evento> buscarEventosTipoUsuario(int idUsuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosTipoUsuario(idUsuario);
    }

    // Metodos que obedezcan al filtrado de eventos
    // Mostrar de mi interes
    public String mostrarDeMiInteres() throws AgendamlgException_Exception {
        this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), buscarUsuario(usuarioManagedBean.getId()), this.ordenarPorDistancia, this.dx,this.dy, this.radio);
        return null;
    }

    // Mostrar de categorias seleccionadas
    public String mostrarDeCategoriasSeleccionadas() throws AgendamlgException_Exception {
        // Si no se han seleccionado categorias se muestran las preferencias de usuario
        Usuario usuarioSesion = buscarUsuario(usuarioManagedBean.getId());
        if (this.seleccionCategorias.isEmpty()) {
            if (usuarioSesion != null) {
                this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), usuarioSesion, this.ordenarPorDistancia, this.dx,this.dy, this.radio);
            }
        } else {

            // Necesario para pasar de una lista con ids de categorias a una lista propiamente de
            // categorias
            List<Categoria> categoriasSeleccionadas = new ArrayList<>();

            for (String categoriaID : this.seleccionCategorias) {
                categoriasSeleccionadas.add(buscarCategoria(Integer.parseInt(categoriaID)));
            }

            this.eventos = buscarEventoCategorias(categoriasSeleccionadas, usuarioSesion, this.ordenarPorDistancia, this.dx,this.dy, this.radio);
        }
        
        return null;
    }

    // Setters y getters para el filtrado de eventos
    public List<Categoria> getCategorias() {
        return categorias;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public List<String> getSeleccionCategorias() {
        return seleccionCategorias;
    }

    public void setSeleccionCategorias(List<String> seleccionCategorias) {
        this.seleccionCategorias = seleccionCategorias;
    }

    public boolean getOrdenarPorDistancia() {
        return this.ordenarPorDistancia;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setOrdenarPorDistancia(boolean ordenarPorDistancia) {
        this.ordenarPorDistancia = ordenarPorDistancia;
    }

    private java.util.List<servicios.Categoria> buscarPreferenciasUsuario(servicios.Usuario usuario) throws AgendamlgException_Exception {
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

    

    public String getCategoriasSeleccionadasString() {
        return categoriasSeleccionadasString;
    }

    public void setCategoriasSeleccionadasString(String categoriasSeleccionadasString) {
        this.categoriasSeleccionadasString = categoriasSeleccionadasString;
    }

    private Categoria buscarCategoria(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarCategoria(id);
    }

    private java.util.List<servicios.Evento> buscarEventoCategorias(java.util.List<servicios.Categoria> categorias, servicios.Usuario usuario, boolean filtrarCategorias, double coordenadaX, double coordenadaY, double radio) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventoCategorias(categorias, usuario, filtrarCategorias, coordenadaX, coordenadaY, radio);
    }

    

}
