package com.example.demo.domain.contract;

import com.example.demo.domain.reward.RewardDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ContractDto {
    private Long id;
    private Long productId;
    private Long userId;
    private Long managerId;
    private Contract.State state;
    private List<RewardDto> rewardList;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;
    private Long insuranceMoneyPerMonth;
    private Long totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    public ContractDto(Contract contract) {
        BeanUtils.copyProperties(contract, this);
        BeanUtils.copyProperties(contract.getContractInfo(), this, "id", "createdAt", "updatedAt");

        setUserId(contract.getUserId());
        setProductId(contract.getProductId());
        setManagerId(contract.getManagerId());
        setRewardList(contract.getRewardHistory().getAllRewards().stream().map(RewardDto::new).collect(Collectors.toList()));
    }
}
