package com.vv.b.s.synology.client.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String country;
    private String state;
    private String county;
    private String city;
    private String town;
    private String district;
    private String village;
    private String route;
    private String landmark;
}
