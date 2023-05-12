package com.hirix.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private String errorId;
    private Integer errorCode;
    private String errorMessage;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
