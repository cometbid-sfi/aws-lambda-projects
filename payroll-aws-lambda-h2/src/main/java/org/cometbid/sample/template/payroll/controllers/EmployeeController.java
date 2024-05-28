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
package org.cometbid.sample.template.payroll.controllers;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.ut.jpa.pagination.SimplePage;
import org.cometbid.sample.template.payroll.employee.CreateEmployeeRequest;
import org.cometbid.sample.template.payroll.employee.Employee;
import org.cometbid.sample.template.payroll.employee.EmployeeFinderService;
import org.cometbid.sample.template.payroll.employee.EmployeeNameDTO;
import org.cometbid.sample.template.payroll.employee.EmployeeRepository;
import org.cometbid.sample.template.payroll.employee.EmployeeService;
import org.cometbid.sample.template.payroll.employee.EmployeeTypeDTO;
import org.cometbid.sample.template.payroll.employee.UpdEmployeeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.cometbid.sample.template.payroll.employee.EmployeePagingUtil;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeFinderService employeeFinderService;
    private final EmployeeRepository employeeRepository;

    /**
     *
     * @return
     */
    @GetMapping("count")
    public ResponseEntity<String> countEmployees() {

        String response = "All employee Count: " + employeeRepository.count();

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }

    /**
     *
     * @return
     */
    @PostMapping("search")
    public ResponseEntity<String> searchEmployees() {
        String response = "To be implemented soon...";

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }

    /**
     *
     * @param qparams
     * @return
     */
    @GetMapping
    public ResponseEntity<SimplePage<Employee>> allEmployees(@RequestParam(required = false) Map<String, String> qparams) {
        log.info("Request params: {}", qparams);

        Pageable pageable = EmployeePagingUtil.preparePageRequest(qparams);

        SimplePage<Employee> pagedEmployees = employeeFinderService.findAll(pageable);
        
        log.info("Total elements {}", pagedEmployees.getTotalElements()); 

        if (pagedEmployees.getTotalElements() == 0) {
            log.info("Status code should be 404"); 
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND) 
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(pagedEmployees);
        }

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(pagedEmployees);
    }

    /**
     *
     * @param employeeId
     * @return
     */
    @GetMapping("/{empId}")
    public ResponseEntity<Employee> searchEmployeesById(@PathVariable("empId") String employeeId) {
        Employee employee = this.employeeFinderService.findByEmpId(employeeId);

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(employee);
    }

    /**
     *
     * @param employeeDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody CreateEmployeeRequest employeeDto) {
        Employee employee = this.employeeService.saveEmployee(employeeDto);

        URI resourceLoc = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{empId}")
                .buildAndExpand(employee.getEmployeeId()).toUri();

        return ResponseEntity.created(resourceLoc)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(employee);
    }

    /**
     *
     * @param employeeId
     * @param employeeDto
     * @return
     */
    @PutMapping(path = "/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("empId") String employeeId,
            @Valid @RequestBody UpdEmployeeRequest employeeDto) {
        Employee employee = this.employeeService.updateEmployee(employeeDto, employeeId);

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(employee);
    }

    /**
     *
     * @param employeeId
     * @param employeeDto
     * @return
     */
    @PatchMapping(path = "/{empId}/name")
    public ResponseEntity<String> updateEmployeeName(@PathVariable("empId") String employeeId,
            @Valid @RequestBody EmployeeNameDTO employeeDto) {
        this.employeeService.updateEmployeeName(employeeDto, employeeId);

        String responseMessage = "Successfully updated Employee name to: " + employeeDto;

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage);
    }

    /**
     *
     * @param employeeId
     * @param employeeDto
     * @return
     */
    @PatchMapping(path = "/{empId}/type")
    public ResponseEntity<String> updateEmployeeType(@PathVariable("empId") String employeeId,
            @Valid @RequestBody EmployeeTypeDTO employeeDto) {
        this.employeeService.updateEmployeeType(employeeDto, employeeId);

        String responseMessage = "Successfully updated Employee type to: " + employeeDto;

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage);
    }

    /**
     *
     * @return
     */
    @GetMapping("all-fields")
    public ResponseEntity<Collection<String>> allFields() {
        Collection<String> coll = Employee.getAllMappedFields();

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(coll);
    }

    /**
     * 
     * @return 
     */
    @GetMapping("mandatory-fields")
    public ResponseEntity<Collection<String>> defaultFields() {
        Collection<String> coll = Employee.getMappedDefaultFields();

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(coll);
    }

    /**
     *
     * @return
     */
    @GetMapping("count-mandatory-fields")
    public ResponseEntity<String> countFields() {
        String responseMessage = "Mandatory fields Count: " + Employee.getMappedDefaultFields().size();

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage);
    }

    /**
     *
     * @return
     */
    @GetMapping("count-all-fields")
    public ResponseEntity<String> countAllFields() {
        String responseMessage = "All fields Count: " + Employee.getAllMappedFields().size();

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage);
    }

    /**
     *
     * @param employeeId
     * @return
     */
    @DeleteMapping("/{empId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("empId") String employeeId) {
        Employee employee = this.employeeFinderService.findByEmpId(employeeId);

        this.employeeService.deleteEmployee(employee);

        String responseMessage = "Successfully deleted Employee with id: " + employeeId;

        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage);
    }

}
