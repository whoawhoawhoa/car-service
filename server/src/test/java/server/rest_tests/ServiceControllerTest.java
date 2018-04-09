package server.rest_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.TestUtil;
import server.jpa.Service;
import server.jpa.ServiceRepository;
import server.jpa.Worker;
import server.web.ServiceController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ServiceControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ServiceController serviceController;

    @Mock
    private ServiceRepository serviceRepositoryMock;


    @Before
    public void setup()
    {
        serviceController = new ServiceController(serviceRepositoryMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    public void getAllServices_ShouldReturnServices() throws Exception
    {
        Service service1 = TestUtil.produceService();
        Service service2 = TestUtil.produceService();

        when(serviceRepositoryMock.findAll()).thenReturn(Arrays.asList(service1, service2));

        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(service1.getId())))
                .andExpect(jsonPath("$[0].coef", is(service1.getCoef())))
                .andExpect(jsonPath("$[0].worker.id", is(service1.getWorker().getId())))
                .andExpect(jsonPath("$[0].price.id", is(service1.getPrice().getId())))
                .andExpect(jsonPath("$[1].id", is(service2.getId())))
                .andExpect(jsonPath("$[1].coef", is(service2.getCoef())))
                .andExpect(jsonPath("$[1].worker.id", is(service2.getWorker().getId())))
                .andExpect(jsonPath("$[1].price.id", is(service2.getPrice().getId())));

        verify(serviceRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void updateService_ShouldReturnOk() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId()))
                .thenReturn(new LinkedList<>());
        when(serviceRepositoryMock.save(service)).thenReturn(service);

        mockMvc.perform(put("/service")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isOk());

        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(serviceRepositoryMock, times(1)).getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        verify(serviceRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void updateService_ShouldReturnConflict() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(put("/service")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isConflict());

        verify(serviceRepositoryMock, times(1)).getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void createService_ShouldReturnCreated() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/service")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(serviceRepositoryMock, times(1)).getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        verify(serviceRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void createService_ShouldReturnConflict() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(post("/service")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isConflict());

        verify(serviceRepositoryMock, times(1)).getServicesByWorkerLoginAndPriceId(service.getWorker().getLogin(), service.getPrice().getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getService_ShouldReturnService() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.findOne(service.getId())).thenReturn(service);

        mockMvc.perform(get("/service?id={id}", service.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(service.getId())))
                .andExpect(jsonPath("$.coef", is(service.getCoef())))
                .andExpect(jsonPath("$.worker.id", is(service.getWorker().getId())))
                .andExpect(jsonPath("$.price.id", is(service.getPrice().getId())));

        verify(serviceRepositoryMock, times(1)).findOne(service.getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void deleteService_ShouldReturnNoContent() throws Exception
    {
        Service service = TestUtil.produceService();

        mockMvc.perform(delete("/service?id={id}", service.getId()))
                .andExpect(status().isNoContent());

        verify(serviceRepositoryMock, times(1)).delete(service.getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getServicesForAvOrder_ShouldReturnServices() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerIdAndPriceServiceTypeAndPriceCarTypeCarType(service.getWorker().getId(),
                service.getPrice().getServiceType(), service.getPrice().getCarType().getCarType()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(get("/serviceForAvOrder?id={id}&serviceType={serviceType}&carType={carType}", service.getWorker().getId(), service.getPrice().getServiceType(), service.getPrice().getCarType().getCarType()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(service.getId())))
                .andExpect(jsonPath("$[0].coef", is(service.getCoef())))
                .andExpect(jsonPath("$[0].worker.id", is(service.getWorker().getId())))
                .andExpect(jsonPath("$[0].price.id", is(service.getPrice().getId())));

        verify(serviceRepositoryMock, times(1)).getServicesByWorkerIdAndPriceServiceTypeAndPriceCarTypeCarType(service.getWorker().getId(),
                service.getPrice().getServiceType(), service.getPrice().getCarType().getCarType());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getServicesByWorkerLogin_ShouldReturnServices() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByWorkerLogin(service.getWorker().getLogin()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(get("/serviceWorker?login={login}", service.getWorker().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(service.getId())))
                .andExpect(jsonPath("$[0].coef", is(service.getCoef())))
                .andExpect(jsonPath("$[0].worker.id", is(service.getWorker().getId())))
                .andExpect(jsonPath("$[0].price.id", is(service.getPrice().getId())));

        verify(serviceRepositoryMock, times(1)).getServicesByWorkerLogin(service.getWorker().getLogin());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getServicesByPriceId_ShouldReturnPrices() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByPriceId(service.getPrice().getId()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(get("/servicePrice?id={id}", service.getPrice().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(service.getId())))
                .andExpect(jsonPath("$[0].coef", is(service.getCoef())))
                .andExpect(jsonPath("$[0].worker.id", is(service.getWorker().getId())))
                .andExpect(jsonPath("$[0].price.id", is(service.getPrice().getId())));

        verify(serviceRepositoryMock, times(1)).getServicesByPriceId(service.getPrice().getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getServicesByServiceTypeAndCarType_ShouldReturnServices() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByPriceServiceTypeAndPriceCarTypeId(service.getPrice().getServiceType(), service.getPrice().getCarType().getId()))
                .thenReturn(Collections.singletonList(service));

        mockMvc.perform(get("/servicesForSendingMails?serviceType={serviceType}&carTypeId={carTypeId}",
                service.getPrice().getServiceType(), service.getPrice().getCarType().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(service.getId())))
                .andExpect(jsonPath("$[0].coef", is(service.getCoef())))
                .andExpect(jsonPath("$[0].worker.id", is(service.getWorker().getId())))
                .andExpect(jsonPath("$[0].price.id", is(service.getPrice().getId())));
        System.out.println(service.getCoef());

        verify(serviceRepositoryMock, times(1)).getServicesByPriceServiceTypeAndPriceCarTypeId(service.getPrice().getServiceType(), service.getPrice().getCarType().getId());
        verifyNoMoreInteractions(serviceRepositoryMock);
    }

    @Test
    public void getWorkersEmailsByServices_ShouldReturnEmptyList() throws Exception
    {
        Service service = TestUtil.produceService();

        when(serviceRepositoryMock.getServicesByPriceServiceTypeAndPriceCarTypeId(service.getPrice().getServiceType(), service.getPrice().getCarType().getId()))
                .thenReturn(Collections.emptyList());

        assertEquals(new ArrayList<>(), serviceController.getWorkersEmailsByServices(service.getPrice().getServiceType(), service.getPrice().getCarType().getId()));
    }

    @Test
    public void getWorkersEmailsByServices_ShouldReturnListOfEmails() throws Exception
    {
        Service service1 = TestUtil.produceService();
        Service service2 = TestUtil.produceService();

        service2.setPrice(service1.getPrice());
        service2.getWorker().setStatus(1);
        service2.getWorker().setEmail("test@test.com");

        when(serviceRepositoryMock.getServicesByPriceServiceTypeAndPriceCarTypeId(service1.getPrice().getServiceType(), service2.getPrice().getCarType().getId()))
                .thenReturn(Arrays.asList(service1, service2));

        ArrayList emails = new ArrayList<>(Collections.singleton(service2.getWorker().getEmail()));

        assertEquals(emails, serviceController.getWorkersEmailsByServices(service1.getPrice().getServiceType(), service1.getPrice().getCarType().getId()));
    }
}