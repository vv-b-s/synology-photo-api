package com.vv.b.s.synology.server;

import com.vv.b.s.synology.client.PhotosResourceFacade;
import com.vv.b.s.synology.client.params.Order;
import com.vv.b.s.synology.server.dto.FetchedImageData;
import com.vv.b.s.synology.server.dto.Image;
import com.vv.b.s.synology.server.dto.ImageMapper;
import com.vv.b.s.synology.server.dto.ImageRequest;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
@Path("images")
public class ImagesResource {

    @Inject
    PhotosResourceFacade photosResource;

    @Inject
    ImageMapper imageMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Image> getImages(@QueryParam("start") @DefaultValue("0") int start,
                                 @QueryParam("end") @DefaultValue("20") int end,
                                 @QueryParam("order") @DefaultValue("ASCENDING") Order order) {
        return Optional.ofNullable(photosResource.listAlbumItems(start, end, order).getData())
                .map(data -> data.getList().stream()
                        .map(imageMapper::mapToImage)
                        .filter(i -> i.getThumbnail() != null) // Images without thumbnails are not ready to be shown
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FetchedImageData getImage(@Valid @NotNull ImageRequest imageRequest) {
        var albumItem = imageMapper.mapFromImageRequest(imageRequest);
        return photosResource.getImage(albumItem, imageRequest.getImageSize());
    }
}
