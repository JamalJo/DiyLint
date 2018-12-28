package com.diy.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

public class MyIssueRegistry extends IssueRegistry {

    public MyIssueRegistry() {
        System.out.println("==== custom lint checks start ====");
    }

    @Override
    public synchronized List<Issue> getIssues() {
        return Arrays.asList(
                NewThreadDetector.ISSUE,
                ModelSerializedNameDetector.ISSUE,
                ModelInitialDetector.ISSUE);
    }
}