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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.component.ut.jpa.AbstractEnumConverter;
import org.cometbid.component.ut.jpa.PersistableEnum;

/**
 *
 * @author samueladebowale
 */
@Log4j2
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EmployeeType implements PersistableEnum<String> {

    @SerializedName("FULL-TIME")
    @JsonProperty("FULL-TIME")
    FULL_TIME("FULL-TIME"),
    @SerializedName("PART-TIME")
    @JsonProperty("PART-TIME")
    PART_TIME("PART-TIME"),
    @SerializedName("CONTRACT")
    @JsonProperty("CONTRACT")
    CONTRACT("CONTRACT");

    @Getter
    @JsonValue
    private final String displayName;

    private EmployeeType(String name) {
        this.displayName = name;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public String getValue() {
        return displayName;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, EmployeeType> stringToEnum = new HashMap<>();

    static { // Initialize map from constant name to enum constant
        for (EmployeeType op : values()) {
            stringToEnum.put(op.toString(), op);
        }
    }

    // Returns Operation for string, or null if string is invalid
    @JsonCreator
    public static EmployeeType fromString(String typeName) {
        return stringToEnum.get(StringUtils.upperCase(typeName));
    }

    public static Collection<EmployeeType> getAllTypes() {
        return stringToEnum.values();
    }

    public static Set<String> getAllNames() {
        return stringToEnum.keySet();
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<EmployeeType, String> {

        public Converter() {
            super(EmployeeType.class);
        }

        @Override
        public String convertToDatabaseColumn(EmployeeType attribute) {

            return attribute != null ? attribute.getValue().toUpperCase() : null;
        }

        @Override
        public EmployeeType convertToEntityAttribute(String s) {

            // which enum is it?
            for (EmployeeType en : EnumSet.allOf(EmployeeType.class)) {
                if (en.getValue().equals(s)) {
                    log.info("""
                                       ======= String %s EmployeeType %s
                                       """.formatted(s, en));
                    return en;
                }
            }
            return null;
        }
    }
}
