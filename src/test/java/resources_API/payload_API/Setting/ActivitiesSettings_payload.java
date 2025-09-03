package resources_API.payload_API.Setting;

import lombok.Getter;
import pojo_API.RequestPojo.Setting.CreateActivitySetting_pojo;
import resources_API.testUtils_API.CommonUtils_API;
import utilities_API.GetProperty_API;

@Getter
public class ActivitiesSettings_payload {

    //  Capture the Request Payload
    private String activityName;
    private String serialKey;

    public CreateActivitySetting_pojo createActivityPayload() {
        activityName = "testAct" + CommonUtils_API.generateRandomString(3);
        serialKey = "ACT" + CommonUtils_API.generateRandomString(3);

        CreateActivitySetting_pojo createActivitySetting_pojo = new CreateActivitySetting_pojo();
        createActivitySetting_pojo.setActivityName(activityName);
        createActivitySetting_pojo.setSerialKey(serialKey);
        createActivitySetting_pojo.setProjectId(Integer.parseInt(GetProperty_API.value("projectId")));
        createActivitySetting_pojo.setAllowForCompanies(1);
        createActivitySetting_pojo.setAllowForOpportunities(1);

        return createActivitySetting_pojo;
    }

}
