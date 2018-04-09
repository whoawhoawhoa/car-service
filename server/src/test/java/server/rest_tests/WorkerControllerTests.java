package server.rest_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.TestUtil;
import server.jpa.OrderRepository;
import server.jpa.Worker;
import server.jpa.WorkerRepository;
import server.web.WorkerController;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WorkerControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private WorkerController workerController;

    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private OrderRepository orderRepository;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(workerRepository);
        reset(orderRepository);
        workerController = new WorkerController(workerRepository, orderRepository);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(workerController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnWorkers() throws Exception {
        Worker first = TestUtil.produceWorker();
        Worker second = TestUtil.produceWorker();

        when(workerRepository.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/workers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].login", is(first.getLogin())))
                .andExpect(jsonPath("$[0].password", is(first.getPassword())))
                .andExpect(jsonPath("$[0].name", is(first.getName())))
                .andExpect(jsonPath("$[0].fname", is(first.getFname())))
                .andExpect(jsonPath("$[0].pnumber", is(first.getPnumber())))
                .andExpect(jsonPath("$[0].city", is(first.getCity())))
                .andExpect(jsonPath("$[0].rating", is(first.getRating())))
                .andExpect(jsonPath("$[0].status", is(first.getStatus())))
                .andExpect(jsonPath("$[0].email", is(first.getEmail())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].login", is(second.getLogin())))
                .andExpect(jsonPath("$[1].password", is(second.getPassword())))
                .andExpect(jsonPath("$[1].name", is(second.getName())))
                .andExpect(jsonPath("$[1].fname", is(second.getFname())))
                .andExpect(jsonPath("$[1].pnumber", is(second.getPnumber())))
                .andExpect(jsonPath("$[1].city", is(second.getCity())))
                .andExpect(jsonPath("$[1].rating", is(second.getRating())))
                .andExpect(jsonPath("$[1].status", is(second.getStatus())))
                .andExpect(jsonPath("$[1].email", is(second.getEmail())));

        verify(workerRepository, times(1)).findAll();
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnWorker() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLoginAndPassword(first.getLogin(), first.getPassword()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/worker?login={login}&password={password}",
                first.getLogin(), first.getPassword()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(first.getId())))
                .andExpect(jsonPath("$.login", is(first.getLogin())))
                .andExpect(jsonPath("$.password", is(first.getPassword())))
                .andExpect(jsonPath("$.name", is(first.getName())))
                .andExpect(jsonPath("$.fname", is(first.getFname())))
                .andExpect(jsonPath("$.pnumber", is(first.getPnumber())))
                .andExpect(jsonPath("$.city", is(first.getCity())))
                .andExpect(jsonPath("$.rating", is(first.getRating())))
                .andExpect(jsonPath("$.status", is(first.getStatus())))
                .andExpect(jsonPath("$.email", is(first.getEmail())));

        verify(workerRepository, times(1)).
                findWorkerByLoginAndPassword(first.getLogin(), first.getPassword());
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnBadRequest() throws Exception {
        when(workerRepository.findWorkerByLoginAndPassword("login", "pass"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/worker?login={login}&password={password}",
                "login", "pass"))
                .andExpect(status().isBadRequest());

        verify(workerRepository, times(1)).
                findWorkerByLoginAndPassword("login", "pass");
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void findByIds_ShouldReturnWorkers() throws Exception {
        Worker first = TestUtil.produceWorker();
        first.setId(1L);
        Worker second = TestUtil.produceWorker();
        second.setId(2L);

        when(workerRepository.findWorkersByIdIn(Arrays.asList(1, 2)))
                .thenReturn(Arrays.asList(first, second));

            mockMvc.perform(get("/workers_by_id?ids=1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].login", is(first.getLogin())))
                    .andExpect(jsonPath("$[0].password", is(first.getPassword())))
                    .andExpect(jsonPath("$[0].name", is(first.getName())))
                    .andExpect(jsonPath("$[0].fname", is(first.getFname())))
                    .andExpect(jsonPath("$[0].pnumber", is(first.getPnumber())))
                    .andExpect(jsonPath("$[0].city", is(first.getCity())))
                    .andExpect(jsonPath("$[0].rating", is(first.getRating())))
                    .andExpect(jsonPath("$[0].status", is(first.getStatus())))
                    .andExpect(jsonPath("$[0].email", is(first.getEmail())))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].login", is(second.getLogin())))
                    .andExpect(jsonPath("$[1].password", is(second.getPassword())))
                    .andExpect(jsonPath("$[1].name", is(second.getName())))
                    .andExpect(jsonPath("$[1].fname", is(second.getFname())))
                    .andExpect(jsonPath("$[1].pnumber", is(second.getPnumber())))
                    .andExpect(jsonPath("$[1].city", is(second.getCity())))
                    .andExpect(jsonPath("$[1].rating", is(second.getRating())))
                    .andExpect(jsonPath("$[1].status", is(second.getStatus())))
                    .andExpect(jsonPath("$[1].email", is(second.getEmail())));

        verify(workerRepository, times(1)).
                findWorkersByIdIn(Arrays.asList(1, 2));
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void updateWorker_UpdateLogin_ShouldReturnOk() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(workerRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verify(workerRepository, times(1)).save(first);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void updateWorker_DoesntUpdateLogin_ShouldReturnOk() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(new Worker()));
        when(workerRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verify(workerRepository, times(1)).save(first);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void updateWorker_ShouldReturnConflict_WorkerDoesntChange() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(put("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void updateWorker_ShouldSetStartDate_ShouldReturnOk() throws Exception {
        Worker first = TestUtil.produceWorker();
        first.setStatus(3);

        when(workerRepository.findWorkerByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));
        Worker newWorker = new Worker(first.getLogin(), first.getPassword(), first.getName(),
                first.getFname(), first.getPnumber(), first.getCity(),first.getRating(),
                0, first.getEmail());
        newWorker.setId(first.getId());
        when(workerRepository.save(newWorker)).thenReturn(newWorker);

        mockMvc.perform(put("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newWorker)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(first.getId())))
                .andExpect(jsonPath("$.login", is(first.getLogin())))
                .andExpect(jsonPath("$.password", is(first.getPassword())))
                .andExpect(jsonPath("$.name", is(first.getName())))
                .andExpect(jsonPath("$.fname", is(first.getFname())))
                .andExpect(jsonPath("$.pnumber", is(first.getPnumber())))
                .andExpect(jsonPath("$.city", is(first.getCity())))
                .andExpect(jsonPath("$.rating", is(first.getRating())))
                .andExpect(jsonPath("$.status", is(newWorker.getStatus())))
                .andExpect(jsonPath("$.email", is(first.getEmail())));

        assertNull(newWorker.getStartDate());
        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verify(workerRepository, times(1)).save(newWorker);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void createWorker_ShouldReturnCreated() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(workerRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verify(workerRepository, times(1)).save(first);
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void createWorker_ShouldReturnConflict() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(post("/worker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verifyNoMoreInteractions(workerRepository);
    }

    @Test
    public void deleteWorker_ShouldReturnNoContent() throws Exception {
        Worker first = TestUtil.produceWorker();

        when(workerRepository.findWorkerByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));
        doNothing().when(workerRepository).delete(first.getId());

        mockMvc.perform(delete("/worker?login={login}",first.getLogin()))
                .andExpect(status().isNoContent());

        verify(workerRepository, times(1)).
                findWorkerByLogin(first.getLogin());
        verify(workerRepository, times(1)).delete(first.getId());
        verifyNoMoreInteractions(workerRepository);
    }
}
