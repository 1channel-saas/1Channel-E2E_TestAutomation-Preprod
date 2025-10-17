// Read and parse JSON
String jsonContent = readJsonFile("src/test/java/resources_API/payload_API/json_Files/activities/testUser/addEditActivity_Offsite.json");
JSONObject jsonObj = new JSONObject(jsonContent);

// Update values directly
jsonObj.put("project_id", GetProperty_API.value("testUserProjectId"));
jsonObj.put("app_Version", GetProperty.value("appVersion"));

// Convert back to string
jsonContentAddEditAct = jsonObj.toString();
