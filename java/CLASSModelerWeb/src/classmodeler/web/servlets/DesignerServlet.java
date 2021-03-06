/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import classmodeler.web.controllers.DesignerControllerBean;
import classmodeler.web.util.JSFOutcomeUtil;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;

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
    
    DesignerControllerBean controller = (DesignerControllerBean) request.getSession().getAttribute("designerController");
    
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
          writer.println(controller.initialize());
        }
        else {
          writer.println(controller.poll());
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
      controller.notify(request.getParameter("xml"));
      response.setStatus(HttpServletResponse.SC_OK);
      break;
      
    case IMAGE:
      String xml      = URLDecoder.decode(request.getParameter("xml"), "UTF-8");
      String width    = request.getParameter("w");
      String height   = request.getParameter("h");
      String bgParam  = request.getParameter("bg");
      String filename = request.getParameter("filename");
      String format   = request.getParameter("format");
      Color bg        = mxUtils.parseColor(bgParam);
      
      try {
        BufferedImage image = mxUtils.createBufferedImage(Integer.parseInt(width), Integer.parseInt(height), bg);
        Graphics2D g2 = image.createGraphics();
        mxUtils.setAntiAlias(g2, true, true);
        XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setContentHandler(new mxSaxOutputHandler(new mxGraphicsCanvas2D(g2)));
        reader.parse(new InputSource(new StringReader(xml)));
        
        response.setContentType("image/" + format);
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        
        ImageIO.write(image, format, response.getOutputStream());
        
        response.setStatus(HttpServletResponse.SC_OK);
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
      
      break;
      
    case SAVE:
      controller.save();
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
    IMAGE ("image"),
    SAVE ("save");
    
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
