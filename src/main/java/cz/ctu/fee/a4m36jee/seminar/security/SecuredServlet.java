/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cz.ctu.fee.a4m36jee.seminar.security;

import cz.ctu.fee.a4m36jee.seminar.security.ejb.TestBean;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A simple servlet that to show basic security features of Java EE.
 *
 * @author Peter Skopek
 */
@WebServlet(name = "SecuredServlet", urlPatterns = { "/secured/" }, loadOnStartup = 1)
@DeclareRoles({"gooduser", "superuser"})
@ServletSecurity(@HttpConstraint(rolesAllowed = { "gooduser", "superuser" }))
// NOTE: don't forget to create realm role "gooduser" and assign it to the user you are using
public class SecuredServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private TestBean testBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.write("<html>");
        writer.println("<head>");
        writer.println("<title>Secured Servlet Example</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=\"white\">");
        String lgo = req.getParameter("logout");
        if (lgo != null && lgo.equalsIgnoreCase("true")) {
            writer.write("Logged Out. Refresh to log <a href=\".\">in</a>.");
            req.logout();
        } else {
            writer.write("GOOD: ");
            writer.write("<a href=\"?logout=true\">Click here to logout</a><br/>");
            writer.write(servletRequestInfo(req));
            ejbCall(resp);
            writer.write(testBean.sessionContextInfo());
        }
        writer.println("</body>");
        writer.println("</html>");
    }

    private void ejbCall(HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("1. PermitAll: ");
        try {
            writer.write(testBean.echo("successful call to echo method<br/>"));
        } catch (EJBAccessException e) {
            writer.write("call to echo method failed<br/>");
        }

        writer.write("2. gooduser: ");
        try {
            writer.write(testBean.goodUserEcho("successful call to goodUserEcho method<br/>"));
        } catch (EJBAccessException e) {
            writer.write("call to goodUserEcho method failed<br/>");
        }

        writer.write("3. superuser: ");
        try {
            writer.write(testBean.superUserEcho("successful call to superUserEcho method<br/>"));
        } catch (EJBAccessException e) {
            writer.write("call to superUserEcho method failed<br/>");
        }
    }

    private String servletRequestInfo(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================<br/>");
        sb.append("From servlet<br/>");
        sb.append("========================================================<br/>");
        sb.append("RemoteUser: ").append(req.getRemoteUser()).append("<br/>");
        sb.append("Principal: ").append(req.getUserPrincipal()).append("<br/>");
        sb.append("User is in superuser role: ").append(req.isUserInRole("superuser")).append("<br/>");
        sb.append("========================================================<br/>");

        return sb.toString();
    }

}
