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
    public void insertEnquete(
            @Field("ville") String ville,
            @Field("codePostal") String codePostal,
            @Field("raison") String raison,
            @Field("budget") String budget,
            @Field("trancheAge") String trancheAge,
            @Field("vip") Boolean vip,
            @Field("categorieSociale") String categorieSociale,
            @Field("bateau1") String bateau1,
            @Field("bateau2") String bateau2,
            @Field("bateau3") String bateau3,
            Callback<Response> callback);
}
