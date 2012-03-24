package org.powertac.weatherserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FacesException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Rest implements PhaseListener {


	public void afterPhase(PhaseEvent arg0) {

	}

	// Intercepts REST calls (get requests) and passes them to the Parser service
	// for parsing and returns the proper response
	public void beforePhase(PhaseEvent pe) {
		// System.out.println(pe.getFacesContext().getViewRoot().getViewId());
		HttpServletRequest r = (HttpServletRequest) pe.getFacesContext()
				.getExternalContext().getRequest();
		if (r.getParameterMap().size() != 0) {
			String url = r.getRequestURL().toString();
			if (url.indexOf("index") > 0) {
				System.out.println("index.xhtml");
				return;
				// Parse incoming rest calls from brokers
			} else if (url.indexOf("brokerLogin") > 0) {
				System.out.println("brokerLogin.jsp");
				HttpServletResponse response = (HttpServletResponse) pe
						.getFacesContext().getExternalContext().getResponse();

				response.setContentType("text/plain; charset=UTF-8");
				try {
					PrintWriter pw = response.getWriter();
					pw.print(Parser.parseRestRequest(r.getParameterMap()));
				} catch (IOException ex) {
					throw new FacesException(ex);
				}
				pe.getFacesContext().responseComplete();

				// Parse incoming rest calls from severs, such as
				// checkBroker(name,token), request config, request pom...
			} else if (url.indexOf("serverInterface") > 0) {
				System.out.println("serverInterface.jsp");
				HttpServletResponse response = (HttpServletResponse) pe
						.getFacesContext().getExternalContext().getResponse();

				response.setContentType("text/plain; charset=UTF-8");
				try {
					PrintWriter pw = response.getWriter();
					pw.print(Parser.parseRestRequest(r.getParameterMap()));
				} catch (IOException ex) {
					throw new FacesException(ex);
				}
				pe.getFacesContext().responseComplete();

			} else {
				return;
			}
		}

	}

	// Which jsf phase to intercept, in this case the Restore View Phase
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
