package com.hirix.controller.requests.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleSearchCriteria {
    private String query;
//    private String birthday;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
