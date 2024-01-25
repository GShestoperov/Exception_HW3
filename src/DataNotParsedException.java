public class DataNotParsedException extends RuntimeException {
    public DataNotParsedException() {
        super("Данные не распарсены");
    }
    public DataNotParsedException(String message) {
        super(message);
    }
}
