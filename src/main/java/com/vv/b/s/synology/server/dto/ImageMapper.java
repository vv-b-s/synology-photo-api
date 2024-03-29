package com.vv.b.s.synology.server.dto;

import com.vv.b.s.synology.client.PhotosResourceFacade;
import com.vv.b.s.synology.client.dto.album.AlbumItem;
import com.vv.b.s.synology.client.dto.album.Exif;
import com.vv.b.s.synology.client.dto.album.Thumbnail;
import com.vv.b.s.synology.client.dto.common.Address;
import com.vv.b.s.synology.client.params.ImageSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import jakarta.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ImageMapper {

    private static final String IMAGE_SIZE_AVAILABLE_STATUS = "ready";
    private static final String MAP_AVAILABLE_SIZES = "mapAvailableSizes";
    private static final String MAP_IMAGE_EXIF = "mapImageExif";
    private static final String MAP_ADDRESS = "mapAddress";
    private static final String MAP_DATE_TAKEN = "mapDateTaken";

    @Inject
    PhotosResourceFacade photosFacade;

    @Mapping(target = "cacheKey", source = "additional.thumbnail.cacheKey")
    @Mapping(target = "thumbnail", expression = "java(mapThumbnail(albumItem))")
    @Mapping(target = "dateTaken", source = "time", qualifiedByName = MAP_DATE_TAKEN)
    @Mapping(target = "address", source = "additional.address", qualifiedByName = MAP_ADDRESS)
    @Mapping(target = "availableSizes", source = "additional.thumbnail", qualifiedByName = MAP_AVAILABLE_SIZES)
    @Mapping(target = "description", source = "additional.description")
    @Mapping(target = "thumbnailUnitId", source = "additional.thumbnail.unitId")
    public abstract Image mapToImage(AlbumItem albumItem);

    @Mapping(target = "additional.thumbnail.cacheKey", source = "cacheKey")
    @Mapping(target = "additional.exif", source = "id", qualifiedByName = MAP_IMAGE_EXIF)
    @Mapping(target = "additional.thumbnail.unitId", source = "thumbnailUnitId")
    public abstract AlbumItem mapFromImageRequest(ImageRequest imageRequest);


    @Named(MAP_DATE_TAKEN)
    String mapDateTaken(Long time) {
        if (time != null) {
            return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else return null;
    }

    @Named(MAP_AVAILABLE_SIZES)
    List<ImageSize> mapAvailableSizes(Thumbnail thumbnail) {
        var sizes = new ArrayList<ImageSize>();
        var properties = thumbnail.getClass().getDeclaredFields();
        for (var property : properties) {
            try {
                var size = ImageSize.valueOf(property.getName());
                property.setAccessible(true);
                var status = property.get(thumbnail);
                if (IMAGE_SIZE_AVAILABLE_STATUS.equals(status)) {
                    sizes.add(size);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
            }
        }

        return sizes;
    }

    @Named(MAP_IMAGE_EXIF)
    Exif mapImageExif(Long imageId) {
        return photosFacade.getImageExif(imageId);
    }

    @Named(MAP_ADDRESS)
    String mapImageAddress(Address address) {
        if (address != null) {
            var addressFields = List.of(address.getCountry(), address.getState(), address.getCounty(),
                    address.getCity(), address.getTown(), address.getDistrict(), address.getVillage(),
                    address.getRoute(), address.getLandmark());
            return addressFields.stream()
                    .filter(af -> af != null && af.length() > 0)
                    .collect(Collectors.joining(", "));
        } else {
            return null;
        }
    }

    String mapThumbnail(AlbumItem albumItem) {
        return Optional.ofNullable(photosFacade.getImage(albumItem, ImageSize.m))
                .map(FetchedImageData::getImage)
                .orElse(null);
    }
}
