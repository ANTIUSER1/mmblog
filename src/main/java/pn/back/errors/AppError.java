/**
 * класс обвязки HTTP=ошибки
 */
package pn.back.errors;

public class AppError {
    private final int statusCode;
    private final String message;

    public AppError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
