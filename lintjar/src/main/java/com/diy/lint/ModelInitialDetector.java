package com.diy.lint;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoumao on 2018/12/28.
 *
 * Model类域初始化检查
 */
public class ModelInitialDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "ModelInitial",
            "model类的域需要在构造函数中初始化",
            "model类的域需要在构造函数中初始化",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(ModelInitialDetector.class, Scope.JAVA_FILE_SCOPE));

    @Nullable
    @Override
    public List<String> applicableSuperClasses() {
        System.out.println("==== applicableSuperClasses ====");
        return Collections.singletonList(CommonClassNames.JAVA_LANG_OBJECT);
    }

    @Override
    public void checkClass(@NonNull JavaContext context, @NonNull PsiClass psiClass) {
        String path = context.getNameLocation(psiClass).getFile().getAbsolutePath();
        if (path.contains(Constants.MODEL_CHECK_DIR)) {

            //无构造函数的情况
            if (psiClass.getConstructors().length <= 0) {
                String message = " model类的域需要在构造函数中初始化";
                context.report(ISSUE, psiClass, context.getNameLocation(psiClass),
                        message);
                return;
            }

            HashMap<String, Boolean> fieldInitMap = new HashMap<>();  //key：域  value：是否初始化了

            // 统计model中需要初始化的域
            for (PsiField uField : psiClass.getFields()) {
                fieldInitMap.put(uField.getName(), false);
            }

            // Constructor函数中，如果域初始化了，则加入到fieldInitMap中
            PsiMethod psiMethod = psiClass.getConstructors()[0];
            if (psiMethod.getBody() != null) {
                for (PsiStatement psiStatement : psiMethod.getBody().getStatements()) {
                    String stateMent = psiStatement.getText();
                    if (stateMent.contains("=")) {
                        String fieldName = psiStatement.getText().split("=")[0].trim();
                        fieldInitMap.put(fieldName, true);
                    }
                }
            }

            // 对于Constructor函数中，未初始化的域，lint提示
            for (Map.Entry<String, Boolean> entry : fieldInitMap.entrySet()) {
                if (!entry.getValue()) {  //未初始化过
                    String message = entry.getKey()
                            + " 作为model类其域需要在构造函数中初始化";
                    context.report(ISSUE, psiClass, context.getLocation(psiMethod), message);
                }
            }
        }
    }
}