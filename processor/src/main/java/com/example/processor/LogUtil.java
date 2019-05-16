package com.example.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class LogUtil {

    private String packageName = "com.example.administrator";
    private Filer filer;
    private List<LogBean> logDataList = new ArrayList<>();

    public LogUtil(Filer filer) {
        this.filer = filer;
    }

    public void log(String key, String value){
        LogBean logBean = new LogBean(key, value);
        logDataList.add(logBean);
    }

    private TypeSpec CreatTypeSpec(){
        MethodSpec.Builder method = MethodSpec.methodBuilder("logString")
                .addModifiers(Modifier.PUBLIC);

        for (int i = 0; i < logDataList.size(); i++) {
            String key = logDataList.get(i).getKey();
            String value = logDataList.get(i).getValue();

            method.addStatement("$T logArg$L = $S", String.class, i, key + " : " + value);
        }


        TypeSpec typeSpec = TypeSpec.classBuilder("LogFile").addMethod(method.build()).build();
        return typeSpec;
    }


    public void createLogFile(){
        try {
            JavaFile.builder(packageName, CreatTypeSpec()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            logDataList.clear();
        }
    }

}
