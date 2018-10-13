package com.wwb.paotui.api;

import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.wwb.paotui.bean.Account;
import com.wwb.paotui.bean.AddrBean;
import com.wwb.paotui.bean.Bean;
import com.wwb.paotui.bean.Data;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.bean.Order;
import com.wwb.paotui.bean.Userinfo;
import com.wwb.paotui.constant.Constant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {
    //获取急救知识列表
//    @FormUrlEncoded
//    //  @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
//    @POST(Api.Login.sLogin)
//    Observable<RetrofitResponse> postLogin(
//            @FieldMap Map<String, String> params);

    @GET("access_token?")
    Call<ResponseBody> getOpenId(
            @Query("appid") String appid,
            @Query("secret") String secret,
            @Query("code") String code,
            @Query("grant_type") String grant_type
    );

    @GET(Api.User.sGetVerifyCode)
    Observable<RetrofitResponse> getCode(
            @Query("telephone") String telephone
    );

    @GET(Api.User.sGetRunnerDetail)
    Observable<ResponseBody> getRunnerDetail(
            @Query("orderId") String orderId,
            @Header("token") String token);

    @GET(Api.User.sIsBindWechat)
    Observable<ResponseBody> isBindWechat(
            @Query("telephone") String telephone,
            @Query("code") String code
    );

    @FormUrlEncoded
    @POST(Api.User.sBindWechat)
    Observable<ResponseBody> bindWechat(
            @Field("telephone") String telephone,
            @Field("code") String code ,
            @Field("type") int type ,
            @Field("openId") String openId

    );


    @FormUrlEncoded
    @POST(Api.User.sPostRunnerCancel)
    Observable<ResponseBody> postRunnerCancel(
            @Field("id") String id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sPostRunnerComplain)
    Observable<ResponseBody> postRunnerComplain(
            @Field("orderId") String orderId,
            @Field("complainRank") String complainRank,
            @Field("complainSubstance") String complainSubstance,
            @Header("token") String token
    );


    @GET(Api.User.sGetMostUser)
    Observable<ResponseBody> getMostLocate(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sWechatLogin)
    Observable<ResponseBody> loginByWechat(

            @Field("type") int type ,
            @Field("openId") String openId
    );
    @GET(Api.User.sGetRunnerOrders)
    Observable<ResponseBody> getRunnerOrders(
            @Query("pn") int pn,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sPostAddSeller)
    Observable<RetrofitResponse> postAddSeller(
            @Field("merchantName") String merchantName,
            @Field("contactName") String contactName,
            @Field("contactNumber") String contactNumber,
            @Field("contactLocation") String contactLocation,
            @Header("token") String token
    );

    @GET(Api.Message.sGetToday)
    Observable<RetrofitResponse<Data<News>>> getToday(
            @Query("page") int page
    );

    @GET(Api.Message.sGetMine)
    Observable<RetrofitResponse<Data<News>>> getMine(
            @Query("page") int page,
            @Header("token") String token
    );

    @GET(Api.User.sGetUserInfo)
    Observable<RetrofitResponse<Userinfo>> getUserinfo(
            @Header("token") String token
    );


    @GET(Api.Message.sGetDetail)
    Observable<RetrofitResponse<News>> getDetail(
            @Query("id") String id
    );

    @GET(Api.Message.sGetTypeMsg)
    Observable<RetrofitResponse<Data<News>>> getMsgByType(
            @Query("page") int page,
            @Query("categoryId") String categoryId
    );

    @GET(Api.Message.sGetBySearch)
    Observable<RetrofitResponse<Data<News>>> getSearchMsg(
            @Query("page") int page,
            @Query("content") String content
    );

    @GET(Api.Comment.sGetComment)
    Observable<RetrofitResponse<Bean>> getComment(
            @Query("messageId") String messageId,
            @Query("page") int page
    );

    @GET(Api.Remove.sGetMine)
    Observable<RetrofitResponse<Data<Order>>> getMyOrder(
            @Query("page") int page,
            @Header("token") String token
    );

    @GET(Api.Remove.sGetDistance)
    Observable<RetrofitResponse> getDistance(
            @Query("longitudeFrom") double longitudeFrom,
            @Query("latitudeFrom") double latitudeFrom,
            @Query("longitudeTo") double longitudeTo,
            @Query("latitudeTo") double latitudeTo
    );


    /****************************************************Seller*****************************************************/


    @GET(Api.Seller.getHomepage)
    Observable<ResponseBody> getHomePage(
            @Header("token") String token);


    @GET(Api.Seller.getInProcess)
    Observable<ResponseBody> getInProcess(
            @Header("token") String token);


    @GET(Api.Seller.getFinished)
    Observable<ResponseBody> getFinished(
            @Query("pn") int pn,
            @Header("token") String token);


    @GET(Api.Seller.getOrderDetail)
    Observable<ResponseBody> getOrderDetail(
            @Query("orderId") String orderId,
            @Header("token") String token);

    @GET(Api.Seller.getPesonalCenter)
    Observable<ResponseBody> getPesonalCenter(
            @Header("token") String token);

    @GET(Api.Seller.getNotice)
    Observable<ResponseBody> getNotice(
            @Query("pn") int pn,
            @Header("token") String token);


    @FormUrlEncoded
    @POST(Api.Seller.addName)
    Observable<ResponseBody> addName(
            @Field("name") String name,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.addTel)
    Observable<ResponseBody> addTel(
            @Field("telNum") String telNum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.addMap)
    Observable<ResponseBody> addMap(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("adress") String adress,
            @Field("heading") String heading,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.add)
    Observable<ResponseBody> addOrder(
            @FieldMap Map<String, Object> params,
            @Header("token") String token
    );


    @POST(Api.Seller.add)
    Observable<ResponseBody> addOneCall(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.cancel)
    Observable<ResponseBody> cancel(
            @Field("id") String id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.complain)
    Observable<ResponseBody> complain(
            @Field("orderId") String orderId,
            @Field("complainRank") String complainRank,
            @Field("complainSubstance") String complainSubstance,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST(Api.Seller.refuseCancel)
    Observable<ResponseBody> refuseCancel(
            @Field("id") String id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.agreeCancel)
    Observable<ResponseBody> agreeCancel(
            @Field("id") String id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Seller.apply)
    Observable<ResponseBody> apply(
            @Field("amount") int amount,
            @Field("receiveMethod") String receiveMethod,
            @Field("name") String name,
            @Field("account") String account,
            @Header("token") String token
    );


    /****************************************************POST请求*****************************************************/


    @FormUrlEncoded
    @POST(Api.Message.sPostPublish)
    Observable<RetrofitResponse> postPublic(
            @FieldMap Map<String, Object> params,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Message.sPostEdit)
    Observable<RetrofitResponse> postEdit(
            @FieldMap Map<String, Object> params,
//            @Field("id") String id,
//            @Field("image1") String image1,
//            @Field("image2") String image2,
//            @Field("image3") String image3,
//            @Field("image4") String image4,
//            @Field("image5") String image5,
//            @Field("image6") String image6,
//            @Field("linkman") String linkman,
//            @Field("contactInfomation") String contactInfomation,
//            @Field("content") String content,
            @Header("token") String token
    );
    @FormUrlEncoded
    @POST(Api.User.sAddRunner)
    Observable<ResponseBody> addRunner(
            @FieldMap Map<String, Object> params,
            @Header("token") String token
    );
    @Multipart
    @POST(Api.User.sUpLoadImg)
    Observable<RetrofitResponse> postHeadImg(
            @Part() MultipartBody.Part image
    );

    @Multipart
    @POST(Api.User.sUpLoadVideo)
    Observable<RetrofitResponse> upLoadVideo(
            @Part() MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST(Api.User.sPostRegister)
    Observable<RetrofitResponse> postRegister(
            @Field("telephone") String telephone,
            @Field("code") String code,
            @Field("password") String password,
            @Field("openId") String openId
    );

    @FormUrlEncoded
    @POST(Api.User.sPostLogin)
    Observable<RetrofitResponse<Userinfo>> postLogin(
            @Field("account") String account,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Api.User.sPostResetPwd)
    Observable<RetrofitResponse> postResetPwd(
            @Field("telephone") String account,
            @Field("code") String code,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Api.User.sPostSugg)
    Observable<RetrofitResponse> postSugg(
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sPostUpdateHead)
    Observable<RetrofitResponse> postUpdateHead(
            @Field("heading") String heading,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Message.sPostDel)
    Observable<RetrofitResponse> postDelMsg(
            @Field("id") String id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sPostUpdateName)
    Observable<RetrofitResponse> postUpdateName(
            @Field("name") String name,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Comment.sPostAdd)
    Observable<RetrofitResponse> postAddComment(
            @Field("messageId") String messageId,
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Comment.sPostInform)
    Observable<RetrofitResponse> postInformComment(
            @Field("commentId") String commentId,
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Comment.sPostReply)
    Observable<RetrofitResponse> postReplyComment(
            @Field("commentId") String commentId,
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Message.sPostMsgInform)
    Observable<RetrofitResponse> postInformMsg(
            @Field("messageId") String messageId,
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Remove.sPostAdd)
    Observable<RetrofitResponse> postAdd(
            @Field("longitudeFrom") double longitudeFrom,
            @Field("latitudeFrom") double latitudeFrom,
            @Field("addressFrom") String addressFrom,
            @Field("addressFromDetail") String addressFromDetail,
            @Field("floorFrom") String floorFrom,
            @Field("longitudeTo") double longitudeTo,
            @Field("latitudeTo") double latitudeTo,
            @Field("addressTo") String addressTo,
            @Field("floorTo") String floorTo,
            @Field("telephoneNum") String telephoneNum,
            @Field("removeTime") String removeTime,
            @Field("note") String note,
            @Field("addressToDetail") String addressToDetail,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Remove.sPostAdd)
    Observable<ResponseBody> removeAdd(
            @FieldMap Map<String, Object> params,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Remove.sAddCityOrder)
    Observable<RetrofitResponse> addCityOrder(
            @FieldMap Map<String, Object> params,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.User.sGetCash)
    Observable<RetrofitResponse> getMoney(
            @Field("distance") double distance
    );
}
