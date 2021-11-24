package com.example.demo.domain.subscription;

import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductService;
import com.example.demo.domain.product.productSearchCondition;
import com.example.demo.domain.subscription.dto.SubscriptionDto;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import com.example.demo.exception.InvalidRequestException;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubscriptionServiceTest {
    private SubscriptionService subscriptionService;
    private UserService userService;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService();
        productService = new ProductService();
        userService = new UserService();
    }

    @Order(1)
    @DisplayName("1. 보험가입자가 보험상품에 대해 보험가입 요청을 할 수 있다.")
    @RepeatedTest(value = 5)
    public void test_subscribe_insurance() {
        Product product = getAnyProduct();
        User user = getAnyUser();
        SubscriptionDto subscriptionDto = new SubscriptionDto();

        Subscription subscription = subscriptionService.subscribeInsurance(product.getId(), user.getId(), subscriptionDto);

        assertThat(subscription)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull()
                .hasFieldOrPropertyWithValue("product", product)
                .hasFieldOrPropertyWithValue("user", user)
                .hasFieldOrPropertyWithValue("state", Subscription.SubscriptionState.PROGRESS);

        assertThat(subscription.getUser())
                .isEqualTo(user);
        assertThat(subscription.getProduct())
                .isEqualTo(product);
        assertThat(subscription.getSubscriptionManager())
                .isNotNull();

        assertThat(subscription.getSubscriptionManagerName())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank();

//        System.out.println(subscription);
    }

    @Order(2)
    @DisplayName("2. 보험가입자가 보험상품에 대해 Bad Request 경우 예외가 발생한다.")
    @Test
    public void test_exception_subscribe_insurance() {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(0L, getAnyUser().getId(), subscriptionDto));

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(getAnyProduct().getId(), 0L, subscriptionDto));

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(getAnyProduct().getId(), getAnyUser().getId(), null));
    }

    @Order(3)
    @DisplayName("3. 보험가입 담당자가 보험가입 인가 요청을 할 수 있다.")
    @Test
    public void test_request_underwriting() {
        List<Subscription> subscriptionInProgressList = subscriptionService.searchSubscriptions(
                Subscription::isInProgress
        );

        subscriptionInProgressList.stream().mapToLong(Subscription::getId).forEach(subscriptionId -> {
            UnderWriting underWriting = subscriptionService.requestUnderWriting(subscriptionId);

            assertThat(underWriting)
                    .isNotNull()
                    .hasFieldOrProperty("id").isNotNull();

            Subscription modifiedSubscription = subscriptionService.findSubscriptionById(subscriptionId);
            assertThat(modifiedSubscription)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("underWriting", underWriting)
                    .hasFieldOrPropertyWithValue("state", Subscription.SubscriptionState.PROGRESS_UW);

            assertThat(modifiedSubscription.getUnderWritingManagerName())
                    .isNotNull()
                    .isNotEmpty()
                    .isNotBlank();

            System.out.println();
//            System.out.println(underWriting);
        });
    }

    @Test
    public void test_find_subscriptions_by_managerId() {
        subscriptionService.findAll().stream().map(Subscription::getSubscriptionManager).mapToLong(SubscriptionManager::getId).forEach(subscriptionManagerId -> {
            List<Subscription> subscriptionList = subscriptionService.findSubscriptionsByManagerId(subscriptionManagerId);

            assertThat(subscriptionList).isNotNull();
        });
    }

    private Product getAnyProduct() {
        List<Product> productList = productService.searchProducts(new productSearchCondition());
        return productList.get(new Random().nextInt(productList.size()));
    }

    private User getAnyUser() {
        List<User> userList = userService.findUsersByPredicate(user -> true);

        return userList.get(new Random().nextInt(userList.size()));
    }


}