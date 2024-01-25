import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class SavePerson {
    public void saveToFile(PersonParser personParser) throws IOException {
        if (!personParser.getIsDataParsed())
            throw new DataNotParsedException("Сохранение невозможно, данные не распарсены");
        String fileName = personParser.getLastName() + ".txt";
        Path path = Paths.get(fileName);
        StandardOpenOption soo = null;
        if (Files.exists(path))
            soo = StandardOpenOption.APPEND;
        else
            soo = StandardOpenOption.CREATE;
        try (BufferedWriter bw = Files.newBufferedWriter(path, soo)) {
            String data = personParser.getLastName() + "," + personParser.getFirstName() + "," + personParser.getMiddleName()
                + " " + personParser.getBirthDate() + " " + personParser.getPhone() + " " + personParser.getGender() + "\n";
            bw.write(data);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
