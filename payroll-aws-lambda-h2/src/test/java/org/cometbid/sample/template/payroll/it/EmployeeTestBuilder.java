/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.sample.template.payroll.it;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.cometbid.sample.template.payroll.employee.CreateEmployeeRequest;
import org.cometbid.sample.template.payroll.employee.Employee;
import org.cometbid.sample.template.payroll.employee.EmployeeNameDTO;
import org.cometbid.sample.template.payroll.employee.EmployeeType;
import org.cometbid.sample.template.payroll.employee.EmployeeTypeDTO;
import org.cometbid.sample.template.payroll.employee.UpdEmployeeRequest;
import org.javamoney.moneta.Money;

/**
 *
 * @author samueladebowale
 */
public class EmployeeTestBuilder {

    private final Faker faker;
    private final CurrencyUnit USD = Monetary.getCurrency("USD");

    private Long id;
    // private String username;
    private String email = "john.doe@example.com";
    private String employeeId;
    private String firstName = "John";
    private String lastName = "Doe";
    private String middleName = "Michael";
    private MonetaryAmount salary;

    private EmployeeTestBuilder() {
        faker = new Faker();

        this.id = faker.random().nextLong();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.middleName = faker.name().firstName();
        this.email = faker.internet().emailAddress();
        this.salary = Money.of(faker.number().randomDigit(), faker.currency().code());
    }

    public static EmployeeTestBuilder employee() {
        return new EmployeeTestBuilder();
    }

    public EmployeeTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EmployeeTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public EmployeeTestBuilder withSalary(MonetaryAmount salary) {
        this.salary = salary;
        return this;
    }

    public EmployeeTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeTestBuilder withMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public Employee build() {

        return Employee.builder()
                //.id(id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .middleName(this.middleName)
                .salary(this.salary)
                .email(this.email)
                .empType(getEmpType())
                .build();
    }

    public CreateEmployeeRequest buildDto() {

        return CreateEmployeeRequest.builder()
                //.id(id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .middleName(this.middleName)
                .salary(this.salary)
                .email(this.email)
                .build();
    }

    public UpdEmployeeRequest buildUpdDto() {

        return UpdEmployeeRequest.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .middleName(this.middleName)
                .salary(this.salary)
                .employeeType(getEmpType())
                .build();
    }

    public UpdEmployeeRequest buildUpdDto(Employee emp) {

        return UpdEmployeeRequest.builder()
                .firstName(emp.getFirstName())
                .lastName(emp.getLastName())
                .middleName(emp.getMiddleName())
                .salary(emp.getSalary())
                .employeeType(emp.getEmpType())
                .build();
    }

    public EmployeeNameDTO buildEmpNameUpdDto() {

        return EmployeeNameDTO.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .middleName(this.middleName)
                .build();
    }

    public EmployeeNameDTO buildEmpNameUpdDto(Employee emp) {

        return EmployeeNameDTO.builder()
                .firstName(emp.getFirstName())
                .lastName(emp.getLastName())
                .middleName(emp.getMiddleName())
                .build();
    }

    public EmployeeTypeDTO buildEmpTypeDto() {

        return EmployeeTypeDTO.builder()
                .salary(this.salary)
                .employeeType(getEmpType())
                .build();
    }

    public EmployeeTypeDTO buildEmpTypeDto(Employee emp) {

        return EmployeeTypeDTO.builder()
                .salary(emp.getSalary())
                .employeeType(emp.getEmpType())
                .build();
    }

    public EmployeeTestBuilder withEmpId() {
        this.employeeId = genEmpId();
        return this;
    }

    public static String genEmpId() {
        Faker faker = Faker.instance();
        String ite = Faker.instance().number().digits(4);
        return faker.letterify("?????" + ite, true);
    }

    public static EmployeeType getEmpType() {
        List<EmployeeType> employeeList = new ArrayList<>(EmployeeType.getAllTypes());

        return employeeList.get(Faker.instance().random().nextInt(0, employeeList.size() - 1));
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        IntStream.range(0, 20)
                .forEach(i -> {
                    Employee employee = new EmployeeTestBuilder().build();
                    employees.add(employee);
                });

        return employees;
    }

    private static final List<String> PAGE_1_CONTENTS = Arrays.asList("Ali", "Brian", "Coddy", "Di", "Eve");

    private static final List<String> PAGE_2_CONTENTS = Arrays.asList("Fin", "Grace", "Harry", "Ivan", "Judy");

    private static final List<String> PAGE_3_CONTENTS = Arrays.asList("Kasim", "Liam", "Mike", "Nick", "Omar");

    private static final List<String> PAGE_4_CONTENTS = Arrays.asList("Penny", "Queen", "Rob", "Sue", "Tammy");

    private static final List<String> EMPTY_PAGE = Collections.emptyList();

    private static Collection<Object[]> testIO() {
        return Arrays.asList(new Object[][]{
            {0, 5, PAGE_1_CONTENTS, 20L, 4L},
            {1, 5, PAGE_2_CONTENTS, 20L, 4L},
            {2, 5, PAGE_3_CONTENTS, 20L, 4L},
            {3, 5, PAGE_4_CONTENTS, 20L, 4L},
            {4, 5, EMPTY_PAGE, 20L, 4L}}
        );
    }

}
