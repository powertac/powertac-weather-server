package org.powertac.weatherserver;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ManagedBean
@RequestScoped
public class Rest implements PhaseListener {

	public void respond ()
  {
		HttpServletRequest r = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		if (r.getParameterMap().size() != 0) {
			String url = r.getRequestURL().toString();
			if (url.contains("index")) {
				HttpServletResponse response = (HttpServletResponse) FacesContext
						.getCurrentInstance().getExternalContext()
						.getResponse();
				response.setContentType("text/plain; charset=UTF-8");
				try {
					PrintWriter pw = response.getWriter();
					pw.print(Parser.parseRestRequest(r.getParameterMap()));
				}
        catch (IOException ex) {
					throw new FacesException(ex);
				}
				FacesContext.getCurrentInstance().responseComplete();
			}
		}
	}

	// Which jsf phase to intercept, in this case the Restore View Phase
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

  // Intercepts REST calls (get requests) and passes them to the Parser service
  // for parsing and returns the proper response
  public void beforePhase (PhaseEvent pe)
  {
  }

  public void afterPhase(PhaseEvent arg0)
  {
  }
}
