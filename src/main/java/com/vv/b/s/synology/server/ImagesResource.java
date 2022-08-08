package com.vv.b.s.synology.server;

import com.vv.b.s.synology.client.PhotosResourceFacade;
import com.vv.b.s.synology.client.params.Order;
import com.vv.b.s.synology.server.dto.Image;
import com.vv.b.s.synology.server.dto.ImageMapper;
import com.vv.b.s.synology.server.dto.ImageRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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
        return photosResource.listAlbumItems(start, end, order)
                .getData().getList().stream()
                .map(imageMapper::mapToImage)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getImage(@Valid @NotNull ImageRequest imageRequest) {
        var albumItem = imageMapper.mapFromImageRequest(imageRequest);
        var imageResponse = photosResource.getImage(albumItem, imageRequest.getImageSize());
        var exif = JsonbBuilder.create().toJson(imageResponse.getExif());
        return Response.ok(imageResponse.getImageInputStream())
                .header("Content-Type", imageResponse.getContentType())
                .header("Content-Disposition", imageResponse.getContentDescription())
                .header("X-EXIF-DATA", exif)
                .build();
    }
}
