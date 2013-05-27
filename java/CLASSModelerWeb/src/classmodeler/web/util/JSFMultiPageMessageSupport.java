/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import classmodeler.service.util.CollectionUtils;

/**
 * Utility class to keep messages between different pages after redirect.
 * 
 * @author Gabriel Leonardo Diaz, 25.05.2013.
 */
public class JSFMultiPageMessageSupport implements PhaseListener {

  private static final long serialVersionUID = 1L;
  private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";

  @Override
  public void afterPhase(PhaseEvent event) {
    if (!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
      FacesContext facesContext = event.getFacesContext();
      this.saveMessages(facesContext);
    }
  }

  @Override
  public void beforePhase(PhaseEvent event) {
    FacesContext facesContext = event.getFacesContext();
    this.saveMessages(facesContext);
    
    if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
      if (!facesContext.getResponseComplete()) {
        this.restoreMessages(facesContext);
      }
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
  
  @SuppressWarnings("unchecked")
  private int saveMessages(final FacesContext facesContext) {
    List<FacesMessage> messages = new ArrayList<FacesMessage>();
    for (Iterator<FacesMessage> iter = facesContext.getMessages(JSFMessageBean.GENERAL_MESSAGE_ID); iter.hasNext();) {
      messages.add(iter.next());
      iter.remove();
    }
    
    if (CollectionUtils.isEmptyCollection(messages)) {
      return 0;
    }
    
    Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
    List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap.get(sessionToken);
    if (existingMessages != null) {
      existingMessages.addAll(messages);
    }
    else {
      sessionMap.put(sessionToken, messages);
    }
    return messages.size();
  }
  
  @SuppressWarnings("unchecked")
  private int restoreMessages(final FacesContext facesContext) {
    Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
    List<FacesMessage> messages = (List<FacesMessage>) sessionMap.remove(sessionToken);
    
    if (messages == null) {
      return 0;
    }
    
    int restoredCount = messages.size();
    
    for (Object element : messages) {
      facesContext.addMessage(JSFMessageBean.GENERAL_MESSAGE_ID, (FacesMessage) element);
    }
    return restoredCount;
  }
  
}
