package com.cms;

import com.cms.entity.Category;
import com.cms.entity.Role;
import com.cms.entity.StatusUser;
import com.cms.entity.Users;
import com.cms.repository.CategoryRepository;
import com.cms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises every public controller URL with the roles expected by {@link com.cms.security.SecurityConfig}.
 * Status may be 404 when no row exists; must never be 401 for authenticated allowed roles or 500 for valid requests.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AllApiEndpointsTest {

    private static final String USER_EMAIL = "u1@test.local";
    private static final String STAFF_EMAIL = "s1@test.local";
    private static final String ADMIN_EMAIL = "a1@test.local";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private int testCategoryId;

    @BeforeEach
    void seedFixtures() {
        persistUserIfMissing(USER_EMAIL, "Api Test User", Role.USER);
        persistUserIfMissing(STAFF_EMAIL, "Api Test Staff", Role.STAFF);
        persistUserIfMissing(ADMIN_EMAIL, "Api Test Admin", Role.ADMIN);

        testCategoryId = categoryRepository.findByName("ApiTestCategory")
                .map(Category::getId)
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName("ApiTestCategory");
                    c.setDescription("fixtures");
                    return categoryRepository.save(c).getId();
                });
    }

    private void persistUserIfMissing(String email, String name, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            Users u = new Users();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode("TestPassword1"));
            u.setPhone("1234567890");
            u.setRole(role);
            u.setStatusUser(StatusUser.ACTIVE);
            userRepository.save(u);
        }
    }

    private static void assertOkOrNotFoundOrBadRequest(int sc) {
        if (sc != 200 && sc != 404 && sc != 400) {
            throw new AssertionError("Expected 200, 404, or 400, got " + sc);
        }
    }

    private static void assertCreatedOrClientError(int sc) {
        if (sc != 201 && sc != 400 && sc != 409) {
            throw new AssertionError("Expected 201, 400, or 409, got " + sc);
        }
    }

    @Nested
    class CorsAndAuth {

        @Test
        void options_anyUrl_ok() throws Exception {
            mockMvc.perform(options("/api/categories")).andExpect(status().isOk());
        }

        @Test
        void registerUser_withoutAuth() throws Exception {
            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Reg User\",\"email\":\"reg.unique" + System.nanoTime()
                                    + "@test.local\",\"password\":\"secret12\",\"phone\":\"9876543210\",\"role\":\"USER\"}"))
                    .andExpect(result -> assertCreatedOrClientError(result.getResponse().getStatus()));
        }

        @Test
        void categories_list_unauthorizedWithoutAuth() throws Exception {
            mockMvc.perform(get("/api/categories")).andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class Categories {

        @Test
        void get_all_name_byId_asUser() throws Exception {
            mockMvc.perform(get("/api/categories").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/categories/" + testCategoryId).with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/categories/name/ApiTestCategory").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
        }

        @Test
        void post_put_staff_delete_admin() throws Exception {
            String body = "{\"name\":\"Cat" + System.nanoTime() + "\",\"description\":\"d\"}";
            mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content(body)
                            .with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(result -> assertCreatedOrClientError(result.getResponse().getStatus()));

            mockMvc.perform(put("/api/categories/" + testCategoryId).contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"ApiTestCategory\",\"description\":\"updated\"}"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(put("/api/categories/" + testCategoryId).contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"ApiTestCategory\",\"description\":\"updated\"}")
                            .with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());

            mockMvc.perform(delete("/api/categories/999999").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isForbidden());

            mockMvc.perform(delete("/api/categories/999999").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }
    }

    @Nested
    class UsersApi {

        @Test
        void staff_listUsers() throws Exception {
            mockMvc.perform(get("/api/users").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());
        }

        @Test
        void user_forbidden_listAllUsers() throws Exception {
            mockMvc.perform(get("/api/users").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void get_byId_email_asUser() throws Exception {
            mockMvc.perform(get("/api/users/1").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
            mockMvc.perform(get("/api/users/email/" + USER_EMAIL).with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
        }

        @Test
        void admin_getByRole_patchStatus() throws Exception {
            mockMvc.perform(get("/api/users/role/USER").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(status().isOk());
            mockMvc.perform(patch("/api/users/1/status").param("status", "ACTIVE")
                            .with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }

        @Test
        void user_put_self() throws Exception {
            String body = "{\"name\":\"Updated Name\",\"email\":\"" + USER_EMAIL
                    + "\",\"password\":\"secret12\",\"phone\":\"1234567890\",\"role\":\"USER\"}";
            mockMvc.perform(put("/api/users/999999").contentType(MediaType.APPLICATION_JSON).content(body)
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }

        @Test
        void admin_deleteUser_user_forbidden() throws Exception {
            mockMvc.perform(delete("/api/users/999999").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isForbidden());
            mockMvc.perform(delete("/api/users/999999").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }
    }

    @Nested
    class ComplaintsApi {

        @Test
        void staff_listAll_user_forbidden() throws Exception {
            mockMvc.perform(get("/api/complaints").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/complaints").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void staff_status_user_assigned_filters() throws Exception {
            mockMvc.perform(get("/api/complaints/status/OPEN").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/complaints/user/1").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/complaints/assigned/1").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isOk());
        }

        @Test
        void user_getById_staff_patchStatus() throws Exception {
            mockMvc.perform(get("/api/complaints/1").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));

            mockMvc.perform(patch("/api/complaints/1/status").param("status", "OPEN")
                            .with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));

            mockMvc.perform(put("/api/complaints/1/status").param("status", "IN_PROGRESS")
                            .with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }

        @Test
        void user_create_complaint() throws Exception {
            String json = String.format(
                    "{\"title\":\"T\",\"description\":\"D\",\"categoryId\":%d,\"priority\":\"MEDIUM\"}",
                    testCategoryId);
            mockMvc.perform(post("/api/complaints").contentType(MediaType.APPLICATION_JSON).content(json)
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> {
                        int sc = result.getResponse().getStatus();
                        if (sc != 201 && sc != 400) {
                            throw new AssertionError("Expected 201 or 400, got " + sc);
                        }
                    });
        }

        @Test
        void user_put_delete_complaint() throws Exception {
            mockMvc.perform(put("/api/complaints/999999").contentType(MediaType.APPLICATION_JSON).content("{}")
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
            mockMvc.perform(delete("/api/complaints/999999").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }
    }

    @Nested
    class CommentsApi {

        @Test
        void get_and_post() throws Exception {
            mockMvc.perform(get("/api/comments/1").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
            mockMvc.perform(post("/api/comments/999999").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"message\":\"hi\"}")
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }
    }

    @Nested
    class AttachmentsApi {

        @Test
        void get_and_post() throws Exception {
            mockMvc.perform(get("/api/attachments/1").with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk());
            mockMvc.perform(post("/api/attachments/999999").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"fileName\":\"f.txt\",\"filePath\":\"/tmp/f\"}")
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }
    }

    @Nested
    class AdminApi {

        @Test
        void admin_users_complaints_status_delete() throws Exception {
            mockMvc.perform(get("/api/admin/users").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/admin/complaints").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(status().isOk());
            mockMvc.perform(put("/api/admin/complaints/1/status").param("status", "OPEN")
                            .with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
            mockMvc.perform(delete("/api/admin/users/999999").with(user(ADMIN_EMAIL).roles("ADMIN")))
                    .andExpect(result -> assertOkOrNotFoundOrBadRequest(result.getResponse().getStatus()));
        }

        @Test
        void staff_forbidden_admin() throws Exception {
            mockMvc.perform(get("/api/admin/users").with(user(STAFF_EMAIL).roles("STAFF")))
                    .andExpect(status().isForbidden());
        }
    }
}
