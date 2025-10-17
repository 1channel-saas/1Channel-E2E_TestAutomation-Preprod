package pojo_API.requestPojo.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateActivitySetting_pojo {

    private String activityName;
    private String serialKey;
    private Integer projectId;
    private int allowForCompanies;
    private int allowForOpportunities;
    private int allowForDialer;
    private int allowGeofence;
    private int geofenceDistance;
    private int captureLiveLocation;

}