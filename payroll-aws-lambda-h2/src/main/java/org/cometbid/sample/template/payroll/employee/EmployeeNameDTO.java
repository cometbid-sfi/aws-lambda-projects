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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.cometbid.sample.template.payroll.employee.Employee.FIRST_NAME;
import static org.cometbid.sample.template.payroll.employee.Employee.LAST_NAME;
import static org.cometbid.sample.template.payroll.employee.Employee.MIDDLE_NAME;

/**
 *
 * @author samueladebowale
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeNameDTO {
   
    @SerializedName(FIRST_NAME)
    @JsonProperty(FIRST_NAME)
    @Size(max = 100, message = "{FirstName.size}")
    @NotBlank(message = "{FirstName.notBlank}")
    private String firstName;

    @SerializedName(MIDDLE_NAME)
    @JsonProperty(MIDDLE_NAME)
    @Size(max = 100, message = "{MiddleName.size}")
    private String middleName;

    @SerializedName(LAST_NAME)
    @JsonProperty(LAST_NAME)
    @Size(max = 100, message = "{LastName.size}")
    @NotBlank(message = "{LastName.notBlank}")
    private String lastName;

}
