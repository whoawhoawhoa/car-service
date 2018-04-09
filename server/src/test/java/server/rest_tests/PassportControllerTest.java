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
import server.jpa.Passport;
import server.jpa.PassportRepository;
import server.web.PassportController;

import java.util.Arrays;
import java.util.Collections;

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
public class PassportControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PassportController passportController;

    @Mock
    private PassportRepository passportRepositoryMock;


    @Before
    public void setup()
    {
        passportController = new PassportController(passportRepositoryMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(passportController).build();
    }

    @Test
    public void getAllPassports_ShouldReturnPassports() throws Exception {
        Passport passport1 = TestUtil.producePassport();
        Passport passport2 = TestUtil.producePassport();

        when(passportRepositoryMock.findAll()).thenReturn(Arrays.asList(passport1, passport2));

        mockMvc.perform(get("/passports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(passport1.getId())))
                .andExpect(jsonPath("$[0].number", is(passport1.getNumber())))
                .andExpect(jsonPath("$[0].issuedBy", is(passport1.getIssuedBy())))
                .andExpect(jsonPath("$[0].worker.id", is(passport1.getWorker().getId())))
                .andExpect(jsonPath("$[1].id", is(passport2.getId())))
                .andExpect(jsonPath("$[1].number", is(passport2.getNumber())))
                .andExpect(jsonPath("$[1].issuedBy", is(passport2.getIssuedBy())))
                .andExpect(jsonPath("$[1].worker.id", is(passport2.getWorker().getId())));

        verify(passportRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void getPassport_ShouldReturnPassport() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findOne(passport.getId())).thenReturn(passport);

        mockMvc.perform(get("/passport?id={id}", passport.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(passport.getId())))
                .andExpect(jsonPath("$.number", is(passport.getNumber())))
                .andExpect(jsonPath("$.issuedBy", is(passport.getIssuedBy())))
                .andExpect(jsonPath("$.worker.id", is(passport.getWorker().getId())));

        verify(passportRepositoryMock, times(1)).findOne(passport.getId());
        verifyNoMoreInteractions(passportRepositoryMock);

    }

    @Test
    public void getPassportByWorkerLogin_ShouldReturnPassport() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findPassportByWorkerLogin(passport.getWorker().getLogin())).thenReturn(passport);

        mockMvc.perform(get("/worker_passport?login={login}", passport.getWorker().getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(passport.getId())))
                .andExpect(jsonPath("$.number", is(passport.getNumber())))
                .andExpect(jsonPath("$.issuedBy", is(passport.getIssuedBy())))
                .andExpect(jsonPath("$.worker.id", is(passport.getWorker().getId())));

        verify(passportRepositoryMock, times(1)).findPassportByWorkerLogin(passport.getWorker().getLogin());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void getPassportByWorkerLogin_ShouldBadRequest() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findPassportByWorkerLogin(passport.getWorker().getLogin())).thenReturn(null);

        mockMvc.perform(get("/worker_passport?login={login}", passport.getWorker().getLogin()))
                .andExpect(status().isBadRequest());

        verify(passportRepositoryMock, times(1)).findPassportByWorkerLogin(passport.getWorker().getLogin());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void postPassport_ShouldReturnCreated() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findPassportByNumber(passport.getNumber())).thenReturn(null);

        mockMvc.perform(post("/passport")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passport)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Passport> captor = ArgumentCaptor.forClass(Passport.class);
        verify(passportRepositoryMock, times(1)).findPassportByNumber(passport.getNumber());
        verify(passportRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void postPassport_ShouldReturnConflict() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findPassportByNumber(passport.getNumber())).thenReturn(passport);

        mockMvc.perform(post("/passport")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passport)))
                .andExpect(status().isConflict());

        verify(passportRepositoryMock, times(1)).findPassportByNumber(passport.getNumber());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void postPassport_ShouldReturnUnprocessableEntity() throws Exception
    {
        Passport passport = TestUtil.producePassport();
        passport.setNumber(1L);

        when(passportRepositoryMock.findPassportByNumber(passport.getNumber())).thenReturn(null);

        mockMvc.perform(post("/passport")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passport)))
                .andExpect(status().isUnprocessableEntity());

        verify(passportRepositoryMock, times(1)).findPassportByNumber(passport.getNumber());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void deletePassport_ShouldReturnNoContent() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        mockMvc.perform(delete("/passport?id={id}", passport.getId()))
                .andExpect(status().isNoContent());

        verify(passportRepositoryMock, times(1)).delete(passport.getId());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void updatePassport_ShouldReturnOk() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.save(passport)).thenReturn(passport);

        mockMvc.perform(put("/passport")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passport)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(passport.getId())))
                .andExpect(jsonPath("$.number", is(passport.getNumber())))
                .andExpect(jsonPath("$.issuedBy", is(passport.getIssuedBy())))
                .andExpect(jsonPath("$.worker.id", is(passport.getWorker().getId())));

        ArgumentCaptor<Passport> captor = ArgumentCaptor.forClass(Passport.class);
        verify(passportRepositoryMock, times(1)).save(captor.capture());
        verifyNoMoreInteractions(passportRepositoryMock);
    }

    @Test
    public void updatePassport_ShouldReturnUnprocessableEntity() throws Exception
    {
        Passport passport = TestUtil.producePassport();
        passport.setNumber(1L);

        mockMvc.perform(put("/passport")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(passport)))
                .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(passportRepositoryMock);
    }

    @Test
    public void getPassportsByStatus_ShouldReturnPassports() throws Exception
    {
        Passport passport = TestUtil.producePassport();

        when(passportRepositoryMock.findPassportByWorkerStatus(passport.getWorker().getStatus())).thenReturn(Collections.singletonList(passport));

        mockMvc.perform(get("/passports_to_admin?status={status}", passport.getWorker().getStatus()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(passport.getId())))
                .andExpect(jsonPath("$[0].number", is(passport.getNumber())))
                .andExpect(jsonPath("$[0].issuedBy", is(passport.getIssuedBy())))
                .andExpect(jsonPath("$[0].worker.id", is(passport.getWorker().getId())));

        verify(passportRepositoryMock, times(1)).findPassportByWorkerStatus(passport.getWorker().getStatus());
        verifyNoMoreInteractions(passportRepositoryMock);
    }
}