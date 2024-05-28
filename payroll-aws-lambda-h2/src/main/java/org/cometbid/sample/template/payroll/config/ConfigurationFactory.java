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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.cometbid.component.api.response.model.AppResponseMetadata;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Getter
@Configuration
@RequiredArgsConstructor
public class ConfigurationFactory {

    public static final String TRACE_ID_KEY = "X-B3-TraceId";

    @Autowired
    private LocalResponseMetadata responseMetadata;

    public AppResponseMetadata createResponseMetadata() {

        log.info("ConfigurationFactory Metadata: {}", responseMetadata);

        return AppResponseMetadata.builder()
                .apiVersion(responseMetadata.getApiVersion())
                .sendReport(responseMetadata.getSendReportUri() + "?id=" + getTraceId())
                .moreInfo(responseMetadata.getMoreInfoUrl())
                .apiDocUrl(responseMetadata.getApiDocUrl()) 
                .technical(responseMetadata.getTechnical()) 
                .build();
    }

    public void putTraceId(String traceId) {
        ThreadContext.put(TRACE_ID_KEY, traceId);
    }

    public String getTraceId() {
        return ThreadContext.get(TRACE_ID_KEY);
    }

}
