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
package org.cometbid.sample.template.payroll.config;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.component.api.util.LocaleContextUtils;
import org.cometbid.component.api.util.TimeZoneUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author samueladebowale
 */
@Log4j2
//@Configuration
public class LocalizationFactory {

    @Value("${api.default.locale}")
    private String defaultLocale;

    @Value("${api.default.timezone}")
    private String defaultTimezone;

    public static Map<String, String> mappedLocales = new HashMap<>();

    public LocalizationFactory() {

        Locale[] locales = SimpleDateFormat.getAvailableLocales();
        for (Locale locale : locales) {
            if (StringUtils.isNotBlank(locale.toString())) {
                mappedLocales.put(locale.toString(), locale.getDisplayLanguage());
            }
        }
    }

    @Value("${api.default.locale}")
    public void setLocaleStatic(String locale) {
        if (StringUtils.isNotBlank(locale)) {
            LocaleContextUtils.DEFAULT_LANG_CODE = locale;
        }
    }

    @Value("${api.default.timezone}")
    public void setTimeZoneStatic(String timezone) {
        if (StringUtils.isNotBlank(timezone)) {
            TimeZoneUtils.DEFAULT_TIMEZONE = timezone;
        }
    }

    public String getDefaultLocale() {
        return LocaleContextUtils.DEFAULT_LANG_CODE;
    }

    public String getDefaultTimezone() {
        return TimeZoneUtils.DEFAULT_TIMEZONE;
    }

    public Map<String, String> getSystemLocales() {
        return new TreeMap<>(mappedLocales);
    }

    public static Locale getContextLocale() {
        return LocaleContextUtils.getContextLocale();
    }

    public static ZoneId getContextZoneId() {
        return TimeZoneUtils.getContextZoneId();
    }

    public static ZoneOffset getContextZoneOffset() {
        return TimeZoneUtils.getContextZoneOffset();
    }

}
