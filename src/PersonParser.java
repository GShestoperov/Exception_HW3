import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;

public class PersonParser {
    private Boolean isDataParsed;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private boolean isGenderParsed;
    private String phone;
    private boolean isPhoneParsed;
    private String birthDate;
    private boolean isBirthDateParsed;
    private boolean isFioParsed;

    public PersonParser() {
        isDataParsed = false;
        isGenderParsed = false;
        isPhoneParsed = false;
        isBirthDateParsed = false;
        isFioParsed = false;
    }

    public Boolean getIsDataParsed() {
        return isDataParsed;
    }

    public String getLastName() {
        if (!isFioParsed)
            throw new DataNotParsedException("Фамилия не распарсена");
        return lastName;
    }

    public String getFirstName() {
        if (!isFioParsed)
            throw new DataNotParsedException("Имя не распарсено");
        return firstName;
    }

    public String getMiddleName() {
        if (!isFioParsed)
            throw new DataNotParsedException("Отчество не распарсено");
        return middleName;
    }

    public String getGender() {
        if (!isGenderParsed)
            throw new DataNotParsedException("Пол человека не распарсен");
        return gender;
    }

    public String getPhone() {
        if (!isPhoneParsed)
            throw new DataNotParsedException("Телефон не распарсен");
        return phone;
    }

    public String getBirthDate() {
        if (!isBirthDateParsed)
            throw new DataNotParsedException("Дата рождения не распарсена");
        return birthDate;
    }

    public void ParseData(String data) {
        isDataParsed = false;
        isGenderParsed = false;
        isPhoneParsed = false;
        isBirthDateParsed = false;
        isFioParsed = false;
        String err_phone_msg = "";
        String err_birthdate_msg = "";
        String err_fio_msg = "";
        String[] dataArr = data.split(" ");
        for (String str : dataArr) {

            // парсим пол человека
            if (!isGenderParsed && (str.equalsIgnoreCase("m") || str.equalsIgnoreCase("f"))) {
                gender = str;
                isGenderParsed = true;
                continue;
            }

            // парсим дату рождения
            if (!isBirthDateParsed) {
                String[] date_parts = str.split("\\.");
                if (date_parts.length == 3) {
                    int[] date_parts_int = new int[3];
                    boolean isNumber = false;
                    boolean isCorrectDate = false;
                    try {
                        for (int i = 0; i < 3; i++)
                            date_parts_int[i] = Integer.parseInt(date_parts[i]);
                        isNumber = true;
                    } catch (NumberFormatException e) {
                        isNumber = false;
                    }

                    if (isNumber && date_parts[0].length() == 2 && date_parts[1].length() == 2
                            && date_parts[2].length() == 4 ) {
                        try {
                            LocalDate.of(date_parts_int[2], date_parts_int[1], date_parts_int[0]);
                            isCorrectDate = true;
                        } catch (DateTimeException e) {
                            isCorrectDate = false;
                            err_birthdate_msg = "(значение не соответствуют реальному значению даты)";
                        }
                    } else {
                        err_birthdate_msg = "(дата рождения не соответствует формату)";
                    }

                    if (isNumber && isCorrectDate) {
                        birthDate = str;
                        isBirthDateParsed = true;
                        continue;
                    }
                }
            }

            // парсим ФИО
            if (!isFioParsed) {
                String[] fio_parts = str.split(",");
                if (fio_parts.length == 3) {
                    boolean isLetter = true;
                    for (int i = 0;  i < 3; i++)
                        if (!checkLetter(fio_parts[i]))
                            isLetter = false;

                    if (isLetter) {
                        lastName = fio_parts[0];
                        firstName = fio_parts[1];
                        middleName = fio_parts[2];
                        isFioParsed = true;
                        continue;
                    } else {
                        err_fio_msg = "(в ФИО присутствуют недопустимые символы)";
                    }
                }

            }

            // парсим телефон
            if(!isPhoneParsed) {
                int index = str.indexOf("+");
                if (index == -1 || index == 0) {
                    if (str.length() >= 11) {
                        if (index == -1 && checkDigit(str)) {
                            phone = str;
                            isPhoneParsed = true;
                            continue;
                        } else if (index == 0 && checkDigit(str.substring(1))) {
                            phone = str;
                            isPhoneParsed = true;
                            continue;
                        } else {
                            err_phone_msg = "(в номере телефона присутствуют недопустимые символы)";
                        }
                    } else {
                        err_phone_msg = "(длина номера телефона должна быть не менее 11 символов)";
                    }
                } else {
                    err_phone_msg = "(в номере телефона неверно указан +)";
                }
            }
        }

        int countDataNotParsed = 0;
        String message = "";
        if (!isGenderParsed) {
            countDataNotParsed++;
            message += " пол человека не найден (m/f);";
        }
        if (!isPhoneParsed ){
            countDataNotParsed++;
            message += " телефон не найден (+12345678901) " + err_phone_msg + ";";
        }
        if (!isBirthDateParsed) {
            countDataNotParsed++;
            message += " дата рождения не найдена (dd.mm.yyyy) " + err_birthdate_msg + ";";
        }
        if (!isFioParsed) {
            countDataNotParsed++;
            message += " ФИО не найдено (Фамилия,Имя,Отчество) " + err_fio_msg + ";";
        }

        if (countDataNotParsed > 0) {
            throw new DataNotParsedException("Не найдены " + countDataNotParsed + " из 4-х данных: " + message);
        }

        isDataParsed = true;
    }

    private boolean checkDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= '0' && (str.charAt(i) <= '9')))
                return false;
        }
        return true;
    }

    private boolean checkLetter(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                    || (ch >= 'а' && ch <= 'я') || (ch >= 'А' && ch <= 'Я')))
                return false;
        }
        return true;
    }
}
