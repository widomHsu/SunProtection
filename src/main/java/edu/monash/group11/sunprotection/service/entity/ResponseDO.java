package edu.monash.group11.sunprotection.service.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResponseDO {

    @Schema(description = "Whether an error occurred in the request")
    private boolean success;
    @Schema(description = "Response message")
    private String msg;
    @Schema(description = "Response data")
    private Object data;

    public ResponseDO() {
    }

    public ResponseDO(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
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
