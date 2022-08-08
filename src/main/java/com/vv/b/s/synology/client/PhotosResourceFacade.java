package com.vv.b.s.synology.client;

import com.vv.b.s.synology.client.dto.ApiRequestForm;
import com.vv.b.s.synology.client.dto.ResponsePayloadDTO;
import com.vv.b.s.synology.client.dto.album.Album;
import com.vv.b.s.synology.client.dto.album.AlbumItem;
import com.vv.b.s.synology.client.dto.album.Exif;
import com.vv.b.s.synology.client.params.ImageSize;
import com.vv.b.s.synology.client.params.Order;
import com.vv.b.s.synology.session.UserSession;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;

import static com.vv.b.s.synology.client.api.Api.FETCH_ALBUM;
import static com.vv.b.s.synology.client.api.Api.FETCH_EXIF;
import static com.vv.b.s.synology.client.api.Api.FETCH_IMAGE;
import static com.vv.b.s.synology.session.UserSession.SYNOLOGY_COOKIE_NAME;

@ApplicationScoped
public class PhotosResourceFacade {

    @Inject
    @RestClient
    PhotosResource photosResource;

    @Inject
    UserSession userSession;

    @Inject
    @ConfigProperty(name = "synology.passphrase")
    String passphrase;

    public String getCookie() {
        var response = photosResource.getCookie(passphrase);
        return response.getCookies().get(SYNOLOGY_COOKIE_NAME).getValue();
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
        var response = photosResource.fetchImage(albumItem.getFileName(), albumItem.getId(),
                albumItem.getAdditional().getThumbnail().getCacheKey(), "unit", size.toString(), passphrase,
                FETCH_IMAGE.getApi(), FETCH_IMAGE.getMethod(), FETCH_IMAGE.getVersion(), passphrase,
                userSession.getSynologyCookie());

        return new FetchedImageData((InputStream) response.getEntity(), albumItem.getAdditional().getExif(),
                response.getHeaderString("Content-Type"), response.getHeaderString("Content-Disposition"));
    }


}
