package com.example.demo.domain.contract;

import com.example.demo.domain.manager.contract.ContractManager;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import com.example.demo.domain.product.IProductService;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductService;
import com.example.demo.domain.product.productSearchCondition;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContractServiceTest {
    private IContractService contractService;
    private IProductService productService;
    private IUserService userService;
    private ISubscriptionService subscriptionService;
    private IUnderWritingService underWritingService;

    @BeforeEach
    void setUp() {
        contractService = new ContractService();
        productService = new ProductService();
        userService = new UserService();
        subscriptionService = new SubscriptionService();
        underWritingService = new UnderWritingService();
    }

    @Order(1)
    @DisplayName("1. 보험가입 요청 -> 보험가입 인가 요청 -> 보험가입 인가 요청 처리 -> 보험가입 요청 처리 -> 보험계약 생성")
    @Test
    void test_create_contract() {
        User user = getAnyUser();
        Product product = getAnyProduct();

        createContract(user, product);
    }

    @Order(2)
    @DisplayName("2. 보험가입자가 userId 기준으로 보험계약 목록을 검색할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5})
    void test_find_contracts_by_userId(int contractCount) {
        User user = getAnyUser();
        for (int i = 0; i < contractCount; i++) {
            createContract(user, getAnyProduct());
        }

        List<Contract> contractList = contractService.findContractsByUserId(user.getId());

        assertThat(contractList)
                .isNotNull()
                .hasSize(contractCount)
                .hasOnlyElementsOfType(Contract.class)
                .doesNotContainNull();
    }

    @Order(3)
    @DisplayName("3. 보험계약 담당자가 managerId 기준으로 보험계약 목록을 검색할 수 있다.")
    @Test
    void test_find_contracts_by_managerId() {
        Contract contract = createContract();

        ContractManager manager = contract.getManager();

        List<Contract> contractList = contractService.findContractsByManagerId(manager.getId());

        assertThat(contractList)
                .isNotNull()
                .hasOnlyElementsOfType(Contract.class)
                .doesNotContainNull()
                .contains(contract);
    }

    @Order(4)
    @DisplayName("4. 보험계약 전체 목록을 검색할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5})
    void test_find_All_Contracts(int contractCount) {
        List<Contract> list = new ArrayList<>();
        for (int i = 0; i < contractCount; i++) {
            Contract contract = createContract();
            list.add(contract);
        }

        List<Contract> contractList = contractService.findAllContracts();

        assertThat(contractList)
                .isNotNull()
                .hasOnlyElementsOfType(Contract.class)
                .doesNotContainNull()
                .containsAll(list);
    }

    @Order(5)
    @DisplayName("5. 보험계약 id 기준으로 보험계약을 검색할 수 있다.")
    @Test
    void test_find_Contract_By_Id() {
        Contract contract = createContract();

        Contract contractResult = contractService.findContractById(contract.getId());

        assertThat(contractResult)
                .isNotNull()
                .isEqualTo(contract);
    }

    private Product getAnyProduct() {
        List<Product> productList = productService.findProducts(new productSearchCondition());
        return productList.get(new Random().nextInt(productList.size()));
    }

    private User getAnyUser() {
        List<User> userList = userService.findUsersByPredicate(user -> true);

        return userList.get(new Random().nextInt(userList.size()));
    }

    private Contract createContract() {
        return createContract(getAnyUser(), getAnyProduct());
    }

    private Contract createContract(User user, Product product) {
        SubscriptionInfo subscriptionInfo = SubscriptionInfo.builder()
                .startDate("2021-11-25")
                .expireDate("2041-11-25")
                .insuranceMoneyPerMonth(100000L)
                .build();

        Subscription subscription = subscriptionService.subscribeInsurance(product.getId(), user.getId(), subscriptionInfo);

        UnderWriting underWriting = subscriptionService.requestUnderWriting(subscription.getId());

        underWritingService.registerUnderWritingResult(underWriting.getId(), true);

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
}