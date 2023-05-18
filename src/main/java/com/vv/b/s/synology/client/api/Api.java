package com.vv.b.s.synology.client.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class serves as a dictionary to easily access API actions with constant parameters
 */
@Getter
@AllArgsConstructor
public enum Api {
    FETCH_ALBUM("SYNO.Foto.Browse.Item", "list", 1, List.of("thumbnail", "resolution","orientation","video_convert","video_meta","provider_user_id", "exif", "address", "description")),
    FETCH_IMAGE("SYNO.Foto.Thumbnail", "get", 2, null),
    FETCH_EXIF("SYNO.Foto.Browse.Item", "get", 2, List.of("exif"));

    private String api;
    private String method;
    private int version;

    @Getter(AccessLevel.NONE)
    private List<String> additionalReturnData;

    public String getApi() {
        return api;
    }

    public String getMethod() {
        return method;
    }

    public int getVersion() {
        return version;
    }

    public String getAdditionalReturnData() {
        return String.format("[%s]", additionalReturnData.stream()
                .map(entry -> String.format("\"%s\"", entry))
                .collect(Collectors.joining(", ")));
    }
}
