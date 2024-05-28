/*
 * The MIT License
 *
 * Copyright 2024 Cometbid.Org.
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

import org.cometbid.component.api.util.LocaleContextUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *
 * @author samueladebowale
 */
public class ResourceBundleAccessor {

    private ResourceBundleAccessor() {

    }

    private static final ReloadableResourceBundleMessageSource messageSource;

    static {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("messages/validation/message");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
    }

    /**
     * 
     * @param messageKey
     * @param args
     * @return 
     */
    public static String accessMessageInBundle(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, LocaleContextUtils.getContextLocale());
    }

    /**
     *
     * @param messageKey
     * @param parameterName
     * @param rejectedValue
     * @param dataType
     * @return
     */
    public static String getLocalizedErrorMessage(String messageKey, Object parameterName, Object rejectedValue,
            String dataType) {

        return accessMessageInBundle(messageKey, new Object[]{parameterName, dataType, rejectedValue});
    }
}
