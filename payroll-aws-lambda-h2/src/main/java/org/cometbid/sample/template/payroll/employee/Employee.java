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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.money.MonetaryAmount;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.cometbid.component.api.validators.MonetaryAmountPositive;
import org.cometbid.component.api.validators.ValidEmail;
import org.cometbid.component.ut.jpa.AbstractEntity;
import org.cometbid.component.ut.jpa.InMemoryUniqueIdGenerator;
import org.cometbid.component.api.jackson.MoneySerializer;
import org.hibernate.annotations.CompositeType;
import org.springframework.data.domain.Sort;

/**
 *
 * @author samueladebowale
 */
@Data
@Entity(name = "Employee")
@Table(name = "employee")
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class Employee extends AbstractEntity<EmployeeId> {

    @SerializedName(FIRST_NAME)
    @JsonProperty(FIRST_NAME)
    @Size(max = 100, message = "{FirstName.size}")
    @NotBlank(message = "{FirstName.notBlank}")
    @Column(name = FIRSTNAME_COL)
    private String firstName;

    @SerializedName(MIDDLE_NAME)
    @JsonProperty(MIDDLE_NAME)
    @Size(max = 100, message = "{MiddleName.size}")
    @Column(name = MIDDLENAME_COL)
    private String middleName;

    @SerializedName(LAST_NAME)
    @JsonProperty(LAST_NAME)
    @Size(max = 100, message = "{LastName.size}")
    @NotBlank(message = "{LastName.notBlank}")
    @Column(name = LASTNAME_COL)
    private String lastName;

    @Basic
    @SerializedName(EMPLOYEE_ID)
    @JsonProperty(EMPLOYEE_ID)
    @Column(name = EMPLOYEE_ID_COL)
    private String employeeId;

    @SerializedName(EMAIL)
    @JsonProperty(EMAIL)
    @Column(name = EMAIL_COL)
    @Size(max = 100, message = "{User.email.size}")
    @NotBlank(message = "{User.email.notBlank}")
    @ValidEmail
    private String email;

    //@SerializedName(EMPLOYEE_TYPE)
    @JsonProperty(EMPLOYEE_TYPE)
    @Column(name = EMPLOYEE_TYPE_COL)
    //@Enumerated(EnumType.STRING)
    //@Convert(converter = EmployeeType.Converter.class)
    private EmployeeType empType;

    @SerializedName(SALARY)
    @JsonProperty(SALARY)
    @MonetaryAmountPositive
    @AttributeOverride(
            name = "amount",
            column = @Column(name = SALARY_AMT_COL)
    )
    @AttributeOverride(
            name = "currency",
            column = @Column(name = SALARY_CURR_COL)
    )
    @CompositeType(MonetaryAmountType.class)
    @JsonSerialize(using = MoneySerializer.class)
    private MonetaryAmount salary;

    @SerializedName(CREATION_DATETIME)
    @JsonProperty(CREATION_DATETIME)
    @Column(name = EMPLOYMENT_DATE_COL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a Z")
    private OffsetDateTime creationDate;

    /**
     *
     * @param id
     * @param firstName
     * @param middleName
     * @param lastName
     * @param email
     * @param salary
     */
    @Builder
    Employee(EmployeeId id, String firstName, String middleName,
            String lastName, String email, EmployeeType empType,
            MonetaryAmount salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        //this.employeeId = employeeId;
        this.empType = empType;
        this.email = email;
        this.salary = salary;
    }

    @Override
    public final EmployeeId getId() {
        return this.id;
    }

    public long getVersion() {
        return this.version;
    }

    // ================================================================================
    public static final String DEFAULT_SORTFIELD = "employeeId";
    public static final Sort DEFAULT_SORT = Sort.by(DEFAULT_SORTFIELD);

    // ================================================================================
    public static final Map<String, String> defaultFields = Collections.synchronizedMap(new HashMap<>());
    //public static final Map<String, String> optionalFieldsPropertyMapping = Collections.synchronizedMap(new HashMap<>());

    /**
     * *************************************************
     * ****** Json fields definition *******************
     * *************************************************
     */
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String EMPLOYEE_ID = "empId";
    public static final String EMPLOYEE_TYPE = "empType";
    public static final String EMAIL = "email";
    public static final String SALARY = "salary";
    public static final String CREATION_DATETIME = "employment_date";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";

    public static final String PERIOD_CHAR = ".";
    /**
     * *******************************************
     * ****** Column fields definition ***********
     * *******************************************
     */
    public static final String ID_COL = "ID";
    public static final String FIRSTNAME_COL = "FIRST_NAME";
    public static final String LASTNAME_COL = "LAST_NAME";
    public static final String MIDDLENAME_COL = "MIDDLE_NAME";
    public static final String EMPLOYEE_ID_COL = "EMPLOYEE_ID";
    public static final String EMPLOYEE_TYPE_COL = "EMPLOYEE_TYPE";
    public static final String EMAIL_COL = "EMAIL";
    public static final String SALARY_AMT_COL = "sal_amount";
    public static final String SALARY_CURR_COL = "sal_currency";
    public static final String EMPLOYMENT_DATE_COL = "creation_date";

    static {
        defaultFields.put(FIRST_NAME, FIRSTNAME_COL);
        defaultFields.put(LAST_NAME, LASTNAME_COL);
        defaultFields.put(MIDDLE_NAME, MIDDLENAME_COL);
        defaultFields.put(EMPLOYEE_ID, EMPLOYEE_ID_COL);
        defaultFields.put(EMPLOYEE_TYPE, EMPLOYEE_TYPE_COL);
        defaultFields.put(EMAIL, EMAIL_COL);
        defaultFields.put(SALARY + PERIOD_CHAR + AMOUNT, SALARY_AMT_COL);
        defaultFields.put(SALARY + PERIOD_CHAR + CURRENCY, SALARY_CURR_COL);
        defaultFields.put(CREATION_DATETIME, EMPLOYMENT_DATE_COL);
    }

    public static Optional<String> getMappedField(String jsonField) {
        return Optional.of(defaultFields.get(jsonField));
    }

    public static Collection<String> getMappedDefaultFields() {
        return defaultFields.keySet();
    }

    /*
    public static Collection<String> getMappedOtherFields() {
        return optionalFieldsPropertyMapping.keySet();
    }
     */
    public static Collection<String> getAllMappedFields() {
        Collection<String> fieldsColl = getMappedDefaultFields();
        // Collection<String> optionalFieldsColl = getMappedOtherFields();

        List<String> all = new ArrayList<>();
        all.addAll(fieldsColl);
        // all.addAll(optionalFieldsColl);

        return all;
    }

    @Override
    protected void setId() {
        Long empId = InMemoryUniqueIdGenerator.generateUniqueLongId();

        this.id = new EmployeeId(empId);
    }

}
