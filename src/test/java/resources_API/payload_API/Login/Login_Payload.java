package resources_API.payload_API.Login;

import pojo_API.RequestPojo.Login.Login_pojo;
import pojo_API.RequestPojo.Login.MobileLoginFirebase_pojo;
import pojo_API.RequestPojo.Login.ReqLoginOtp_pojo;
import pojo_API.RequestPojo.Login.ReqOtp_pojo;
import utilities_API.GetProperty_API;

public class Login_Payload {

    //  Assistive Login Payload
    public Login_pojo loginPayload() {
        Login_pojo loginpojo = new Login_pojo();
        loginpojo.setUsername(GetProperty_API.value("username"));
        loginpojo.setPassword(GetProperty_API.value("password"));
        return loginpojo;
    }


    //  1Channel login with test user creds
    public Login_pojo loginTestUserPayload() {
        Login_pojo loginpojo = new Login_pojo();
        loginpojo.setUsername(GetProperty_API.value("testUsername"));
        loginpojo.setPassword(GetProperty_API.value("testPassword"));
        return loginpojo;
    }


    //  Loyalty Login Payload
    public Login_pojo loginLoyaltyPayload() {
        Login_pojo loginpojo = new Login_pojo();
        loginpojo.setUsername(GetProperty_API.value("login_usernameLoyalty"));
        loginpojo.setPassword(GetProperty_API.value("login_passwordLoyalty"));
        return loginpojo;
    }


    //  mobile App Login Payload with req-login-otp
    public ReqLoginOtp_pojo loginMobileReqLoginOtpPayload() {
        ReqLoginOtp_pojo mobileOTPpojo = new ReqLoginOtp_pojo();
        mobileOTPpojo.setMobileNumber(GetProperty_API.value("appLogin_mobileNumber"));
        mobileOTPpojo.setAttributeId(GetProperty_API.value("appLogin_mobAttributeId"));
        return  mobileOTPpojo;
    }


    //  mobile App Login Payload with requestOTPV2
    public ReqLoginOtp_pojo loginMobileRequestOTPV2Payload() {
        ReqLoginOtp_pojo mobileOTPV2pojo = new ReqLoginOtp_pojo();
        mobileOTPV2pojo.setMobileNumber(GetProperty_API.value("tecnoMobileNumber"));
        mobileOTPV2pojo.setAttributeId(GetProperty_API.value("tecnoMobAttributeId"));
        return  mobileOTPV2pojo;
    }


    //  mobile App Login Payload with reqOtp
    public ReqOtp_pojo loginMobileReqOtpPayload() {
        ReqOtp_pojo mobileOTPpojo = new ReqOtp_pojo();
        mobileOTPpojo.setMobileNumber(GetProperty_API.value("appLogin_mobileNumber"));
        return  mobileOTPpojo;
    }



    //  Firebase payload for mobile app
    public MobileLoginFirebase_pojo loginMobileCreateFirebasePayload() {
        MobileLoginFirebase_pojo mobileFirebasepojo = new MobileLoginFirebase_pojo();
        mobileFirebasepojo.setUserId(GetProperty_API.value("Firebase_userId"));
        mobileFirebasepojo.setDeviceId(GetProperty_API.value("Firebase_deviceId"));
        mobileFirebasepojo.setDeviceName(GetProperty_API.value("Firebase_deviceName"));
        mobileFirebasepojo.setDeviceType(GetProperty_API.value("Firebase_deviceType"));
        mobileFirebasepojo.setFirebaseToken(GetProperty_API.value("Firebase_firebaseToken"));
        mobileFirebasepojo.setActive("1");
        return mobileFirebasepojo;
    }

}