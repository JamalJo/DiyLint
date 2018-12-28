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

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhoumao on 2018/12/28.
 *
 * Model类域注解@SerializedName检查
 */
public class ModelSerializedNameDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "ModelSerializedName",
            "model类添加@SerializedName",
            "model注意添加@SerializedName",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(ModelSerializedNameDetector.class, Scope.JAVA_FILE_SCOPE));

    @Nullable
    @Override
    public List<String> applicableSuperClasses() {
        return Collections.singletonList(CommonClassNames.JAVA_LANG_OBJECT);
    }

    @Override
    public void checkClass(@NonNull JavaContext context, @NonNull PsiClass psiClass) {
        String path = context.getNameLocation(psiClass).getFile().getAbsolutePath();
        if (path.contains(Constants.MODEL_CHECK_DIR)) {
            // 判断是否加了注解
            for (PsiField uField : psiClass.getFields()) {
                if (uField.getText().contains("@SerializedName")) {
                    continue;
                }
                String message = uField.getName()
                        + " 作为model类其域需加上注解：@SerializedName";
                context.report(ISSUE, psiClass, context.getLocation(uField), message);
            }
        }
    }
}