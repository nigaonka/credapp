package com.ng.credit.creditapp.kafka.producer;




import java.util.Objects;

public interface IntComponent <I extends ICompBO, R extends ICompBO> {
    default boolean isValid(I input) {
        return Objects.nonNull(input);
    }

    R process(I input) throws Exception;
}
