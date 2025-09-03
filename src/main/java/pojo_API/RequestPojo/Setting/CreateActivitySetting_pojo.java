package pojo_API.RequestPojo.Setting;

import lombok.Getter;

@Getter
public class CreateActivitySetting_pojo {
    private String activityName;
    private String serialKey;
    private int projectId;
    private int allowForCompanies;
    private int allowForOpportunities;
    private int allowForDialer;
    private int allowGeofence;
    private int geofenceDistance;
    private int captureLiveLocation;

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setSerialKey(String serialKey) {
        this.serialKey = serialKey;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setAllowForCompanies(int allowForCompanies) {
        this.allowForCompanies = allowForCompanies;
    }

    public void setAllowForOpportunities(int allowForOpportunities) {
        this.allowForOpportunities = allowForOpportunities;
    }

    public void setAllowForDialer(int allowForDialer) {
        this.allowForDialer = allowForDialer;
    }

    public void setAllowGeofence(int allowGeofence) {
        this.allowGeofence = allowGeofence;
    }

    public void setGeofenceDistance(int geofenceDistance) {
        this.geofenceDistance = geofenceDistance;
    }

    public void setCaptureLiveLocation(int captureLiveLocation) {
        this.captureLiveLocation = captureLiveLocation;
    }

}
