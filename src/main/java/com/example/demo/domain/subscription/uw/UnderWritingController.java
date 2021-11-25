package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.common.ApiResult;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractDto;
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
public class UnderWritingController {
    private final IUnderWritingService underWritingService;

    @RequestMapping(value = "/uws", method = RequestMethod.GET)
    public ResponseEntity<?> searchAllContracts() {
        List<UnderWriting> underWritingListList = underWritingService.findAllUnderWritings();

        if (Objects.isNull(underWritingListList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(underWritingListList.stream().map(UnderWritingDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/uws/{underWritingId}", method = RequestMethod.GET)
    public ResponseEntity<?> searchContractById(@PathVariable Long underWritingId) {
        UnderWriting underWriting = underWritingService.findUnderWritingById(underWritingId);

        if (Objects.isNull(underWriting)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(new UnderWritingDto(underWriting), HttpStatus.OK);
    }

    @RequestMapping(value = "/uws/manager/search", method = RequestMethod.POST)
    public ResponseEntity<?> searchContractsByManagerId(@RequestBody UnderWritingDto underWritingDto) {
        List<UnderWriting> underWritingList = underWritingService.findUnderWritingsByManagerId(underWritingDto.getManagerId());

        if (Objects.isNull(underWritingList)) {
            return ApiResult.failed("Not Found.", HttpStatus.NOT_FOUND);
        }

        return ApiResult.succeed(underWritingList.stream().map(UnderWritingDto::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/uws/{id}", method = {RequestMethod.POST})
    public ResponseEntity<ApiResult<UnderWritingDto>> requestUnderWriting(@PathVariable Long id, @RequestBody UnderWritingDto underWritingDto) {
        UnderWriting underWriting = underWritingService.registerUnderWritingResult(id, underWritingDto.getResult());

        return ApiResult.succeed(new UnderWritingDto(underWriting), HttpStatus.OK);
    }
}
