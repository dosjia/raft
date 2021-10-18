package cn.martinzhao.raft.exception;

import lombok.Getter;

/**
 * @author Martin.Zhao
 * @version 1.0
 * @since 2021/10/18
 */
@Getter
public class ApplicationBaseException extends RuntimeException {
    private final int errorCode;
    private final String errorMessage;

    public ApplicationBaseException(ExceptionEnum exception, Exception e) {
        super(e);
        this.errorCode = exception.getValue();
        this.errorMessage = exception.getMessage();
    }

    public ApplicationBaseException(int code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    public ApplicationBaseException(ExceptionEnum exception) {
        super(exception.getMessage());
        this.errorCode = exception.getValue();
        this.errorMessage = exception.getMessage();
    }

    /**
     * errorMessage field in ExceptionEnum should contain %s placeholder,
     * message is the value to fill in the placeholder.
     *
     * @param exception Enumeration of the exception
     * @param message message to fill in original exception message
     */
    public ApplicationBaseException(ExceptionEnum exception, String message) {
        super(String.format(exception.getMessage(), message));
        this.errorCode = exception.getValue();
        this.errorMessage = super.getMessage();
    }
}
