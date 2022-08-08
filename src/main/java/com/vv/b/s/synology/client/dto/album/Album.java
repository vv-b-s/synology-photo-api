package com.vv.b.s.synology.client.dto.album;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Album {
    private List<AlbumItem> list;
}
