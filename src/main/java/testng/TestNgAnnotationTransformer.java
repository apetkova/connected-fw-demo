package testng;

import jira.JiraClient;
import jira.JiraIssue;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.testng.log4testng.Logger;

public class TestNgAnnotationTransformer implements IAnnotationTransformer {
    private static List<JiraIssue> openIssues;

    private static Logger logger = Logger.getLogger(TestNgAnnotationTransformer.class);

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        String testId = testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName();
        if (disable(testId)) {
            annotation.setEnabled(false);
        }
    }

    private boolean disable(String testId) {
        for (JiraIssue issue : getJiraIssues()) {
            if (issue.getTestIds().contains(testId)) {
                logger.warn(String.format("Disable test %s due to open issue %s;", testId, issue.getKey()));
                return true;
            }
        }
        return false;
    }

        private List<JiraIssue> getJiraIssues () {
            int activeIssuesFilterId = 10000;
            if (openIssues == null) {
                openIssues = Arrays.asList(JiraClient.getIssuesFromFilter(activeIssuesFilterId));
            }
            return openIssues;
        }
    }
