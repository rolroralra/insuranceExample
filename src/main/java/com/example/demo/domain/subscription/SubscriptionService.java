package com.example.demo.domain.subscription;

import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.message.MessageService;
import com.example.demo.domain.message.mock.MockMessageService;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductRepository;
import com.example.demo.domain.product.mock.MockProductRepository;
import com.example.demo.domain.subscription.dto.SubscriptionDto;
import com.example.demo.domain.subscription.mock.MockSubscriptionRepository;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.subscription.uw.UnderWritingService;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.domain.user.mock.MockUserRepository;
import com.example.demo.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {
    private final UnderWritingService underWritingService;
    private final MessageService messageService;
    private final TaskManagerConnectionPool taskManagerConnectionPool;
    private final SubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SubscriptionService() {
        this(
                new UnderWritingService(),
                new MockMessageService(),
                new TaskManagerConnectionPool(),
                MockSubscriptionRepository.getInstance(),
                MockProductRepository.getInstance(),
                MockUserRepository.getInstance()
        );
    }

    //    protected List<Subscription> searchAllSubscriptions() {
//        return subscriptionRepository.findAll();
//    }

    protected List<Subscription> searchSubscriptions(Predicate<Subscription> searchCondition) {
        return subscriptionRepository.findAll().stream().filter(searchCondition).collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription findSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }

    @Override
    public List<Subscription> findSubscriptionsByManagerId(Long managerId) {
        return subscriptionRepository.findByPredicate(subscription -> Objects.equals(managerId, subscription.getSubscriptionManager().getId()));
    }

    @Override
    public Subscription subscribeInsurance(Long productId, Long userId, SubscriptionDto subscriptionDto) {
        // 1. Request Parameter Validation Check
        if (!checkValidationOfSubscription(productId, userId, subscriptionDto)) {
            throw new InvalidRequestException(String.format("Not Valid Subscription Request. [%s]", subscriptionDto));
        }

        User user = userRepository.findById(userId);
        Product product = productRepository.findById(productId);

        // 2. Subscription 신규 추가
        Subscription subscription = createAndSaveSubscription(product, user, subscriptionDto);

        // 2. SMS 메시지 전송
        messageService.send(subscription.getSubscriptionManagerName(), "[신규 보험가입 요청] %s", subscription);
        messageService.send(subscription.getUserName(), "[신규 보험가입 요청 처리 중] %s", subscription);

        return subscription;
    }

    public UnderWriting requestUnderWriting(Long subscriptionId) {
        // 1. UnderWriting 요청
        UnderWriting underWriting = underWritingService.requestUnderWriting(subscriptionId);

        // 2. Subscription 조회
        Subscription subscription = underWriting.getSubscription();

        // 3. Subscription State Update (PROGRESS_UW)
        subscription.setState(Subscription.SubscriptionState.PROGRESS_UW);

        // 4. Subscription DB에 저장
        Subscription modifiedSubscription = subscriptionRepository.save(subscription);

        // 5. 보험가입 인가 요청 처리 진행 사항 메시지 전송
        messageService.send(modifiedSubscription.getSubscriptionManagerName(), "[보험가입 인가 요청 처리 중] %s", underWriting);

        return underWriting;
    }

//    protected Subscription addSubscription(Subscription subscription) {
//        return subscriptionRepository.save(subscription);
//    }
//
//    protected Subscription modifySubscription(Subscription subscription) {
//        return subscriptionRepository.save(subscription);
//    }

    private Subscription createAndSaveSubscription(Product product, User user, SubscriptionDto subscriptionDto) {
        // 1. Subscription 초기 생성
        Subscription subscription = new Subscription(product, user, subscriptionDto);

        // 2. SubscriptionManager 할당
        SubscriptionManager subscriptionManager = taskManagerConnectionPool.allocateSubscriptionManager();
        subscription.allocateManager(subscriptionManager);

        // 3. Subscription DB에 저장후 Return
        return subscriptionRepository.save(subscription);
    }

    private Boolean checkValidationOfSubscription(Long productId, Long userId, SubscriptionDto subscriptionDto) {
        if (Objects.isNull(productId) || Objects.isNull(userId) || Objects.isNull(subscriptionDto)) {
            return false;
        }

        User user = userRepository.findById(userId);
        Product product = productRepository.findById(productId);

        if (Objects.isNull(user) || Objects.isNull(product)) {
            return false;
        }

        // TODO: implementation


        return true;
    }
}
