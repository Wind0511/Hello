package com.wind.server.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//普通字符转化为URL编码
public class URLEncodTools {
    public String encode(String url) {
        try {
            String encodeURL = URLEncoder.encode(url, "UTF-8");
            return encodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while encoding" + e.getMessage();
        }
    }
}
