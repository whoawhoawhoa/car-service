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
import server.jpa.CarType;
import server.jpa.CarTypeRepository;
import server.web.CarTypeController;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CarTypeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CarTypeController carTypeController;

    @Mock
    private CarTypeRepository carTypeRepositoryMock;


    @Before
    public void setup()
    {
        carTypeController = new CarTypeController(carTypeRepositoryMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(carTypeController).build();
    }

    @Test
    public void addCarType_ShouldReturnCarType() throws Exception {
        CarType carType = new CarType(1L, "Cartype");

        when(carTypeRepositoryMock.findByCarType(carType.getCarType().toLowerCase())).thenReturn(null);

        mockMvc.perform(post("/car_type")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carType)))
                .andExpect(status().isCreated());

        ArgumentCaptor<CarType> captor = ArgumentCaptor.forClass(CarType.class);
        verify(carTypeRepositoryMock, times(1)).findByCarType(carType.getCarType().toLowerCase());
        verify(carTypeRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(carTypeRepositoryMock);

        CarType argument = captor.getValue();
        assertThat(argument.getId(), is(1L));
        assertThat(argument.getCarType(), is("Cartype"));

    }

    @Test
    public void addCarType_ShouldReturnConflict() throws Exception
    {
        CarType carType = new CarType(1L, "Cartype");

        when(carTypeRepositoryMock.findByCarType(carType.getCarType().toLowerCase())).thenReturn(carType);

        mockMvc.perform(post("/car_type")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carType)))
                .andExpect(status().isConflict());

        verify(carTypeRepositoryMock, times(1)).findByCarType(carType.getCarType().toLowerCase());
        verifyNoMoreInteractions(carTypeRepositoryMock);

    }

    @Test
    public void getAllCarTypes() throws Exception {
        CarType carType1 = TestUtil.produceCarType();
        CarType carType2 = TestUtil.produceCarType();

        when(carTypeRepositoryMock.findAll()).thenReturn(Arrays.asList(carType1, carType2));

        mockMvc.perform(get("/car_types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(carType1.getId())))
                .andExpect(jsonPath("$[0].carType", is(carType1.getCarType())))
                .andExpect(jsonPath("$[1].id", is(carType2.getId())))
                .andExpect(jsonPath("$[1].carType", is(carType2.getCarType())));

        verify(carTypeRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }

    @Test
    public void updateCarType_ShouldReturnOk() throws Exception
    {
        CarType carType = TestUtil.produceCarType();

        when(carTypeRepositoryMock.findOne(carType.getId())).thenReturn(new CarType());
        when(carTypeRepositoryMock.save(carType)).thenReturn(carType);

        mockMvc.perform(put("/cartypeupdate")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(carType.getId())))
                .andExpect(jsonPath("$.carType", is(carType.getCarType())));

        ArgumentCaptor<CarType> argumentCaptor = ArgumentCaptor.forClass(CarType.class);
        verify(carTypeRepositoryMock, times(1)).findOne(carType.getId());
        verify(carTypeRepositoryMock, times(1)).save(argumentCaptor.capture());
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }

    @Test
    public void updateCarType_ShouldReturnConflict() throws Exception
    {
        CarType carType = TestUtil.produceCarType();

        when(carTypeRepositoryMock.findOne(carType.getId())).thenReturn(carType);

        mockMvc.perform(put("/cartypeupdate")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carType)))
                .andExpect(status().isConflict());

        verify(carTypeRepositoryMock, times(1)).findOne(carType.getId());
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }

    @Test
    public void deleteCarType_ShouldReturnOk() throws Exception
    {
        CarType carType = TestUtil.produceCarType();

        mockMvc.perform(delete("/deletecartype?id={id}", carType.getId()))
                .andExpect(status().isOk());

        verify(carTypeRepositoryMock, times(1)).delete(carType.getId());
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }

    @Test
    public void getCarTypeById_ShouldReturnCarType() throws Exception
    {
        CarType carType = TestUtil.produceCarType();

        when(carTypeRepositoryMock.findOne(carType.getId())).thenReturn(carType);

        mockMvc.perform(get("/car_type?id={id}", carType.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(carType.getId())))
                .andExpect(jsonPath("$.carType", is(carType.getCarType())));

        verify(carTypeRepositoryMock, times(1)).findOne(carType.getId());
        verifyNoMoreInteractions(carTypeRepositoryMock);

    }

    @Test
    public void getCarTypeByType_ShouldReturnCarType() throws Exception
    {
        CarType carType = TestUtil.produceCarType();

        when(carTypeRepositoryMock.findByCarType(carType.getCarType())).thenReturn(carType);

        mockMvc.perform(get("/car_type_type?type={carType}", carType.getCarType()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(carType.getId())))
                .andExpect(jsonPath("$.carType", is(carType.getCarType())));

        verify(carTypeRepositoryMock, times(1)).findByCarType(carType.getCarType());
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }
}