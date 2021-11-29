package com.example.demo.domain.reward;

import com.example.demo.domain.common.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RewardController {
    private final IRewardService rewardService;

    @RequestMapping(value = "/rewards/request", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<RewardDto>> requestReward(@RequestBody RewardDto rewardDto, UriComponentsBuilder uriComponentsBuilder) {
        Reward reward = rewardService.requestReward(rewardDto.getContractId(), new RewardInfo(rewardDto));

        URI uri = uriComponentsBuilder
                .path("/api/v1/rewards/{rewardId}")
                .buildAndExpand(reward.getId())
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return ApiResult.succeed(new RewardDto(reward), httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rewards/{rewardId}", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<RewardDto>> registerRewardResult(Long rewardId, RewardResult rewardResult) {
        Reward reward = rewardService.registerRewardResult(rewardId, rewardResult);

        return ApiResult.succeed(new RewardDto(reward), HttpStatus.OK);
    }
}
