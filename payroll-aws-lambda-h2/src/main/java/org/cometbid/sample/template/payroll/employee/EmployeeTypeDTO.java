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

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import javax.money.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.cometbid.component.api.validators.VerifyEnumValue;
import static org.cometbid.sample.template.payroll.employee.Employee.EMPLOYEE_TYPE;
import static org.cometbid.sample.template.payroll.employee.Employee.SALARY;

/**
 *
 * @author samueladebowale
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTypeDTO {

    @SerializedName(EMPLOYEE_TYPE)
    @JsonProperty(EMPLOYEE_TYPE)
    @VerifyEnumValue(EmployeeType.class)
    private EmployeeType employeeType;

    @SerializedName(SALARY)
    @JsonProperty(SALARY)
    private MonetaryAmount salary;
    
}
