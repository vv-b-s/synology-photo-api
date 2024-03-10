package com.vv.b.s.synology.server.dto;

import com.vv.b.s.synology.client.params.ImageSize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ImageRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String thumbnailUnitId;

    @NotBlank
    private String fileName;

    @NotBlank
    private String cacheKey;

    @NotNull
    ImageSize imageSize;
}
