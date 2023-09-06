package com.example.mypetlife.calendar;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalendarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String accessToken;

    @Test
    @WithMockUser
    public void testCreateSchedule() throws Exception {
        accessToken = performUserLoginAndGetAccessToken();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/calendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date\":\"2023-09-07\",\"startTime\":\"20:00\",\"endTime\":\"21:00\",\"title\":\"산책\",\"location\":\"집 앞\",\"alarm\":30}")
                        .header("Authorization",  "Bearer " + accessToken)) // Authorization 헤더 추가
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("일정이 등록되었습니다."));
    }

    @Test
    @WithMockUser
    public void testDeleteSchedule() throws Exception {
        accessToken = performUserLoginAndGetAccessToken();

        // 일정 생성 API 호출
        ResultActions createAction = mockMvc.perform(MockMvcRequestBuilders
                .post("/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\":\"2023-09-07\",\"startTime\":\"20:00\",\"endTime\":\"21:00\",\"title\":\"산책\",\"location\":\"집 앞\",\"alarm\":30}")
                .header("Authorization", "Bearer " + accessToken)
        );

        // 생성된 일정 정보 가져오기
        String content = createAction.andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        Long scheduleId = jsonNode.get("id").asLong();

        // 일정 삭제 API 호출
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/calendar/{id}", scheduleId)
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("일정이 삭제되었습니다."));
    }


    private String performUserLoginAndGetAccessToken() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"jiseon1113@naver.com\",\"password\":\"1111\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 access-token을 추출
        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        return jsonNode.get("accessToken").asText();
    }
}
