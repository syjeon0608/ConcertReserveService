package com.hhpl.concertreserve.infra.filter;

import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueRepository;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WaitingQueueFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;


    @Test
    @DisplayName("유효한 uuid는 필터를 통과한다")
    @Transactional
    void validUuidPassFilter() throws Exception {
        String uuid = "f7f7b91a-9b60-4d0e-a7b2-1c4b2e2a8c71";
        Long concertId = 1L;

        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId,10L);
        waitingQueue.activate();
        waitingQueueRepository.save(waitingQueue);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 uuid는 필터에서 예외가 터진다.")
    @Transactional
    void whenInvalidUuid_thenReturnsForbidden() throws Exception {
        String uuid = "invalid-uuid";
        Long concertId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/"+concertId+"/schedules")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("uuid 헤더가 없으면 필터에서 예외가 터진다.")
    @Transactional
    void whenUuidHeaderIsMissingThenReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1/schedules"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("uuid의 값이 빈문자열이면 없으면 필터에서 예외가 터진다.")
    @Transactional
    void whenUuidIsMissingThenReturnsForbidden() throws Exception {
        String uuid = "";
        Long concertId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1/schedules")
                .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isForbidden());
    }

}