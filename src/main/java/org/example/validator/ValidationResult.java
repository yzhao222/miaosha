package org.example.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMessageMap() {
        return errorMessageMap;
    }

    public void setErrorMessageMap(Map<String, String> errorMessageMap) {
        this.errorMessageMap = errorMessageMap;
    }

    //校验结果是否有错
    private boolean hasErrors=false;

    //存放错误信息的map
    private Map<String,String> errorMessageMap=new HashMap<>();
    public  String getErrorMessage(){
        return StringUtils.join(errorMessageMap.values().toArray(),",");
    }

}
