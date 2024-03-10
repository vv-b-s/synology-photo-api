package com.vv.b.s.synology.client.exception;

import lombok.Getter;

import jakarta.ws.rs.core.Response;

public class SynologyResponseException extends RuntimeException {

    @Getter
    private int status;

    @Getter
    private Response response;

    public SynologyResponseException(Integer status, Response response) {
        this.status = status;
        this.response = response;
    }

}
