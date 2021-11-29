package com.example.demo.domain.user;

import com.example.demo.domain.common.ApiResult;
import com.example.demo.domain.product.ProductDto;
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
public class UserController {
    private final IUserService userService;

    @RequestMapping(value = "/users", method = {RequestMethod.GET})
//    @SortDefault.SortDefaults({
//            @SortDefault(sort = "id", direction = Sort.Direction.DESC),
//            @SortDefault(sort = "name", direction = Sort.Direction.DESC)
//    })
    public ResponseEntity<ApiResult<List<UserDto>>> searchAllUsers(@PageableDefault(sort = {"id", "name"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        System.out.println(pageable);
        return ApiResult.succeed(
                userService.findAllUsers().stream()
                        .map(com.example.demo.domain.user.UserDto::new)
                        .collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/users/{userId}", method = {RequestMethod.GET})
    public ResponseEntity<?> searchUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        if (Objects.isNull(user)) {
            return ApiResult.failed("Not Exists", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(new com.example.demo.domain.user.UserDto(user));
    }

    @RequestMapping(value = "/users", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<?>> searchUsersByCondition(@RequestBody UserDto userDto) {
        User user = new User(userDto);

        return ApiResult.failed("Not Implemented");
    }

    @RequestMapping(value = "/users/add", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ApiResult<UserDto>> addUser(@RequestBody com.example.demo.domain.user.UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
        User user = userService.addUser(new User(userDto));

        URI location = uriComponentsBuilder.path("/api/v1/users/{userId}")
                .buildAndExpand(user.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        HttpStatus httpStatus = HttpStatus.CREATED;

        return ApiResult.succeed(new com.example.demo.domain.user.UserDto(user), httpHeaders, httpStatus);
    }

    @RequestMapping(value = "/users/{userId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<ApiResult<UserDto>> modifyUser(@PathVariable Long userId, @RequestBody com.example.demo.domain.user.UserDto userDto) {
        userDto.setId(userId);
        User user = new User(userDto);

        return ApiResult.succeed(new com.example.demo.domain.user.UserDto(userService.modifyUser(user)));
    }
}
