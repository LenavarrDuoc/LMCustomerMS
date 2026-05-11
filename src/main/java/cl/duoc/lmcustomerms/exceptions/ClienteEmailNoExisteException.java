package cl.duoc.lmcustomerms.exceptions;

public class ClienteEmailNoExisteException extends RuntimeException {
    public ClienteEmailNoExisteException(String message) {
        super(message);
    }
}
