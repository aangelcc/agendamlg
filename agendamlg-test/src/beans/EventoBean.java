package beans;

import servicios.*;

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
import java.util.stream.Collectors;

@RequestScoped
@Named
public class EventoBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Agendamlg/Agendamlg?wsdl")
    private Agendamlg_Service service;

    @Inject private CategoriaBean categoriaBean;

    private Evento evento = new Evento(), eventoEnEdicion = new Evento();
    private List<Evento> eventos = new ArrayList<>(), todosLosEventos;
    private boolean filtroCercania = false;
    private double x, y, radio;
    private String htmlId = null;

    public Evento getEvento() {
        return evento;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public Integer getEventoId() {
        return evento.getId();
    }

    public void setEventoId(Integer id) {
        evento.setId(id);
    }

    public List<Evento> getEventosDesdeServicio() {
        if(todosLosEventos == null) {
            todosLosEventos = service.getAgendamlgPort().buscarTodosLosEventos();
        }
        return todosLosEventos;
    }

    public Date getEventoFecha() {
        return evento.getFecha() == null ? null : evento.getFecha().toGregorianCalendar().getTime();
    }

    public void setEventoFecha(Date date) throws DatatypeConfigurationException {
        inside(evento, date);
    }

    public boolean isFiltroCercania() {
        return filtroCercania;
    }

    public void setFiltroCercania(boolean filtroCercania) {
        this.filtroCercania = filtroCercania;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public Evento getEventoEnEdicion() {
        if(eventoEnEdicion != null && eventoEnEdicion.getCreador() == null) eventoEnEdicion.setCreador(new Usuario());
        return eventoEnEdicion;
    }

    public void setEventoEnEdicion(Evento eventoEnEdicion) {
        this.eventoEnEdicion = eventoEnEdicion;
    }

    public Date getEventoEnEdicionFecha() {
        return eventoEnEdicion.getFecha() == null ? null : eventoEnEdicion.getFecha().toGregorianCalendar().getTime();
    }

    public void setEventoEnEdicionFecha(Date date) throws DatatypeConfigurationException {
        inside(eventoEnEdicion, date);
    }

    public String getHtmlId() {
        return htmlId;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public void eliminarEvento(Usuario u) throws AgendamlgException_Exception {
        service.getAgendamlgPort().eliminarEvento(u.getId(), evento);
        todosLosEventos = null;
        htmlId = "#eliminarEvento";
    }

    public String buscarEvento() {
        Evento evento = service.getAgendamlgPort().buscarEvento(this.evento.getId());
        if(evento != null) eventos.add(evento);
        htmlId = "#buscarEvento";
        return "verEvento";
    }

    public String buscarTodosLosEventos() {
        eventos.addAll(service.getAgendamlgPort().buscarTodosLosEventos());
        htmlId = "#buscarTodosLosEventos";
        return "verEvento";
    }

    public String buscarEventosUsuario(Usuario usuario) throws AgendamlgException_Exception {
        eventos.addAll(service.getAgendamlgPort().buscarEventosUsuario(usuario.getId()));
        htmlId = "#buscarEventosUsuario";
        return "verEvento";
    }

    public String buscarEventosTipoUsuario(Usuario usuario) {
        eventos.addAll(service.getAgendamlgPort().buscarEventosTipoUsuario(usuario.getId()));
        htmlId = "#buscarEventosTipoUsuario";
        return "verEvento";
    }

    public String validarEvento(Evento evento, Usuario usuario) throws AgendamlgException_Exception {
        service.getAgendamlgPort().validarEvento(evento.getId(), usuario.getId());
        htmlId = "#validarEvento";
        return null;
    }

    public String crearEventoTipoUsuario(Evento evento, Usuario usuario, List<Categoria> categorias) throws AgendamlgException_Exception {
        evento.setCreador(usuario);
        service.getAgendamlgPort().crearEventoTipoUsuario(evento, categorias);
        this.eventos.add(evento);
        evento.setCreador(service.getAgendamlgPort().buscarUsuario(usuario.getId()));
        htmlId = "#crearEventoTipoUsuario";
        return "verEvento";
    }

    public String buscarEventoCategorias(List<Categoria> categorias, Usuario usuario) {
        eventos.addAll(service.getAgendamlgPort().buscarEventoCategorias(categorias, usuario, filtroCercania, x, y, radio));
        htmlId = "#buscarEventoCategorias";
        return "verEvento";
    }

    public void prepararEdicion() {
        eventoEnEdicion = service.getAgendamlgPort().buscarEvento(evento.getId());
        categoriaBean.setCategoriasId(
                service
                        .getAgendamlgPort()
                        .obtenerCategoriasEvento(eventoEnEdicion)
                        .stream()
                        .map(Categoria::getId)
                        .map(Object::toString)
                        .collect(Collectors.toList())
        );
    }

    public String editarEvento(Evento evento, Usuario usuario, List<Categoria> categorias) throws AgendamlgException_Exception {
        service.getAgendamlgPort().actualizarEventoTipoUsuario(evento, categorias, usuario);
        this.eventos.add(evento);
        evento.setCreador(service.getAgendamlgPort().buscarUsuario(usuario.getId()));
        htmlId = "#editarEvento";
        return "verEvento";
    }

    public void deshacerEditarEvento() {
        eventoEnEdicion = new Evento();
        evento.setId(null);
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public String mostrarPrecio(Evento evento) {
        if(evento.getPrecio() != null) {
            return evento.getPrecio() + "€";
        } else {
            return "Gratis";
        }
    }

    public String mostrarValidado(Evento evento) {
        if(evento.getValidado() == 0) {
            return "No";
        } else {
            return "Si";
        }
    }

    public String mostrarTipo(Evento evento) {
        if(evento.getTipo() == 1) {
            return "Un dia";
        } else if(evento.getTipo() == 2) {
            return "Recurrente";
        } else if(evento.getTipo() == 3) {
            return "Fijo";
        } else {
            return "No se de que tipo es " + evento.getTipo();
        }
    }

    private void inside(Evento evento, Date date) throws DatatypeConfigurationException {
        if(date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            evento.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
        } else {
            evento.setFecha(null);
        }
    }

}
