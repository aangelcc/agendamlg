package managed;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import servicios.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author johncarlo
 */
@Named(value = "crearEventoManagedBean")
@RequestScoped
public class CrearEventoManagedBean {

    @Inject
    private UsuarioManagedBean usuarioManagedBean;
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Agendamlg/Agendamlg.wsdl")
    private Agendamlg_Service service;

    private short tipo;
    private String nombre;
    private String descripcion;
    private String fecha;
    private BigDecimal precio;
    private String direccion;
    private int idCreador;
    private List<Categoria> categorias;
    private List<Categoria> categoriasEvento = new ArrayList<>();
    private List<String> categoriasSeleccionadas = new ArrayList<>();

    public List<String> getCategoriasSeleccionadas() {
        return categoriasSeleccionadas;
    }

    public void setCategoriasSeleccionadas(List<String> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
    }

    public List<Categoria> getCategoriasEvento() {
        return categoriasEvento;
    }

    public void setCategoriasEvento(List<Categoria> categoriasEvento) {
        this.categoriasEvento = categoriasEvento;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public int getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(int idCreador) {
        this.idCreador = idCreador;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    /**
     * Creates a new instance of EventoManagedBean
     */
    public CrearEventoManagedBean() {
    }
    
    @PostConstruct
    public void init() {
       this.setIdCreador(usuarioManagedBean.getId());
       this.categorias = this.buscarTodasLasCategorias();
    }
    
    public String subirEvento() throws ParseException, DatatypeConfigurationException, AgendamlgException_Exception{
        Evento evento = new Evento();
        evento.setTipo(tipo);
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        
        DateFormat format = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date date = format.parse(fecha);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        evento.setFecha(xmlGregCal);
        
        evento.setPrecio(precio);
        evento.setDireccion(direccion);
        evento.setCreador(buscarUsuario(idCreador));
        
        this.categoriasSeleccionadas.forEach((i) -> categoriasEvento.add(this.buscarCategoria(Integer.parseInt(i))));
        
        this.crearEventoTipoUsuario(evento,categoriasEvento);
        return "index";
    }
    
    private Usuario buscarUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarUsuario(id);
    }

    private java.util.List<servicios.Categoria> buscarTodasLasCategorias() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarTodasLasCategorias();
    }

    private Categoria buscarCategoria(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarCategoria(id);
    }

    private void crearEventoTipoUsuario(servicios.Evento evento, java.util.List<servicios.Categoria> categoriasEvento) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        port.crearEventoTipoUsuario(evento, categoriasEvento);
    }
    
    
}
