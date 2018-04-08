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
import server.jms.client_to_worker.messaging.ClientJmsSender;
import server.jms.worker_to_client.service.WorkerJmsService;
import server.jpa.AvailableOrder;
import server.jpa.AvailableOrderRepository;
import server.jpa.Car;
import server.jpa.CarType;
import server.web.AvailableOrderController;
import server.web.ServiceController;

import java.nio.charset.Charset;
import java.sql.Date;
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
public class AvailableOrderControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private AvailableOrderController availableOrderController;

    @Mock
    private AvailableOrderRepository availableOrderRepository;

    @Mock
    private ServiceController serviceController;

    @Mock
    private ClientJmsSender clientJmsSender;

    @Mock
    WorkerJmsService workerJmsService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(availableOrderRepository);
        reset(workerJmsService);
        reset(clientJmsSender);
        reset(serviceController);
        availableOrderController = new AvailableOrderController(availableOrderRepository,
                serviceController, clientJmsSender, workerJmsService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(availableOrderController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnAvOrders() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();
        AvailableOrder second = TestUtil.produceAvailableOrder();

        when(availableOrderRepository.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/available_orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].orderDate", is(second.getOrderDate().toString())))
                .andExpect(jsonPath("$[1].serviceType", is(second.getServiceType())))
                .andExpect(jsonPath("$[1].address", is(second.getAddress())))
                .andExpect(jsonPath("$[1].commentary", is(second.getCommentary())));

        verify(availableOrderRepository, times(1)).findAll();
        verifyNoMoreInteractions(availableOrderRepository);
    }

    @Test
    public void findByClientLogin_ShouldReturnAvOrders() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();

        when(availableOrderRepository.findAvailableOrderByClientLogin(first.getClient().getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/avorderbyclientlogin?login={login}",
                first.getClient().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())));

        verify(availableOrderRepository, times(1))
                .findAvailableOrderByClientLogin(first.getClient().getLogin());
        verifyNoMoreInteractions(availableOrderRepository);
    }

    @Test
    public void findByCarId_ShouldReturnAvOrders() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();

        when(availableOrderRepository.findAvailableOrderByCarId(first.getCar().getId()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/avorderbycarid?id={id}",
                first.getCar().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())));

        verify(availableOrderRepository, times(1))
                .findAvailableOrderByCarId(first.getCar().getId());
        verifyNoMoreInteractions(availableOrderRepository);
    }

    @Test
    public void updateAvOrder_ShouldReturnOk() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();

        when(availableOrderRepository.findOne(first.getId()))
                .thenReturn(new AvailableOrder());
        when(availableOrderRepository.save(first)).thenReturn(first);
        doNothing().when(workerJmsService).sendMessage(first);

        mockMvc.perform(put("/avorderupdate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(availableOrderRepository, times(1)).
                findOne(first.getId());
        verify(availableOrderRepository, times(1)).
                save(first);
        verify(workerJmsService, times(1)).
                sendMessage(first);
        verifyNoMoreInteractions(availableOrderRepository);
        verifyNoMoreInteractions(workerJmsService);
    }

    @Test
    public void updateAvOrder_ShouldReturnConflict_AvOrderDoesntChange() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();

        when(availableOrderRepository.findOne(first.getId())).thenReturn(first);

        mockMvc.perform(put("/avorderupdate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(availableOrderRepository, times(1)).findOne(first.getId());
        verifyNoMoreInteractions(availableOrderRepository);
    }

    @Test
    public void createAvOrder_ShouldReturnCreated() throws Exception {
        AvailableOrder first = TestUtil.produceAvailableOrder();
        first.setOrderDate(null);

        when(availableOrderRepository.save(any(AvailableOrder.class)))
                .thenReturn(first);
        when(serviceController.getWorkersEmailsByServices(first.getServiceType(),
                first.getCar().getCarType().getId()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/putavorder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        assertNull(first.getOrderDate());

        verify(serviceController, times(1)).
                getWorkersEmailsByServices(first.getServiceType(),
                        first.getCar().getCarType().getId());
        verify(availableOrderRepository, times(1))
                .save(any(AvailableOrder.class));
        verifyNoMoreInteractions(availableOrderRepository);
        verifyNoMoreInteractions(serviceController);
    }

    @Test
    public void deleteAvOrder_ShouldReturnNoContent() throws Exception {
        doNothing().when(availableOrderRepository).delete(1L);

        mockMvc.perform(delete("/deleteavorder?id={id}",1L))
                .andExpect(status().isNoContent());

        verify(availableOrderRepository, times(1)).delete(1L);
        verifyNoMoreInteractions(availableOrderRepository);
    }
}
