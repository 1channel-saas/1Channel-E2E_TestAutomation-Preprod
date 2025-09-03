package resources_API.testUtils_API;

//This is enum class which contains all the API Endpoints
public enum Endpoints {

//    *** Login Microservice
//    -----------------------------------------------------Login-------------------------------------------------------

    // Auth endpoints #
    loginAPI("/login/api/auth/login"),
    requestOTP("/login/api/auth/requestOTP"),
    requestOTPV2("/login/api/auth/v2/requestOTP"),
    validateOTP("/login/api/auth/validateOtp"),
    validateOTPV2("/login/api/auth/v2/validateOTP"),
    request_login_otp("/login/api/auth/request-login-otp"),
    verify_otp("/login/api/auth/verify-otp"),
    confirmLogin("/login/api/auth/confirm-login"),
    signup("/login/api/auth/signup"),
    resendAuthEmail("/login/api/auth/resendAuthEmail"),
    validateToken("/login/api/auth/validateToken"),
    createPassword("/login/api/auth/createPassword"),
    resetPassword("/login/api/auth/password/resetPassword"),
    resetPasswordV1("/login/api/auth/v1/loyalty/password/resetPassword"),
    passValidateToken("/login/api/auth/password/validateToken"),
    updatePassword("/login/api/auth/password/update"),
    isValidToken("/login/api/auth/isValidToken"),
    logoutApi("/login/api/logout"),


    // Menu endpoints #
    assignedWebMenus("/login/api/menu/assignedWebMenus"),
    getAllMenu("/login/api/menu/getAll"),
    webMenu("/login/api/menu/web"),
    webMenuProjectId("/login/api/menu/web/{projectId}"),
    webMenuV2("/login/api/menu/web/v2"),
    mobileMenu_login("/login/api/menu/mobile"),
    mobileMenuProjectId("/login/api/menu/mobile/{projectId}"),
    mobileMenuV2_login("/login/api/menu/mobile/v2"),
    reportBuilderMenu("/login/api/menu/reportBuilder"),
    assignedReportBulderMenus("/login/api/menu/reportBuilder/assignedReportBulderMenus"),


    // Users endpoints #
    createFirebaseToken_login("/login/api/users/mobile/createFirebaseToken"),
    users("/login/api/users"),
    fetchModuleOwnerOptions("/login/api/users/fetchModuleOwnerOptions"),
    getAllUsers("/login/api/users/getAll"),
    usersId("/login/api/users/{userId}"),
    usersGetOne("/login/api/users/getOne/{userId}"),
    downHierarchyUsers("/login/api/users/downHierarchy"),
    activeProjectUsersWithoutLoggedInUser("/login/api/users/activeProjectUsersWithoutLoggedInUser"),
    ownerAndDownHierarchyUsers("/login/api/users/ownerAndDownHierarchyUsers"),
    reportsToOptions("/login/api/users/reportsToOptions"),
    fetchSpecifiedUsersDetail("/login/api/users/fetchSpecifiedUsersDetail"),
    saveDefaultProject("/login/api/users/saveDefaultProject"),
    uploadProfilePic("/login/api/users/profile/uploadProfilePic"),
    fetchProfilePic("/login/api/users/profile/fetchProfilePic"),
    removeProfilePic("/login/api/users/profile/removeProfilePic"),


    // Loyalty endpoints #
    userlistLoyalty("/login/api/loyalty/public/v1/userlist"),
    fetchFilteredUserDetailsLoyalty("/login/api/loyalty/public/v1/fetchFilteredUserDetails"),




//    *** Mobile Microservice
//    ----------------------------------------------------Mobile-------------------------------------------------------

    // Sync endpoints #
    sync("/mobile/api/mobile/sync/sync"),


    // Menu endpoints #
    mobileMenu("/mobile/api/menu/mobile"),
    mobileMenuV2("/mobile/api/menu/mobile/v2"),
    assignedMobileMenus("/mobile/api/menu/assignedMobileMenus"),
    assignedMenusV1("/mobile/api/menu/loyalty/mobile/v1/assignedMenus"),
    assignedMobileReports("/mobile/api/menu/assignedMobileReports"),



    // Customer endpoints #
    getCompanyMob("/mobile/api/company/getCompany"),


    // Users endpoints #
    fetchOwnerOptions("/mobile/api/users/fetchOwnerOptions"),
    generateOTP("/mobile/api/users/generateOTP"),
    createFirebaseToken("/mobile/api/users/createFirebaseToken"),




//    *** Setting Microservice
//    ------------------------------------------------------Sett-------------------------------------------------------

    //  Configuration endpoints #
    assistant("/setting/api/configuration/assistant"),
    getCompanySettings_AccSetUp("/setting/api/company/getCompanySettings"),


    //  Custom Type #
    getFieldsInSetting("/setting/api/customType/getFieldsInSetting"),
    getAllAttributeTypes("/setting/api/customType/getAllAttributeTypes"),
    getChildAttributes("/setting/api/customType/getChildAttributes"),
    getAllCustomAttributes("/setting/api/customType/getAllCustomAttributes"),
    getStdLookupForAttributeTypes("/setting/api/customType/getStdLookupForAttributeTypes"),
    getAttribLookupValues("/setting/api/customType/getAttribLookupValues"),
    getCustomDateAttributes("/setting/api/customType/getCustomDateAttributes"),
    addCustomAttribute("/setting/api/customType/addCustomAttribute"),
    editCustomAttribute("/setting/api/customType/editCustomAttribute"),
    getDataListLookupValues("/setting/api/customType/getDataListLookupValues"),
    getProductQuantityTransferDetail("/setting/api/customType/getProductQuantityTransferDetail"),
    getPreviousSubmittedFormDetails("/setting/api/customType/getPreviousSubmittedFormDetails"),
    getFieldRelation("/setting/api/customType/getFieldRelation"),
    addEditRelation("/setting/api/customType/addEditRelation"),
    deleteRelation("/setting/api/customType/deleteRelation"),
    editRelation("/setting/api/customType/addEditRelation"),
    sequenceFields("/setting/api/customType/sequenceFields"),


    //  Fields Type #
    getFieldVisibilitySettings("/setting/api/fields/getFieldVisibilitySettings"),
    addEditFieldVisibilitySettings("/setting/api/fields/addEditFieldVisibilitySettings"),
    getDetail("/setting/api/fields/getDetail"),
    calculatedField("/setting/api/calculatedField/validate"),


    //  Activity Setting #
    manageActivitiesSettings("/setting/api/activities/manageActivities/settings/{activityId}"),
    createActivitiesSettings("/setting/api/activities/manageActivities/settings"),


    //  Product Form #
    getSettingsProductForm("/setting/api/productForm/getSettings"),
    getSettingIdProductForm("/setting/api/productForm/getSetting/{id}"),
    addUpdateSettingProductForm("/setting/api/productForm/addUpdateSetting"),
    sequenceProductForms("/setting/api/productForm/sequenceProductForms"),
    deleteProductForm("/setting/api/productForm/deleteProductForm"),


    //  Roles #
    getRoles("/setting/api/roles"),
    postRoles("/setting/api/roles"),





//    *** Transaction Microservice
//    --------------------------------------------------Trans----------------------------------------------------------

    //  Activities #
    getActivitySku("/transactions/api/activities/getActivitySku"),
    saveActivityImages("/transactions/api/activities/saveActivityImages"),
    updateSequence("/transactions/api/activities/manageActivities/updateSequence"),
    delManageActivities("/transactions/api/activities/manageActivities"),
    getManageActivities("/transactions/api/activities/manageActivities"),
    fetchPerformWebActivities("/transactions/api/activities/fetchPerformWebActivities"),
    addEditActivity("/transactions/api/activities/addEditActivity"),
    getGetActivity("/transactions/api/activities/getActivity"),
    postGetActivity("/transactions/api/activities/getActivity"),
    custLoginGetGetActivity("/transactions/api/activities/customerLogin/getActivity"),
    custLoginPostGetActivity("/transactions/api/activities/customerLogin/getActivity"),
    getActivityLookup("/transactions/api/activities/getActivityLookup"),
    getProductFormRecord("/transactions/api/activities/getProductFormRecord"),
    deleteActivityData("/transactions/api/activities/deleteActivityData"),
    getAllActivityDummy("/transactions/api/activities/getAllActivityDummy"),
    fetchAllByFilter("/transactions/api/activities/v1/fetchAllByFilter"),
    getActivitiesFilter("/transactions/api/activities/v1/mobile/filter/getActivities"),
    getActivitySerialNo("/transactions/api/activities/getActivitySerialNo"),
    getProductFormRecordV2("/transactions/api/activities/getProductFormRecord/v2"),
    getActivityLookupView("/transactions/api/activities/v1/getActivityLookupView"),
    getUnperformedPlannedActivityLookupView("/transactions/api/activities/v1/getUnperformedPlannedActivityLookupView"),

    //  Approvals #
    approvalPending("/transactions/api/approvals/pending"),
    approvalCompleted("/transactions/api/approvals/completed"),
    approvalUpdate("/transactions/api/approvals/update"),
    approvalFetchActionOptions("/transactions/api/approvals/fetchActionOptions"),

    //  Company #
    getCompanySettings("/transactions/api/company/getCompanySettings"),
    getAllActivitiesComp("/transactions/api/company/getAllActivities"),     // not found
    getFrontImageComp("/transactions/api/company/getFrontImage/{projectId}/{companyId}/{imgName}"),     // not found
    addEditCompany("/transactions/api/company/addEditCompany"),
    getCompanyTrans("/transactions/api/company/getCompany"),
    getCompanyLookup("/transactions/api/company/getCompanyLookup"),
    getCompanyIdAndNameSelfOrTeam("/transactions/api/company/getCompanyIdAndNameSelfOrTeam"),
    assignContact("/transactions/api/company/assignContact"),
    getCompanyInfoDash("/transactions/api/company/dashboard/getCompanyInfo"),
    getContactsDash("/transactions/api/company/dashboard/getContacts"),
    getOpportunitiesDash("/transactions/api/company/dashboard/getOpportunities"),
    getActivitiesDash("/transactions/api/company/dashboard/getActivities"),
    getStatsDash("/transactions/api/company/dashboard/getStats"),
    getAllCompanyDummy("/transactions/api/company/getAllCompanyDummy"),
    getCompanyLookupSelfOrTeam("/transactions/api/company/getCompanyLookupSelfOrTeam"),
    uploadComp("/transactions/api/company/upload"),
    getCompanyLookupSelfOrTeamV2("/transactions/api/company/v2/getCompanyLookupSelfOrTeam"),
    getCompanyLookupSelfOrTeamV3("/transactions/api/company/v3/getCompanyLookupSelfOrTeam"),
    getCompanyIdName("/transactions/api/company/getCompanyIdName"),
    getCompanyIdNameV1("/transactions/api/company/v1/mobile/filter/getCompanyIdName"),
    getCompanyLookupBasedOnCustomerViewCount("/transactions/api/company/v3/getCompanyLookupBasedOnCustomerViewCount"),
    checkPrimaryAttribute("/transactions/api/bulkUpload/checkPrimaryAttribute"),

    //  Contact #
    addEditContact("/transactions/api/contact/addEditContact"),
    getContact("/transactions/api/contact/getContact"),
    getContactLookup("/transactions/api/contact/getContactLookup"),
    getCustomerContacts("/transactions/api/contact/getCustomerContacts"),
    getFieldEditHistory("/transactions/api/contact/getFieldEditHistory"),
    getContactIdAndNameSelfOrTeam("/transactions/api/contact/getContactIdAndNameSelfOrTeam"),
    getContactLookupSelfOrTeam("/transactions/api/contact/getContactLookupSelfOrTeam"),
    fetchAdvancedSearchOperators("/transactions/api/contact/fetchAdvancedSearchOperators"),
    getContactLookupSelfOrTeamPost("/transactions/api/contact/getContactLookupSelfOrTeam"),

    //  custom Type #
    getImageCustomAttribId("/transactions/api/customType/getImage/{projectId}/{moduleName}/{moduleId}/{attributeId}/{imgName}"),
    getImageCustom("/transactions/api/customType/getImage/{imgName}"),
    uploadFieldImage("/transactions/api/customType/uploadFieldImage"),
    uploadAttributeRefDoc("/transactions/api/customType/uploadAttributeRefDoc"),
    getFields("/transactions/api/customType/getFields"),
    getDataLists("/transactions/api/customType/getDataLists"),
    getProductFilterConfig("/transactions/api/customType/getProductFilterConfig"),
    getLinkedDataList("/transactions/api/customType/getLinkedDataList"),
    getMappedDataListOptions("/transactions/api/customType/getMappedDataListOptions"),

    //  Day Planner #
    getCustomerListDayPlan("/transactions/api/dayPlanner/getCustomerList"),
    postGetFullMonthPlanDistributionDayPlan("/transactions/api/dayPlanner/getFullMonthPlanDistribution"),
    getGetFullMonthPlanDistributionDayPlan("/transactions/api/dayPlanner/getFullMonthPlanDistribution"),
    saveDayPlanning("/transactions/api/dayPlanner/saveDayPlanning"),
    getCustomerRelatedDataForValidationDayPlan("/transactions/api/dayPlanner/getCustomerRelatedDataForValidation"),
    getExistingDayPlan("/transactions/api/dayPlanner/getExistingDayPlan"),
    uploadDayPlan("/transactions/api/dayPlanner/uploadDayPlan"),

    //  Document #
    getDocuments("/transactions/api/document/getDocuments"),
    addEditFoldersDoc("/transactions/api/document/addEditFolders"),
    addEditFilesDoc("/transactions/api/document/addEditFiles"),
    deleteFilesDoc("/transactions/api/document/deleteFiles"),
    saveDisplayOrderDoc("/transactions/api/document/saveDisplayOrder"),

    //  Integration #
    searchCallerIvr("/transactions/api/integration/searchCaller"),
    ozonetelCallbackIvr("/transactions/api/integration/ozonetel/Callback"),
    fetchCallDetailsIvr("/transactions/api/integration/fetchCallDetails"),
    callLogsIvr("/transactions/api/integration/callLogs"),
    getCompanyIdNameByMobileNoIvr("/transactions/api/integration/getCompanyIdNameByMobileNo"),

    //  user activity #
    addUpdateUserActivity("/transactions/api/mobile/lastSeenTime/addUpdateUserActivity"),

    //  notification #
    sendAppNotification("/transactions/api/notification/sendAppNotification"),

    //  Opportunity #
    getOpportunitySetting("/transactions/api/opportunity/getOpportunitySetting"),
    getFrontImage("/transactions/api/opportunity/getFrontImage/{projectId}/{companyId}/{oppId}/{imgName}"),
    addEditOpportunity("/transactions/api/opportunity/addEditOpportunity"),
    getOpportunity("/transactions/api/opportunity/getOpportunity"),
    getOpportunityLookup("/transactions/api/opportunity/getOpportunityLookup"),
    postGetOpportunityLookupSelfOrTeam("/transactions/api/opportunity/getOpportunityLookupSelfOrTeam"),
    getGetOpportunityLookupSelfOrTeam("/transactions/api/opportunity/getOpportunityLookupSelfOrTeam"),
    postGetOpportunityGridViewSelfOrTeam("/transactions/api/opportunity/getOpportunityGridViewSelfOrTeam"),
    getGetOpportunityGridViewSelfOrTeam("/transactions/api/opportunity/getOpportunityGridViewSelfOrTeam"),
    uploadOppo("/transactions/api/opportunity/upload"),
    fetchAllByFilterOppo("/transactions/api/opportunity/v1/fetchAllByFilter"),
    getOpportunityDeatils("/transactions/api/opportunity/getOpportunityDeatils"),
    getAllOpportunityDeatils("/transactions/api/opportunity/getAllOpportunityDeatils"),
    getOpportunityTypeCustomAttributes("/transactions/api/opportunity/getOpportunityTypeCustomAttributes"),

    //  Product #
    allProduct("/transactions/api/product/all"),
    addEditProduct("/transactions/api/product/addEditProduct"),
    getProduct("/transactions/api/product/getProduct"),
    getGetProductLookup("/transactions/api/product/getProductLookup"),
    postGetProductLookup("/transactions/api/product/getProductLookup"),

    //  WebResources #
    IVRCallInputResponse("/transactions/api/webresources/IVRCallInputResponse"),

    //  Workflows #





//    *** Loyalty Microservice
//    --------------------------------------------------Loyalty--------------------------------------------------------

    //  bulk-upload #
    customerBulkUpload("/loyalty/api/bulk-upload/company");




    private final String endpoint;

    Endpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getValOfEndpoint() {
        return endpoint;
    }

    public String getEndpointWithPathParam(String paramName, String paramValue) {
        return endpoint.replace("{" + paramName + "}", paramValue);
    }

}