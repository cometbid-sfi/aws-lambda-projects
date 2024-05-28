package org.cometbid.sample.template.payroll.test;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.sample.template.payroll.it.EmployeeTestBuilder;
import org.cometbid.sample.template.payroll.employee.CreateEmployeeRequest;
import org.cometbid.sample.template.payroll.employee.Employee;
import org.cometbid.sample.template.payroll.employee.EmployeeBuilder;
import org.cometbid.sample.template.payroll.employee.EmployeeMapper;
import org.cometbid.sample.template.payroll.employee.EmployeeNameDTO;
import org.cometbid.sample.template.payroll.employee.EmployeeRepository;
import org.cometbid.sample.template.payroll.employee.EmployeeService;
import org.cometbid.sample.template.payroll.employee.EmployeeServiceImpl;
import org.cometbid.sample.template.payroll.employee.EmployeeTypeDTO;
import org.cometbid.sample.template.payroll.employee.UpdEmployeeRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 *
 * @author samueladebowale
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PayrollFunctionApplicationTests {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeBuilder employeeBuilder;

    private EmployeeMapper employeeMapper;

    private EmployeeService employeeService;

    @BeforeEach
    void init() {
        this.employeeBuilder = Mappers.getMapper(EmployeeBuilder.class);
        this.employeeMapper = Mappers.getMapper(EmployeeMapper.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository, employeeBuilder, employeeMapper);
    }

    @DisplayName("create employee")
    @Test
    void testSaveEmployee() {
        CreateEmployeeRequest emplDto = EmployeeTestBuilder.employee().buildDto();

        Employee mapped = employeeBuilder.toEmployeeEntity(emplDto);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(mapped);

        Employee employee = employeeService.saveEmployee(emplDto);
        assertEquals(emplDto.getEmail(), employee.getEmail());
        assertEquals(emplDto.getFirstName(), employee.getFirstName());
        assertEquals(emplDto.getLastName(), employee.getLastName());
        assertEquals(emplDto.getMiddleName(), employee.getMiddleName());
        assertEquals(emplDto.getSalary(), employee.getSalary());
        assertEquals(emplDto.getEmployeeType(), employee.getEmpType());
        assertNotNull(employee.getCreationDate());

        // =====================================================
        emplDto = EmployeeTestBuilder.employee().buildDto();

        employee = employeeService.saveEmployee(emplDto);
        assertNotNull(employee);
        assertNotNull(employee.getId());
        assertNotNull(employee.getCreationDate());
        assertTrue(StringUtils.isNotBlank(employee.getEmployeeId()));
    }

    @DisplayName("update employee")
    @Test
    void testUpdateEmployee() {
        CreateEmployeeRequest emplDto = EmployeeTestBuilder.employee().buildDto();

        Employee employee = employeeBuilder.toEmployeeEntity(emplDto);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        //Employee employee = employeeBuilder.updateEmployee(emplUpdDto);
        when(employeeRepository.findByEmployeeIdIgnoreCase(anyString()))
                .thenReturn(Optional.of(employee));
        // ================ Update begins ================
        UpdEmployeeRequest emplUpdDto = EmployeeTestBuilder.employee().buildUpdDto(employee);
        String employeeId = EmployeeTestBuilder.genEmpId();

        Employee updatedEmployee = employeeService.updateEmployee(emplUpdDto, employeeId);
        assertNotNull(updatedEmployee);
        assertEquals(employee, updatedEmployee);
        assertNotNull(updatedEmployee.getId());
        assertNotNull(updatedEmployee.getCreationDate());
        assertTrue(StringUtils.isNotBlank(updatedEmployee.getEmployeeId()));

        assertEquals(emplUpdDto.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(emplUpdDto.getLastName(), updatedEmployee.getLastName());
        assertEquals(emplUpdDto.getMiddleName(), updatedEmployee.getMiddleName());
        assertEquals(emplUpdDto.getSalary(), updatedEmployee.getSalary());
        assertEquals(emplUpdDto.getEmployeeType(), updatedEmployee.getEmpType());

        assertEquals(employee.getId(), updatedEmployee.getId());
        assertEquals(employee.getEmail(), updatedEmployee.getEmail());
    }

    @DisplayName("update employee name")
    @Test
    void testUpdateEmployeeName() {
        // ================= Create begins ==================
        CreateEmployeeRequest emplDto = EmployeeTestBuilder.employee().buildDto();

        Employee employee = employeeBuilder.toEmployeeEntity(emplDto);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        //Employee employee = employeeBuilder.updateEmployee(emplUpdDto);
        when(employeeRepository.findByEmployeeIdIgnoreCase(anyString()))
                .thenReturn(Optional.of(employee));

        // ================ Update begins ===================
        EmployeeNameDTO emplUpdDto = EmployeeTestBuilder.employee().buildEmpNameUpdDto(employee);
        String employeeId = EmployeeTestBuilder.genEmpId();

        Employee updatedEmployee = employeeService.updateEmployeeName(emplUpdDto, employeeId);
        assertNotNull(updatedEmployee);
        assertEquals(employee, updatedEmployee);
        assertNotNull(updatedEmployee.getId());
        assertNotNull(updatedEmployee.getCreationDate());
        assertTrue(StringUtils.isNotBlank(updatedEmployee.getEmployeeId()));

        assertEquals(emplUpdDto.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(emplUpdDto.getLastName(), updatedEmployee.getLastName());
        assertEquals(emplUpdDto.getMiddleName(), updatedEmployee.getMiddleName());

        assertNotNull(updatedEmployee.getCreationDate());

        assertEquals(employee.getId(), updatedEmployee.getId());
        assertEquals(employee.getCreationDate(), updatedEmployee.getCreationDate());
        assertEquals(employee.getEmployeeId(), updatedEmployee.getEmployeeId());
        assertEquals(employee.getEmail(), updatedEmployee.getEmail());
    }

    @DisplayName("update employee type")
    @Test
    void testUpdateEmployeeType() {
        // ================= Create begins ================
        CreateEmployeeRequest emplDto = EmployeeTestBuilder.employee().buildDto();

        Employee employee = employeeBuilder.toEmployeeEntity(emplDto);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        //Employee employee = employeeBuilder.updateEmployee(emplUpdDto);
        when(employeeRepository.findByEmployeeIdIgnoreCase(anyString()))
                .thenReturn(Optional.of(employee));

        // ================ Update begins ================
        EmployeeTypeDTO emplUpdDto = EmployeeTestBuilder.employee().buildEmpTypeDto(employee);
        String employeeId = EmployeeTestBuilder.genEmpId();

        Employee updatedEmployee = employeeService.updateEmployeeType(emplUpdDto, employeeId);
        assertNotNull(updatedEmployee);
        assertEquals(employee, updatedEmployee);
        assertNotNull(updatedEmployee.getId());
        assertNotNull(updatedEmployee.getCreationDate());
        assertTrue(StringUtils.isNotBlank(updatedEmployee.getEmployeeId()));

        assertEquals(emplUpdDto.getSalary(), updatedEmployee.getSalary());
        assertEquals(emplUpdDto.getEmployeeType(), updatedEmployee.getEmpType());

        assertEquals(employee.getId(), updatedEmployee.getId());
        assertEquals(employee.getCreationDate(), updatedEmployee.getCreationDate());
        assertEquals(employee.getEmployeeId(), updatedEmployee.getEmployeeId());
        assertEquals(employee.getEmail(), updatedEmployee.getEmail());
    }
}
