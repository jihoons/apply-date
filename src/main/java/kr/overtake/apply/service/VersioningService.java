package kr.overtake.apply.service;

import kr.overtake.apply.mapper.LastIdMapper;
import kr.overtake.apply.mapper.dto.ApplyVersioning;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VersioningService {
    private final LastIdMapper lastIdMapper;
    private final LocalDateTime defaultEndDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999);

    public <T extends ApplyVersioning> long addNewVersion(List<? extends ApplyVersioning> closestList, T newData, VersionUpdater<T> updater) {
        Map<String, ApplyVersioning> map = closestList.stream().collect(Collectors.toMap(ApplyVersioning::getType, s -> s));
        ApplyVersioning lt = map.get("lt");
        if (lt != null) {
            updater.updateEndDt(lt.getId(), newData.getApplyStartDt().minusSeconds(1));
        }

        ApplyVersioning gt = map.get("gt");
        if (gt != null) {
            newData.setApplyEndDt(gt.getApplyStartDt().minusSeconds(1));
        } else {
            newData.setApplyEndDt(defaultEndDate);
        }

        updater.addVersion(newData);
        long lastSeq = lastIdMapper.selectLastInsertId();

        log.info("add seq : {}", lastSeq);
        return lastSeq;
    }

    public interface VersionUpdater<T extends ApplyVersioning> {
        void updateEndDt(long id, LocalDateTime modifiedEndDt);
        void addVersion(T newData);
    }
}
