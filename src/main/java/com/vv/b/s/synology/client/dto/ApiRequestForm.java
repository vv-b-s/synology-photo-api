package com.vv.b.s.synology.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.ws.rs.FormParam;

@Getter
@AllArgsConstructor
public class ApiRequestForm {

    @FormParam("id")
    private String id;

    @FormParam("api")
    private String api;

    @FormParam("method")
    private String method;

    @FormParam("version")
    private Integer version;

    @FormParam("additional")
    private String additional;

    @FormParam("offset")
    private Integer offset;

    @FormParam("limit")
    private Integer limit;

    @FormParam("sort_by")
    private String sort;

    @FormParam("sort_direction")
    private String order;

    @FormParam("passphrase")
    private String passphrase;
}
