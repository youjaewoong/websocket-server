package com.websocket.server.controller.redis.kcc.error;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean success;

    @ApiModelProperty(value = "응답 코드 번호 : 0000 정상, 9999 비정상")
    private String return_code;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;
 
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("success", success)
                .append("return_code", return_code)
                .append("msg", msg)
                .toString();
    }
}