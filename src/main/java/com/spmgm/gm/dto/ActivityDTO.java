package com.spmgm.gm.dto;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record ActivityDTO(String source, String codeListCode, String code, String displayValue, String longDescription, LocalDate fromDate, LocalDate toDate, Integer sortingPriority) {
}