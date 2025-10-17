package pojo_API.responsePojo.Login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse_pojo {
    private User user;
    private String token;
    private String serverTimezone;
    private String appServerUrl;
    private String userSessionId;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private int userId;
        private int userCode;
        private String firstName;
        private String middleName;
        private String lastName;
        private String userName;
        private String password;
        private String communicationEmail;
        private String personalEmail;
        private String mobile;
        private int active;
        private int userType;
        private int defaultProjectId;
        private String resetPasswordToken;
        private String profilePictureName;
        private List<UserProject> userProject;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UserProject {
            private int id;
            private int active;
            private String nodeKey;
            private String serialNo;
            private long assignedOn;
            private Project project;
            private Object updatedTimestamp;
            private Map<String, String> fieldValue; // For dynamic field-value pairs
            private Map<String, Object> dataListFieldValue;

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Project {
                private int projectId;
                private String projectName;
                private String projectLogoImage;
                private int cmProjectType;
                private Timezone timezone;
                private Language language;
                private Country country;
                private int active;
                private String logicalName;
                private int selectCarouselFlag;
                private Object carouselList;
                private int userTargetFlag;
                private int customerTargetFlag;
                private int opportunityTargetFlag;
                private Object updatedBy;
                private Object createdDate;
                private Object allowedDaysForPointHistory;
                private Object otpSmsUrl;
                private Object otpMessage;
                private Object sendOtpFromWhatsapp;
                private Object whatsappOtpUrl;
                private Object whatsappOtpRequest;
                private Object whatsappOtpUrlHeader;
                private Object pointsExpireInDays;
                private Object sendPointTransactionEmail;
                private Object isSmsEnabled;
                private Object otpValidityDuration;
                private Object resendOtpInterval;
                private Object maxSendOtpRequest;
                private Object otpCountResetAfter;
                private Object maxFailureAttempt;
                private Object failureAttemptResetAfter;
                private Object resetAccountLock;

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Timezone {
                    private int id;
                    private String timeZoneName;
                    private int active;
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Language {
                    private int id;
                    private String languageName;
                    private int active;
                }

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Country {
                    private int countryId;
                    private String countryCode;
                    private String callingCode;
                    private String countryName;
                    private int projectId;
                }
            }
        }
    }

}