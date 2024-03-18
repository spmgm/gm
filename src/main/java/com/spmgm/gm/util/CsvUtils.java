package com.spmgm.gm.util;

import com.spmgm.gm.exception.BadRequestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.util.Strings;
import com.spmgm.gm.entity.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@UtilityClass
public final class CsvUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static List<Activity> parse(InputStream file) {
        List<Activity> activity = null;
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder().setHeader("source", "codeListCode", "code", "displayValue", "longDescription", "fromDate", "toDate", "sortingPriority")
                             .setIgnoreHeaderCase(true).setSkipHeaderRecord(true).setTrim(true).build())) {

            activity = csvParser.getRecords().stream().map(
                    record -> Activity.builder()
                            .source(record.get("source"))
                            .codeListCode(record.get("codeListCode"))
                            .code(record.get("code"))
                            .displayValue(record.get("displayValue"))
                            .longDescription(record.get("longDescription"))
                            .fromDate(LocalDate.parse(record.get("fromDate"), FORMATTER))
                            .toDate(Strings.isNotBlank(record.get("toDate")) ? LocalDate.parse(record.get("toDate"), FORMATTER) : null)
                            .sortingPriority(Strings.isNotBlank(record.get("sortingPriority")) ? Integer.parseInt(record.get("sortingPriority")) : null)
                            .build()).toList();
        } catch (IOException e) {
            log.warn("Error parsing the file", e);
            throw new BadRequestException("File format is not valid");
        }  catch (DateTimeParseException e) {
            log.warn("Error parsing date", e);
            throw new BadRequestException("At least one date has invalid format");
        }
        return activity;
    }
}
