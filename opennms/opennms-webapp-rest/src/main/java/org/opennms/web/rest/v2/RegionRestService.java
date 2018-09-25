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
import org.opennms.netmgt.dao.api.RegionDao;
import org.opennms.netmgt.model.OnmsRegion;
import org.opennms.netmgt.model.OnmsRegionList;
import org.opennms.web.rest.support.Aliases;
import org.opennms.web.rest.support.RedirectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

/**
 * Basic Web Service using REST for {@link OnmsRegion} entity
 */
@Component
@Path("regions")
@Transactional
public class RegionRestService extends AbstractDaoRestService<OnmsRegion, SearchBean, Integer, String> {

    private static final Logger LOG = LoggerFactory.getLogger(RegionRestService.class);

    @Autowired
    private RegionDao m_dao;

    @Override
    protected RegionDao getDao() {
        return m_dao;
    }

    @Override
    protected Class<OnmsRegion> getDaoClass() {
        return OnmsRegion.class;
    }

    @Override
    protected Class<SearchBean> getQueryBeanClass() {
        return SearchBean.class;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder(UriInfo uriInfo) {
        final CriteriaBuilder builder = new CriteriaBuilder(OnmsRegion.class, Aliases.region.toString());
        return builder;
    }

    @Override
    protected JaxbListWrapper<OnmsRegion> createListWrapper(Collection<OnmsRegion> list) {
        return new OnmsRegionList(list);
    }

    @Override
    public Response doCreate(final SecurityContext securityContext, final UriInfo uriInfo, final OnmsRegion region) {
        if (region == null) {
            throw getException(Status.BAD_REQUEST, "Region object cannot be null");
        }
        final Integer id = getDao().save(region);
        return Response.created(RedirectHelper.getRedirectUri(uriInfo, id)).build();
    }

    @Override
    protected Response doUpdate(SecurityContext securityContext, UriInfo uriInfo, Integer key, OnmsRegion region) {
        if (region == null) {
            throw getException(Status.BAD_REQUEST, "Region object cannot be null");
        }
        getDao().saveOrUpdate(region);
        return Response.noContent().build();
    }

    @Override
    protected void doDelete(SecurityContext securityContext, UriInfo uriInfo, OnmsRegion region) {
        getDao().delete(region);
    }

    @Override
    protected OnmsRegion doGet(UriInfo uriInfo, String id) {
        return getDao().get(Integer.parseInt(id));
    }

}
