package testng;

import jira.JiraClient;
import jira.JiraIssue;
import org.testng.*;
import java.util.*;


public class CustomListener implements ISuiteListener, ITestListener {
    private Map<String, List<String>> testIds;

    public void onStart(ISuite suite) {
        testIds = new HashMap<>();
        int fixedIssuesFilterId = 10001;
        JiraIssue[] fixedIssues = JiraClient.getIssuesFromFilter(fixedIssuesFilterId);
        for (JiraIssue issue : fixedIssues){
            for (String testId : issue.getTestIds()) {
                List<String> jiraIssues = Optional.ofNullable(testIds.get(testId))
                        .orElse(new ArrayList<>());
                jiraIssues.add(issue.getKey());
                testIds.put(testId, jiraIssues);
            }
        }
    }

    public void onFinish(ISuite suite) {
        // not needed
    }

    public void onTestSuccess(ITestResult result) {
        String currentTestId = result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
        if (testIds.keySet().contains(currentTestId)){
            for (String issueKey : testIds.get(currentTestId)) {
                JiraClient.postComment(issueKey, "RoboDog: This test has passed.");
            }
        }
    }

    public void onTestFailure(ITestResult result) {
        String currentTestId = result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
        if (testIds.keySet().contains(currentTestId)){
            for (String issueKey : testIds.get(currentTestId)) {
                JiraClient.postComment(issueKey, "RoboDog: Failed test " + currentTestId);
                JiraClient.reopenIssue(issueKey);
            }
        }
    }

    public void onTestStart(ITestResult result) {
        // not needed
    }

    public void onTestSkipped(ITestResult result) {
        // not needed
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // not needed
    }

    public void onStart(ITestContext context) {
        // not needed
    }

    public void onFinish(ITestContext context) {
        // not needed
    }
}
