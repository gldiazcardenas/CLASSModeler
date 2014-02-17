/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import classmodeler.web.controllers.DesignerControllerBean;
import classmodeler.web.util.JSFOutcomeUtil;

/**
 * Servlet implementation class DesignerServlet that handles the requests from
 * the mxGraph client side component, processes notifications when the graph is
 * modified by adding, editing or removing elements.
 * 
 * @author Gabriel Leonardo Diaz, 02.05.2013.
 */
@WebServlet(JSFOutcomeUtil.DESIGNER_SERVLET)
public class DesignerServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public DesignerServlet() {
    super();
  }
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
  /**
   * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    EDesignerAction action = getAction(request);
    if (action == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    
    DesignerControllerBean designerController = (DesignerControllerBean) request.getSession().getAttribute("designerController");
    
    switch (action) {
    case INIT:
    case POLL:
      response.setContentType("text/xml;charset=UTF-8");
      response.setHeader("Pragma", "no-cache"); // HTTP 1.0
      response.setHeader("Cache-control", "private, no-cache, no-store");
      response.setHeader("Expires", "0");
      
      PrintWriter writer = response.getWriter();
      
      try {
        if (action == EDesignerAction.INIT) {
          writer.println(designerController.init());
        }
        else {
          writer.println(designerController.poll());
        }
      }
      catch (InterruptedException e) {
        throw new ServletException(e);
      }
      finally {
        writer.close();
      }
      
      response.setStatus(HttpServletResponse.SC_OK);
      break;
      
    case NOTIFY:
      designerController.process(request.getParameter("xml"));
      response.setStatus(HttpServletResponse.SC_OK);
      break;
      
    case IMAGE:
      String xml        = request.getParameter("xml");
      String width      = request.getParameter("w");
      String height     = request.getParameter("h");
      String background = request.getParameter("bg");
      String filename   = request.getParameter("filename");
      String format     = request.getParameter("format");
      
      try {
        designerController.generateImage(xml, filename, format, background, Integer.parseInt(width), Integer.parseInt(height), response.getOutputStream());
      }
      catch (NumberFormatException e) {
        throw new ServletException(e);
      }
      catch (SAXException e) {
        throw new ServletException(e);
      }
      catch (ParserConfigurationException e) {
        throw new ServletException(e);
      }
      
      response.setContentType("image/" + format);
      response.setHeader("Content-Disposition", "attachment; filename=" + filename);
      response.setStatus(HttpServletResponse.SC_OK);
      break;
      
    default:
      break;
    }
  }
  
  private EDesignerAction getAction (HttpServletRequest request) {
    return EDesignerAction.getByName(request.getQueryString());
  }
  
  /**
   * Enumeration that defines the action kinds available for the designer.
   * 
   * @author Gabriel Leonardo Diaz, 15.02.2014.
   */
  protected static enum EDesignerAction {
    
    INIT ("init"),
    POLL ("poll"),
    NOTIFY ("notify"),
    IMAGE ("image");
    
    private String name;
    
    private EDesignerAction(String name) {
      this.name = name;
    }
    
    public String getName() {
      return name;
    }
    
    /**
     * Gets the action represented by the given name.
     * 
     * @param name
     *          The name to look for the action.
     * @return The respective action, null if the name does not match any.
     * @author Gabriel Leonardo Diaz, 15.02.2014.
     */
    public static EDesignerAction getByName (String name) {
      if (name != null) {
        for (EDesignerAction action : values()) {
          if (action.name.equals(name)) {
            return action;
          }
        }
      }
      
      return null;
    }
    
  }
  
}
