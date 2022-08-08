package com.vv.b.s.synology.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePayloadDTO<T> {
    private Boolean success;
    private T data;
}
