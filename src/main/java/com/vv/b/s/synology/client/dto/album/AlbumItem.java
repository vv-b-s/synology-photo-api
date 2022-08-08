package com.vv.b.s.synology.client.dto.album;

import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
public class AlbumItem {
    private Long id;

    @JsonbProperty("filename")
    private String fileName;

    @JsonbProperty("filesize")
    private String fileSize;
    private Long time;

    @JsonbProperty("indexed_time")
    private Long indexedTime;

    @JsonbProperty("owner_user_id")
    private Long ownerUserId;

    @JsonbProperty("folder_id")
    private Long folderId;

    private String type;

    private AdditionalItemData additional;
}
