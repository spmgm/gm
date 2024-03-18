package com.spmgm.gm.service;

import com.spmgm.gm.dto.ActivityDTO;
import com.spmgm.gm.mapper.ActivityMapper;
import com.spmgm.gm.util.CsvUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.spmgm.gm.repository.ActivityRepository;
import java.io.InputStream;
import java.util.List;

import com.spmgm.gm.exception.NotFoundException;


@Service
@AllArgsConstructor
public class ActivityService {
    private ActivityRepository repo;
    public void process(InputStream file) {
        repo.saveAll(CsvUtils.parse(file));
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public List<ActivityDTO> findAll() {
        return ActivityMapper.toDto(repo.findAll());
    }

    public ActivityDTO findByCode(String code) {
        return ActivityMapper.toDto(repo.findByCode(code).orElseThrow(NotFoundException::new));
    }
}
