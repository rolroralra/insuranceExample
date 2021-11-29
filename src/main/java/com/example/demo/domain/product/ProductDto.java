package com.example.demo.domain.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {
    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "상해보험")
    private String name;

    @ApiModelProperty(example = "LIFE_INSURANCE")
    private ProductType type;

    @ApiModelProperty(example = "2021-10-11 10:00:24")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ApiModelProperty(example = "2021-10-11 10:00:24")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ProductDto(Product product) {
        BeanUtils.copyProperties(product, this);
    }
}
