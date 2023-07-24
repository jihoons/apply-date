package kr.overtake.apply.service;

import kr.overtake.apply.mapper.OverviewTblMapper;
import kr.overtake.apply.mapper.dto.OverviewApply;
import kr.overtake.apply.service.dto.OverviewTbl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OverviewService implements VersioningService.VersionUpdater<OverviewTbl> {
    private final OverviewTblMapper mapper;
    private final VersioningService versioningService;

    @Transactional
    public long add(OverviewTbl overviewTbl) {
        List<OverviewApply> closestOverviewTblList = mapper.selectOverviewClosest(overviewTbl.getProductSeq(), overviewTbl.getApplyStartDt());

        return versioningService.addNewVersion(closestOverviewTblList,
                overviewTbl,
                this
        );
    }

    @Transactional
    public void modify(OverviewTbl overviewTbl) {
//        mapper.modifyOverview(overviewTbl.getOverviewTblSeq(), overviewTbl.getApplyEndDt());
    }

    @Override
    public void addVersion(OverviewTbl newData) {
        mapper.addOverview(newData.getProductSeq(), newData.getName(), newData.getApplyStartDt(), newData.getApplyEndDt());
    }

    @Override
    public void updateEndDt(long id, LocalDateTime modifiedEndDt) {
        mapper.modifyOverview(id, modifiedEndDt);
    }
}
