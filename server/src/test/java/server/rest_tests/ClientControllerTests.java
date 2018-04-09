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
import server.jpa.Client;
import server.jpa.ClientRepository;
import server.jpa.Worker;
import server.web.ClientController;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ClientControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientRepository clientRepository;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(clientRepository);
        clientController = new ClientController(clientRepository);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnClients() throws Exception {
        Client first = TestUtil.produceClient();
        Client second = TestUtil.produceClient();

        when(clientRepository.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/clients"))
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
                .andExpect(jsonPath("$[0].email", is(first.getEmail())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].login", is(second.getLogin())))
                .andExpect(jsonPath("$[1].password", is(second.getPassword())))
                .andExpect(jsonPath("$[1].name", is(second.getName())))
                .andExpect(jsonPath("$[1].fname", is(second.getFname())))
                .andExpect(jsonPath("$[1].pnumber", is(second.getPnumber())))
                .andExpect(jsonPath("$[1].city", is(second.getCity())))
                .andExpect(jsonPath("$[1].rating", is(second.getRating())))
                .andExpect(jsonPath("$[1].email", is(second.getEmail())));

        verify(clientRepository, times(1)).findAll();
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnClient() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLoginAndPassword(
                first.getLogin(), first.getPassword()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/client?login={login}&password={password}",
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
                .andExpect(jsonPath("$.email", is(first.getEmail())));

        verify(clientRepository, times(1)).
                findClientsByLoginAndPassword(first.getLogin(), first.getPassword());
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnBadRequest() throws Exception {
        when(clientRepository.findClientsByLoginAndPassword("login", "pass"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/client?login={login}&password={password}",
                "login", "pass"))
                .andExpect(status().isBadRequest());

        verify(clientRepository, times(1)).
                findClientsByLoginAndPassword("login", "pass");
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void updateClient_ShouldReturnOk() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(clientRepository.findClientById(first.getId())).thenReturn(new Client());
        when(clientRepository.save(first)).thenReturn(first);

        mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verify(clientRepository, times(1)).
                findClientById(first.getId());
        verify(clientRepository, times(1)).save(first);
        verifyNoMoreInteractions(clientRepository);
    }


    @Test
    public void updateClient_ShouldReturnConflict_ThereIsClientsWithThisLogin() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));
        when(clientRepository.findClientById(first.getId())).thenReturn(new Client());

        mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verify(clientRepository, times(1)).
                findClientById(first.getId());
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void updateClient_ShouldReturnConflict_ClientDoesntChange() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin()))
                .thenReturn(new ArrayList<>());
        when(clientRepository.findClientById(first.getId())).thenReturn(first);

        mockMvc.perform(put("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verify(clientRepository, times(1)).
                findClientById(first.getId());
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void createClient_ShouldReturnCreated() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(clientRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verify(clientRepository, times(1)).save(first);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void createClient_ShouldReturnConflict() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    public void deleteClient_ShouldReturnNoContent() throws Exception {
        Client first = TestUtil.produceClient();

        when(clientRepository.findClientsByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));
        doNothing().when(clientRepository).delete(first.getId());

        mockMvc.perform(delete("/client?login={login}",first.getLogin()))
                .andExpect(status().isNoContent());

        verify(clientRepository, times(1)).
                findClientsByLogin(first.getLogin());
        verify(clientRepository, times(1)).delete(first.getId());
        verifyNoMoreInteractions(clientRepository);
    }
}
