package pojo_API.ResponsePojo.Setting;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateActivitySettingResp_pojo {

    private int statusCode;
    private String message;
    private ResponseDataPojo responseData;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseDataPojo {
        @Setter
        private String userTime;
        @Setter
        private String transTimeZone;
        @Setter
        private int activityId;
        @Setter
        private String activityName;
        @Setter
        private Integer displayOrder;   // Nullable fields should be Integer instead of int
        @Setter
        private Integer allowForCompanies;
        @Setter
        private Integer allowForOpportunities;
        @Setter
        private Integer allowForActivities;
        @Setter
        private Integer allowForDialer;
        @Setter
        private Integer allowGeofence;
        @Setter
        private Integer geofenceDistance;
        @Setter
        private String serialKey;
        @Setter
        private Boolean defaultActivity;
        @Setter
        private Boolean active;
        private Integer projectId;
        @Setter
        private String activityTypeIdentifier;
        @Setter
        private Integer captureLiveLocation;
        @Setter
        private Boolean opportunityEnabled;
        @Setter
        private String activeFor;
        @Setter
        private String selectedIdList;
        @Setter
        private String startDate;
        @Setter
        private String endDate;
        @Setter
        private String startTime;
        @Setter
        private String endTime;
        @Setter
        private String additionalFilter;
        @Setter
        private Boolean allowUserToAssignOther;
        @Setter
        private String advanceSetting;
        @Setter
        private Boolean anyTransactionPerformed;
        @Setter
        private String additionalEntityFilterList;

        public String getUserTime() {
            return userTime;
        }

        public String getTransTimeZone() {
            return transTimeZone;
        }

        public int getActivityId() {
            return activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public Integer getAllowForCompanies() {
            return allowForCompanies;
        }

        public Integer getAllowForOpportunities() {
            return allowForOpportunities;
        }

        public Integer getAllowForActivities() {
            return allowForActivities;
        }

        public Integer getAllowForDialer() {
            return allowForDialer;
        }

        public Integer getAllowGeofence() {
            return allowGeofence;
        }

        public Integer getGeofenceDistance() {
            return geofenceDistance;
        }

        public String getSerialKey() {
            return serialKey;
        }

        public Boolean getDefaultActivity() {
            return defaultActivity;
        }

        public Boolean getActive() {
            return active;
        }

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }

        public String getActivityTypeIdentifier() {
            return activityTypeIdentifier;
        }

        public Integer getCaptureLiveLocation() {
            return captureLiveLocation;
        }

        public Boolean getOpportunityEnabled() {
            return opportunityEnabled;
        }

        public String getActiveFor() {
            return activeFor;
        }

        public String getSelectedIdList() {
            return selectedIdList;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getAdditionalFilter() {
            return additionalFilter;
        }

        public Boolean getAllowUserToAssignOther() {
            return allowUserToAssignOther;
        }

        public String getAdvanceSetting() {
            return advanceSetting;
        }

        public Boolean getAnyTransactionPerformed() {
            return anyTransactionPerformed;
        }

        public String getAdditionalEntityFilterList() {
            return additionalEntityFilterList;
        }

    }

}
