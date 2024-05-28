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
package org.cometbid.sample.template.payroll.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.cometbid.component.api.gson.LocalDateTimeTypeAdapter;
import org.cometbid.component.api.gson.LocalDateTypeAdapter;
import org.cometbid.component.api.gson.MoneyTypeAdapterFactory;
import org.cometbid.component.api.gson.ZonedDateTimeTypeAdapter;
import org.cometbid.component.ut.jpa.AnnotationExclusionStrategy;

/**
 *
 * @author samueladebowale
 */
public class BaseFunctionHandler {

    protected Gson gson() {
        GsonBuilder b = new GsonBuilder();

        b.setExclusionStrategies(new AnnotationExclusionStrategy());
        b.registerTypeAdapterFactory(new MoneyTypeAdapterFactory());
        //b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
        //b.registerTypeAdapterFactory(TimestampTypeAdapter.FACTORY);
        b.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter());
        return b.create();
    }
}
