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

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.api.employee.exceptions.EmployeeNotFoundException;
import org.cometbid.component.ut.jpa.pagination.SimplePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeFinderServiceImpl implements EmployeeFinderService {

    private final EmployeeRepository employeeRepository;

    /**
     *
     * @param employeeId
     * @return
     */
    @Override
    public Employee findByEmpId(String employeeId) {
        log.info("Getting employee with id {}", employeeId);

        return employeeRepository.findByEmployeeIdIgnoreCase(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(
                "employee.notfound.byEmpId", new Object[]{employeeId}));
    }

    /**
     *
     * @param pageable
     * @return
     */
    @Override
    public SimplePage<Employee> findAll(Pageable pageable) {

        Page<Employee> page = employeeRepository.findAll(pageable);

        return new SimplePage<>(page.getContent()
                .stream()
                .map(m -> m)
                //.map(employeeMapper::mapToEmployeeDTO)
                .collect(Collectors.toList()),
                pageable, page.getTotalElements());
    }

    /**
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Employee> getEmployees(int page, int size) {
        Pageable pageRequest = createPageRequestUsing(page, size);

        List<Employee> allEmployees = employeeRepository.findAll();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), allEmployees.size());

        List<Employee> pageContent = allEmployees.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, allEmployees.size());
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }

}
