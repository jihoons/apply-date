package kr.overtake.apply.controller;

import kr.overtake.apply.service.OverviewService;
import kr.overtake.apply.service.dto.OverviewTbl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OverviewController {
    private final OverviewService overviewService;
    @PostMapping("/overview")
    public ResponseEntity<Long> save(@RequestBody OverviewTbl overviewTbl) {
        if (overviewTbl.getOverviewTblSeq() <= 0) {
            long newSeq = overviewService.add(overviewTbl);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSeq);
        } else {
            overviewService.modify(overviewTbl);
            return ResponseEntity.ok(overviewTbl.getOverviewTblSeq());
        }
    }
}
