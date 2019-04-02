package jira;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class JiraClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String API_PATH = "/rest/api/2/";
    private static final String COMMENT_ENDPOINT = "issue/%s/comment/";
    private static final String STATUS_ENDPOINT = "issue/%s/transitions";
    private static final int REOPEN_STATUS_ID = 3;
    private static String username = "apetkova";
    private static String password = "";


    public static JiraIssue[] getIssuesFromFilter(int filterId){
        JiraIssue[] issues = null;
        try {
            URL jiraUrl = new URL("http", HOST, PORT, API_PATH + "search");
            Response response = given().queryParam("jql", "filter=" + filterId)
                     .queryParam("fields", "summary", "status", "customfield_10007")
                     .auth().preemptive().basic(username, password)
                     .contentType(ContentType.JSON)
                     .accept(ContentType.JSON)
                        .when().get(jiraUrl);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode newNode = objectMapper.readTree(response.getBody().asString());
            issues = objectMapper.readValue(newNode.findValue("issues").toString(), JiraIssue[].class);
        }catch (IOException mue){
            //do something
            mue.printStackTrace();
        }
        return issues;
    }

    public static boolean postComment(String issueId, String comment){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        payload.put("body", comment);
        try {
            URL jiraUrl = new URL("http", HOST, PORT, API_PATH + String.format(COMMENT_ENDPOINT, issueId));
            Response response = given().body(payload.toString())
                    .auth().preemptive().basic(username, password)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .when().post(jiraUrl);
            return (response.getStatusCode() == 201);
        }catch (IOException mue){
            //do something
            mue.printStackTrace();
        }
        return false;
    }

    public static boolean updateStatus(String issueId, int status){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ObjectNode child = mapper.createObjectNode();
        child.put("id", status);
        payload.set("transition", child);
        try {
            URL jiraUrl = new URL("http", HOST, PORT, API_PATH + String.format(STATUS_ENDPOINT, issueId));
            Response response = given().body(payload.toString())
                    .auth().preemptive().basic(username, password)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .when().post(jiraUrl);
            return (response.getStatusCode() == 204);
        }catch (IOException mue){
            //do something
            mue.printStackTrace();
        }
        return false;
    }

    public static boolean reopenIssue(String issueId){
        return updateStatus(issueId, REOPEN_STATUS_ID);
    }
}
