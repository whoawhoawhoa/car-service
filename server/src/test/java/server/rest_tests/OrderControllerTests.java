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
import server.jpa.Order;
import server.jpa.OrderRepository;
import server.web.OrderController;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientJmsSender clientJmsSender;


    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(orderRepository);
        reset(clientJmsSender);
        orderController = new OrderController(orderRepository, clientJmsSender);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnOrders() throws Exception {
        Order first = TestUtil.produceOrder();
        Order second = TestUtil.produceOrder();

        when(orderRepository.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())))
                .andExpect(jsonPath("$[0].clientMark", is(first.getClientMark())))
                .andExpect(jsonPath("$[0].workerMark", is(first.getWorkerMark())))
                .andExpect(jsonPath("$[0].totalPrice", is(first.getTotalPrice())))
                .andExpect(jsonPath("$[0].status", is(first.getStatus())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].orderDate", is(second.getOrderDate().toString())))
                .andExpect(jsonPath("$[1].serviceType", is(second.getServiceType())))
                .andExpect(jsonPath("$[1].address", is(second.getAddress())))
                .andExpect(jsonPath("$[1].commentary", is(second.getCommentary())))
                .andExpect(jsonPath("$[1].clientMark", is(second.getClientMark())))
                .andExpect(jsonPath("$[1].workerMark", is(second.getWorkerMark())))
                .andExpect(jsonPath("$[1].totalPrice", is(second.getTotalPrice())))
                .andExpect(jsonPath("$[1].status", is(second.getStatus())));

        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findByClientLogin_ShouldReturnOrders() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.findOrderByClientLogin(first.getClient().getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/orderbyclient?login={login}",
                first.getClient().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())))
                .andExpect(jsonPath("$[0].clientMark", is(first.getClientMark())))
                .andExpect(jsonPath("$[0].workerMark", is(first.getWorkerMark())))
                .andExpect(jsonPath("$[0].totalPrice", is(first.getTotalPrice())))
                .andExpect(jsonPath("$[0].status", is(first.getStatus())));

        verify(orderRepository, times(1))
                .findOrderByClientLogin(first.getClient().getLogin());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findByCarId_ShouldReturnOrders() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.findOrderByCarId(first.getCar().getId()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/orderbycarid?id={id}",first.getCar().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())))
                .andExpect(jsonPath("$[0].clientMark", is(first.getClientMark())))
                .andExpect(jsonPath("$[0].workerMark", is(first.getWorkerMark())))
                .andExpect(jsonPath("$[0].totalPrice", is(first.getTotalPrice())))
                .andExpect(jsonPath("$[0].status", is(first.getStatus())));

        verify(orderRepository, times(1))
                .findOrderByCarId(first.getCar().getId());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void findByWorkerLogin_ShouldReturnOrders() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.findOrderByWorkerLogin(first.getWorker().getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/orderbyworker?login={login}",first.getWorker().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].orderDate", is(first.getOrderDate().toString())))
                .andExpect(jsonPath("$[0].serviceType", is(first.getServiceType())))
                .andExpect(jsonPath("$[0].address", is(first.getAddress())))
                .andExpect(jsonPath("$[0].commentary", is(first.getCommentary())))
                .andExpect(jsonPath("$[0].clientMark", is(first.getClientMark())))
                .andExpect(jsonPath("$[0].workerMark", is(first.getWorkerMark())))
                .andExpect(jsonPath("$[0].totalPrice", is(first.getTotalPrice())))
                .andExpect(jsonPath("$[0].status", is(first.getStatus())));

        verify(orderRepository, times(1))
                .findOrderByWorkerLogin(first.getWorker().getLogin());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void updateOrder_ShouldReturnOk_IdNotNull() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.findOne(first.getId())).thenReturn(new Order());
        when(orderRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(orderRepository, times(1)).findOne(first.getId());
        verify(orderRepository, times(1)).save(first);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void updateOrder_ShouldReturnOk_IdIsNull() throws Exception {
        Order first = TestUtil.produceOrder();
        first.setId(0);

        when(orderRepository.findOrderByClientAndWorkerAndOrderDateAndCar(
                eq(first.getClient()), eq(first.getWorker()),
                any(Date.class), eq(first.getCar())))
                .thenReturn(null);
        when(orderRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(orderRepository, times(1)).
                findOrderByClientAndWorkerAndOrderDateAndCar(eq(first.getClient()),
                        eq(first.getWorker()), any(Date.class), eq(first.getCar()));
        verify(orderRepository, times(1)).save(first);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void updateOrder_ShouldReturnConflict_OrderDoesntChange() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.findOne(first.getId())).thenReturn(first);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(orderRepository, times(1))
                .findOne(first.getId());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void updateOrder_ShouldReturnOk_ShouldSetId() throws Exception {
        Order first = TestUtil.produceOrder();
        first.setId(0);
        Order second = TestUtil.produceOrder();
        Order toSave = first.clone();
        toSave.setId(second.getId());

        when(orderRepository.findOrderByClientAndWorkerAndOrderDateAndCar(
                eq(first.getClient()), eq(first.getWorker()),
                any(Date.class), eq(first.getCar())))
                .thenReturn(Collections.singletonList(second));
        when(orderRepository.save(toSave)).thenReturn(toSave);

        mockMvc.perform(put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(second.getId())));

        verify(orderRepository, times(1)).
                findOrderByClientAndWorkerAndOrderDateAndCar(eq(first.getClient()),
                        eq(first.getWorker()), any(Date.class), eq(first.getCar()));
        verify(orderRepository, times(1)).save(toSave);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void createOrder_ShouldReturnCreated() throws Exception {
        Order first = TestUtil.produceOrder();

        when(orderRepository.save(first)).thenReturn(first);
        doNothing().when(clientJmsSender).sendMessage("toReportOnConfirm " + first.getWorker().getEmail());

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        verify(clientJmsSender, times(1)).
                sendMessage("toReportOnConfirm " + first.getWorker().getEmail());
        verify(orderRepository, times(1)).save(first);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(clientJmsSender);
    }

    @Test
    public void deleteOrder_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderRepository).delete(1L);

        mockMvc.perform(delete("/order?id={id}",1L))
                .andExpect(status().isNoContent());

        verify(orderRepository, times(1)).delete(1L);
        verifyNoMoreInteractions(orderRepository);
    }
}
