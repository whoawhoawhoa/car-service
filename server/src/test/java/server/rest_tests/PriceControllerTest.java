package server.web;

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
import server.jpa.Price;
import server.jpa.PriceRepository;
import server.jpa.Service;

import java.util.*;

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
public class PriceControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PriceController priceController;

    @Mock
    private PriceRepository priceRepositoryMock;


    @Before
    public void setup()
    {
        priceController = new PriceController(priceRepositoryMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(priceController).build();
    }

    @Test
    public void getAllPrices_ShouldReturnPrices() throws Exception
    {
        Price price1 = TestUtil.producePrice();
        Price price2 = TestUtil.producePrice();

        when(priceRepositoryMock.findAll()).thenReturn(Arrays.asList(price1, price2));

        mockMvc.perform(get("/prices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(price1.getId())))
                .andExpect(jsonPath("$[0].serviceType", is(price1.getServiceType())))
                .andExpect(jsonPath("$[0].price", is(price1.getPrice())))
                .andExpect(jsonPath("$[0].carType.id", is(price1.getCarType().getId())));

        verify(priceRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void getPrice_ShouldReturnPrice() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findOne(price.getId())).thenReturn(price);

        mockMvc.perform(get("/price?id={id}", price.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(price.getId())))
                .andExpect(jsonPath("$.serviceType", is(price.getServiceType())))
                .andExpect(jsonPath("$.price", is(price.getPrice())))
                .andExpect(jsonPath("$.carType.id", is(price.getCarType().getId())));

        verify(priceRepositoryMock, times(1)).findOne(price.getId());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void deletePrice_ShouldReturnNoContent() throws Exception
    {
        Price price = TestUtil.producePrice();

        mockMvc.perform(delete("/price?id={id}", price.getId()))
                .andExpect(status().isNoContent());

        verify(priceRepositoryMock, times(1)).delete(price.getId());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void addPrice_ShouldReturnCreated() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findPriceByCarTypeCarTypeAndServiceType(price.getCarType().getCarType(), price.getServiceType()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/price")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        verify(priceRepositoryMock, times(1)).findPriceByCarTypeCarTypeAndServiceType(price.getCarType().getCarType(), price.getServiceType());
        verify(priceRepositoryMock,times(1)).save(captor.capture());
        verifyNoMoreInteractions(priceRepositoryMock);

    }

    @Test
    public void addPrice_ShouldReturnConflict() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findPriceByCarTypeCarTypeAndServiceType(price.getCarType().getCarType(), price.getServiceType()))
                .thenReturn(Collections.singletonList(price));

        mockMvc.perform(post("/price")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isConflict());

        verify(priceRepositoryMock, times(1)).findPriceByCarTypeCarTypeAndServiceType(price.getCarType().getCarType(), price.getServiceType());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void updatePrice_ShouldReturnOk() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findPriceById(price.getId())).thenReturn(new Price());
        when(priceRepositoryMock.save(price)).thenReturn(price);

        mockMvc.perform(put("/updatePrice")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(price.getId())))
                .andExpect(jsonPath("$.serviceType", is(price.getServiceType())))
                .andExpect(jsonPath("$.price", is(price.getPrice())))
                .andExpect(jsonPath("$.carType.id", is(price.getCarType().getId())));

        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        verify(priceRepositoryMock, times(1)).findPriceById(price.getId());
        verify(priceRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void updatePrice_ShouldReturnConflict() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findPriceById(price.getId())).thenReturn(price);

        mockMvc.perform(put("/updatePrice")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(price)))
                .andExpect(status().isConflict());

        verify(priceRepositoryMock, times(1)).findPriceById(price.getId());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void getPricesByCarTypeCarType_ShouldReturnPrices() throws Exception
    {
        Price price = TestUtil.producePrice();
        HashSet<Service> services = new HashSet<>();
        services.add(TestUtil.produceService());
        price.setServiceSet(services);

        when(priceRepositoryMock.findByCarTypeCarType(price.getCarType().getCarType()))
                .thenReturn(Collections.singletonList(price));

        mockMvc.perform(get("/pricesByCarType?carType={carType}", price.getCarType().getCarType()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(price.getId())))
                .andExpect(jsonPath("$[0].serviceType", is(price.getServiceType())))
                .andExpect(jsonPath("$[0].price", is(price.getPrice())))
                .andExpect(jsonPath("$[0].carType.id", is(price.getCarType().getId())));

        verify(priceRepositoryMock, times(1)).findByCarTypeCarType(price.getCarType().getCarType());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void getPricesByCarTypeCarType_ShouldReturnEmptyList() throws Exception
    {
        Price priceToDelete1 = TestUtil.producePrice();
        Price priceToDelete2 = TestUtil.producePrice();
        Price priceToReturn = TestUtil.producePrice();

        priceToDelete2.setCarType(priceToDelete1.getCarType());
        priceToReturn.setCarType(priceToDelete1.getCarType());

        HashSet<Service> emptyHash = new HashSet<>();
        priceToDelete1.setServiceSet(emptyHash);
        priceToDelete2.setServiceSet(emptyHash);

        HashSet<Service> notEmptyHash = new HashSet<>();
        notEmptyHash.add(TestUtil.produceService());

        priceToReturn.setServiceSet(notEmptyHash);

        List<Price> prices = new LinkedList<>(Arrays.asList(priceToDelete1, priceToDelete2, priceToReturn));

        when(priceRepositoryMock.findByCarTypeCarType(priceToDelete1.getCarType().getCarType()))
                .thenReturn(prices);

        mockMvc.perform(get("/pricesByCarType?carType={carType}", priceToDelete1.getCarType().getCarType()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(priceToReturn.getId())))
                .andExpect(jsonPath("$[0].serviceType", is(priceToReturn.getServiceType())))
                .andExpect(jsonPath("$[0].price", is(priceToReturn.getPrice())))
                .andExpect(jsonPath("$[0].carType.id", is(priceToReturn.getCarType().getId())));

        verify(priceRepositoryMock, times(1)).findByCarTypeCarType(priceToDelete1.getCarType().getCarType());
        verifyNoMoreInteractions(priceRepositoryMock);
    }

    @Test
    public void getPricesByServiceType_ShouldReturnPrices() throws Exception
    {
        Price price = TestUtil.producePrice();

        when(priceRepositoryMock.findPriceByServiceTypeOrderByCarType(price.getServiceType()))
                .thenReturn(Collections.singletonList(price));

        mockMvc.perform(get("/pricesByServiceType?serviceType={serviceType}", price.getServiceType()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(price.getId())))
                .andExpect(jsonPath("$[0].serviceType", is(price.getServiceType())))
                .andExpect(jsonPath("$[0].price", is(price.getPrice())))
                .andExpect(jsonPath("$[0].carType.id", is(price.getCarType().getId())));

        verify(priceRepositoryMock, times(1)).findPriceByServiceTypeOrderByCarType(price.getServiceType());
        verifyNoMoreInteractions(priceRepositoryMock);
    }
}