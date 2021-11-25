package com.example.demo.domain.subscription;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.product.IProductService;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductService;
import com.example.demo.domain.product.productSearchCondition;
import com.example.demo.domain.subscription.uw.IUnderWritingService;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.subscription.uw.UnderWritingService;
import com.example.demo.domain.user.IUserService;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import com.example.demo.exception.InvalidRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubscriptionServiceTest {
    private ISubscriptionService subscriptionService;
    private IUserService userService;
    private IProductService productService;
    private IUnderWritingService underWritingService;

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService();
        productService = new ProductService();
        userService = new UserService();
        underWritingService = new UnderWritingService();
    }

    @Order(1)
    @DisplayName("1. 보험가입자가 보험상품에 대해 보험가입 요청을 할 수 있다.")
    @RepeatedTest(value = 5)
    public void test_subscribe_insurance() {
        Product product = getAnyProduct();
        User user = getAnyUser();
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo();

        Subscription subscription = subscriptionService.subscribeInsurance(product.getId(), user.getId(), subscriptionInfo);

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
        assertThat(subscription.getManager())
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
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(0L, getAnyUser().getId(), subscriptionInfo));

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(getAnyProduct().getId(), 0L, subscriptionInfo));

        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> subscriptionService.subscribeInsurance(getAnyProduct().getId(), getAnyUser().getId(), null));
    }

    @Order(3)
    @DisplayName("3. 보험가입 담당자가 보험가입 인가 요청을 할 수 있다.")
    @Test
    public void test_request_underwriting() {
        List<Subscription> subscriptionInProgressList = subscriptionService.findSubscriptions(
                Subscription::isProgress
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

    @Order(4)
    @DisplayName("4. 보험가입 담당자가 보험가입 담당자의 id를 기준으로 보험가입 목록을 검색할 수 있다.")
    @Test
    public void test_find_subscriptions_by_managerId() {
        subscriptionService.findAllSubscriptions().stream().map(Subscription::getManager).mapToLong(SubscriptionManager::getId).forEach(subscriptionManagerId -> {
            List<Subscription> subscriptionList = subscriptionService.findSubscriptionsByManagerId(subscriptionManagerId);

            assertThat(subscriptionList).isNotNull();
        });
    }

    @Order(5)
    @DisplayName("5. 보험가입 담당자가 보험가입 요청에 대한 처리 결과를 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void test_register_subscription_result(Boolean underWritingResult) {
        Subscription subscription = subscriptionService.subscribeInsurance(getAnyProduct().getId(), getAnyUser().getId(), new SubscriptionInfo());
        assertThat(subscription)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull();

        UnderWriting underWriting = subscriptionService.requestUnderWriting(subscription.getId());
        assertThat(underWriting)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull();

        UnderWriting resultUnderWriting = underWritingService.registerUnderWritingResult(underWriting.getId(), underWritingResult);
        assertThat(resultUnderWriting)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull()
                .hasFieldOrPropertyWithValue("result", underWritingResult);

        Contract contract = subscriptionService.registerSubscriptionResult(subscription.getId());

        if (underWritingResult) {
            assertThat(contract)
                    .isNotNull()
                    .hasFieldOrProperty("id").isNotNull()
                    .hasFieldOrProperty("product").isNotNull()
                    .hasFieldOrProperty("user").isNotNull()
                    .hasFieldOrProperty("manager").isNotNull()
                    .hasFieldOrProperty("contractInfo").isNotNull();
        } else {
            assertThat(contract)
                    .isNull();
        }
    }

    private Product getAnyProduct() {
        List<Product> productList = productService.findProducts(new productSearchCondition());
        return productList.get(new Random().nextInt(productList.size()));
    }

    private User getAnyUser() {
        List<User> userList = userService.findUsersByPredicate(user -> true);

        return userList.get(new Random().nextInt(userList.size()));
    }
}