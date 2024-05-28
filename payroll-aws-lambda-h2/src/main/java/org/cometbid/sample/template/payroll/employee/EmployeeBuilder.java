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

import io.micrometer.common.util.StringUtils;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.OffsetDateTime;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.api.util.RandomUtil;
import org.cometbid.component.api.util.TimeZoneUtils;
import static org.cometbid.sample.template.payroll.config.LocalizationFactory.getContextLocale;
import org.javamoney.moneta.Money;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public abstract class EmployeeBuilder {

    @Mapping(target = "empType", source = "employeeType")
    public abstract Employee toEmployeeEntity(CreateEmployeeRequest toCreate);

    @Mapping(source = "toUpdate.firstName", target = "firstName")
    @Mapping(source = "toUpdate.lastName", target = "lastName")
    @Mapping(source = "toUpdate.middleName", target = "middleName")
    @Mapping(source = "toUpdate.salary", target = "salary")
    @Mapping(source = "toUpdate.employeeType", target = "empType")
    public abstract Employee updateEmployee(Employee employee, UpdEmployeeRequest toUpdate);

    static String generateEmployeeIdBy(Employee emp) {

        String firstName = emp.getFirstName();
        String lastName = emp.getLastName();
        String middleName = emp.getMiddleName();

        String res1 = firstName.length() > 2 ? firstName.substring(0, 2) : "";
        String res2 = StringUtils.isBlank(middleName) ? "" : middleName.substring(0, 1);
        String res3 = lastName.length() > 2 ? lastName.substring(0, 2) : "";
        String res4 = res1 + res2 + res3;

        final String digits = RandomUtil.getRandomDigitsOnly(4);

        String finalResult = res4 + digits;
        return finalResult.toUpperCase();
    }

    /*
    @BeforeMapping
    protected void enrichUpdEmployeeType(UpdEmployeeRequest dto, @MappingTarget Employee employee) {
        //String employeeType = dto.getEmployeeType();
        EmployeeType empType = dto.getEmployeeType(); //EmployeeType.fromString(employeeType);

        log.info("Employee type@BeforeMapping: " + empType);
        employee.setEmpType(empType);
    }

    @BeforeMapping
    protected void enrichDTOWithEmployeeType(CreateEmployeeRequest dto, @MappingTarget Employee employee) {
        //String employeeType = dto.getEmployeeType();
        EmployeeType empType = dto.getEmployeeType(); //EmployeeType.fromString(employeeType);

        log.info("Employee type@BeforeMapping: " + empType);
        employee.setEmpType(empType);
    }
   */

    @AfterMapping
    protected void convertToEntity(@MappingTarget Employee employee) {
        employee.setEmployeeId(generateEmployeeIdBy(employee));
        employee.setId();

        OffsetDateTime instZonedTime = TimeZoneUtils.getOffsetDateTimeInUTC();
        employee.setCreationDate(instZonedTime);

        log.info("Employee type@AfterMapping: " + employee);
    }

    public String formatCurrency(BigDecimal amount) {
        MonetaryAmount monetaryAmount = Money.of(12345.67, "EUR");
        MonetaryAmountFormat formatAmount = MonetaryFormats.getAmountFormat(getContextLocale());
        return formatAmount.format(monetaryAmount);
    }

    public void formatCurrency(String currencyString) {
        try {
            NumberFormat localizedCurrencyFormat = NumberFormat.getCurrencyInstance(getContextLocale());
            Number parsedNumber = localizedCurrencyFormat.parse(currencyString);
            double parsedAmount = parsedNumber.doubleValue();
            log.info(parsedAmount);  // Output: 1234.57
        } catch (ParseException e) {
            // e.printStackTrace();
            log.error("Error occurred parsing Number");
        }
    }
}
