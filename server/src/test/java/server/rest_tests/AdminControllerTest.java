package server.rest_tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.TestUtil;
import server.jpa.Admin;
import server.jpa.AdminRepository;
import server.jpa.User;
import server.web.AdminController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AdminControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminRepository adminRepositoryMock;


    @Before
    public void setup()
    {
        adminController = new AdminController(adminRepositoryMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void findAll_ShouldReturnAdmins() throws Exception {

        Admin firstAdmin = TestUtil.produceAdmin();
        Admin secondAdmin = TestUtil.produceAdmin();

        when(adminRepositoryMock.findAll()).thenReturn(Arrays.asList(firstAdmin, secondAdmin));

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstAdmin.getId())))
                .andExpect(jsonPath("$[0].login", is(firstAdmin.getLogin())))
                .andExpect(jsonPath("$[0].password", is(firstAdmin.getPassword())))
                .andExpect(jsonPath("$[0].user.id", is(firstAdmin.getUser().getId())))
                .andExpect(jsonPath("$[0].user.login", is(firstAdmin.getUser().getLogin())))
                .andExpect(jsonPath("$[0].user.password", is(firstAdmin.getUser().getPassword())))
                .andExpect(jsonPath("$[0].user.role", is(firstAdmin.getUser().getRole())))
                .andExpect(jsonPath("$[1].id", is(secondAdmin.getId())))
                .andExpect(jsonPath("$[1].login", is(secondAdmin.getLogin())))
                .andExpect(jsonPath("$[1].password", is(secondAdmin.getPassword())))
                .andExpect(jsonPath("$[1].user.id", is(secondAdmin.getUser().getId())))
                .andExpect(jsonPath("$[1].user.login", is(secondAdmin.getUser().getLogin())))
                .andExpect(jsonPath("$[1].user.password", is(secondAdmin.getUser().getPassword())))
                .andExpect(jsonPath("$[1].user.role", is(secondAdmin.getUser().getRole())));

        verify(adminRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(adminRepositoryMock);
    }

    @Test
    public void postAdmin_ShouldReturnAdmin() throws Exception {
        Admin admin = TestUtil.produceAdmin();

        mockMvc.perform(post("/admin")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(admin)))
                .andExpect(status().isCreated());

        ArgumentCaptor<Admin> dtoCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepositoryMock, times(1)).findAdminByLogin(admin.getLogin());
        verify(adminRepositoryMock, times(1)).save(dtoCaptor.capture());

        Admin argument = dtoCaptor.getValue();
        assertThat(argument.getId(), is(admin.getId()));
        assertThat(argument.getLogin(), is(admin.getLogin()));
        assertThat(argument.getPassword(), is(admin.getPassword()));
        assertThat(argument.getUser().getLogin(), is(admin.getUser().getLogin()));
        assertThat(argument.getUser().getPassword(), is(admin.getUser().getPassword()));
        assertThat(argument.getUser().getRole(), is(admin.getUser().getRole()));
    }

    @Test
    public void postAdmin_ShouldReturnConflict() throws Exception {
        Admin admin = TestUtil.produceAdmin();

        when(adminRepositoryMock.findAdminByLogin(admin.getLogin())).thenReturn(Collections.singletonList(admin));

        mockMvc.perform(post("/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(admin)))
                .andExpect(status().isConflict());

        verify(adminRepositoryMock, times(1)).findAdminByLogin(admin.getLogin());
        verifyNoMoreInteractions(adminRepositoryMock);
    }

    @Test
    public void checkAuthA_ShouldReturnAdmin() throws Exception {
        Admin admin = TestUtil.produceAdmin();


        when(adminRepositoryMock.findAdminByLoginAndPassword(admin.getLogin(), admin.getPassword()))
                .thenReturn(Collections.singletonList(admin));

        mockMvc.perform(get("/admin?login={login}&password={password}", admin.getLogin(), admin.getPassword()))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(admin.getId())))
                .andExpect(jsonPath("$.login", is(admin.getLogin())))
                .andExpect(jsonPath("$.password", is(admin.getPassword())))
                .andExpect(jsonPath("$.user.id", is(admin.getUser().getId())))
                .andExpect(jsonPath("$.user.login", is(admin.getUser().getLogin())))
                .andExpect(jsonPath("$.user.password", is(admin.getUser().getPassword())))
                .andExpect(jsonPath("$.user.role", is(admin.getUser().getRole())));


        verify(adminRepositoryMock, times(1)).findAdminByLoginAndPassword(admin.getLogin(), admin.getPassword());
        verifyNoMoreInteractions(adminRepositoryMock);
    }

    @Test
    public void checkAuthA_ShouldReturnBadRequest() throws Exception
    {
        Admin admin = TestUtil.produceAdmin();

        when(adminRepositoryMock.findAdminByLoginAndPassword(admin.getLogin(), admin.getPassword()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin?login={login}&password={password}", admin.getLogin(), admin.getPassword()))
                .andExpect(status().isBadRequest());

        verify(adminRepositoryMock, times(1)).findAdminByLoginAndPassword(admin.getLogin(), admin.getPassword());
        verifyNoMoreInteractions(adminRepositoryMock);
    }

    @Test
    public void deleteAdmin_ShouldReturnNoContent() throws Exception
    {
        Admin admin = TestUtil.produceAdmin();

        mockMvc.perform(delete("/admin?id={id}", admin.getId()))
                .andExpect(status().isNoContent());

        verify(adminRepositoryMock, times(1)).delete(admin.getId());
        verifyNoMoreInteractions(adminRepositoryMock);
    }


    @Test
    public void updateAdmin_ShouldReturnOk() throws Exception {
        Admin admin = TestUtil.produceAdmin();

        when(adminRepositoryMock.findOne(admin.getId())).thenReturn(new Admin());
        when(adminRepositoryMock.save(admin)).thenReturn(admin);

        mockMvc.perform(put("/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(admin.getId())))
                .andExpect(jsonPath("$.login", is(admin.getLogin())))
                .andExpect(jsonPath("$.password", is(admin.getPassword())));


        ArgumentCaptor<Admin> argument = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepositoryMock, times(1)).findOne(admin.getId());
        verify(adminRepositoryMock, times(1)).save(argument.capture());
        verifyNoMoreInteractions(adminRepositoryMock);
    }

    @Test
    public void updateAdmin_ShouldReturnConflict() throws Exception
    {
        Admin admin = TestUtil.produceAdmin();

        when(adminRepositoryMock.findOne(admin.getId())).thenReturn(admin);

        mockMvc.perform(put("/admin")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(admin)))
                .andExpect(status().isConflict());

        verify(adminRepositoryMock, times(1)).findOne(admin.getId());
        verifyNoMoreInteractions(adminRepositoryMock);
    }
}