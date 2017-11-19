package managed;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import servicios.AgendamlgException_Exception;
import servicios.Agendamlg_Service;
import servicios.Evento;
import servicios.Usuario;

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
        this.crearEventoTipoUsuario(evento);
        return "index";
    }
    
    private Usuario buscarUsuario(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        return port.buscarUsuario(id);
    }

    private void crearEventoTipoUsuario(servicios.Evento evento) throws AgendamlgException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        servicios.Agendamlg port = service.getAgendamlgPort();
        port.crearEventoTipoUsuario(evento);
    }
    
}
