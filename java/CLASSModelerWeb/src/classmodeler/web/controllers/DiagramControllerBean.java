/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.DiagramService;
import classmodeler.service.UserService;
import classmodeler.service.util.CollectionUtils;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.FormatControllerBean;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller to manage the interactions of the user with the form to
 * create and edit projects.
 * 
 * @author Gabriel Leonardo Diaz, 30.05.2013.
 */
@ManagedBean(name="diagramController")
@ViewScoped
public class DiagramControllerBean extends JSFGenericBean implements JSFFormControllerBean, Converter {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Default representation of the diagram XML.
   */
  public static final String DEFAULT_XML_DIAGRAM = new StringBuilder("<mxGraphModel>")
                                                                .append("<root>")
                                                                   .append("<Workflow label=\"UML\" id=\"0\" />")
                                                                   .append("<Layer label=\"ClassDiagram\" id=\"1\"><mxCell parent=\"0\" /></Layer>")
                                                                .append("</root>")
                                                             .append("</mxGraphModel>")
                                                             .toString();
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @ManagedProperty("#{sessionController.diagrammer}")
  private Diagrammer diagrammer;
  
  @ManagedProperty("#{formatController}")
  private FormatControllerBean formatController;
  
  @EJB
  private DiagramService diagramService;
  
  @EJB
  private UserService userService;
  
  private String name;
  private String description;
  private String title;
  private Diagram diagram;
  private EDiagramControllerMode mode;
  private List<Diagrammer> diagrammers;
  private Map<Integer, Diagrammer> cache = new HashMap<Integer, Diagrammer>();
  private boolean readOnly;
  
  public DiagramControllerBean() {
    super();
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setDashBoardController(DashboardControllerBean dashBoardController) {
    this.dashBoardController = dashBoardController;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public void setFormatController(FormatControllerBean formatController) {
    this.formatController = formatController;
  }
  
  public List<Diagrammer> getDiagrammers() {
    return diagrammers;
  }
  
  public void setDiagrammers(List<Diagrammer> diagrammers) {
    this.diagrammers = diagrammers;
  }
  
  public boolean isReadOnly() {
    return readOnly;
  }
  
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }
  
  /**
   * Prepares the controller to create a new diagram. Sets an empty object to
   * the controller.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareNewDiagram () {
    name        = null;
    description = null;
    diagram     = new Diagram();
    diagram.setXML(DEFAULT_XML_DIAGRAM);
    
    title       = GenericUtils.getLocalizedMessage("DIAGRAM_NEW_FORM_TITLE");
    mode        = EDiagramControllerMode.CREATE;
  }
  
  /**
   * Prepares the controller to edit a diagram by getting the selected diagram
   * instance {@link DashboardControllerBean#diagram}, if the object is null
   * this method does nothing.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareEditDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name        = diagram.getName();
      description = diagram.getDescription();
      title       = GenericUtils.getLocalizedMessage("DIAGRAM_EDIT_FORM_TITLE", name);
      mode        = EDiagramControllerMode.EDIT;
    }
  }
  
  /**
   * Prepares the controller to delete the selected diagram in the table, this
   * method takes the object from {@link DashboardControllerBean}.
   * 
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void prepareDeleteDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name   = diagram.getName();
      title  = GenericUtils.getLocalizedMessage("DIAGRAM_DELETE_FORM_TITLE", name);
      mode   = EDiagramControllerMode.DELETE;
    }
  }
  
  /**
   * Prepares the controller to create a new diagram based on the information
   * copied from the selected diagram.
   * 
   * @author Gabriel Leonardo Diaz, 02.06.2013.
   */
  public void prepareCopyDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name           = diagram.getName();
      description    = diagram.getDescription();
      String copyXML = diagram.getXML();
      
      diagram = new Diagram();
      diagram.setXML(copyXML);
      
      title   = GenericUtils.getLocalizedMessage("DIAGRAM_COPY_FORM_TITLE", name);
      mode    = EDiagramControllerMode.COPY;
    }
  }
  
  /**
   * Prepares the controller to share a diagram with other users.
   * 
   * @author Gabriel Leonardo Diaz, 05.07.2013.
   */
  public void prepareShareDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name           = diagram.getName();
      title          = GenericUtils.getLocalizedMessage("DIAGRAM_SHARE_FORM_TITLE", name);
      mode           = EDiagramControllerMode.SHARE;
      readOnly       = false;
      diagrammers    = null;
    }
  }
  
  @Override
  public boolean isAllValid() {
    return diagram != null && diagrammer != null && !GenericUtils.isEmptyString(name);
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    try {
      switch (mode) {
      case CREATE:
      case COPY:
        diagram.setName(name);
        diagram.setDescription(description);
        diagram.setCreatedBy(diagrammer);
        diagramService.insertDiagram(diagram);
        dashBoardController.addDiagram(diagram);
        break;
        
      case EDIT:
        diagram.setName(name);
        diagram.setDescription(description);
        diagram.setModifiedBy(diagrammer);
        diagramService.updateDiagram(diagram);
        
        break;
      case DELETE:
        if (diagram.isOwner(diagrammer)) {
          diagramService.deleteDiagram(diagram.getKey());
        }
        else {
          diagramService.deleteSharedItem(diagram.getKey(), diagrammer.getKey());
        }
        
        dashBoardController.deleteDiagram(diagram);
        break;
      
      case SHARE:
        CollectionUtils.removeDuplicates(diagrammers);
        diagramService.shareDiagram(diagram, diagrammers, !readOnly);
        dashBoardController.setDiagram(diagram);// Reload shared items
        break;
        
      default:
        break;
      }
      
      diagram     = null;
      name        = null;
      description = null;
      title       = null;
      mode        = null;
    }
    catch (Exception e) {
      addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
    }
  }
  
  @Override
  public String process() {
    return null; // Not used.
  }
  
  /**
   * Generates an empty diagram with the default XML representation.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public static Diagram generateEmpty() {
    Diagram diagram = new Diagram();
    diagram.setKey(-1);
    diagram.setName(GenericUtils.getLocalizedMessage("NEW_DIAGRAM_NAME"));
    diagram.setXML(DEFAULT_XML_DIAGRAM);
    return diagram;
  }
  
  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    Integer diagrammerKey = Integer.valueOf(value);
    
    Diagrammer diagrammer = cache.get(diagrammerKey);
    
    if (diagrammer == null) {
      diagrammer = userService.getDiagrammerByKey(diagrammerKey.intValue());
      if (diagrammer != null) {
        cache.put(diagrammer.getKey(), diagrammer);
      }
    }
    
    return diagrammer;
  }
  
  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    return String.valueOf(((Diagrammer) value).getKey());
  }
  
  /**
   * Filters the diagrammers by the given query pattern.
   * 
   * @param query The query to filter the diagrammers.
   * @return A list of diagrammers result of the filter process.
   * @author Gabriel Leonardo Diaz, 29.05.2014
   */
  public List<Diagrammer> complete (String pattern) {
    List<Diagrammer> filtered = userService.filterDiagrammersToShareDiagram(diagram, pattern);
    
    // Store in cache the diagrammers loaded
    if (!CollectionUtils.isEmptyCollection(filtered)) {
      for (Diagrammer aDiagrammer : filtered) {
        cache.put(aDiagrammer.getKey(), aDiagrammer);
      }
    }
    
    return filtered;
  }
  
  /**
   * The operation modes of this controller.
   *
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public enum EDiagramControllerMode {
    CREATE,
    EDIT,
    DELETE,
    COPY,
    SHARE
  }

}
