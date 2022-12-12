package org.knowledge4retail.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ShelfDto implements BasicDto {

    private Integer id;

    private Double positionX;

    private Double positionY;

    private Double positionZ;

    private Double orientationX;

    private Double orientationY;

    private Double orientationZ;

    private Double orientationW;

    @NotNull
    private Double width;

    @NotNull
    private Double height;

    @NotNull
    private Double depth;

    @NotNull
    private Integer lengthUnitId;

    private Integer storeId;

    private Integer productGroupId;

    private String cadPlanId;

    private String externalReferenceId;
}
