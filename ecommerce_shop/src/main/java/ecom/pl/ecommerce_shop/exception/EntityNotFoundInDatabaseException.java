package ecom.pl.ecommerce_shop.exception;

public class EntityNotFoundInDatabaseException extends RuntimeException {

    public EntityNotFoundInDatabaseException(String message) {
        super(message);
    }
}
