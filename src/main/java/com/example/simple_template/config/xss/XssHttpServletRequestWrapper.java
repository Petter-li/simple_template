package com.example.simple_template.config.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values != null) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if(!StrUtil.hasEmpty(value)) {
                    value = HtmlUtil.filter(value);
                }
                values[i] = value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        LinkedHashMap<String, String[]> map = new LinkedHashMap();
        if(parameters != null) {
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(!StrUtil.hasEmpty(value)) {
                        value = HtmlUtil.filter(value);
                    }
                    values[i] = value;
                };
                map.put(key, values);
            }
        }
        return map;
    };

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // For non-json type, return directly
        if (super.getContentType() == null || !super.getContentType().equalsIgnoreCase("application/json")) {
            return super.getInputStream();
        }

        // For JSON, read the stream and filter
        InputStream in = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder body = new StringBuilder();
        String line = buffer.readLine();
        while (line != null) {
            body.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();

        String bodyStr = body.toString();
        if (StrUtil.isBlank(bodyStr)) {
            // Return an empty input stream for an empty body
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return -1;
                }

                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }

        Map<String, Object> map = JSONUtil.parseObj(bodyStr);
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : map.keySet()) {
            Object val = map.get(key);
            if (val instanceof String) {
                if (!StrUtil.hasEmpty(val.toString())) {
                    result.put(key, HtmlUtil.filter(val.toString()));
                } else {
                    result.put(key, val);
                }
            } else {
                result.put(key, val);
            }
        }

        String json = JSONUtil.toJsonStr(result);
        ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes("UTF-8"));
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
