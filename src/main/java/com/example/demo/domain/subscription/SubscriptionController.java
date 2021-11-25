package com.example.demo.domain.subscription;

import com.example.demo.domain.common.ApiResult;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractDto;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.subscription.uw.UnderWritingDto;
import lombok.RequiredArgsConstructor;
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
public class SubscriptionController {
    private final ISubscriptionService subscriptionService;

    @RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllSubscriptions() {
        List<Subscription> subscriptionList = subscriptionService.findAllSubscriptions();

        if (Objects.isNull(subscriptionList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(subscriptionList.stream().map(SubscriptionDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscriptions/{subscriptionId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchSubscriptionById(@PathVariable Long subscriptionId) {
        Subscription subscription = subscriptionService.findSubscriptionById(subscriptionId);

        if (Objects.isNull(subscription)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(new com.example.demo.domain.subscription.SubscriptionDto(subscription), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscriptions/user/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchSubscriptionsByUserId(@RequestBody SubscriptionDto subscriptionDto) {
        List<Subscription> subscriptionList = subscriptionService.findSubscriptionsByUserId(subscriptionDto.getUserId());

        if (Objects.isNull(subscriptionList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(subscriptionList.stream().map(SubscriptionDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscriptions/manager/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchSubscriptionsByManagerId(@RequestBody SubscriptionDto subscriptionDto) {
        List<Subscription> subscriptionList = subscriptionService.findSubscriptionsByManagerId(subscriptionDto.getManagerId());

        if (Objects.isNull(subscriptionList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(subscriptionList.stream().map(SubscriptionDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/subscriptions/request", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<SubscriptionDto>> subscribeInsurance(@RequestBody com.example.demo.domain.subscription.SubscriptionDto subscriptionDto, UriComponentsBuilder uriComponentsBuilder) {
        Subscription subscription = subscriptionService.subscribeInsurance(subscriptionDto.getProductId(), subscriptionDto.getUserId(), SubscriptionInfo.builder().build());

        URI uri = uriComponentsBuilder
                .path("/api/v1/subscriptions/{subscriptionId}")
                .buildAndExpand(subscription.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return ApiResult.succeed(new SubscriptionDto(subscription), httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/subscriptions/uw/request/{subscriptionId}", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<UnderWritingDto>> requestUnderWriting(@PathVariable Long subscriptionId, UriComponentsBuilder uriComponentsBuilder) {
        UnderWriting underWriting = subscriptionService.requestUnderWriting(subscriptionId);

        URI uri = uriComponentsBuilder
                .path("/api/v1/uws/{underWritingId}")
                .buildAndExpand(underWriting.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return ApiResult.succeed(new UnderWritingDto(underWriting), httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/subscriptions/{subscriptionId}", method = {RequestMethod.POST})
    public ResponseEntity<?> registerSubscriptionResult(@PathVariable Long subscriptionId, UriComponentsBuilder uriComponentsBuilder) {
        Contract contract = subscriptionService.registerSubscriptionResult(subscriptionId);

        if (Objects.isNull(contract)) {
            return ApiResult.succeed("Failed to created contract. Because of underWriting.", HttpStatus.OK);
        }

        URI uri = uriComponentsBuilder
                .path("/api/v1/contracts/{contractId}")
                .buildAndExpand(contract.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return ApiResult.succeed(new ContractDto(contract), httpHeaders, HttpStatus.CREATED);
    }

}
