package com.spmgm.gm.mapper;

import com.spmgm.gm.dto.ActivityDTO;
import com.spmgm.gm.entity.Activity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.StreamSupport;

@UtilityClass
public class ActivityMapper {
    public static List<ActivityDTO> toDto(Iterable<Activity> entities) {
        return StreamSupport.stream(entities.spliterator(), false).map(ActivityMapper::toDto).toList();
    }

    public static ActivityDTO toDto(Activity entity) {
        return ActivityDTO.builder()
                .code(entity.getCode())
                .longDescription(entity.getLongDescription())
                .codeListCode(entity.getCodeListCode())
                .displayValue(entity.getDisplayValue())
                .toDate(entity.getToDate())
                .fromDate(entity.getFromDate())
                .source(entity.getSource())
                .sortingPriority(entity.getSortingPriority())
                .build();
    }
}
