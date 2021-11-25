package com.example.demo.domain.product;

import com.example.demo.domain.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
    private final IProductService productService;

    @RequestMapping(value = "/products", method = {RequestMethod.GET})
    @ApiOperation(value = "보험상품 전체 목록 조회", notes = "보험상품 전체 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
//    @SortDefault.SortDefaults({
//            @SortDefault(sort = "id", direction = Sort.Direction.DESC),
//            @SortDefault(sort = "name", direction = Sort.Direction.DESC)
//    })
    public ResponseEntity<ApiResult<List<ProductDto>>> searchAllProducts(@PageableDefault(sort = {"id", "name"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        System.out.println(pageable);
        return ApiResult.succeed(
                productService.findAllProducts().stream()
                        .map(ProductDto::new)
                        .collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/products/{productId}", method = {RequestMethod.GET})
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<?> searchProductById(@PathVariable Long productId) {
        Product product = productService.findProductById(productId);

        if (Objects.isNull(product)) {
            return ApiResult.failed("Not Exists", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(new ProductDto(product));
    }

    @RequestMapping(value = "/products", method = {RequestMethod.POST})
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<ApiResult<?>> searchProductsByCondition(@RequestBody ProductDto productDto) {
        Product product = new Product(productDto);

        return ApiResult.failed("Not Implemented");
    }

    @RequestMapping(value = "/products/add", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<ApiResult<ProductDto>> addProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriComponentsBuilder) {
        Product product = productService.addProduct(new Product(productDto));

        URI location = uriComponentsBuilder.path("/api/v1/products/{productId}")
                .buildAndExpand(product.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        HttpStatus httpStatus = HttpStatus.CREATED;

        return ApiResult.succeed(new ProductDto(product), httpHeaders, httpStatus);
    }

    @RequestMapping(value = "/products/{productId}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<ApiResult<ProductDto>> modifyProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        productDto.setId(productId);
        Product product = new Product(productDto);

        return ApiResult.succeed(new ProductDto(productService.modifyProduct(product)));
    }
}
