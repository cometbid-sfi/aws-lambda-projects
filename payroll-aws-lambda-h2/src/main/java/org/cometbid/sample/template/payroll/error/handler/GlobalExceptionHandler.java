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
package org.cometbid.sample.template.payroll.error.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cometbid.component.api.auth.exceptions.CustomConstraintViolationException;
import org.cometbid.component.api.util.ErrorCode;
import org.cometbid.component.api.employee.exceptions.EmployeeAlreadyExistException;
import org.cometbid.component.api.employee.exceptions.EmployeeNotFoundException;
import org.cometbid.component.api.generic.exceptions.ResourceNotFoundException;
import org.cometbid.component.api.generic.exceptions.ServerTimeoutRequestException;
import org.cometbid.component.api.generic.exceptions.ServiceUnavailableException;
import org.cometbid.component.api.response.model.ApiError;
import org.cometbid.component.api.response.model.AppResponse;
import static org.cometbid.component.api.util.ErrorCode.*;
import org.cometbid.sample.template.payroll.config.ConfigurationFactory;
import static org.cometbid.sample.template.payroll.config.LocalizationFactory.getContextLocale;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import static org.springframework.http.HttpStatus.*;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Configuration
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_CODE = ErrorCode.SYS_DEFINED_ERR_CODE.getErrCode();

    //@Autowired
    private final ConfigurationFactory configurationFactory;
    private final MessageSource messageSource;

    public GlobalExceptionHandler(ConfigurationFactory configurationFactory,
            @Qualifier("messageSource") MessageSource messageSource) {
        this.configurationFactory = configurationFactory;
        this.messageSource = messageSource;
    }

    /**
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public @ResponseBody
    AppResponse genericServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        String message = getErrorMessage(ErrorCode.SYS_DEFINED_ERR_CODE.getErrMsgKey());

        return createHttpErrorInfo(INTERNAL_SERVER_ERROR, DEFAULT_ERROR_CODE, request, message, ex);
    }

    /**
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {ServiceUnavailableException.class, ServerTimeoutRequestException.class})
    public @ResponseBody
    AppResponse serviceFailure(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        switch (ex) {
            case ServerTimeoutRequestException enfe -> {
                return createHttpErrorInfo(GATEWAY_TIMEOUT, enfe.getErrorCode(), request, enfe.getErrorMessage(), ex);
            }
            case ServiceUnavailableException enfe -> {
                return createHttpErrorInfo(SERVICE_UNAVAILABLE, enfe.getErrorCode(), request, enfe.getErrorMessage(), ex);
            }
            default -> {
                String message = getErrorMessage(ErrorCode.UNAVAILABLE_SERVICE_ERR_CODE.getErrMsgKey());
                return createHttpErrorInfo(SERVICE_UNAVAILABLE, UNAVAILABLE_SERVICE_ERR_CODE.getErrCode(), request, message, ex);
            }
        }

    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class, EmployeeNotFoundException.class})
    public @ResponseBody
    AppResponse handleNotFoundExceptions(ResourceNotFoundException ex, HttpServletRequest request) {

        if (ex instanceof EmployeeNotFoundException enfe) {
            return createHttpErrorInfo(NOT_FOUND, enfe.getErrorCode(), request, enfe.getErrorMessage(), ex);
        }

        return createHttpErrorInfo(NOT_FOUND, ex != null ? ex.getErrorCode() : DEFAULT_ERROR_CODE, request,
                ex != null ? ex.getErrorMessage() : NOT_FOUND.getReasonPhrase(), ex);
    }

    /**
     *
     * @param exception
     * @param request
     * @return
     */
    @ResponseStatus(CONFLICT)
    @ExceptionHandler({EmployeeAlreadyExistException.class})
    public @ResponseBody
    AppResponse handleEmployeeAlreadyExistsException(EmployeeAlreadyExistException exception, HttpServletRequest request) {

        return createHttpErrorInfo(CONFLICT, exception.getErrorCode(), request, exception.getErrorMessage(), exception);
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, CustomConstraintViolationException.class,
        ConversionFailedException.class, MethodArgumentNotValidException.class})
    public @ResponseBody
    AppResponse handleValidationExceptions(Exception ex, HttpServletRequest request) {

        AppResponse customError = null;

        switch (ex) {
            case CustomConstraintViolationException err -> {
                ApiError apiError = createError(HttpStatus.BAD_REQUEST, err.getErrorCode(), request, err.getErrorMessage(), ex);

                apiError.addValidationErrors(err.getConstraintViolations());
                customError = createHttpErrorInfo(apiError);
            }
            case ConversionFailedException err -> {
                String message = getErrorMessage(ErrorCode.RESOURCE_CONVERSION_ERR_CODE.getErrMsgKey());
                ApiError apiError = createError(HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE, request, message, ex);

                Object obj = err.getValue();
                apiError.addValidationError(new ObjectError(obj != null ? obj.toString() : "", err.getMessage()));

                customError = createHttpErrorInfo(apiError);
            }
            case MethodArgumentNotValidException err -> {
                String message = getErrorMessage(ErrorCode.BAD_REQUEST_ERR_CODE.getErrMsgKey());
                ApiError apiError = createError(HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE, request, message, ex);

                final BindingResult result = err.getBindingResult();

                result.getAllErrors().stream().forEach(e -> {

                    if (e instanceof FieldError fieldError) {
                        apiError.addValidationError(fieldError);
                        // return ((FieldError) e).getField() + " : " + e.getDefaultMessage();
                    } else {
                        if (e != null) {
                            apiError.addValidationError(new ObjectError(e.getObjectName(), e.getDefaultMessage()));
                            // return e.getObjectName() + " : " + e.getDefaultMessage();
                        }
                    }
                });
                customError = createHttpErrorInfo(apiError);
            }
            default -> {
                String message = getErrorMessage(ErrorCode.CONSTRAINT_VIOLATION_ERR_CODE.getErrMsgKey());

                customError = createHttpErrorInfo(HttpStatus.BAD_REQUEST,
                        ErrorCode.CONSTRAINT_VIOLATION_ERR_CODE.getErrCode(), request, message, ex);
            }
        }

        return customError;
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler({JsonParseException.class})
    public @ResponseBody
    AppResponse handleJsonParseException(JsonParseException ex, HttpServletRequest request) {

        String message = getErrorMessage(ErrorCode.JSON_PARSE_ERROR.getErrMsgKey());

        log.info("JsonParseException :: request.getMethod(): " + request.getMethod());

        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, ErrorCode.JSON_PARSE_ERROR.getErrCode(), request, message, ex);
    }

    /**   
     *   
     * @param ex  
     * @param request
     * @return
     */
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotAcceptableException.class,
        HttpMediaTypeNotSupportedException.class, HttpMessageNotWritableException.class,
        HttpMediaTypeNotAcceptableException.class})
    public @ResponseBody
    AppResponse handleHttpMessageException(Exception ex, HttpServletRequest request) {

        String message = "Not available";
        String localErrorCode = DEFAULT_ERROR_CODE;

        if (ex instanceof HttpMessageNotReadableException) {
            message = getErrorMessage(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MESSAGE_NOT_READABLE.getErrCode();
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            message = getErrorMessage(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getErrCode();
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = getErrorMessage(ErrorCode.HTTP_MEDIATYPE_NOT_SUPPORTED.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MEDIATYPE_NOT_SUPPORTED.getErrCode();
        } else if (ex instanceof HttpMessageNotWritableException) {
            message = getErrorMessage(ErrorCode.HTTP_MESSAGE_NOT_WRITABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MESSAGE_NOT_WRITABLE.getErrCode();
        }

        log.info("Exception :: request.getMethod(): " + request.getMethod());

        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, localErrorCode, request, message, ex);
    }

    /**
     *
     * @param httpStatus
     * @param errorCode
     * @param request
     * @param message
     * @param ex
     * @return
     */
    private AppResponse createHttpErrorInfo(HttpStatus httpStatus, String errorCode, HttpServletRequest request,
            String message, Exception ex) {

        final String path = request.getRequestURI();

        if (StringUtils.isBlank(message)) {
            message = httpStatus.getReasonPhrase();
        }

        log.info("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        ApiError apiError = ApiError.create(path, request.getMethod(), errorCode,
                httpStatus.name(), httpStatus.value(),
                message, ex.getMessage());

        log.info("Response Metadata: {}", configurationFactory.createResponseMetadata());
        return AppResponse.error(apiError, configurationFactory.createResponseMetadata());
    }

    /**
     *
     * @param apiError
     * @return
     */
    private AppResponse createHttpErrorInfo(ApiError apiError) {

        log.info("Response Metadata: {}", configurationFactory.createResponseMetadata());
        return AppResponse.error(apiError, configurationFactory.createResponseMetadata());
    }

    /**
     *
     * @param httpStatus
     * @param errorCode
     * @param request
     * @param message
     * @param ex
     * @return
     */
    private ApiError createError(HttpStatus httpStatus, String errorCode, HttpServletRequest request,
            String message, Exception ex) {
        final String path = request.getRequestURI();

        if (StringUtils.isBlank(message)) {
            message = httpStatus.getReasonPhrase();
        }

        log.info("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return ApiError.create(path, request.getMethod(), errorCode,
                httpStatus.name(), httpStatus.value(),
                message, ex.getMessage());
    }

    private String getErrorMessage(String messagekey) {

        return messageSource.getMessage(messagekey, new Object[]{}, getContextLocale());
    }
}
