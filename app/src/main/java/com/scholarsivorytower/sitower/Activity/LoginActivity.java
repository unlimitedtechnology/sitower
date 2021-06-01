package com.scholarsivorytower.sitower.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scholarsivorytower.sitower.Model.LoginRegister.LoginRegiModel;
import com.scholarsivorytower.sitower.R;
import com.scholarsivorytower.sitower.Utility.PrefManager;
import com.scholarsivorytower.sitower.Webservice.AppAPI;
import com.scholarsivorytower.sitower.Webservice.BaseURL;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText et_fullname, et_email, et_password, et_phone;
    String str_fullname, str_email, str_password, str_phone;

    TextView txt_already_signup, txt_login, txt_skip, txt_forgot;

    ProgressDialog progressDialog;
    private PrefManager prefManager;

    ImageView iv_login_icon;
    InterstitialAd interstitial;

    LoginButton loginButton;
    ImageView fb, btn_google;

    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    private static final String EMAIL = "email";
    private static final String PROFILE = "public_profile";

    String fb_name, fb_email;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sign_in_button;
    FirebaseAuth mAuth;

    String str_firstname, str_lastname;
    MultipartBody.Part body;
    RequestBody first_name, last_name, email, password, type, mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginactivity);

        PrefManager.forceRTLIfSupported(getWindow(), LoginActivity.this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        Init();

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {
            //Using Graph API
            useLoginInformation(AccessToken.getCurrentAccessToken());
        }

        Log.e("interstital_ad", "" + prefManager.getValue("interstital_ad"));
        if (prefManager.getValue("interstital_ad").equalsIgnoreCase("yes")) {
            rewardAds();
        }

        Picasso.get().load(BaseURL.Image_URL + "" + prefManager.getValue("app_logo")).into(iv_login_icon);

        txt_already_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Registration.class));
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_email = et_email.getText().toString();
                str_password = et_password.getText().toString();

                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                email = RequestBody.create(MediaType.parse("text/plain"), "" + str_email);
                password = RequestBody.create(MediaType.parse("text/plain"), "" + str_password);
                type = RequestBody.create(MediaType.parse("text/plain"), "1");

                SignIn();
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getValue("interstital_ad").equalsIgnoreCase("yes")) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(PROFILE, EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("loginResult1", "Token::" + loginResult.getAccessToken());
                Log.e("loginResult", "" + loginResult.getAccessToken().getToken());
                AccessToken accessToken = loginResult.getAccessToken();
                Log.e("loginResult3", "" + accessToken);
                useLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("exception", "" + error.getMessage());
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            // This method is invoked everytime access token changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                useLoginInformation(currentAccessToken);
            }
        };

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    public void Init() {
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        txt_already_signup = findViewById(R.id.txt_already_signup);
        txt_login = findViewById(R.id.txt_login);
        txt_skip = findViewById(R.id.txt_skip);
        txt_forgot = findViewById(R.id.txt_forgot);

        iv_login_icon = findViewById(R.id.iv_login_icon);

        fb = findViewById(R.id.fb);
        btn_google = findViewById(R.id.btn_google);

    }

    public void SignIn() {
        if(!((Activity) LoginActivity.this).isFinishing()){
            progressDialog.show();
        }

        AppAPI bookNPlayAPI = BaseURL.getVideoAPI();
        Call<LoginRegiModel> call = bookNPlayAPI.login(first_name,
                last_name, email, type, mobile_number, password, body);
        call.enqueue(new Callback<LoginRegiModel>() {
            @Override
            public void onResponse(Call<LoginRegiModel> call, Response<LoginRegiModel> response) {
                Log.e("==>", "" + response.body());
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    if (response.body().getStatus() == 200) {
                        Log.e("email==>", "" + response.body());

                        prefManager.setFirstTimeLaunch(false);
                        prefManager.setLoginId("" + response.body().getResult().get(0).getId());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();

                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("" + getResources().getString(R.string.app_name))
                                .setMessage("" + response.body().getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Whatever...
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginRegiModel> call, Throwable t) {
                Log.e("Throwable", "" + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void rewardAds() {
        interstitial = new InterstitialAd(LoginActivity.this);
        interstitial.setAdUnitId(prefManager.getValue("interstital_adid"));
        interstitial.loadAd(new AdRequest.Builder().build());
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("onAdFailedToLoad2=>", "" + errorCode);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void onClick(View v) {
        if (v == btn_google) {
            Log.e("gmail", "perform");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        }
    }

    public void onClickFacebookButton(View view) {
        if (view == fb) {
            mGoogleSignInClient.signOut();
            Log.e("fb", "facebook");
            loginButton.performClick();
        }
    }

    private void useLoginInformation(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (object != null) {

                    String f_name = object.optString("first_name");
                    String l_name = object.optString("last_name");
                    fb_email = object.optString("email");
                    String id = object.optString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    Log.e("Firstname", "" + f_name);
                    Log.e("last_name", "" + l_name);
                    Log.e("fb_email", "" + fb_email);
                    Log.e("id", "" + id);
                    Log.e("image_url", "" + image_url);

                    fb_email = object.optString("email");
                    fb_name = f_name + l_name;

                    if (fb_email.length() == 0) {
                        fb_email = fb_name.trim() + "@facebook.com";
                    }
                    Log.e("name", "" + fb_name);
                    Log.e("email", "" + fb_email);

                    first_name = RequestBody.create(MediaType.parse("text/plain"), "" + fb_name);
                    last_name = RequestBody.create(MediaType.parse("text/plain"), "" + l_name);
                    email = RequestBody.create(MediaType.parse("text/plain"), "" + fb_email);
                    type = RequestBody.create(MediaType.parse("text/plain"), "2");
                    mobile_number = RequestBody.create(MediaType.parse("text/plain"), "");
                    password = RequestBody.create(MediaType.parse("text/plain"), "");

                    if (image_url != null)
                        new DownloadTask().execute("" + image_url);
                    else
                        SignIn();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Log.e("getDisplayName", "" + account.getDisplayName());
            Log.e("getEmail", "" + account.getEmail());
            Log.e("getIdToken", "" + account.getIdToken());
            Log.e("getPhotoUrl", "" + account.getPhotoUrl());

            str_firstname = "" + account.getDisplayName();
            str_lastname = "";
            str_email = "" + account.getEmail();

            first_name = RequestBody.create(MediaType.parse("text/plain"), "" + str_firstname);
            last_name = RequestBody.create(MediaType.parse("text/plain"), "" + str_lastname);
            email = RequestBody.create(MediaType.parse("text/plain"), "" + str_email);
            type = RequestBody.create(MediaType.parse("text/plain"), "2");
            mobile_number = RequestBody.create(MediaType.parse("text/plain"), "");
            password = RequestBody.create(MediaType.parse("text/plain"), "");

            if (account.getPhotoUrl() != null)
                new DownloadTask().execute("" + account.getPhotoUrl());
            else
                SignIn();

        } catch (ApiException e) {
            Log.e("ApiException", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
        }

        protected Bitmap doInBackground(String... url) {
            String imageURL = url[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            if (result != null) {
                new fileFromBitmap(result, getApplicationContext()).execute();
            } else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;
        String path_external = Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg";
        File file_thumbnail;

        public fileFromBitmap(Bitmap bitmap, Context context) {
            this.bitmap = bitmap;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            Log.e("ts", "" + ts);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            file_thumbnail = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "" + ts + ".jpg");
            try {
                FileOutputStream fo = new FileOutputStream(file_thumbnail);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file_thumbnail);
            body = MultipartBody.Part.createFormData("image", file_thumbnail.getName(), requestFile);

            Log.e("email", "" + email);

            SignIn();
        }
    }

}
