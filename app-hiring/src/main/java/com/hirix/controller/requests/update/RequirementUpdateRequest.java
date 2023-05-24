package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.RequirementCreateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Object with information necessary to update requirement")
public class RequirementUpdateRequest extends RequirementCreateRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "updated requirement id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long id;
}
