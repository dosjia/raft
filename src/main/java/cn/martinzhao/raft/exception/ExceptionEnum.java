package cn.martinzhao.raft.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    SUCCESS(0, "Action performed successfully."),
    INVALID_DATA(1, "{}");
    //    FAILURE(0, "Action performed failed.");
    private final Integer value;
    private final String message;

    public static ExceptionEnum of(Integer value) {
        for (ExceptionEnum result : ExceptionEnum.values()) {
            if (result.getValue().equals(value)) {
                return result;
            }
        }
        return null;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getMessage() {
        return this.message;
    }
}
