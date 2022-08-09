package com.vv.b.s.synology.server.dto;

import com.vv.b.s.synology.client.dto.album.Exif;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FetchedImageData {
    String image;
    Exif exif;
    String contentType;
    String contentDescription;
}
