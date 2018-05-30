package com.nathaniel.app.core.start;

import com.nathaniel.app.core.constant.CoreContant;
import com.nathaniel.app.core.util.ExecuteEnvironment;

public class AppLauncher {

    public static void main(String[] args) {
        detectEnvironment();
        launch();
    }


    private static void detectEnvironment() {
        try {
            Class.forName("com.nathaniel.app.base.context.AnnotationConfigContextStater", false, AppLauncher.class.getClassLoader());
            System.getenv().put(CoreContant.EXE_ENV, ExecuteEnvironment.STANDARD.getKey());
            return;
        } catch (Exception e) {
        }

        try {
            Class.forName("com.nathaniel.app.servlet.support.container.UndertowWrapper", false, AppLauncher.class.getClassLoader());
            System.getenv().put(CoreContant.EXE_ENV, ExecuteEnvironment.WEN_EMBED.getKey());
            return;
        } catch (Exception e) {
        }

    }

    private static void launch() {
        String env = System.getenv().get(CoreContant.EXE_ENV);
        if (ExecuteEnvironment.STANDARD.equals(ExecuteEnvironment.getInstance(env))) {

        }else {
            try {
                Class.forName("com.nathaniel.app.mvc.starter.AnnotationConfigWebContextStarter");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
