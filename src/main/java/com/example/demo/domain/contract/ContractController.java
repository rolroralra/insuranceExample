package com.example.demo.domain.contract;

import com.example.demo.domain.common.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ContractController {
    private final IContractService contractService;

    @RequestMapping(value = "/contracts", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllContracts() {
        List<Contract> contractList = contractService.findAllContracts();

        if (Objects.isNull(contractList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(contractList.stream().map(ContractDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/contracts/{contractId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchContractById(@PathVariable Long contractId) {
        Contract contract = contractService.findContractById(contractId);

        if (Objects.isNull(contract)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(new ContractDto(contract), HttpStatus.OK);
    }

    @RequestMapping(value = "/contracts/user/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchContractsByUserId(@RequestBody ContractDto contractDto) {
        List<Contract> contractList = contractService.findContractsByUserId(contractDto.getUserId());

        if (Objects.isNull(contractList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(contractList.stream().map(ContractDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/contracts/manager/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchContractsByManagerId(@RequestBody ContractDto contractDto) {
        List<Contract> contractList = contractService.findContractsByManagerId(contractDto.getManagerId());

        if (Objects.isNull(contractList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(contractList.stream().map(ContractDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/contracts/product/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchContractsByProductId(@RequestBody ContractDto contractDto) {
        List<Contract> contractList = contractService.findContractsByPredicate(contract -> Objects.equals(contract.getProductId(), contractDto.getProductId()));

        if (Objects.isNull(contractList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(contractList.stream().map(ContractDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }


}
