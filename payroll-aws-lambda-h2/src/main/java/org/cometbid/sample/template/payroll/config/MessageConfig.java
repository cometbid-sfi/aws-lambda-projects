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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import org.cometbid.component.api.gson.LocalDateTimeTypeAdapter;
import org.cometbid.component.api.gson.LocalDateTypeAdapter;
import org.cometbid.component.api.gson.MoneyTypeAdapterFactory;
import org.cometbid.component.api.gson.OffsetDateTimeTypeAdapter;
import org.cometbid.component.api.gson.ZonedDateTimeTypeAdapter;
import org.cometbid.component.api.interceptors.CustomLocaleChangeInterceptor;
import org.cometbid.component.api.interceptors.CustomTimezoneChangeInterceptor;
import static org.cometbid.sample.template.payroll.config.LocalizationFactory.getContextLocale;
import org.cometbid.component.api.jackson.MoneySerializer;

import org.cometbid.component.api.jackson.*;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.javamoney.moneta.Money;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.zalando.jackson.datatype.money.MoneyModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.cometbid.component.api.interceptors.RecursiveLocaleContextMessageInterpolator;
import org.cometbid.component.api.util.converters.CustomStringToEnumConverterFactory;
import org.cometbid.component.ut.jpa.AnnotationExclusionStrategy;

/**
 *
 * @author samueladebowale
 */
@Configuration
public class MessageConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomLocaleChangeInterceptor());
        registry.addInterceptor(new CustomTimezoneChangeInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }

    @Bean
    public ConverterRegistry initConverter(@Qualifier("mvcConversionService") ConverterRegistry registry) {
        registry.addConverterFactory(new CustomStringToEnumConverterFactory());
        return registry;
    }

    /*
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }
    */
    
    @Bean
    @DependsOn("localizationFactory")
    public LocaleResolver localeResolver(LocalizationFactory factory) {
        CookieLocaleResolver r = new CookieLocaleResolver("localeInfo");
        r.setDefaultLocale(getContextLocale());

        // if set to -1, the cookie is deleted
        // when browser shuts down
        r.setCookieMaxAge(Duration.ofSeconds(24 * 60 * 60));
        return r;
    }

    @Bean
    public LocalizationFactory localizationFactory() {
        return new LocalizationFactory();
    }

    @Bean("messageSource")
    public MessageSource bundleMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:response-messages",
                "classpath:messages/validation/message");
        //messageSource.setBasenames("classpath:messages/business/messages",
        //     "classpath:language/messages");

        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    public MessageInterpolator getMessageInterpolator() {
        MessageSourceResourceBundleLocator resourceBundleLocator = new MessageSourceResourceBundleLocator(bundleMessageSource());
        ResourceBundleMessageInterpolator messageInterpolator = new ResourceBundleMessageInterpolator(resourceBundleLocator);
        return new RecursiveLocaleContextMessageInterpolator(messageInterpolator);
    }

    /**
     *
     * @param messageInterpolator
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageInterpolator messageInterpolator) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(bundleMessageSource());
        bean.setMessageInterpolator(messageInterpolator);
        return bean;
    }

    @Bean
    public Validator getValidatorBean(MessageInterpolator interpolator) {
        return Validation.byDefaultProvider()
                .configure()
                //.messageInterpolator(new ParameterMessageInterpolator())
                .messageInterpolator(interpolator)
                .buildValidatorFactory()
                .usingContext()
                .getValidator();
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new MoneyModule().withQuotedDecimalNumbers().withRoundedMoney());
        //mapper.setAnnotationIntrospector(new SerializedNameAnnotationIntrospector());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        simpleModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());

        simpleModule.addSerializer(Money.class, new MoneySerializer());

        mapper.registerModule(simpleModule);
        return mapper;
    }

    @Bean
    public Gson gson() {
        GsonBuilder b = new GsonBuilder();
        b.setExclusionStrategies(new AnnotationExclusionStrategy());
        b.registerTypeAdapterFactory(new MoneyTypeAdapterFactory());
        b.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
        //b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        //b.registerTypeAdapterFactory(TimestampTypeAdapter.FACTORY);
        b.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter());
        return b.create();
    }

}
