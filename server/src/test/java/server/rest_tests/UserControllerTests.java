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
import server.jpa.User;
import server.jpa.UserRepository;
import server.web.UserController;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepositoryMock;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Before
    public void setup() {
        reset(userRepositoryMock);
        userController = new UserController(userRepositoryMock);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void findAll_ShouldReturnUsers() throws Exception {
        User first = TestUtil.produceUser();
        User second = TestUtil.produceUser();

        when(userRepositoryMock.findAll()).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(first.getId())))
                .andExpect(jsonPath("$[0].login", is(first.getLogin())))
                .andExpect(jsonPath("$[0].password", is(first.getPassword())))
                .andExpect(jsonPath("$[0].role", is(first.getRole())))
                .andExpect(jsonPath("$[1].id", is(second.getId())))
                .andExpect(jsonPath("$[1].login", is(second.getLogin())))
                .andExpect(jsonPath("$[1].password", is(second.getPassword())))
                .andExpect(jsonPath("$[1].role", is(second.getRole())));

        verify(userRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnUser() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLoginAndPassword(
                first.getLogin(), first.getPassword()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(get("/user?login={login}&password={password}",
                first.getLogin(), first.getPassword()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(first.getId())))
                .andExpect(jsonPath("$.login", is(first.getLogin())))
                .andExpect(jsonPath("$.password", is(first.getPassword())))
                .andExpect(jsonPath("$.role", is(first.getRole())));

        verify(userRepositoryMock, times(1)).
                findUserByLoginAndPassword(first.getLogin(), first.getPassword());
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void findByLoginAndPassword_ShouldReturnBadRequest() throws Exception {
        when(userRepositoryMock.findUserByLoginAndPassword("login", "pass"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/user?login={login}&password={password}",
                "login", "pass"))
                .andExpect(status().isBadRequest());

        verify(userRepositoryMock, times(1)).
                findUserByLoginAndPassword("login", "pass");
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void updateUser_ShouldReturnOk() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(userRepositoryMock.findOne(first.getId())).thenReturn(new User());
        when(userRepositoryMock.save(first)).thenReturn(first);

        mockMvc.perform(put("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isOk());

        verify(userRepositoryMock, times(1)).
                findUserByLogin(first.getLogin());
        verify(userRepositoryMock, times(1)).findOne(first.getId());
        verify(userRepositoryMock, times(1)).save(first);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void updateUser_ShouldReturnConflict_UserDoesntChange() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(userRepositoryMock.findOne(first.getId())).thenReturn(first);

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(userRepositoryMock, times(1)).
                findUserByLogin(first.getLogin());
        verify(userRepositoryMock, times(1)).findOne(first.getId());
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void updateUser_ShouldReturnConflict_ThereIsUserWithThisLogin() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLogin(first.getLogin())).
                thenReturn(Collections.singletonList(first));
        when(userRepositoryMock.findOne(first.getId())).thenReturn(new User());

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(userRepositoryMock, times(1)).
                findUserByLogin(first.getLogin());
        verify(userRepositoryMock, times(1)).findOne(first.getId());
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void createUser_ShouldReturnCreated() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLogin(first.getLogin())).thenReturn(new ArrayList<>());
        when(userRepositoryMock.save(first)).thenReturn(first);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isCreated());

        verify(userRepositoryMock, times(1)).
                findUserByLogin(first.getLogin());
        verify(userRepositoryMock, times(1)).save(first);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void createUser_ShouldReturnConflict() throws Exception {
        User first = TestUtil.produceUser();

        when(userRepositoryMock.findUserByLogin(first.getLogin()))
                .thenReturn(Collections.singletonList(first));

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(first)))
                .andExpect(status().isConflict());

        verify(userRepositoryMock, times(1)).
                findUserByLogin(first.getLogin());
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userRepositoryMock).delete(1L);

        mockMvc.perform(delete("/user?id={id}",1L))
                .andExpect(status().isNoContent());

        verify(userRepositoryMock, times(1)).delete(1L);
        verifyNoMoreInteractions(userRepositoryMock);
    }
}
