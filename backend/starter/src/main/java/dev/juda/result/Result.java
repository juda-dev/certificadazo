package dev.juda.result;

import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.juda.error.dto.ErrorResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface Result<T> permits Result.Success, Result.Error {

    static <T> Result<T> success(T value){
        return new Success<>(value);
    }

    static <T> Result<T> success(){
        return new Success<>(null);
    }

    static <T> Result<T> error(HttpStatus hs, String message, List<ValidationError> errors) {
        return new Error<>(hs, message, errors);
    }

    static <T> Result<T> validationError(HttpStatus hs, String message, List<ValidationError> errors) {
        return new Error<>(hs, message, errors);
    }

    boolean isSuccess();
    boolean isError();

    T getValue();

    @JsonIgnore
    Error<T> getError();

    @SuppressWarnings("unchecked")
    default <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        if (this instanceof Success<T> s) {
            return Result.success(mapper.apply(s.value));
        }
        return (Result<U>) this;
    }

    @SuppressWarnings("unchecked")
    default <U> Result<U> flatMap(Function<? super T, Result<U>> mapper) {
        if (this instanceof Success<T> s) {
            return mapper.apply(s.value);
        }
        return (Result<U>) this;
    }

    record Success<T>(T value) implements Result<T> {

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        @JsonIgnore
        public Error<T> getError() {
            throw new IllegalStateException("Cannot get error from a success result");
        }
        
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Error<T>(
        HttpStatus hs, 
        String message, 
        @JsonProperty("errors") List<ValidationError> errors) implements Result<T> {

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        @JsonIgnore
        public T getValue() {
            throw new IllegalStateException("Cannot get value from an error result");
        }

        @Override
        public Error<T> getError() {
            return this;
        }

        public ErrorResponse toErrorResponse(String path) {
            return ErrorResponse.of(hs, message, path, errors);
        }

        public ErrorResponse toErrorResponse() {
            return toErrorResponse(null);
        }

        public int httpStatus(){
            return HttpStatus.httpStatus(hs);
        }
    }

    record ValidationError(String field, String message) {
    }
}
