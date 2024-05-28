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
package org.cometbid.sample.template.payroll.employee;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.component.api.employee.exceptions.EmployeeAlreadyExistException;
import org.cometbid.component.api.employee.exceptions.EmployeeNotFoundException;
import org.cometbid.component.api.generic.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBuilder employeeBuilder;
    private final EmployeeMapper employeeMapper;

    /**
     *
     * @param requestDto
     * @return
     */
    @Override
    public Employee saveEmployee(CreateEmployeeRequest requestDto) {
        log.info("Create the employee {}", requestDto);

        String email = requestDto.getEmail();

        if (employeeRepository.existsByEmail(email)) {
            throw new EmployeeAlreadyExistException(new Object[]{"email: " + email});
        }

        log.info("Before Employee Create: ");
        Employee updatedEmployee = employeeBuilder.toEmployeeEntity(requestDto);
        log.info("After Employee Create: " + updatedEmployee);

        return employeeRepository.save(updatedEmployee);
    }

    /**
     *
     * @param requestDto
     * @param employeeId
     * @return
     */
    @Override
    public Employee updateEmployee(final UpdEmployeeRequest requestDto, final String employeeId) {
        log.info("Update the employee {}", requestDto);

        if (StringUtils.isNotBlank(employeeId)) {

            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeIdIgnoreCase(employeeId);

            if (employeeOpt.isPresent()) {
                Employee updatedEmployee = employeeBuilder.updateEmployee(employeeOpt.get(), requestDto);

                log.info("Employee Update: " + updatedEmployee);
                return employeeRepository.save(updatedEmployee);
            }
            throw new EmployeeNotFoundException();
        }
        throw new BadRequestException("emp.notSpecified", new Object[]{employeeId});
    }

    /**
     *
     * @param employeeDto
     * @param employeeId
     * @return
     */
    @Override
    public Employee updateEmployeeName(final EmployeeNameDTO employeeDto, final String employeeId) {
        log.info("Update employee name {}", employeeDto);

        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeIdIgnoreCase(employeeId);

        if (employeeOpt.isPresent()) {
            log.info("Employee is present {}", employeeDto != null);
            Employee updatedEmployee = employeeMapper.updateEmployeeName(employeeOpt.get(), employeeDto);

            log.info("Update Employee is present {}", updatedEmployee);
            return employeeRepository.save(updatedEmployee);
        }

        throw new EmployeeNotFoundException();
    }

    /**
     *
     * @param employeeDto
     * @param employeeId
     * @return
     */
    @Override
    public Employee updateEmployeeType(final EmployeeTypeDTO employeeDto, final String employeeId) {
        log.info("Update employee type {}", employeeDto);

        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeIdIgnoreCase(employeeId);

        if (employeeOpt.isPresent()) {
            log.info("Employee is present {}", employeeDto != null);
            Employee updatedEmployee = employeeMapper.updateEmployeeType(employeeOpt.get(), employeeDto);

            log.info("Update Employee is present {}", updatedEmployee);
            return employeeRepository.save(updatedEmployee);
        }

        throw new EmployeeNotFoundException();
    }

    /**
     *
     * @param employee
     */
    @Override
    public void deleteEmployee(final Employee employee) {

        employeeRepository.delete(employee);
    }
}
