package companytest3.applicationtest3;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Belal on 11/5/2015.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/insertEnquete.php")
    public void insertUser(
            @Field("ville") String ville,
            @Field("codePostal") String codePostal,
            @Field("raison") String raison,
            @Field("budget") String budget,
            @Field("trancheAge") String trancheAge,
            @Field("vip") Boolean vip,
            @Field("categorieSociale") String categorieSociale,
            Callback<Response> callback);
}
