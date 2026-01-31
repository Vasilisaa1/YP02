package com.example.pr1;

import android.util.Patterns;
import java.util.regex.Pattern;

public class Utils {

    // Паттерны для валидации
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    );

    // Исправленный паттерн для имени без UNICODE_CHARACTER_CLASS
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$"
    );

    // Валидация email
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Валидация пароля
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // Валидация имени
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty() || name.length() < 2 || name.length() > 50) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches();
    }

    // Проверка совпадения паролей
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    // Получение сообщения об ошибке для пароля
    public static String getPasswordErrorMessage() {
        return "Пароль должен содержать:\n" +
                "• Минимум 8 символов\n" +
                "• Хотя бы одну цифру (0-9)\n" +
                "• Хотя бы одну строчную букву\n" +
                "• Хотя бы одну заглавную букву\n" +
                "• Хотя бы один специальный символ (@#$%^&+=!)\n" +
                "• Без пробелов";
    }

    // Получение сообщения об ошибке для имени
    public static String getNameErrorMessage() {
        return "Имя должно:\n" +
                "• Содержать только буквы (русские и английские)\n" +
                "• Быть длиной от 2 до 50 символов\n" +
                "• Может содержать пробелы, точки, апострофы и дефисы";
    }

    // Очистка текста от лишних пробелов
    public static String cleanText(String text) {
        if (text == null) return "";
        return text.trim();
    }

    // Проверка минимальной длины пароля
    public static boolean isPasswordLengthValid(String password) {
        return password != null && password.length() >= 8; // Изменено с 6 на 8 для соответствия паттерну
    }

    // Проверка, не пустое ли поле
    public static boolean isFieldNotEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }
}