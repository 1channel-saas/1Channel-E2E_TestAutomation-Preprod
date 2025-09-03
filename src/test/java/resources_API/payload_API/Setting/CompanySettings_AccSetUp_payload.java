package resources_API.payload_API.Setting;

import pojo_API.RequestPojo.Setting.CompanySettings_AccSetUp_pojo;
import utilities_API.GetProperty_API;

public class CompanySettings_AccSetUp_payload {

    public CompanySettings_AccSetUp_pojo companySettings_AccSetUp_payload() {
        CompanySettings_AccSetUp_pojo companySettingsAccSetUpPojo = new CompanySettings_AccSetUp_pojo();
        companySettingsAccSetUpPojo.setProjectId(Integer.parseInt(GetProperty_API.value("projectId")));
        return companySettingsAccSetUpPojo;
    }

}
