package com.hirix.controller.requests.create;

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
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Object with information necessary to create new requirement")
public class RequirementCreateRequest {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2", type = "integer",
            description = "minimum required employee experience in years")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "2")
    @Min(value = 0)
    @Max(value = 100)
    @Pattern(regexp = "^[1-9][\\d]+")
    private Integer experience;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true", type = "boolean",
            description = "is employee must be active now with his skill")
    private boolean active;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "integer",
            description = "minimum required number of employee recommendations")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 0)
    @Max(value = 100)
    @Pattern(regexp = "^[\\d]+")
    private Integer recommendations;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Spring Data", type = "string",
            description = "equipments required in employee skill")
    @NotNull
    @Size(min = 2, max = 250)
    private String equipments;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1500", type = "integer",
            description = "monthly salary offered to employee in dollar equivalent")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1)
    @Max(value = 100000)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Integer salary;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "12", type = "integer",
            description = "term of contract offered to employee in months")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "12")
    @Min(value = 1)
    @Max(value = 1000)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Integer term;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "requirement company id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long companyId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "required industry id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long industryId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "required profession id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long professionId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "required specialization id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long specializationId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "required rank id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long rankId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "required position id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long positionId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1", type = "long",
            description = "offered to employee location id")
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    @Pattern(regexp = "^[1-9]([\\d]+)")
    private Long locationOfferedId;
}

