package com.hirix.controller.requests.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria object: search by equipment text description")
@Validated
public class RequirementSearchCriteria {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "spring data", type = "string", description = "text query")
    @NotNull
    @Size(min = 2, max = 250)
    private String query;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
