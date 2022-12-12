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
public class ShelfLayerDto implements BasicDto {

    private Integer id;

    private Integer shelfId;

    private Integer level;

    private String type;

    private Double positionZ;

    @NotNull
    private Double width;

    @NotNull
    private Double height;

    @NotNull
    private Double depth;

    @NotNull
    private Integer lengthUnitId;

    private String externalReferenceId;
}
