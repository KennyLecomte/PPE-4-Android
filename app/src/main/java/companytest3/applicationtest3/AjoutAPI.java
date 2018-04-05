package companytest3.applicationtest3;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Belal on 11/5/2015.
 */
public interface AjoutAPI {
    @FormUrlEncoded
    @POST("/insertBateau")
    public void insertBateau(
            @Field("modele") String modele,
            @Field("categorie") String categorie,
            Callback<Response> callback);
}