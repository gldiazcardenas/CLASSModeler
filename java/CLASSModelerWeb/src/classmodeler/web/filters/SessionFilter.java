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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import classmodeler.web.controllers.DesignerControllerBean;
import classmodeler.web.controllers.SessionControllerBean;
import classmodeler.web.util.JSFOutcomeUtil;

/**
 * Servlet Filter implementation to check the user session when a privileged
 * page is requested.
 * 
 * @author Gabriel Leonardo Diaz, 15.04.2013.
 */
@WebFilter(filterName="UserSessionValidationFilter", urlPatterns= {JSFOutcomeUtil.INDEX, JSFOutcomeUtil.PAGES_PATH})
public class SessionFilter implements Filter {
  
  /**
   * Default constructor.
   */
  public SessionFilter() {
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
    
    HttpServletRequest req  = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    
    final HttpSession session = req.getSession(false);
    String url = req.getServletPath();
    
    if (session != null) {
      SessionControllerBean sessionController = (SessionControllerBean) session.getAttribute("sessionController");
      DesignerControllerBean designerController = (DesignerControllerBean) session.getAttribute("designerController");
      
      if (sessionController == null || sessionController.getUser() == null) {
        if (url.contains(JSFOutcomeUtil.DASHBOARD_PATH) || url.contains(JSFOutcomeUtil.DESIGNER_PATH)) {
          // Forbidden access, redirect to the index page.
          session.setAttribute("from", req.getRequestURI());
          res.sendRedirect(req.getContextPath() + JSFOutcomeUtil.INDEX);
          return;
        }
      }
      else if (url.contains(JSFOutcomeUtil.INDEX) || url.contains(JSFOutcomeUtil.PORTAL_PATH)) {
        if (sessionController.isRegisteredUser()) {
          // Avoids the user get out without closing the session.
          session.setAttribute("from", req.getRequestURI());
          res.sendRedirect(req.getContextPath() + JSFOutcomeUtil.DASHBOARD);
          return;
        }
      }
      else if (url.contains(JSFOutcomeUtil.DASHBOARD_PATH) && !sessionController.isRegisteredUser()) {
        // DashBoard page is only for registered users.
        session.setAttribute("from", req.getRequestURI());
        res.sendRedirect(req.getContextPath() + JSFOutcomeUtil.DESIGNER);
        return;
      }
      else if (url.contains(JSFOutcomeUtil.DESIGNER_PATH) && (designerController == null || designerController.getDiagram() == null)) {
        // Requested the Designer Page but the data is invalid.
        req.getSession().setAttribute("from", req.getRequestURI());
        res.sendRedirect(req.getContextPath() + JSFOutcomeUtil.INDEX);
        return;
      }
    }
    // Forbidden access, redirect to the index page.
    else if (url.contains(JSFOutcomeUtil.DASHBOARD_PATH) || url.contains(JSFOutcomeUtil.DESIGNER_PATH)) {
      req.getSession().setAttribute("from", req.getRequestURI());
      res.sendRedirect(req.getContextPath() + JSFOutcomeUtil.INDEX);
      return;
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
