package pojo_API.ResponsePojo.Setting;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class getFieldVisibilitySettings_pojo {
    private int statusCode;
    private String message;
    private List<ResponseData> responseData;

    @Getter
    @Setter
    public static class ResponseData {
        private int projectId;
        private int moduleType;
        private int attributeId;
        private String visibility;
        private Integer attributeTypeId;
        private List<FieldVisibilityConfig> fieldVisibilityConfig;

        @Getter
        @Setter
        public static class FieldVisibilityConfig {
            private int id;
            private int projectId;
            private int roleId;
            private int moduleType;
            private int attributeId;
            private Integer parentDataListId;
            private int fieldVisibility;
            private String value;
            private int createdBy;
            private int updatedBy;
        }
    }

}
