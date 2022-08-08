package com.vv.b.s.synology.client.dto.album;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
public class Thumbnail {
    private String m;
    private String xl;
    private String preview;
    private String sm;

    @JsonbProperty("cache_key")
    private String cacheKey;

    @JsonbProperty("unit_id")
    private Long unitId;
}
