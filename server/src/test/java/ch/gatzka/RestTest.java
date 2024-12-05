package ch.gatzka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ContextConfiguration(classes = PostgresSQLContainerConfiguration.class)
@AutoConfigureMockMvc
public abstract class RestTest {

    @Autowired
    protected MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    protected <V> ResultActions performPost(String route, V body, HttpStatusCode statusCode) {
        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(body));
        ResultActions resultActions = assertDoesNotThrow(() -> mockMvc.perform(post(route).contentType(MediaType.APPLICATION_JSON_VALUE).content(json)));
        return assertDoesNotThrow(() -> resultActions.andDo(log()).andExpect(status().is(statusCode.value())));
    }

    protected <V> void performPostBadRequest(String route, V body, String expectedMessage) {
        ResultActions resultActions = performPost(route, body, HttpStatus.BAD_REQUEST);

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String content = assertDoesNotThrow(() -> response.getContentAsString());

        ProblemDetail problemDetail = assertDoesNotThrow(() -> mapper.readValue(content, ProblemDetail.class));

        assertEquals(expectedMessage, problemDetail.getDetail());
    }

    protected <V> void performPostOk(String route, V body) {
        performPost(route, body, HttpStatus.OK);
    }

}
