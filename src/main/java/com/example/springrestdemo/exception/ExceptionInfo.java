package com.example.springrestdemo.exception;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;


public class ExceptionInfo {
    private final String exception;
    private final String message;
    private final String details;

    private ExceptionInfo(String exception, String message, String details) {
        this.exception = exception;
        this.message = message;
        this.details = details;
    }

    public static ExceptionInfo from(Throwable e) {
        final String exception = e.getClass().getName();
        if (e instanceof MethodArgumentNotValidException) {
            String validationMessage = generateValidationMessage((MethodArgumentNotValidException) e);
            return ExceptionInfo.builder().withException(exception).withMessage(validationMessage).build();
        }
        return ExceptionInfo.builder().withException(exception).withMessage(e.getMessage()).build();
    }

    private static String generateValidationMessage(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        var sb = new StringBuilder("Validation failed with ")
                .append(bindingResult.getErrorCount()).append(" error");
        if (bindingResult.getErrorCount() > 1) {
            sb.append("s");
        }
        sb.append(": ");
        for (FieldError error : bindingResult.getFieldErrors()) {
            sb.append("[").append(error.getField()).append("]");
            sb.append("[").append(error.getDefaultMessage()).append("] ");
        }
        return sb.toString();
    }

    protected static ExceptionInfoBuilder builder() {
        return new ExceptionInfoBuilder();
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    private static class ExceptionInfoBuilder {

        String exception;
        String message;
        String details;

        public ExceptionInfoBuilder withException(String exception) {
            this.exception = exception;
            return this;
        }

        public ExceptionInfoBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ExceptionInfoBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public ExceptionInfo build() {
            return new ExceptionInfo(exception, message, details);
        }
    }
}
