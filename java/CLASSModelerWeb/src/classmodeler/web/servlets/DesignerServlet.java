/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import classmodeler.service.util.GenericUtils;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.sharing.mxSession;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;

/**
 * Servlet implementation class DesignerServlet that handles the requests from
 * the mxGraph client side component, processes notifications when the graph is
 * modified by adding, editing or removing elements.
 * 
 * @author Gabriel Leonardo Diaz, 02.05.2013.
 */
@WebServlet("/Designer")
public class DesignerServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  
  private static final String SESSION_ID = "MXSESSIONID";
  
  private static mxSharedState sharedResource = new mxSharedState("<mxGraphModel><root><Workflow label=\"Diagram\" id=\"0\"></Workflow><Layer label=\"Default Layer\" id=\"1\"><mxCell parent=\"0\" /></Layer></root></mxGraphModel>");
  
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
  /**
   * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    mxSession session = getSession(request);
    String query = getQueryString(request);
    
    if (GenericUtils.equals(query, "init") || GenericUtils.equals(query, "poll")) {
      response.setContentType("text/xml;charset=UTF-8");
      response.setHeader("Pragma", "no-cache"); // HTTP 1.0
      response.setHeader("Cache-control", "private, no-cache, no-store");
      response.setHeader("Expires", "0");
      
      PrintWriter writer = response.getWriter();
      
      try {
        if (GenericUtils.equals(query, "init")) {
          String xml = session.init();
          writer.println(xml);
        }
        else {
          String xml = session.poll();
          writer.println(xml);
        }
      }
      catch (InterruptedException e) {
        throw new ServletException(e);
      }
      finally {
        writer.close();
      }
      
      response.setStatus(HttpServletResponse.SC_OK);
    }
    else if (GenericUtils.equals(query, "notify")) {
      String xml = URLDecoder.decode(request.getParameter("xml"), "UTF-8");
      Document doc = mxXmlUtils.parseXml(xml);
      session.receive(doc.getDocumentElement());
      response.setStatus(HttpServletResponse.SC_OK);
    }
    else if (GenericUtils.equals(query, "image")) {
      try {
        String xml = URLDecoder.decode(request.getParameter("xml"), "UTF-8");
        String width = request.getParameter("w");
        String height = request.getParameter("h");
        String bgParam = request.getParameter("bg");
        String filename = request.getParameter("filename");
        String format = request.getParameter("format");
        
        Color bg = mxUtils.parseColor(bgParam);
        
        if (xml != null && width != null && height != null && filename != null && format != null) {
          BufferedImage image = mxUtils.createBufferedImage(Integer.parseInt(width), Integer.parseInt(height), bg);
          Graphics2D g2 = image.createGraphics();
          mxUtils.setAntiAlias(g2, true, true);
          XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
          reader.setContentHandler(new mxSaxOutputHandler(new mxGraphicsCanvas2D(g2)));
          reader.parse(new InputSource(new StringReader(xml)));
          
          if (filename.length() == 0) {
            filename = "export." + format;
          }
          
          response.setContentType("image/" + format);
          response.setHeader("Content-Disposition", "attachment; filename=" + filename);
          ImageIO.write(image, format, response.getOutputStream());
          
          response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
//        ((Request) request).setHandled(true);
      }
      catch (Exception e) {
        throw new ServletException(e);
      }
    }
  }
  
  /**
   * Helper method that never returns null. If there is no query in the given
   * request then an empty string will be returned.
   */
  protected String getQueryString (HttpServletRequest request) {
    String query = request.getQueryString();
    if (query == null) {
      query = "";
    }
    return query;
  }
  
  /**
   * Returns the session for the given request.
   */
  protected mxSession getSession (HttpServletRequest request) {
    HttpSession httpSession = request.getSession(true);
    mxSession session = (mxSession) httpSession.getAttribute(SESSION_ID);
    
    if (session == null) {
      session = new mxBoundSession(httpSession.getId(), sharedResource);
      httpSession.setAttribute(SESSION_ID, session);
      httpSession.setMaxInactiveInterval(20);
    }
    
    return session;
  }

  /**
   * Implements a session with some lifecycle debugging output.
   */
  public class mxBoundSession extends mxSession implements HttpSessionBindingListener {
    
    public mxBoundSession (String id, mxSharedState diagram) {
      super(id, diagram);
    }
    
    public void valueBound(HttpSessionBindingEvent evt) {
      //System.out.println("session " + id + " created");
    }
    
    public void valueUnbound(HttpSessionBindingEvent evt) {
      //System.out.println("session " + id + " destroyed");
      destroy();
    }
    
  }
  
}
