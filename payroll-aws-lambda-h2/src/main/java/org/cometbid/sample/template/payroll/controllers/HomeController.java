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
package org.cometbid.sample.template.payroll.controllers;

import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cometbid.sample.template.payroll.config.CurrencyFactory;
import org.cometbid.component.api.util.TimeZoneUtils;
import org.cometbid.sample.template.payroll.config.LocalizationFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@RestController
@RequiredArgsConstructor
public class HomeController {
    
    private final LocalizationFactory localizationFactory;

    @GetMapping("/")
    public String index() {
        return "Hello, this is Cometbid.org's public api";
    }

    @GetMapping("timezones")
    public Set<String> timezones() {
        return TimeZoneUtils.getAvailableTimeZoneIds();
    }

    @GetMapping("locales")
    public Map<String, String> locales() {
        return localizationFactory.getSystemLocales();
    }
    
    @GetMapping("currencies")
    public Set<String> currencies() {
        return CurrencyFactory.Currency.getAllNames();
    }

}
