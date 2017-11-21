/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
            List<Categoria> categoriasList = buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId()));

            for (Categoria categoria : categoriasList) {
                this.categoriasInteresado += " <" + categoria.getNombre() + ">";
            }

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

    private java.util.List<servicios.Evento> buscarEventosTipoUsuario(int idUsuario) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarEventosTipoUsuario(idUsuario);
    }

    // Este metodo gestiona la ordenacion de eventos por distancia si asi se ha solicitado
    private void hacerOrdenacionPorDistancia(){
        if (this.ordenarPorDistancia) {
            // Se almacenan los eventos ordenados por distancia (de cercano a lejano)
            // La clave del mapa es la que se usa para la ordenacion
            Map<Double, Evento> mapa = new TreeMap<>();

            // Se procede a rellnar el mapa
            for (Evento evento : this.eventos) {
                double distAEvento = distanciaAEvento(this.dx, this.dy, evento);
                
                // Solo se meten en el mapa aquellos eventos que esten dentro del radio
                if(distAEvento < this.radio){
                mapa.put(distAEvento, evento);
                }
            }

            // A continuacion se obtiene una lista ordenada de los eventos que se han introducido en el map
            // Se vacia la lista de eventos
            this.eventos.clear();

            for (Map.Entry<Double, Evento> entrada : mapa.entrySet()) {
                this.eventos.add(entrada.getValue());
            }

        }
    }
    
    // Metodos que obedezcan al filtrado de eventos
    // Mostrar de mi interes
    public String mostrarDeMiInteres() {
        this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), buscarUsuario(usuarioManagedBean.getId()));

        // El usuario decide ordenar los eventos de mas cercano a mas lejano
        hacerOrdenacionPorDistancia();

        return null;
    }

    // Mostrar de categorias seleccionadas
    public String mostrarDeCategoriasSeleccionadas() {
        // Si no se han seleccionado categorias se muestran las preferencias de usuario
        Usuario usuarioSesion = buscarUsuario(usuarioManagedBean.getId());
        if (this.seleccionCategorias.isEmpty()) {
            if (usuarioSesion != null) {
                this.eventos = buscarEventoCategorias(buscarPreferenciasUsuario(buscarUsuario(usuarioManagedBean.getId())), usuarioSesion);
                hacerOrdenacionPorDistancia();
            }
        } else {

            // Necesario para pasar de una lista con ids de categorias a una lista propiamente de
            // categorias
            List<Categoria> categoriasSeleccionadas = new ArrayList<>();

            for (String categoriaID : this.seleccionCategorias) {
                categoriasSeleccionadas.add(buscarCategoria(Integer.parseInt(categoriaID)));
            }

            this.eventos = buscarEventoCategorias(categoriasSeleccionadas, usuarioSesion);
            hacerOrdenacionPorDistancia();
        }
        
        return null;
    }

    // Dada una ubicacion (x,y) y un evento devuelve la distancia hasta ese evento
    private double distanciaAEvento(int x, int y, Evento evento) {
        // Obtener coordenadas x e y del evento
        String[] coordenadas = evento.getDireccion().split(",");
        int eventoX = Integer.parseInt(coordenadas[0]);
        int eventoY = Integer.parseInt(coordenadas[1]);
        return distanciaEntreDosPuntos(x, y, eventoX, eventoY);
    }

    private double distanciaEntreDosPuntos(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
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

    public List<String> getSeleccionCategorias() {
        return seleccionCategorias;
    }

    public void setSeleccionCategorias(List<String> seleccionCategorias) {
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

    private Categoria buscarCategoria(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarCategoria(id);
    }

}
