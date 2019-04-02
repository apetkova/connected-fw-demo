package jira;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    private String key;
    private String summary;
    private String status;
    private ArrayList<String> testIds;

    @SuppressWarnings("unchecked")
    @JsonProperty("fields")
    private void unpackNested(Map<String,Object> fields) {
        this.summary = (String) fields.get("summary");
        Map<String,String> jsonStatus = (Map<String,String>)fields.get("status");
        this.status = jsonStatus.get("name");
        this.testIds = (ArrayList<String>) fields.get("customfield_10007");
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return summary;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getTestIds() {
        return testIds;
    }
}


