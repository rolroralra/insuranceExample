package com.example.demo.domain.reward;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractService;
import com.example.demo.domain.contract.IContractService;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductService;
import com.example.demo.domain.subscription.ISubscriptionService;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionInfo;
import com.example.demo.domain.subscription.SubscriptionService;
import com.example.demo.domain.subscription.uw.IUnderWritingService;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.subscription.uw.UnderWritingService;
import com.example.demo.domain.user.IUserService;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RewardServiceTest {
    private IRewardService rewardService;
    private IUserService userService;
    private ProductService productService;
    private ISubscriptionService subscriptionService;
    private IUnderWritingService underWritingService;
    private IContractService contractService;

    @BeforeEach
    void setUp() {
        rewardService = new RewardService();
        userService = new UserService();
        productService = new ProductService();
        subscriptionService = new SubscriptionService();
        underWritingService = new UnderWritingService();
        contractService = new ContractService();
    }

    @Order(1)
    @DisplayName("1. 보험가입자가 특정 계약에 대해 보상청구 요청할 수 있다.")
    @Test
    void test_request_reward() {
        Reward reward = createReward();
        assertThat(reward)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("manager")
                .hasFieldOrPropertyWithValue("state", Reward.State.PROGRESS);

    }

    @Order(2)
    @DisplayName("2. 보상청구 담당자는 보상청구 요청 처리 결과를 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void test_register_reward_result(Boolean rewardResultValue) {
        Reward reward = createReward();
        assertThat(reward)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("manager")
                .hasFieldOrPropertyWithValue("state", Reward.State.PROGRESS);

        RewardResult rewardResult = RewardResult.builder()
                .rewardAmount(25000L)
                .receiver(reward.getUserName())
                .result(rewardResultValue)
                .build();

        Reward resultReward = rewardService.registerRewardResult(reward.getId(), rewardResult);
        assertThat(resultReward).isNotNull();

        Contract searchContractResult = contractService.findContractById(resultReward.getContract().getId());

        assertThat(searchContractResult)
                .isNotNull();

        assertThat(searchContractResult.getRewardHistory().getRewardMap())
                .containsEntry(resultReward.getId(), resultReward);
    }

    @Order(3)
    @DisplayName("3. 보상청구 담당자는 managerId를 통해 보상청구 목록을 검색할 수 있다.")
    @Test
    void findRewardsByManagerId() {
        Reward reward = createReward();

        RewardManager rewardManager = reward.getManager();

        List<Reward> rewardList = rewardService.findRewardsByManagerId(rewardManager.getId());

        assertThat(rewardList)
                .isNotNull()
                .isNotEmpty()
                .contains(reward);
    }

    @Order(4)
    @DisplayName("4. 보험 가입자는 userId를 통해 보상청구 목록을 검색할 수 있다")
    @Test
    void findRewardsByUserId() {
        Reward reward = createReward();

        User user = reward.getUser();

        List<Reward> rewardList = rewardService.findRewardsByUserId(user.getId());

        assertThat(rewardList)
                .isNotNull()
                .isNotEmpty()
                .contains(reward);
    }

    private User getAnyUser() {
        List<User> userList = userService.findAllUsers();
        return userList.get(new Random().nextInt(userList.size()));
    }

    private Product getAnyProduct() {
        List<Product> productList = productService.findAllProducts();
        return productList.get(new Random().nextInt(productList.size()));
    }

    private Contract createContract(User user, Product product) {
        SubscriptionInfo subscriptionInfo = SubscriptionInfo.builder()
                .startDate("2021-11-25")
                .expireDate("2041-11-25")
                .insuranceMoneyPerMonth(100000L)
                .build();

        Subscription subscription = subscriptionService.subscribeInsurance(product.getId(), user.getId(), subscriptionInfo);
        System.out.println();

        UnderWriting underWriting = subscriptionService.requestUnderWriting(subscription.getId());
        System.out.println();

        underWritingService.registerUnderWritingResult(underWriting.getId(), true);
        System.out.println();

        Contract contract = subscriptionService.registerSubscriptionResult(subscription.getId());

        assertThat(contract)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("product", product)
                .hasFieldOrPropertyWithValue("user", user)
                .hasFieldOrProperty("contractInfo")
                .hasFieldOrProperty("manager")
                .hasFieldOrPropertyWithValue("state", Contract.State.PROGRESS);

        assertThat(contract.getContractInfo())
                .isNotNull()
                .hasFieldOrProperty("startDate")
                .hasFieldOrProperty("expireDate")
                .hasFieldOrProperty("insuranceMoneyPerMonth")
                .hasFieldOrProperty("totalAmount");

        return contract;
    }

    private Contract createContract() {
       return createContract(getAnyUser(), getAnyProduct());
    }

    private Reward createReward() {
        Contract newContract = createContract();
        User user = newContract.getUser();
        System.out.println();

        RewardInfo rewardInfo = RewardInfo.builder()
                .type(RewardType.getDefaultOr(null))
                .addFile("MEDICAL_TREATMENT", "진료명세서...")
                .addFile("MEDICAL_DETAIL", "진료세부내역서...")
                .description("보상청구 요청 정상 처리하였습니다.")
                .build();

        Contract contract = contractService.findContractsByUserId(user.getId()).stream().findAny().orElse(null);
        assertThat(contract)
                .isNotNull()
                .extracting(Contract::getId).isNotNull();

        Reward reward = rewardService.requestReward(contract.getId(), rewardInfo);

        assertThat(reward)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("manager")
                .hasFieldOrPropertyWithValue("state", Reward.State.PROGRESS)
                .hasFieldOrPropertyWithValue("contract", contract)
                .hasFieldOrPropertyWithValue("rewardInfo", rewardInfo);

        assertThat(reward.getId()).isNotNull();
        assertThat(reward.getManager()).isNotNull();

        Contract searchContractResult = contractService.findContractById(reward.getContract().getId());

        System.out.println(searchContractResult);
        assertThat(searchContractResult)
                .isNotNull();

        assertThat(searchContractResult.totalRewardHistoryCount())
                .isGreaterThanOrEqualTo(0);

        return reward;
    }



}