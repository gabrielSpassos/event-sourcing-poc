package com.gabrielpassos.event.producer.controllers.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class EventDTO {

    @ApiModelProperty(value = "Event name")
    @NotNull(message = "Event name should be informed")
    private String name;
    @ApiModelProperty(value = "Event numeric value")
    @NotNull(message = "Event value should be informed")
    private BigDecimal value;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
