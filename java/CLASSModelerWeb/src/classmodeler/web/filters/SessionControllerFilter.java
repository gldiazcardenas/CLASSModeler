/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import classmodeler.web.controllers.SessionControllerBean;

/**
 * Servlet Filter implementation to check the user session when a privileged
 * page is requested.
 * 
 * @author Gabriel Leonardo Diaz, 15.04.2013.
 */
@WebFilter(filterName="UserSessionValidationFilter", urlPatterns= {"/*"})
public class SessionControllerFilter implements Filter {
  
  /**
   * Default constructor.
   */
  public SessionControllerFilter() {
    super();
  }
  
  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    // Not used
  }
  
  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                   ServletException {
    
    final HttpSession session = ((HttpServletRequest) request).getSession(false);  
    if (session != null) {
      SessionControllerBean sessionController = (SessionControllerBean) session.getAttribute("sessionController");
      if (sessionController == null || sessionController.getLoggedUser() == null) {
        // TODO put the error
      }
    }
    else {
      // TODO put the error, there is no session and a privileged page was requested.
    }
    
    // pass the request along the filter chain
    chain.doFilter(request, response);
  }
  
  /**
   * @see Filter#init(FilterConfig)
   */
  public void init(FilterConfig config) throws ServletException {
    System.out.print("");
  }
  
}
