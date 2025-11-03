import java.io.IOException;

public class EmptyFileException extends IOException {
    public EmptyFileException(String path) {
        super(path + " was empty");
    }
}
