package com.vv.b.s.synology.client;

import com.vv.b.s.synology.client.dto.album.Exif;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class FetchedImageData {
    InputStream imageInputStream;
    Exif exif;
    String contentType;
    String contentDescription;
}
