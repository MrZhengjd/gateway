package com.game.common.util;


import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TxtUtil {
    public static final String configPath0 = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator;

    public static JSONObject loadTxt(String fileName){
        String path = configPath0 + File.separator + "json" + File.separator + fileName + ".txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String json ="";
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            try {
                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                json = sb.toString();
            }catch (Exception e){
                try {
                    reader.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            } finally {
                try {
                    reader.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return JSONObject.parseObject(json);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("cannot read the txt");

        }

    }
}
