package org.knowledge4retail.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StoreDto implements BasicDto {

    private Integer id;

    @NotBlank
    private String storeName;

    private String storeNumber;

    private String addressCountry;

    private String addressState;

    private String addressCity;

    private String addressPostcode;

    private String addressStreet;

    private String addressStreetNumber;

    private String addressAdditional;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String cadPlanId;

    private String externalReferenceId;
}
