package cl.duoc.lmcustomerms.exceptions;

public class ClienteEmailExisteException extends RuntimeException {
    public ClienteEmailExisteException(String message) {
        super(message);
    }
}
