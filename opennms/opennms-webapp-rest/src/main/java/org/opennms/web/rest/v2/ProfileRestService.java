/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.rest.v2;

import org.apache.cxf.jaxrs.ext.search.SearchBean;
import org.opennms.core.config.api.JaxbListWrapper;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.netmgt.dao.api.ProfileDao;
import org.opennms.netmgt.model.OnmsProfile;
import org.opennms.netmgt.model.OnmsProfileList;
import org.opennms.web.rest.support.Aliases;
import org.opennms.web.rest.support.RedirectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.Collection;
import java.util.List;

/**
 * Basic Web Service using REST for {@link OnmsProfile} entity
 */
@Component
@Path("profiles")
@Transactional
public class ProfileRestService extends AbstractDaoRestService<OnmsProfile, SearchBean, Integer, String> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileRestService.class);

    @Autowired
    private ProfileDao m_dao;

    @Override
    protected ProfileDao getDao() {
        return m_dao;
    }

    @Override
    protected Class<OnmsProfile> getDaoClass() {
        return OnmsProfile.class;
    }

    @Override
    protected Class<SearchBean> getQueryBeanClass() {
        return SearchBean.class;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder(UriInfo uriInfo) {
        final CriteriaBuilder builder = new CriteriaBuilder(OnmsProfile.class, Aliases.profile.toString());
        return builder;
    }

    @Override
    protected JaxbListWrapper<OnmsProfile> createListWrapper(Collection<OnmsProfile> list) {
        return new OnmsProfileList(list);
    }

    @POST
    @Path("/{profileId}/applyToNodes")
    public Response applyProfiles(@Context final SecurityContext securityContext, @Context final UriInfo uriInfo,
                                  @PathParam("profileId") Integer id,
                                  @RequestBody List<String> nodesToApply) {
        writeLock();
        try {
            System.out.println("id = " + id);
            System.out.println("nodesToApply = " + nodesToApply);
            for (String node : nodesToApply) {
                System.out.println(" ==> node = " + node);
            }
            System.out.println(" ** uriinfo = " + uriInfo);
            final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            System.out.println("queryParameters = " + queryParameters);
            for (MultivaluedMap.Entry<String, List<String>> param : queryParameters.entrySet()) {
                System.out.println("param.key = " + param.getKey());
                System.out.println("param.value = " + param.getValue());
            }
            return Response.accepted().build();
        } finally {
            writeUnlock();
        }
    }


    @Override
    public Response doCreate(final SecurityContext securityContext, final UriInfo uriInfo, final OnmsProfile profile) {
        if (profile == null) {
            throw getException(Status.BAD_REQUEST, "Profile object cannot be null");
        }
        final Integer id = getDao().save(profile);
        return Response.created(RedirectHelper.getRedirectUri(uriInfo, id)).build();
    }

    @Override
    protected Response doUpdate(SecurityContext securityContext, UriInfo uriInfo, Integer key, OnmsProfile profile) {
        if (profile == null) {
            throw getException(Status.BAD_REQUEST, "Profile object cannot be null");
        }
        getDao().saveOrUpdate(profile);
        return Response.noContent().build();
    }

    @Override
    protected void doDelete(SecurityContext securityContext, UriInfo uriInfo, OnmsProfile profile) {
        getDao().delete(profile);
    }

    @Override
    protected OnmsProfile doGet(UriInfo uriInfo, String id) {
        return getDao().get(Integer.parseInt(id));
    }

}
