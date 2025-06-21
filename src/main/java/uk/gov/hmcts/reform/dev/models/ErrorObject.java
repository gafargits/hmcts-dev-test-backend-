package uk.gov.hmcts.reform.dev.models;

import java.util.HashMap;
import java.util.Map;

public class ErrorObject {
    private final Map<String, Object> error = new HashMap<>();

    public void addError(String key, Object value) {
        error.put(key, value);
    }

    public Map<String, Object> getError() {
        return error;
    }
}
