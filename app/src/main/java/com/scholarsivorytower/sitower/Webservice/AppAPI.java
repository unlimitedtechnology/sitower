package com.scholarsivorytower.sitower.Webservice;


import com.scholarsivorytower.sitower.Model.AuthorModel.AuthorModel;
import com.scholarsivorytower.sitower.Model.BannerModel.BannerModel;
import com.scholarsivorytower.sitower.Model.BookModel.BookModel;
import com.scholarsivorytower.sitower.Model.CategoryModel.CategoryModel;
import com.scholarsivorytower.sitower.Model.CommentModel.CommentModel;
import com.scholarsivorytower.sitower.Model.GeneralSettings.GeneralSettings;
import com.scholarsivorytower.sitower.Model.LoginRegister.LoginRegiModel;
import com.scholarsivorytower.sitower.Model.ProfileModel.ProfileModel;
import com.scholarsivorytower.sitower.Model.ReadDowncntModel.ReadDowncntModel;
import com.scholarsivorytower.sitower.Model.SuccessModel.SuccessModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AppAPI {


    @FormUrlEncoded
    @POST("login")
    Call<LoginRegiModel> login(@Field("email") String email_id,
                               @Field("password") String password);

    @Multipart
    @POST("login")
    Call<LoginRegiModel> login(@Part("fullname") RequestBody fullname,
                               @Part("last_name") RequestBody last_name,
                               @Part("email") RequestBody email,
                               @Part("type") RequestBody type,
                               @Part("mobile_number") RequestBody mobile_number,
                               @Part("password") RequestBody password,
                               @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("login_fb")
    Call<LoginRegiModel> login_fb(@Field("email") String email_id);

    @FormUrlEncoded
    @POST("registration")
    Call<LoginRegiModel> Registration(@Field("fullname") String full_name,
                                      @Field("email") String email_id,
                                      @Field("password") String password,
                                      @Field("mobile") String phone);

    @FormUrlEncoded
    @POST("registration_fb")
    Call<LoginRegiModel> registration_fb(@Field("fullname") String full_name,
                                         @Field("email") String email_id);


    @GET("get_ads_banner")
    Call<BannerModel> get_ads_banner();

    @GET("general_setting")
    Call<GeneralSettings> general_settings();

    @GET("categorylist")
    Call<CategoryModel> categorylist();

    @FormUrlEncoded
    @POST("book_by_category")
    Call<BookModel> books_by_category(@Field("category_id") String cat_id);

    @GET("newarriaval")
    Call<BookModel> newarriaval();

    @GET("feature_item")
    Call<BookModel> feature_item();

    @GET("popularbooklist")
    Call<BookModel> popularbooklist();

    @GET("autherlist")
    Call<AuthorModel> autherlist();

    @FormUrlEncoded
    @POST("book_by_author")
    Call<BookModel> books_by_author(@Field("author_id") String a_id);

    @FormUrlEncoded
    @POST("bookdetails")
    Call<BookModel> bookdetails(@Field("book_id") String b_id,
                                @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("add_transaction")
    Call<SuccessModel> add_purchase(@Field("book_id") String book_id,
                                    @Field("user_id") String user_id,
                                    @Field("amount") String amount,
                                    @Field("currency_code") String currency_code,
                                    @Field("description") String short_description,
                                    @Field("state") String state,
                                    @Field("author_id") String author_id,
                                    @Field("payment_id") String payment_id);

    @FormUrlEncoded
    @POST("purchaselist")
    Call<BookModel> purchaselist(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("alldownload")
    Call<BookModel> alldownload(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("related_item")
    Call<BookModel> related_item(@Field("category_id") String fcat_id);

    @FormUrlEncoded
    @POST("add_download")
    Call<SuccessModel> add_download(@Field("user_id") String user_id,
                                    @Field("book_id") String b_id);

    @FormUrlEncoded
    @POST("profile")
    Call<ProfileModel> profile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("view_add_by_user")
    Call<SuccessModel> view_add_by_user(@Field("id") String id);

    @FormUrlEncoded
    @POST("download_add_by_user")
    Call<SuccessModel> download_add_by_user(@Field("id") String id);

    @FormUrlEncoded
    @POST("add_continue_read")
    Call<SuccessModel> add_continue_read(@Field("user_id") String user_id,
                                         @Field("book_id") String b_id);

    @FormUrlEncoded
    @POST("continue_read")
    Call<BookModel> continue_read(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("add_comment")
    Call<SuccessModel> add_comment(@Field("book_id") String b_id,
                                   @Field("user_id") String user_id,
                                   @Field("comment") String comment);

    @FormUrlEncoded
    @POST("view_comment")
    Call<CommentModel> view_comment(@Field("book_id") String b_id);

    @FormUrlEncoded
    @POST("add_bookmark")
    Call<SuccessModel> add_bookmark(@Field("user_id") String user_id,
                                    @Field("book_id") String b_id);

    @FormUrlEncoded
    @POST("all_bookmark")
    Call<BookModel> allBookmark(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("checkbookmark")
    Call<SuccessModel> checkbookmark(@Field("user_id") String user_id,
                                     @Field("book_id") String book_id);

    @FormUrlEncoded
    @POST("add_rating")
    Call<SuccessModel> give_rating(@Field("user_id") String user_id,
                                   @Field("book_id") String book_id,
                                   @Field("rating") String rating);

    @FormUrlEncoded
    @POST("readcount_by_author")
    Call<ReadDowncntModel> readcnt_by_author(@Field("author_id") String a_id);

    @FormUrlEncoded
    @POST("free_paid_booklist")
    Call<BookModel> free_paid_booklist(@Field("is_paid") String is_paid);

    @FormUrlEncoded
    @POST("update_profile")
    Call<SuccessModel> update_profile(@Field("user_id") String user_id,
                                      @Field("fullname") String fullname,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("mobile") String mobile_number);


}
