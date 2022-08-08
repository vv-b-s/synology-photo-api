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

    private byte[] thumbnail;

    private List<ImageSize> availableSizes;
}
