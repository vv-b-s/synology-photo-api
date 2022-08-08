package com.vv.b.s.synology.session;

import com.vv.b.s.synology.client.PhotosResourceFacade;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

import static com.vv.b.s.synology.session.UserSession.SYNOLOGY_COOKIE_NAME;

@Provider
public class SynologyCookieFilter implements ContainerResponseFilter, ContainerRequestFilter {

    @Inject
    UserSession userSession;

    @Inject
    PhotosResourceFacade photosResource;

    //On Request
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        var synologyCookie = Optional.ofNullable(containerRequestContext.getCookies().get(SYNOLOGY_COOKIE_NAME))
                .map(Cookie::getValue)
                .orElseGet(photosResource::getCookie);
        userSession.setSynologyCookie(synologyCookie);
    }

    //On Response
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        containerResponseContext.getHeaders().add("Set-Cookie", new NewCookie(SYNOLOGY_COOKIE_NAME, userSession.getSynologyCookie()));
    }
}
