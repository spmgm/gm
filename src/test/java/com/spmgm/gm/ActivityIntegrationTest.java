package com.spmgm.gm;

import com.spmgm.gm.entity.Activity;
import com.spmgm.gm.repository.ActivityRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActivityIntegrationTest {

    private final static String BASE_URL = "/activities";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ActivityRepository repo;

    @AfterEach
    public void cleanUp() {
        repo.deleteAll();
    }

    // Get all
    /* TODO: Implement missing tests
     * Get all when db is empty
     * Get all when there is n entries
     */
    @Test
    @DisplayName("When no entries get all should return OK and empty array")
    void givenNoEntriesGetAllShouldReturnEmptyList() throws Exception {
        mvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("When N entries exist get all should return OK and all entries")
    void givenNEntriesGetAllShouldReturnList() throws Exception {
        Activity activity = Activity.builder()
                .code("abc")
                .codeListCode("efg")
                .displayValue("hij")
                .longDescription("klm")
                .fromDate(LocalDate.of(2024, 3, 18))
                .toDate(LocalDate.of(2024, 3, 19))
                .sortingPriority(1).build();
        repo.save(activity);
        Activity anotherActivity = Activity.builder()
                .code("cba")
                .codeListCode("gfe")
                .displayValue("jih")
                .longDescription("mlk")
                .fromDate(LocalDate.of(2024, 3, 18))
                .build();
        repo.save(anotherActivity);
        mvc.perform(get(BASE_URL)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code").value("abc"))
                .andExpect(jsonPath("$[1].code").value("cba")); // TODO add the remaining fields;
    }

    // Post
    /* TODO: Implement missing tests
     * Post with empty file
     * Post with parse error on date
     * Post with only header
     * Post with n correct entries
     * Post with non CSV file
     */
    @Test
    @DisplayName("When file provided is empty should throw bad request")
    void givenEmptyFilePostShouldThrowBadRequestException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv",
                "text/csv", "".getBytes()
        );
        mvc.perform(multipart(BASE_URL).file(file)).andExpect(status().isBadRequest()).andExpect(jsonPath("$").value("File can not be empty"));
    }

    @Test
    @DisplayName("When file is correct all entries are saved")
    void givenCorrectFileAllEntriesAreSaved() throws Exception {
        long before = repo.count();
        MockMultipartFile file = new MockMultipartFile("file", "correct.csv",
                "text/csv", ("""
                "source","codeListCode","code","displayValue","longDescription","fromDate","toDate","sortingPriority"
                "a","a1","123","Test123","TEST123","01-01-2019","","1"
                "a","a2","456","Test456","TEST456","01-01-2019","01-01-2019",""").getBytes()
        );
        mvc.perform(multipart(BASE_URL).file(file)).andExpect(status().isOk());
        assertEquals(before + 2L, repo.count());
        List<Activity> fromDb = StreamSupport.stream(repo.findAll().spliterator(), false).toList();
        assertAll(
                () -> assertTrue(fromDb.stream().anyMatch(e -> "a".equals(e.getSource()) && "a1".equals(e.getCodeListCode() /* TODO add all fields */))),
                () -> assertTrue(fromDb.stream().anyMatch(e -> LocalDate.of(2019, 1, 1).equals(e.getFromDate()) && "a2".equals(e.getCodeListCode() /* TODO add all fields */)))
        );
    }

    @Test
    @DisplayName("When a header is missing throw bad request")
    void givenFileWithoutAllColumnsReturnBadRequest() throws Exception {
        long before = repo.count();
        MockMultipartFile file = new MockMultipartFile("file", "correct.csv",
                "text/csv", ("""
                "source","codeListCode","code","displayValue","longDescription","fromDate","toDate","sortingPriority""").getBytes()
        );
        mvc.perform(multipart(BASE_URL).file(file)).andExpect(status().isBadRequest()).andExpect(jsonPath("$").value("File format is not valid"));
        assertEquals(before, repo.count());
    }

    // Get by code
    /* TODO: Implement missing tests
     * Code does not exist
     * Code exists
     */
    @Test
    @DisplayName("When no entries get by code should return not found")
    void givenNoEntriesGetOneShouldReturnNotFound() throws Exception {
        String code = RandomStringUtils.randomAlphanumeric(5);
        mvc.perform(get(BASE_URL + "/{code)", code)).andExpect(status().isNotFound());
    }

    // Delete
    /* TODO: Implement missing tests
     * No entries before deleting
     * Entries before deleting
     */
    @Test
    @DisplayName("When no entries delete all should return OK")
    void givenNoEntriesDeleteShouldReturnOK() throws Exception {
        mvc.perform(delete(BASE_URL)).andExpect(status().isNoContent());
    }
}