package pojo_API.ResponsePojo.Setting;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateActivitySettingResp_pojo {

    private int statusCode;
    private String message;
    private ResponseDataPojo responseData;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseDataPojo getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseDataPojo responseData) {
        this.responseData = responseData;
    }


    public static class ResponseDataPojo {
        private String userTime;
        private String transTimeZone;
        private int activityId;
        private String activityName;
        private Integer displayOrder;   // Nullable fields should be Integer instead of int
        private Integer allowForCompanies;
        private Integer allowForOpportunities;
        private Integer allowForActivities;
        private Integer allowForDialer;
        private Integer allowGeofence;
        private Integer geofenceDistance;
        private String serialKey;
        private Boolean defaultActivity;
        private Boolean active;
        private Integer projectId;
        private String activityTypeIdentifier;
        private Integer captureLiveLocation;
        private Boolean opportunityEnabled;
        private String activeFor;
        private String selectedIdList;
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private String additionalFilter;
        private Boolean allowUserToAssignOther;
        private String advanceSetting;
        private Boolean anyTransactionPerformed;
        private String additionalEntityFilterList;

        public String getUserTime() {
            return userTime;
        }

        public void setUserTime(String userTime) {
            this.userTime = userTime;
        }

        public String getTransTimeZone() {
            return transTimeZone;
        }

        public void setTransTimeZone(String transTimeZone) {
            this.transTimeZone = transTimeZone;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public Integer getAllowForCompanies() {
            return allowForCompanies;
        }

        public void setAllowForCompanies(Integer allowForCompanies) {
            this.allowForCompanies = allowForCompanies;
        }

        public Integer getAllowForOpportunities() {
            return allowForOpportunities;
        }

        public void setAllowForOpportunities(Integer allowForOpportunities) {
            this.allowForOpportunities = allowForOpportunities;
        }

        public Integer getAllowForActivities() {
            return allowForActivities;
        }

        public void setAllowForActivities(Integer allowForActivities) {
            this.allowForActivities = allowForActivities;
        }

        public Integer getAllowForDialer() {
            return allowForDialer;
        }

        public void setAllowForDialer(Integer allowForDialer) {
            this.allowForDialer = allowForDialer;
        }

        public Integer getAllowGeofence() {
            return allowGeofence;
        }

        public void setAllowGeofence(Integer allowGeofence) {
            this.allowGeofence = allowGeofence;
        }

        public Integer getGeofenceDistance() {
            return geofenceDistance;
        }

        public void setGeofenceDistance(Integer geofenceDistance) {
            this.geofenceDistance = geofenceDistance;
        }

        public String getSerialKey() {
            return serialKey;
        }

        public void setSerialKey(String serialKey) {
            this.serialKey = serialKey;
        }

        public Boolean getDefaultActivity() {
            return defaultActivity;
        }

        public void setDefaultActivity(Boolean defaultActivity) {
            this.defaultActivity = defaultActivity;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            projectId =
            this.projectId = projectId;
        }

        public String getActivityTypeIdentifier() {
            return activityTypeIdentifier;
        }

        public void setActivityTypeIdentifier(String activityTypeIdentifier) {
            this.activityTypeIdentifier = activityTypeIdentifier;
        }

        public Integer getCaptureLiveLocation() {
            return captureLiveLocation;
        }

        public void setCaptureLiveLocation(Integer captureLiveLocation) {
            this.captureLiveLocation = captureLiveLocation;
        }

        public Boolean getOpportunityEnabled() {
            return opportunityEnabled;
        }

        public void setOpportunityEnabled(Boolean opportunityEnabled) {
            this.opportunityEnabled = opportunityEnabled;
        }

        public String getActiveFor() {
            return activeFor;
        }

        public void setActiveFor(String activeFor) {
            this.activeFor = activeFor;
        }

        public String getSelectedIdList() {
            return selectedIdList;
        }

        public void setSelectedIdList(String selectedIdList) {
            this.selectedIdList = selectedIdList;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getAdditionalFilter() {
            return additionalFilter;
        }

        public void setAdditionalFilter(String additionalFilter) {
            this.additionalFilter = additionalFilter;
        }

        public Boolean getAllowUserToAssignOther() {
            return allowUserToAssignOther;
        }

        public void setAllowUserToAssignOther(Boolean allowUserToAssignOther) {
            this.allowUserToAssignOther = allowUserToAssignOther;
        }

        public String getAdvanceSetting() {
            return advanceSetting;
        }

        public void setAdvanceSetting(String advanceSetting) {
            this.advanceSetting = advanceSetting;
        }

        public Boolean getAnyTransactionPerformed() {
            return anyTransactionPerformed;
        }

        public void setAnyTransactionPerformed(Boolean anyTransactionPerformed) {
            this.anyTransactionPerformed = anyTransactionPerformed;
        }

        public String getAdditionalEntityFilterList() {
            return additionalEntityFilterList;
        }

        public void setAdditionalEntityFilterList(String additionalEntityFilterList) {
            this.additionalEntityFilterList = additionalEntityFilterList;
        }
    }

}
