package beans;

import servicios.Agendamlg_Service;
import servicios.Usuario;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class UsuarioBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Agendamlg/Agendamlg?wsdl")
    private Agendamlg_Service service;

    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios, rusuarios;
    private String htmlId = null;

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Integer getUsuarioId() {
        return usuario.getId();
    }

    public void setUsuarioId(Integer id) {
        usuario.setId(id);
    }

    public List<Usuario> getRusuarios() {
        return rusuarios;
    }

    public String getHtmlId() {
        return htmlId;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    @PostConstruct
    public void construit() {
        usuarios = new ArrayList<>();
        rusuarios = new ArrayList<>();
        usuarios.addAll(service.getAgendamlgPort().buscarTodosLosUsuarios());
    }

    public void parametrosxD() {
        if(usuario.getId() != null && rusuarios.size() == 0)
            rusuarios.add(service.getAgendamlgPort().buscarUsuario(usuario.getId()));
        htmlId = "javascript:history.back()";
    }

    public String buscarUsuario() {
        Usuario u = service.getAgendamlgPort().buscarUsuario(usuario.getId());
        if(u != null) rusuarios.add(u);
        htmlId = "#buscarUsuario";
        return "verUsuario";
    }

    public String buscarTodosLosUsuarios() {
        rusuarios.addAll(usuarios);
        htmlId = "#buscarTodosLosUsuarios";
        return "verUsuario";
    }

    public void login() {
        Usuario logged = service.getAgendamlgPort().login(usuario.getAlias(), usuario.getPassword());
        if(logged != null) usuario = logged;
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public String mostrarTipo(Usuario u) {
        if(u == null || u.getId() == -1) return "Anónimo";
        if(u.getTipo() == 1) return "Usuario";
        if(u.getTipo() == 2) return "Super-Usuario";
        if(u.getTipo() == 3) return "Periodista";
        return "Desconocido";
    }
}
