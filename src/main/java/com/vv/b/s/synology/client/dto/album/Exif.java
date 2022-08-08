package com.vv.b.s.synology.client.dto.album;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
public class Exif {
    private String aperture;

    private String camera;

    @JsonbProperty("exposure_time")
    private String exposureTime;

    @JsonbProperty("focal_length")
    private String focalLength;

    private String iso;

    private String lens;
}
