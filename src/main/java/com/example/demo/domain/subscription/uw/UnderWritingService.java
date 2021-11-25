package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import com.example.demo.domain.message.MessageService;
import com.example.demo.domain.message.MockMessageService;
import com.example.demo.domain.subscription.MockSubscriptionRepository;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;
import com.example.demo.exception.DuplicatedUnderWritingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UnderWritingService implements IUnderWritingService {
    private final UnderWritingRepository underWritingRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TaskManagerConnectionPool taskManagerConnectionPool;
    private final MessageService messageService;

    public UnderWritingService() {
        this(
                MockUnderWritingRepository.getInstance(),
                MockSubscriptionRepository.getInstance(),
                new TaskManagerConnectionPool(),
                new MockMessageService()
        );
    }

    @Override
    public UnderWriting requestUnderWriting(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId);

        if (Objects.nonNull(subscription.getUnderWriting())) {
            throw new DuplicatedUnderWritingException("Already Exists UnderWriting in progress.");
        }

        UnderWriting underWriting = _createAndSaveUnderWriting(subscription);

        messageService.send(subscription.getUnderWritingManagerName(), "[신규 보험가입 인가 요청] %s", subscription);
        messageService.send(subscription.getSubscriptionManagerName(), "[보험가입 인가 요청 처리 중] %s", underWriting);

        return underWriting;
    }

    @Override
    public UnderWriting registerUnderWritingResult(Long underWritingId, Boolean result) {
        UnderWriting underWriting = underWritingRepository.findById(underWritingId);

        underWriting.complete(result);

        UnderWriting underWritingResult = underWritingRepository.save(underWriting);

        Subscription modifiedSubscription = underWritingResult.getSubscription();
        subscriptionRepository.save(modifiedSubscription);

        messageService.send(underWritingResult.getManagerName(), "[보험가입 인가 요청 처리 결과 등록 완료] %s", underWritingResult);
        messageService.send(modifiedSubscription.getSubscriptionManagerName(), "[보험가입 인가 요청 처리 완료] %s", modifiedSubscription);

        return underWritingResult;
    }

    @Override
    public List<UnderWriting> findAllUnderWritings() {
        return underWritingRepository.findAll();
    }

    @Override
    public UnderWriting findUnderWritingById(Long underWritingId) {
        return underWritingRepository.findById(underWritingId);
    }

    @Override
    public List<UnderWriting> findUnderWritingsByManagerId(Long managerId) {
        return underWritingRepository.findByPredicate(underWriting -> Objects.equals(underWriting.getManager().getId(), managerId));
    }

    private UnderWriting _createAndSaveUnderWriting(Subscription subscription) {
        UnderWriting underWriting = new UnderWriting(subscription);
        UnderWritingManager underWritingManager = taskManagerConnectionPool.allocateUnderWritingManager();
        underWriting.allocateManager(underWritingManager);
        UnderWriting savedUnderWriting = underWritingRepository.save(underWriting);

        subscription.allocateUnderWriting(savedUnderWriting);
        subscriptionRepository.save(subscription);

        return savedUnderWriting;
    }
}
