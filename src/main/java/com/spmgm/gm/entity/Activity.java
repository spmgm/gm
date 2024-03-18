package com.spmgm.gm.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Activity {

    private String source;
    private String codeListCode;
    @Id
    private String code;
    private String displayValue;
    private String longDescription;
    private LocalDate fromDate;
    @Nullable
    private LocalDate toDate;
    @Nullable
    private Integer sortingPriority;
}
