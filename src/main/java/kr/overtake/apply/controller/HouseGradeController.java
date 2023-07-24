package kr.overtake.apply.controller;

import kr.overtake.apply.service.HouseGradeService;
import kr.overtake.apply.service.dto.HouseGrade;
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
public class HouseGradeController {
    private final HouseGradeService houseGradeService;
    @PostMapping("/houseGrade")
    public ResponseEntity<Long> save(@RequestBody HouseGrade houseGrade) {
        if (houseGrade.getHouseGradeSeq() <= 0) {
            long newSeq = houseGradeService.add(houseGrade);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSeq);
        } else {
            houseGradeService.modify(houseGrade);
            return ResponseEntity.ok(houseGrade.getHouseGradeSeq());
        }
    }
}
