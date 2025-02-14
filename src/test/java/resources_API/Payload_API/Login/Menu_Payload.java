package resources_API.Payload_API.Login;

import pojo_API.RequestPojo.Login.getAllMenu_pojo;
import utilities_API.GetProperty_API;

public class Menu_Payload {

    public getAllMenu_pojo getAllMenuPayload() {
        getAllMenu_pojo getAllMenuPojo = new getAllMenu_pojo();
        getAllMenuPojo.setProjectId(GetProperty_API.value("projectId"));
        getAllMenuPojo.setUserId(GetProperty_API.value("adminUserId"));
        return getAllMenuPojo;
    }

}
