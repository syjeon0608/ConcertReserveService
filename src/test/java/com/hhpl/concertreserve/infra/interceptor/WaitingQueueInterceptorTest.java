package com.hhpl.concertreserve.infra.interceptor;

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
class WaitingQueueInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;


    @Test
    @Transactional
    @DisplayName("대기열이 활성화되어 있고 만료되지 않은 경우, 인터셉터를 통과한다.")
    void activeAndNotExpiredQueuePassesInterceptor() throws Exception {
        String uuid = "f7f7b91a-9b60-4d0e-a7b2-1c4b2e2a8c71";
        Long concertId = 1L;

        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, 0L);
        waitingQueue.activate();
        waitingQueueRepository.save(waitingQueue);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1/schedules/1")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 콘서트 ID는 인터셉터에서 예외가 발생한다.")
    @Transactional
    void invalidConcertIdReturnsForbidden() throws Exception {
        String uuid = "invalid-uuid";
        Long concertId = -1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/" + concertId + "/schedules")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @DisplayName("대기열이 활성화되지 않으면 인터셉터에서 예외가 발생한다.")
    void inactiveQueueReturnsForbidden() throws Exception {
        String uuid = "f7f7b91a-9b60-4d0e-a7b2-1c4b2e2a8c71";
        Long concertId = 1L;

        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, 5L);
        waitingQueueRepository.save(waitingQueue);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1/schedules/1")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    @DisplayName("대기열이 만료된 경우, 인터셉터에서 예외가 발생한다.")
    void expiredQueueReturnsForbidden() throws Exception {
        String uuid = "f7f7b91a-9b60-4d0e-a7b2-1c4b2e2a8c71";
        Long concertId = 1L;

        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, 5L);
        waitingQueue.expire();
        waitingQueueRepository.save(waitingQueue);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/concerts/1/schedules/1")
                        .header("X-WAITING-QUEUE-ID", uuid))
                .andExpect(status().isForbidden());
    }

}