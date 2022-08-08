package com.vv.b.s.synology.session;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;

@Setter
@Getter
@RequestScoped
public class UserSession {
    public static final String SYNOLOGY_COOKIE_NAME = "sharing_sid";

    private String synologyCookie;

}
