package beans;

import servicios.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class CategoriaBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Agendamlg/Agendamlg?wsdl")
    private Agendamlg_Service service;
    private String htmlId;

    private List<Categoria> categorias = new ArrayList<>();
    private List<String> categoriasId = new ArrayList<>();
    private List<Categoria> rcategorias = new ArrayList<>();
    private Categoria categoria = new Categoria();

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Categoria> getCategoriasDesdeElServicio() {
        return service.getAgendamlgPort().buscarTodasLasCategorias();
    }

    public List<String> getCategoriasId() {
        return categoriasId;
    }

    public void setCategoriasId(List<String> list) {
        categoriasId.addAll(list);
        categoriasId.stream()
                .map(Integer::parseInt)
                .map(id -> service.getAgendamlgPort().buscarCategoria(id))
                .forEach(categorias::add);
    }

    public List<Categoria> getRcategorias() {
        return rcategorias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getHtmlId() {
        return htmlId;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public String buscarTodasLasCategorias() {
        rcategorias.addAll(service.getAgendamlgPort().buscarTodasLasCategorias());
        htmlId = "#buscarTodasLasCategorias";
        return "verCategoria";
    }

    public String buscarCategoria() {
        Categoria c = service.getAgendamlgPort().buscarCategoria(categoria.getId());
        if(c != null) rcategorias.add(c);
        htmlId = "#buscarCategoria";
        return "verCategoria";
    }

    public String buscarPreferenciasUsuario(Usuario u) throws AgendamlgException_Exception {
        rcategorias.addAll(service.getAgendamlgPort().buscarPreferenciasUsuario(u));
        htmlId = "#buscarPreferenciasUsuario";
        return "verCategoria";
    }

    public String obtenerCategoriasEvento(Evento e) {
        rcategorias.addAll(service.getAgendamlgPort().obtenerCategoriasEvento(e));
        htmlId = "#obtenrCategoriasEvento";
        return "verCategoria";
    }

}
