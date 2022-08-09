package com.vv.b.s.synology.client.dto.album;

import com.vv.b.s.synology.client.dto.common.Address;
import com.vv.b.s.synology.client.dto.common.Resolution;
import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
public class AdditionalItemData {
    private Resolution resolution;
    private Integer orientation;

    @JsonbProperty("orientation_original")
    private Integer orientationOriginal;

    private Thumbnail thumbnail;

    @JsonbProperty("provider_user_id")
    private Long providerUserId;

    private Exif exif;

    private Address address;
}
