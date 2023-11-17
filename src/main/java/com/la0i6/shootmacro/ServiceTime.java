package com.la0i6.purgatorymacro;

import java.util.List;

/****
 *** author：lao
 *** package：com.la0i6.purgatorymacro
 *** project：PurgatoryMacro
 *** name：ServiceTime
 *** date：2023/11/16  20:16
 *** filename：ServiceTime
 *** desc：淘宝服务器时间
 ***/

public class ServiceTime {

    /**
     * api : mtop.common.getTimestamp
     * v : *
     * ret : ["SUCCESS::接口调用成功"]
     * data : {"t":"1586519130440"}
     */

    private String api;
    private String v;
    /**
     * t : 1586519130440
     */

    private DataBean data;
    private List<String> ret;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<String> getRet() {
        return ret;
    }

    public void setRet(List<String> ret) {
        this.ret = ret;
    }

    public static class DataBean {
        private String t;

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }
    }

    @Override
    public String toString() {
        return "ServiceTime{" +
                "api='" + api + '\'' +
                ", v='" + v + '\'' +
                ", data=" + data +
                ", ret=" + ret +
                '}';
    }
}
