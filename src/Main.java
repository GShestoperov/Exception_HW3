import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PersonParser personParser = new PersonParser();
        SavePerson savePerson = new SavePerson();
        Scanner scanner = new Scanner(System.in);

        System.out.println("------------------------ Вас приветствует программа СуперПарсер! -----------------------------");
        while (true) {
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.println("Введите данные в произвольном порядке (Фамилия,Имя,Отчество дата_рождения номер_телефона пол) " +
                    "\n (дата_рождения в формате dd.mm.yyyy; номер_телефона в формате +12345678901; пол - m/f) (q - выход): ");
            String data = scanner.nextLine();
            if (data.equalsIgnoreCase("q"))
                break;

            try {
                personParser.ParseData(data);
                savePerson.saveToFile(personParser);
                System.out.println("Данные сохранены.");
            } catch (DataNotParsedException e) {
                String err_msg = "Ошибка распознавания данных. " + e.getMessage();
                System.out.println(err_msg);
            } catch (IOException e) {
                System.out.println("Ошибка записи в файл: " + e.toString());
                for (StackTraceElement el : e.getStackTrace())
                    System.out.println(el.toString());
            }
        }
    }
}
