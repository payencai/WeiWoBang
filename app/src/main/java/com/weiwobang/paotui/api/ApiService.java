package com.weiwobang.paotui.api;

import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.weiwobang.paotui.bean.Account;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.Bean;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Order;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.constant.Constant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @FormUrlEncoded
    //  @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST(Api.Login.sLogin)
    Observable<RetrofitResponse> postLogin(
            @FieldMap Map<String, String> params);

    @GET(Api.User.sGetVerifyCode)
    Observable<RetrofitResponse> getCode(
            @Query("telephone") String telephone
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
            @Query("longitudeFrom") double longitudeFrom ,
            @Query("latitudeFrom") double latitudeFrom,
            @Query("longitudeTo") double longitudeTo,
            @Query("latitudeTo") double latitudeTo
    );

    @FormUrlEncoded
    @POST(Api.Message.sPostPublish)
    Observable<RetrofitResponse> postPublic(
            @Field("title") String title,
            @Field("categoryId") String categoryId,
            @Field("categoryName") String categoryName,
            @Field("image1") String image1,
            @Field("image2") String image2,
            @Field("image3") String image3,
            @Field("image4") String image4,
            @Field("image5") String image5,
            @Field("image6") String image6,
            @Field("linkman") String linkman,
            @Field("contactInfomation") String contactInfomation,
            @Field("content") String content,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST(Api.Message.sPostEdit)
    Observable<RetrofitResponse> postEdit(
            @Field("id") String id,
            @Field("image1") String image1,
            @Field("image2") String image2,
            @Field("image3") String image3,
            @Field("image4") String image4,
            @Field("image5") String image5,
            @Field("image6") String image6,
            @Field("linkman") String linkman,
            @Field("contactInfomation") String contactInfomation,
            @Field("content") String content,
            @Header("token") String token
    );

    @Multipart
    @POST(Api.User.sUpLoadImg)
    Observable<RetrofitResponse> postHeadImg(
            @Part() MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST(Api.User.sPostRegister)
    Observable<RetrofitResponse> postRegister(
            @Field("telephone") String telephone,
            @Field("code") String code,
            @Field("password") String password
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
}
