package com.desiremc.hcf.util;

import com.desiremc.hcf.session.HCFSession;

public class SettingsUtil {

    /**
     * Only for use of true/false settings only!!!
     *
     * @param s
     * @param setting
     */
    public static void toggleSetting(HCFSession s, String setting) {
        if (s.getSettings().get(setting).equals("true")) {
            s.getSettings().put(setting, "false");
        } else if (s.getSettings().get(setting).equals("false")) {
            s.getSettings().put(setting, "true");
        }
    }

}
