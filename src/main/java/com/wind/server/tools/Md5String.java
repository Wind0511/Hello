package com.wind.server.tools;

import java.security.MessageDigest;
//md5加密
public class Md5String {
    public String getMd5(String text){
        String res = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(text.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
}
