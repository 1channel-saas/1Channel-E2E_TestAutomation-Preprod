package resources_API.payload_API.setting;

import pojo_API.requestPojo.setting.CompanySettings_AccSetUp_pojo;
import utilities_API.GetProperty_API;

public class CompanySettings_AccSetUp_payload {

    public CompanySettings_AccSetUp_pojo companySettings_AccSetUp_payload() {
        CompanySettings_AccSetUp_pojo companySettingsAccSetUpPojo = new CompanySettings_AccSetUp_pojo();
        companySettingsAccSetUpPojo.setProjectId(Integer.parseInt(GetProperty_API.value("testUserProjectId")));
        return companySettingsAccSetUpPojo;
    }

}
