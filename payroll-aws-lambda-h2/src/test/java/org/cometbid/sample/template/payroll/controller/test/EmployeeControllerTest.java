/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.sample.template.payroll.controller.test;

import org.springframework.util.MultiValueMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cometbid.sample.template.payroll.controllers.EmployeeController;
import org.cometbid.sample.template.payroll.employee.EmployeeRepository;
import org.cometbid.sample.template.payroll.employee.EmployeeService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.util.LinkedMultiValueMap;

/**
 *
 * @author samueladebowale
 */
@AutoConfigureMockMvc
@ContextConfiguration(classes = {EmployeeController.class, EmployeeService.class})
@WebMvcTest
public class EmployeeControllerTest {

    private final static String TEST_EMP_ID = "emp-id-123";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeRepository employeeRepository;

    @Disabled
    public void testCountEmployees() throws Exception {
        get("/api/v1/employees/count", "All employee Count: " + 9, null);
    }

    @Disabled
    public void searchEmployees() throws Exception {
        post("/api/v1/employees/search", "To be implemented soon...");
    }

    @Disabled
    public void allEmployees() throws Exception {
        Map<String, List<String>> q = new HashMap<>();

        get("/api/v1/employees/search", null, q);
                /*
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode", is(200)))
                .andExpect(jsonPath("message.[0].username", is("paul_pop")));
                */
    }

    @Disabled
    public void searchById() throws Exception {
        get("/api/v1/employees/" + TEST_EMP_ID, null, null);
    }

    @Disabled
    public void addEmployee() throws Exception {
        post("/api/v1/employees", TEST_EMP_ID);
    }

    @Disabled
    public void updateEmployee() throws Exception {
        put("/api/v1/employees", TEST_EMP_ID);
    }

    @Disabled
    public void updateEmployeeName() throws Exception {
        patch("/api/v1/employees", TEST_EMP_ID);
    }

    @Disabled
    public void updateEmployeeType() throws Exception {
        patch("/api/v1/employees", TEST_EMP_ID);
    }

    @Disabled
    public void allFields() throws Exception {
        get("/api/v1/employees/all-fields", null, null);
    }

    @Disabled
    public void defaultFields() throws Exception {
        get("/api/v1/employees/mandatory-fields", null, null);
    }

    @Disabled
    public void countFields() throws Exception {
        get("/api/v1/employees/count-mandatory-fields", "Mandatory fields Count: " + 9, null);
    }

    @Disabled
    public void countAllFields() throws Exception {
        get("/api/v1/employees/count-all-fields", "All fields Count: " + 9, null);
    }

    @Disabled
    public void deleteEmployeeById() throws Exception {
        delete("/api/v1/employees/" + TEST_EMP_ID);
    }

    private void get(String uri, String expected, Map<String, List<String>> q) throws Exception {
        MultiValueMap<String, String> mulmap = new LinkedMultiValueMap<>(q);
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(uri)
                       .queryParams(mulmap) 
                        //.with(user(TEST_EMP_ID))
                        //.with(csrf())
                        //.content(birthday)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals(expected, resultDOW);
    }

    private void put(String uri, String expected) throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        //.with(user(TEST_EMP_ID))
                        //.with(csrf())
                        //.content(birthday)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals(expected, resultDOW);
    }

    private void post(String uri, String expected) throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        //.with(user(TEST_EMP_ID))
                        //.with(csrf())
                        //.content(birthday)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals(expected, resultDOW);
    }

    private void patch(String uri, String expected) throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.patch(uri)
                        //.with(user(TEST_EMP_ID))
                        //.with(csrf())
                        //.content(birthday)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);
        assertEquals(expected, resultDOW);
    }

    private void delete(String uri) throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete(uri)
                        //.with(user(TEST_EMP_ID))
                        //.with(csrf())
                        //.content(birthday)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultCZ = result.getResponse().getContentAsString();
        assertNotNull(resultCZ);
        assertTrue(resultCZ.startsWith("Successfully deleted Employee with id: "));
    }

}
