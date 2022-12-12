package org.knowledge4retail.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScanDto implements BasicDto {

    private String entityType;  // referenceFrame and referenceId for further steps
    private String timestamp;
    private String id;
    private String origin;
    private Double positionX;
    private Double positionY;
    private Double positionZ;
    private Double orientationX;
    private Double orientationY;
    private Double orientationZ;
    private Double orientationW;
    private Double height;
    private Double width;
    private Double depth;
    private Integer lengthUnitId;
    private String externalReferenceId;
    private String additionalInfo;
}
