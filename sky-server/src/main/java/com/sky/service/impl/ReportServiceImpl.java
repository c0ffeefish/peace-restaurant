package com.sky.service.impl;

import com.sky.entity.Turnover;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<Turnover> turnovers = orderMapper.selectDateAndTurnover(begin, end);

        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();

        /*for (Turnover turnover : turnovers) {
            log.info("date: {}, sum: {}", turnover.getDate(), turnover.getSum());
        }*/

        int idx = 0;
        int len = turnovers.size();

        while(!begin.equals(end.plusDays(1))){
            dateList.add(begin);
            if(idx < len && begin.equals(turnovers.get(idx).getDate())){
                turnoverList.add(turnovers.get(idx).getSum());
                idx++;
            }else{
                turnoverList.add(0.0);
            }

            begin = begin.plusDays(1);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
