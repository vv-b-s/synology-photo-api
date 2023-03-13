package com.vv.b.s.synology.client;

import com.vv.b.s.synology.client.dto.ApiRequestForm;
import com.vv.b.s.synology.client.dto.ResponsePayloadDTO;
import com.vv.b.s.synology.client.dto.album.Album;
import com.vv.b.s.synology.client.exception.SynologyResponseException;
import com.vv.b.s.synology.client.exception.SynologyResponseExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.Form;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Path("/webapi/entry.cgi/{fileName}")
    Response fetchImage(@PathParam("fileName") String fileName, @QueryParam("id") long id,
                           @QueryParam("cache_key") String cacheKey, @QueryParam("type") String type,
                           @QueryParam("size") String size, @QueryParam("passphrase") String passphrase,
                           @QueryParam("api") String api, @QueryParam("method") String method,
                           @QueryParam("version") int version, @QueryParam("_sharing_id") String sharingId,
                           @CookieParam("sharing_sid") String sharingSid) throws SynologyResponseException;

}
