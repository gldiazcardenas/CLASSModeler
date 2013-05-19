/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classmodeler.domain.email.EVerificationType;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;

/**
 * Servlet implementation that allows to check the verification code sent to the
 * user email address.
 * 
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@WebServlet("/VerifyCode")
public class VerifyCodeServlet extends HttpServlet {
  
  @EJB
  private UserService userService;
  
  private static final long serialVersionUID = 1L;
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public VerifyCodeServlet() {
    super();
  }
  
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String code = request.getParameter("code");
    String email = request.getParameter("email");
    String action = request.getParameter("action");
    
    if (EVerificationType.ACTIVATE_ACCOUNT.toString().equals(action)) {
      User user = userService.getUserByEmail(email);
      userService.activateUserAccount(user, code);
    }
    
    request.getRequestDispatcher("/pages/portal/activatedAccount.xhtml").forward(request, response);
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
