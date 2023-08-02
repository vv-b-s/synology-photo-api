package com.vv.b.s.synology.client;

import com.vv.b.s.synology.client.dto.ApiRequestForm;
import com.vv.b.s.synology.client.dto.ResponsePayloadDTO;
import com.vv.b.s.synology.client.dto.album.Album;
import com.vv.b.s.synology.client.dto.album.AlbumItem;
import com.vv.b.s.synology.client.dto.album.Exif;
import com.vv.b.s.synology.client.exception.SynologyResponseException;
import com.vv.b.s.synology.client.params.ImageSize;
import com.vv.b.s.synology.client.params.Order;
import com.vv.b.s.synology.server.dto.FetchedImageData;
import com.vv.b.s.synology.session.UserSession;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static com.vv.b.s.synology.client.api.Api.FETCH_ALBUM;
import static com.vv.b.s.synology.client.api.Api.FETCH_EXIF;
import static com.vv.b.s.synology.client.api.Api.FETCH_IMAGE;
import static com.vv.b.s.synology.session.UserSession.SYNOLOGY_COOKIE_NAME;

@ApplicationScoped
public class PhotosResourceFacade {

    private static final Logger LOGGER = Logger.getLogger(PhotosResourceFacade.class);

    private static final String PARAMETER_FORMAT = "\"%s\"";

    @Inject
    @RestClient
    PhotosResource photosResource;

    @Inject
    UserSession userSession;

    @Inject
    @ConfigProperty(name = "synology.passphrase")
    String passphrase;

    public String getCookie() {
        try(var response = photosResource.getCookie(passphrase)) {
            return response.getCookies().get(SYNOLOGY_COOKIE_NAME).getValue();
        }
    }

    public Exif getImageExif(long imageId) {
        var sharingSid = userSession.getSynologyCookie();
        var form = new ApiRequestForm(String.format("[%s]", imageId), FETCH_EXIF.getApi(), FETCH_EXIF.getMethod(), FETCH_EXIF.getVersion(),
                FETCH_EXIF.getAdditionalReturnData(), null, null, null, null, passphrase);
        var response = photosResource.listAlbumItems(sharingSid, passphrase, form);
        if (response.getData().getList().size() != 0) {
            return response.getData().getList().get(0).getAdditional().getExif();
        }

        return null;
    }

    public ResponsePayloadDTO<Album> listAlbumItems(int start, int end, Order order) {
        var sharingSid = userSession.getSynologyCookie();
        var form = new ApiRequestForm(null, FETCH_ALBUM.getApi(), FETCH_ALBUM.getMethod(), FETCH_ALBUM.getVersion(),
                FETCH_ALBUM.getAdditionalReturnData(), start, end, "takentime", order.getQueryValue(), passphrase);
        return photosResource.listAlbumItems(sharingSid, passphrase, form);
    }

    public FetchedImageData getImage(AlbumItem albumItem, ImageSize size) {
        try(var response = photosResource.fetchImage(albumItem.getAdditional().getThumbnail().getUnitId(),
                formatParameter(albumItem.getAdditional().getThumbnail().getCacheKey()),
                formatParameter("unit"), formatParameter(size.toString()), formatParameter(passphrase),
                formatParameter(FETCH_IMAGE.getApi()), formatParameter(FETCH_IMAGE.getMethod()),
                FETCH_IMAGE.getVersion(), formatParameter(passphrase), userSession.getSynologyCookie())) {

            String image;
            try(var is = (InputStream) response.getEntity()) {
                byte[] entity = is.readAllBytes();

                if (response.getMediaType().toString().contains(MediaType.APPLICATION_JSON)){
                    LOGGER.errorf("Unable to fetch image: %s", new String(entity));
                    return null;
                }

                image = Base64.getEncoder().encodeToString(entity);
            } catch (IOException e) {
                LOGGER.error("Unable to parse image", e);
                return null;
            }

            return new FetchedImageData(image, albumItem.getAdditional().getExif(),
                    response.getHeaderString("Content-Type"), response.getHeaderString("Content-Disposition"));
        } catch (SynologyResponseException e) {
            LOGGER.error("Unable to obtain image", e);
            return null;
        }
    }

    private String formatParameter(String parameter) {
        return PARAMETER_FORMAT.formatted(parameter);
    }
}
