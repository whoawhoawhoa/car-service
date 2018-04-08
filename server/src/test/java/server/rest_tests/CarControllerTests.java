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
import server.jpa.Car;
import server.jpa.CarRepository;
import server.jpa.CarType;
import server.jpa.Client;
import server.web.CarController;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CarControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private CarController carController;

    @Mock
    private CarRepository carRepository;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(carRepository);
        carController = new CarController(carRepository);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(carController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnCars() throws Exception {
        Car first = TestUtil.produceCar();
        Car second = TestUtil.produceCar();

        when(carRepository.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].number", is(first.getNumber())))
                .andExpect(jsonPath("$[0].brand", is(first.getBrand())))
                .andExpect(jsonPath("$[0].color", is(first.getColor())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].number", is(second.getNumber())))
                .andExpect(jsonPath("$[1].brand", is(second.getBrand())))
                .andExpect(jsonPath("$[1].color", is(second.getColor())));

        verify(carRepository, times(1)).findAll();
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findByClientLogin_ShouldReturnCars() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.findCarsByClientLogin(first.getClient().getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/client_cars?login={login}",
                first.getClient().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].number", is(first.getNumber())))
                .andExpect(jsonPath("$[0].brand", is(first.getBrand())))
                .andExpect(jsonPath("$[0].color", is(first.getColor())));

        verify(carRepository, times(1))
                .findCarsByClientLogin(first.getClient().getLogin());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findById_ShouldReturnCar() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.findOne(first.getId())).thenReturn(first);

        mockMvc.perform(get("/car?id={id}", first.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(first.getId())))
                .andExpect(jsonPath("$.number", is(first.getNumber())))
                .andExpect(jsonPath("$.brand", is(first.getBrand())))
                .andExpect(jsonPath("$.color", is(first.getColor())));

        verify(carRepository, times(1)).findOne(first.getId());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findById_ShouldReturnBadRequest() throws Exception {
        when(carRepository.findOne(1L)).thenReturn(null);

        mockMvc.perform(get("/car?id={id}", 1L))
                .andExpect(status().isBadRequest());

        verify(carRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void findByClientIdAndCarTypeId_ShouldReturnCar() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.getCarsByClientIdAndCarTypeId(
                first.getClient().getId(), first.getCarType().getId()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/carsfororder?id={id}&carTypeId={ctid}",
                first.getClient().getId(), first.getCarType().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].number", is(first.getNumber())))
                .andExpect(jsonPath("$[0].brand", is(first.getBrand())))
                .andExpect(jsonPath("$[0].color", is(first.getColor())));

        verify(carRepository, times(1))
                .getCarsByClientIdAndCarTypeId(
                        first.getClient().getId(), first.getCarType().getId());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void updateCar_ShouldReturnOk() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.findOne(first.getId())).thenReturn(new Car());
        when(carRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(carRepository, times(1))
                .findOne(first.getId());
        verify(carRepository, times(1)).save(first);
        verifyNoMoreInteractions(carRepository);
    }


    @Test
    public void updateCar_ShouldReturnConflict_CarDoesntChange() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.findOne(first.getId())).thenReturn(first);

        mockMvc.perform(put("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(carRepository, times(1)).findOne(first.getId());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void createCar_ShouldReturnCreated() throws Exception {
        Car first = TestUtil.produceCar();

        when(carRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        verify(carRepository, times(1)).save(first);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    public void deleteCar_ShouldReturnNoContent() throws Exception {
        doNothing().when(carRepository).delete(1L);

        mockMvc.perform(delete("/car?id={id}",1L))
                .andExpect(status().isNoContent());

        verify(carRepository, times(1)).delete(1L);
        verifyNoMoreInteractions(carRepository);
    }
}
