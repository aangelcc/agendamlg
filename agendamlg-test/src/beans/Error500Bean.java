package beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@RequestScoped
@Named
public class Error500Bean {

    private HttpServletRequest r;

    public Throwable getThrowable() {
        return (Throwable) r.getAttribute("javax.servlet.error.exception");
    }

    public String getStackTrace() {
        Throwable throwable = getThrowable();
        if(throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            pw.close();
            return sw.getBuffer().toString();
        } else {
            return "";
        }
    }

    @PostConstruct
    public void init() {
        r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

}
