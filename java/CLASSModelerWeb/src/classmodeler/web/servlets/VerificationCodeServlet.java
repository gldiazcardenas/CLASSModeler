/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classmodeler.web.util.JSFOutcomeUtil;

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
    
    FacesContext context = FacesContext.getCurrentInstance();
    
    context.addMessage("generalMessage", null);
    
    request.getRequestDispatcher(JSFOutcomeUtil.INDEX).forward(request, response);
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
