package pojo_API.RequestPojo.Setting;

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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getSerialKey() {
        return serialKey;
    }

    public void setSerialKey(String serialKey) {
        this.serialKey = serialKey;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public int getCaptureLiveLocation() {
        return captureLiveLocation;
    }

    public void setCaptureLiveLocation(int captureLiveLocation) {
        this.captureLiveLocation = captureLiveLocation;
    }

}
