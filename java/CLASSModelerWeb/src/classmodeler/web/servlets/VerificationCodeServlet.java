/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Java Servlet to process the verification codes sent to the user email
 * address.
 * 
 * @author Gabriel Leonardo Diaz, 23.05.2013.
 */
@WebServlet("/VerifyCode")
public class VerificationCodeServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public VerificationCodeServlet() {
    super();
  }
  
  /**
   * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String email = request.getParameter("email");
    String hashCode = request.getParameter("code");
    String type = request.getParameter("type");
    
    if (email !=  null && hashCode != null && type != null) {
      System.out.print("");
    }
    
    LifecycleFactory lFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    
    Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    
    FacesContextFactory fcFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    
    FacesContext context = fcFactory.getFacesContext(getServletContext(), request, response, lifecycle);
    
    Application app = context.getApplication();
    
    //app.createValueBinding("#{tabset.cartBean}").setValue(context, null);
    
    ViewHandler viewHandler = app.getViewHandler();
    
    UIViewRoot view = viewHandler.createView(context, "/index.xhtml");
    
    context.setViewRoot(view);
    
    lifecycle.render(context);
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

}
