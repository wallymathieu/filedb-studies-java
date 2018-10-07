package se.gewalli.data;

public class EntityNotFound extends Exception {
    public EntityNotFound(){}
    public EntityNotFound(String message){
        super(message);
    }
}
