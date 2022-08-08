package com.vv.b.s.synology.client.params;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Order {
    ASCENDING("asc"), DESCENDING("desc");

    private String queryValue;
}
