package pojo_API.ResponsePojo.Setting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageActivitiesSettingsResp_pojo {
    private int statusCode;
    private String message;
    private manageActResponseDataPojo responseData;

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

    public manageActResponseDataPojo getResponseData() {
        return responseData;
    }

    public void setResponseData(manageActResponseDataPojo responseData) {
        this.responseData = responseData;
    }


    public static class manageActResponseDataPojo {
        private String userTime;
        private String transTimeZone;
        private long activityId;
        private String activityName;
        private Integer displayOrder;
        private int allowForCompanies;
        private int allowForOpportunities;
        private int allowForActivities;
        private int allowForDialer;
        private int allowGeofence;
        private int geofenceDistance;
        private String serialKey;
        private int defaultActivity;
        private int active;
        private Integer projectId;
        private int activityTypeIdentifier;
        private int captureLiveLocation;
        private boolean opportunityEnabled;
        private String activeFor;
        private List<Integer> selectedIdList;
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private int additionalFilter;
        private int allowUserToAssignOther;
        private int advanceSetting;
        private boolean anyTransactionPerformed;
        private List<Object> additionalEntityFilterList;

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

        public long getActivityId() {
            return activityId;
        }

        public void setActivityId(long activityId) {
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

        public int getAllowForCompanies() {
            return allowForCompanies;
        }

        public void setAllowForCompanies(int allowForCompanies) {
            this.allowForCompanies = allowForCompanies;
        }

        public int getAllowForOpportunities() {
            return allowForOpportunities;
        }

        public void setAllowForOpportunities(int allowForOpportunities) {
            this.allowForOpportunities = allowForOpportunities;
        }

        public int getAllowForActivities() {
            return allowForActivities;
        }

        public void setAllowForActivities(int allowForActivities) {
            this.allowForActivities = allowForActivities;
        }

        public int getAllowForDialer() {
            return allowForDialer;
        }

        public void setAllowForDialer(int allowForDialer) {
            this.allowForDialer = allowForDialer;
        }

        public int getAllowGeofence() {
            return allowGeofence;
        }

        public void setAllowGeofence(int allowGeofence) {
            this.allowGeofence = allowGeofence;
        }

        public int getGeofenceDistance() {
            return geofenceDistance;
        }

        public void setGeofenceDistance(int geofenceDistance) {
            this.geofenceDistance = geofenceDistance;
        }

        public String getSerialKey() {
            return serialKey;
        }

        public void setSerialKey(String serialKey) {
            this.serialKey = serialKey;
        }

        public int getDefaultActivity() {
            return defaultActivity;
        }

        public void setDefaultActivity(int defaultActivity) {
            this.defaultActivity = defaultActivity;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }

        public int getActivityTypeIdentifier() {
            return activityTypeIdentifier;
        }

        public void setActivityTypeIdentifier(int activityTypeIdentifier) {
            this.activityTypeIdentifier = activityTypeIdentifier;
        }

        public int getCaptureLiveLocation() {
            return captureLiveLocation;
        }

        public void setCaptureLiveLocation(int captureLiveLocation) {
            this.captureLiveLocation = captureLiveLocation;
        }

        public boolean isOpportunityEnabled() {
            return opportunityEnabled;
        }

        public void setOpportunityEnabled(boolean opportunityEnabled) {
            this.opportunityEnabled = opportunityEnabled;
        }

        public String getActiveFor() {
            return activeFor;
        }

        public void setActiveFor(String activeFor) {
            this.activeFor = activeFor;
        }

        public List<Integer> getSelectedIdList() {
            return selectedIdList;
        }

        public void setSelectedIdList(List<Integer> selectedIdList) {
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

        public int getAdditionalFilter() {
            return additionalFilter;
        }

        public void setAdditionalFilter(int additionalFilter) {
            this.additionalFilter = additionalFilter;
        }

        public int getAllowUserToAssignOther() {
            return allowUserToAssignOther;
        }

        public void setAllowUserToAssignOther(int allowUserToAssignOther) {
            this.allowUserToAssignOther = allowUserToAssignOther;
        }

        public int getAdvanceSetting() {
            return advanceSetting;
        }

        public void setAdvanceSetting(int advanceSetting) {
            this.advanceSetting = advanceSetting;
        }

        public boolean isAnyTransactionPerformed() {
            return anyTransactionPerformed;
        }

        public void setAnyTransactionPerformed(boolean anyTransactionPerformed) {
            this.anyTransactionPerformed = anyTransactionPerformed;
        }

        public List<Object> getAdditionalEntityFilterList() {
            return additionalEntityFilterList;
        }

        public void setAdditionalEntityFilterList(List<Object> additionalEntityFilterList) {
            this.additionalEntityFilterList = additionalEntityFilterList;
        }
    }

}
