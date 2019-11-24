package se.gewalli.data;

public class EntityNotFound extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1615032090714212552L;

    public EntityNotFound() {
    }
    public EntityNotFound(String message){
        super(message);
    }
}
