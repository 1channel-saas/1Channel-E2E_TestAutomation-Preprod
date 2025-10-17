package pojo_API.requestPojo.login;

public class ReqLoginOtp_pojo {

    //  for generate OTP with mobile number & attributeId
    private String mobileNumber;
    private String attributeId;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

}
