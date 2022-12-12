package org.knowledge4retail.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacingDto implements BasicDto {

    private Integer id;
    private Integer shelfLayerId;
    private Integer layerRelativePosition;
    private Integer noOfItemsWidth;
    private Integer noOfItemsDepth;
}
