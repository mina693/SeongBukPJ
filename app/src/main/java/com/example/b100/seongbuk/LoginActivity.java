package com.example.b100.seongbuk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    SessionCallback callback;
    private CallbackManager callbackManager;
/*
    public static final String NICKNAME = "nick";
    public static final String USER_ID = "id";
    public static final String PROFILE_IMG = "img";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        /**         카 카 오 톡        **/

        //자동 로그인 위해 token 다시 받기
        Session.getCurrentSession().checkAccessTokenInfo();
        //처음 로그인 이후 세션 열려있는경우 로그인 버튼을 누르지 않아도 다음 화면으로 이동
        if (Session.getCurrentSession().isOpened() == true) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

      /*  UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });*/

        callback = new SessionCallback();

        Session.getCurrentSession().addCallback(callback);

        /**          페 이 스 북        **/
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인 성공 시 로그인한 사용자의 일련번호, 닉네임, 이미지url 리턴
                    //사용자 캐시 정보 업데이트 - 별 필요 없는듯
                    if (userProfile != null) {
                        userProfile.saveUserToCache();
                    }
                    Logger.e("succeeded to update user profile", userProfile, "\n");
                    //////////////////

                    /*final String nickName = userProfile.getNickname();//닉네임
                    final long userID = userProfile.getId();//사용자 고유번호
                    final String pImage = userProfile.getProfileImagePath();//사용자 프로필 경로*/
                    Log.e("UserProfile", userProfile.toString());//전체 정보 출력

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    /*intent.putExtra(NICKNAME,nickName);
                    intent.putExtra(USER_ID,String.valueOf(userID));
                    intent.putExtra(PROFILE_IMG,pImage);*/
                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴

    }

}


/**
 * 참조 : http://blog.naver.com/yunjeong0547/220977069777 *
 **/