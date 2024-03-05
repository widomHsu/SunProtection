package edu.monash.group11.sunprotection.service.entity;

import lombok.Data;

@Data
public class ResponseDO {

    private boolean success;
    private String msg;
    private Object data;

    public ResponseDO() {
    }

    public ResponseDO(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseDO success(Object data){
        return new ResponseDO(true, "ok", data);
    }

    public static  ResponseDO fail(String msg){
        return new ResponseDO(false, msg, null);
    }

    public static  ResponseDO fail(String msg, Object data){
        return new ResponseDO(false, msg, data);
    }

}
