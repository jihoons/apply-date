package kr.overtake.apply.service;

import kr.overtake.apply.mapper.HouseGradeMapper;
import kr.overtake.apply.mapper.dto.HouseGradeApply;
import kr.overtake.apply.mapper.dto.HouseGradeDetail;
import kr.overtake.apply.service.dto.HouseGrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HouseGradeService implements VersioningService.VersionUpdater<HouseGrade> {
    private final HouseGradeMapper mapper;
    private final VersioningService versioningService;

    @Transactional
    public long add(HouseGrade houseGrade) {
        List<HouseGradeApply> houseGradeApplies = mapper.selectHouseGradeClosest(houseGrade.getProductSeq(), houseGrade.getApplyStartDt());
        long houseGradeSeq = versioningService.addNewVersion(houseGradeApplies, houseGrade, this);
        List<HouseGrade.HouseDongGrade> gradeList = houseGrade.getGradeList();
        if (!CollectionUtils.isEmpty(gradeList)) {
            gradeList.forEach(hgd -> mapper.addHouseGradeDetail(houseGradeSeq, hgd.getDongCd(), hgd.getGrade()));
        }
        return houseGradeSeq;
    }

    @Transactional
    public void modify(HouseGrade houseGrade) {
        List<HouseGradeDetail> details = mapper.selectHouseGradeDetailList(houseGrade.getHouseGradeSeq());
        Map<String, HouseGradeDetail> savedDetailMapByDongCd = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(details)) {
            savedDetailMapByDongCd = details.stream().collect(Collectors.toMap(HouseGradeDetail::getDongCd, r -> r));
        }

        List<HouseGrade.HouseDongGrade> addList = new ArrayList<>();
        List<HouseGradeDetail> modifyList = new ArrayList<>();
        List<HouseGradeDetail> removeList = new ArrayList<>();

        Map<String, HouseGrade.HouseDongGrade> requestDetailMapByDongCd = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(houseGrade.getGradeList())) {
            requestDetailMapByDongCd = new HashMap<>();
            for (HouseGrade.HouseDongGrade houseDongGrade : houseGrade.getGradeList()) {
                HouseGradeDetail detail = savedDetailMapByDongCd.get(houseDongGrade.getDongCd());
                // 추가되는 dong
                if (detail == null) {
                    addList.add(houseDongGrade);
                } else if (!detail.getGrade().equals(houseDongGrade.getGrade())) {
                    detail.setGrade(houseDongGrade.getGrade());
                    modifyList.add(detail);
                }
                requestDetailMapByDongCd.put(houseDongGrade.getDongCd(), houseDongGrade);
            }
        }

        if (!CollectionUtils.isEmpty(savedDetailMapByDongCd)) {
            // 삭제된 dong
            for (HouseGradeDetail detail : details) {
                HouseGrade.HouseDongGrade requestDong = requestDetailMapByDongCd.get(detail.getDongCd());
                if (requestDong == null) {
                    removeList.add(detail);
                }
            }
        }

        addList.forEach(detail -> mapper.addHouseGradeDetail(houseGrade.getHouseGradeSeq(), detail.getDongCd(), detail.getGrade()));
        modifyList.forEach(detail -> mapper.modifyHouseGradeDetail(detail.getHouseGradeDetailSeq(), detail.getGrade()));
        removeList.forEach(detail -> mapper.removeHouseGradeDetail(detail.getHouseGradeDetailSeq()));
    }

    @Override
    public void updateEndDt(long id, LocalDateTime modifiedEndDt) {
        mapper.modifyHouseGrade(id, modifiedEndDt);
    }

    @Override
    public void addVersion(HouseGrade newData) {
        mapper.addHouseGrade(newData.getProductSeq(), newData.getName(), newData.getApplyStartDt(), newData.getApplyEndDt());
    }
}
