package com.example.demo.domain.subscription;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractService;
import com.example.demo.domain.contract.IContractService;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.message.MessageService;
import com.example.demo.domain.message.MockMessageService;
import com.example.demo.domain.product.MockProductRepository;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductRepository;
import com.example.demo.domain.subscription.uw.IUnderWritingService;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.subscription.uw.UnderWritingService;
import com.example.demo.domain.user.MockUserRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {
    private final IContractService contractService;

    private final SubscriptionRepository subscriptionRepository;

    private final IUnderWritingService underWritingService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final MessageService messageService;
    private final TaskManagerConnectionPool taskManagerConnectionPool;

    public SubscriptionService() {
        this(
                new ContractService(),
                MockSubscriptionRepository.getInstance(),
                new UnderWritingService(),
                MockProductRepository.getInstance(),
                MockUserRepository.getInstance(),
                new MockMessageService(),
                new TaskManagerConnectionPool()
        );
    }

    @Override
    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> findSubscriptions(Predicate<Subscription> predicate) {
        return findAllSubscriptions().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public Subscription findSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.findByPredicate(subscription -> Objects.equals(userId, subscription.getUserId()));
    }

    @Override
    public List<Subscription> findSubscriptionsByManagerId(Long managerId) {
        return subscriptionRepository.findByPredicate(subscription -> Objects.equals(managerId, subscription.getManager().getId()));
    }

    @Override
    public Subscription subscribeInsurance(Long productId, Long userId, SubscriptionInfo subscriptionInfo) {
        // 1. Request Parameter Validation Check
        if (!_checkValidationOfSubscription(productId, userId, subscriptionInfo)) {
            throw new InvalidRequestException(String.format("Not Valid Subscription Request. [%s]", subscriptionInfo));
        }

        User user = userRepository.findById(userId);
        Product product = productRepository.findById(productId);

        // 2. Subscription ?????? ??????
        Subscription subscription = _createAndSaveSubscription(product, user, subscriptionInfo);

        // 3. SMS ????????? ??????
        messageService.send(subscription.getSubscriptionManagerName(), "[?????? ???????????? ??????] %s", subscription);
        messageService.send(subscription.getUserName(), "[?????? ???????????? ?????? ?????? ???] %s", subscription);

        return subscription;
    }

    public UnderWriting requestUnderWriting(Long subscriptionId) {
        // 1. UnderWriting ??????
        UnderWriting underWriting = underWritingService.requestUnderWriting(subscriptionId);

        // 2. Subscription State Update (PROGRESS_UW)
        Subscription subscription = underWriting.getSubscription();
        subscription.progressUW();
        subscriptionRepository.save(subscription);

        return underWriting;
    }

    @Override
    public Contract registerSubscriptionResult(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId);
        if (Objects.isNull(subscription)) {
            return null;
        }

        subscription.complete();
        subscriptionRepository.save(subscription);

        messageService.send(subscription.getSubscriptionManagerName(), "[???????????? ?????? ?????? ?????? ?????? ??????] %s", subscription);
        messageService.send(subscription.getUserName(), "[???????????? ?????? ??????: %s] %s", subscription.isValid() ? "??????" : "??????", subscription);

        if (subscription.isNotValid()) {
            return null;
        }

        return contractService.createContract(subscription.getId());
    }

    private Subscription _createAndSaveSubscription(Product product, User user, SubscriptionInfo subscriptionInfo) {
        // 1. Subscription ?????? ??????
        Subscription subscription = new Subscription(product, user, subscriptionInfo);

        // 2. SubscriptionManager ??????
        SubscriptionManager subscriptionManager = taskManagerConnectionPool.allocateSubscriptionManager();
        subscription.allocateManager(subscriptionManager);

        // 3. Subscription DB??? ????????? Return
        return subscriptionRepository.save(subscription);
    }

    private Boolean _checkValidationOfSubscription(Long productId, Long userId, SubscriptionInfo subscriptionInfo) {
        if (Objects.isNull(productId) || Objects.isNull(userId) || Objects.isNull(subscriptionInfo)) {
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
