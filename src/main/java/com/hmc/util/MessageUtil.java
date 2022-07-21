package com.hmc.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static void setInfoMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message);
		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}
	
	public static void setErrorMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message);
		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}
	
	public static void setWarnMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", message);
		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}
	
	public static void setFatalMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", message);
		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}

}
