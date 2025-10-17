package resources_API.payload_API.login;

import pojo_API.requestPojo.login.getAllMenu_pojo;
import utilities_API.GetProperty_API;

public class Menu_Payload {

    public getAllMenu_pojo getAllMenuPayload() {
        getAllMenu_pojo getAllMenuPojo = new getAllMenu_pojo();
        getAllMenuPojo.setProjectId(GetProperty_API.value("projectId"));
        getAllMenuPojo.setUserId(GetProperty_API.value("adminUserId"));
        return getAllMenuPojo;
    }

}
