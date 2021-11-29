package com.example.demo.domain.user;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTests {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }


    @Order(1)
    @DisplayName("1. 보험가입자 전체 목록을 검색할 수 있다.")
    @Test
    public void test_search_all_users() {
        List<User> userList = userService.findAllUsers();
        assertThat(userList)
                .isNotEmpty();

        System.out.println(userList);
    }

    @Order(2)
    @DisplayName("2. 보험가입자의 목록을 검색조건을 통해 검색할 수 있다.")
    @Test
    public void test_search_user_list() {
        List<User> userList = userService.findUsersByPredicate(user -> true);

        assertThat(userList).isNotNull();

        System.out.println(userList);
    }

    @Order(3)
    @DisplayName("3. 보험가입자 id를 통해 보험가입자를 검색할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void test_search_user_by_id(Long userId) {
        User user = userService.findUserById(userId);
        assertThat(user)
                .isNotNull()
                .extracting(User::getId).isEqualTo(userId);

        System.out.println(user);
    }

    @Order(4)
    @DisplayName("4. 보험가입자를 추가할 수 있다. (회원가입)")
    @Test
    public void test_save_user() {
        User user = new User("New Added User");
        User addedUser = userService.addUser(user);

        assertThat(addedUser)
                .isNotNull()
                .extracting(User::getId).isNotNull();

        assertThat(userService.findUserById(addedUser.getId())).isNotNull();

        System.out.println(addedUser);
    }

    @Order(5)
    @DisplayName("5. 보험가입자를 수정할 수 있다. (보험가입자 개인정보 변경)")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void test_modify_user(Long userId) {
        User user = userService.findUserById(userId);
        assertThat(user)
                .isNotNull()
                .extracting(User::getId).isEqualTo(userId);

        user.setName("Modified_" + user.getName());

        User modifiedProduct = userService.modifyUser(user);

        assertThat(modifiedProduct)
                .isNotNull();

        assertThat(userService.findUserById(userId))
                .isNotNull()
                .isEqualTo(modifiedProduct);

        System.out.println(modifiedProduct);
    }
}