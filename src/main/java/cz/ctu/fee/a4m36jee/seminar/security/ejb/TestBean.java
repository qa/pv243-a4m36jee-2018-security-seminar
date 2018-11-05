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
package cz.ctu.fee.a4m36jee.seminar.security.ejb;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class TestBean
 *
 * @author Peter Skopek
 */
@Stateless
@LocalBean
@DeclareRoles({ "superuser", "gooduser" })
public class TestBean {

    @Resource
    SessionContext ctx;

    @PermitAll
    public String echo(String whatToEcho) {
        return whatToEcho;
    }

    @RolesAllowed({"gooduser"})
    public String goodUserEcho(String whatToEcho) {
        return whatToEcho;
    }

    @RolesAllowed({"superuser"})
    public String superUserEcho(String whatToEcho) {
        return whatToEcho;
    }

    @PermitAll
    public String sessionContextInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------------------------------<br/>");
        sb.append("From EJB<br/>");
        sb.append("-----------------------------------------------------------------<br/>");
        sb.append(ctx.getCallerPrincipal()).append("<br/>");
        sb.append("Caller is in gooduser role = ").append(ctx.isCallerInRole("gooduser")).append("<br/>");
        sb.append("-----------------------------------------------------------------<br/>");

        return sb.toString();
    }

}
