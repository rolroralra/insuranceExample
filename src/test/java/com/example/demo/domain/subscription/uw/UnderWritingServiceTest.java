package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.product.*;
import com.example.demo.domain.subscription.*;
import com.example.demo.domain.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnderWritingServiceTest {
    private IUnderWritingService underWritingService;
    private SubscriptionRepository subscriptionRepository;
    private IProductService productService;
    private IUserService userService;
    private ISubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        underWritingService = new UnderWritingService();
        subscriptionRepository = MockSubscriptionRepository.getInstance();
        productService = new ProductService();
        userService = new UserService();
        subscriptionService = new SubscriptionService();
    }

    @Order(1)
    @DisplayName("1. 보험가입 인가 요청을 처리할 수 있다.")
    @Test
    void test_request_under_writing() {
        Subscription subscription = createSubscription();
        Long subscriptionId = subscription.getId();

        UnderWriting underWriting = underWritingService.requestUnderWriting(subscriptionId);

        assertThat(underWriting)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.State.PROGRESS);

        assertThat(underWriting.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.State.PROGRESS_UW);
    }

    @Order(2)
    @DisplayName("2. 보험가입 인가 담당자는 보험가입 인가 처리 결과 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void registerUnderWritingResult(Boolean underWritingResult) {
        Subscription subscription = createSubscription();
        UnderWriting underWriting = createUnderWriting(subscription.getId());

        UnderWriting result = underWritingService.registerUnderWritingResult(underWriting.getId(), underWritingResult);
        System.out.println();

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.State.COMPLETED)
                .hasFieldOrPropertyWithValue("result", underWritingResult);

        assertThat(result.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.State.COMPLETED_UW);
    }

    private Product getAnyProduct() {
        List<Product> productList = productService.findProducts(new ProductSearchCondition());
        return productList.get(new Random().nextInt(productList.size()));
    }

    private User getAnyUser() {
        List<User> userList = userService.findUsersByPredicate(user -> true);
        return userList.get(new Random().nextInt(userList.size()));
    }

    private Subscription createSubscription() {
        Product product = getAnyProduct();
        User user = getAnyUser();
        SubscriptionInfo subscriptionInfo = SubscriptionInfo.builder()
                .startDate("2021-11-25")
                .expireDate("2041-11-25")
                .insuranceMoneyPerMonth(100000L)
                .build();

        Subscription subscription = subscriptionService.subscribeInsurance(product.getId(), user.getId(), subscriptionInfo);
        System.out.println();

        assertThat(subscription)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull()
                .hasFieldOrPropertyWithValue("product", product)
                .hasFieldOrPropertyWithValue("user", user)
                .hasFieldOrPropertyWithValue("state", Subscription.State.PROGRESS);

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

        return subscription;
    }

    private UnderWriting createUnderWriting(Long subscriptionId) {
        UnderWriting underWriting = subscriptionService.requestUnderWriting(subscriptionId);

        assertThat(underWriting)
                .isNotNull()
                .hasFieldOrProperty("id").isNotNull();

        Subscription modifiedSubscription = subscriptionService.findSubscriptionById(subscriptionId);
        assertThat(modifiedSubscription)
                .isNotNull()
                .hasFieldOrPropertyWithValue("underWriting", underWriting)
                .hasFieldOrPropertyWithValue("state", Subscription.State.PROGRESS_UW);

        assertThat(modifiedSubscription.getUnderWritingManagerName())
                .isNotNull()
                .isNotEmpty()
                .isNotBlank();

        System.out.println();

        return underWriting;
    }

}