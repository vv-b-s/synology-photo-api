package com.vv.b.s.synology.client;

import com.vv.b.s.synology.client.dto.ApiRequestForm;
import com.vv.b.s.synology.client.dto.ResponsePayloadDTO;
import com.vv.b.s.synology.client.dto.album.Album;
import com.vv.b.s.synology.client.exception.SynologyResponseException;
import com.vv.b.s.synology.client.exception.SynologyResponseExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.Form;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mo/sharing")
@RegisterRestClient(configKey = "photos-api")
@RegisterProvider(SynologyResponseExceptionMapper.class)
public interface PhotosResource {

    @GET
    @Path("{passphrase}")
    Response getCookie(@PathParam("passphrase") String passphrase);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/webapi/entry.cgi/SYNO.Foto.Browse.Item")
    ResponsePayloadDTO<Album> listAlbumItems(@CookieParam("sharing_sid") String sharingSid,
                                             @HeaderParam("X-SYNO-SHARING") String passphrase,
                                             @Form ApiRequestForm requestForm);

    @GET
    @Path("/webapi/entry.cgi")
    Response  fetchImage(@QueryParam("id") long id,
                           @QueryParam("cache_key") String cacheKey, @QueryParam("type") String type,
                           @QueryParam("size") String size, @QueryParam("passphrase") String passphrase,
                           @QueryParam("api") String api, @QueryParam("method") String method,
                           @QueryParam("version") int version, @QueryParam("_sharing_id") String sharingId,
                           @CookieParam("sharing_sid") String sharingSid) throws SynologyResponseException;

}
