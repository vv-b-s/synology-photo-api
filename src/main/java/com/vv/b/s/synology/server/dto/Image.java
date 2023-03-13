package com.vv.b.s.synology.server.dto;

import com.vv.b.s.synology.client.params.ImageSize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Image {

    private String id;

    private String fileName;

    private String cacheKey;
    private String dateTaken;
    private String thumbnail;

    private String address;

    private List<ImageSize> availableSizes;
}
