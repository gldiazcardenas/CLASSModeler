/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DesignerServlet that handles the requests from
 * the designer graph.
 */
@WebServlet("/Designer")
public class DesignerServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public DesignerServlet() {
    super();
  }
  
  /**
   * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/xml;charset=UTF-8");
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setHeader("Cache-control", "private, no-cache, no-store");
    response.setHeader("Expires", "0");
    response.setStatus(HttpServletResponse.SC_OK);
  }
  
  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    service(request, response);
  }
  
}
