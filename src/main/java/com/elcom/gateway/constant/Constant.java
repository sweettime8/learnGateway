/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.gateway.constant;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;

/**
 *
 * @author Admin
 */
public class Constant {
    
    //Typesafe config
    public static final String  CONFIG_DIR = System.getProperty("user.dir") + File.separator + "config" + File.separator;
    private static final Config CONFIG;
    
    //Constant
    public static String  CONSTANT_STR1 = "";
    public static int     CONSTANT_INT2 = 0;
    public static boolean CONSTANT_BOOL3 = false;
    public static final String DOWNLOAD_FILE_PATH = "downloadFile";
    
    static {
        Config baseConfig = ConfigFactory.load("constant");
        CONFIG = ConfigFactory.parseFile(new File(CONFIG_DIR + "constant.conf")).withFallback(baseConfig);
        loadConfig();
    }
    
    public static void loadConfig() {
        CONSTANT_STR1 = CONFIG.getString("CONSTANT_STR1");
        CONSTANT_INT2 = CONFIG.getInt("CONSTANT_INT2");
        CONSTANT_BOOL3 = CONFIG.getBoolean("CONSTANT_BOOL3");
    }
}
