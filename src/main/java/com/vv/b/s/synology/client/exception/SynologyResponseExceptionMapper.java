package com.vv.b.s.synology.client.exception;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.ws.rs.core.Response;

public class SynologyResponseExceptionMapper implements ResponseExceptionMapper<SynologyResponseException> {

    @Override
    public SynologyResponseException toThrowable(Response response) {
        return new SynologyResponseException(response.getStatus(), response);
    }
}
