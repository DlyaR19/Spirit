package com.spirit.application.util;


import com.vaadin.flow.component.notification.Notification;

public class RegisterUtils {

    private RegisterUtils() {
        throw new UnsupportedOperationException("Class should not be instantiated, it is a utility class.");
    }

    public static boolean validateInput(String username, String firstName, String lastName, String email, String password, String passwordConfirmation) {
        if (!isValidVorname(firstName)) {
            Notification.show("Kein gültiger Vorname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).");
            return false;
        }
        if (!isValidNachname(lastName)) {
            Notification.show("Kein gültiger Nachname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).");
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    public static boolean validateInput(String username, String unternehmenName, String email, String password, String passwordConfirmation) {
        if (!isValidUnternehmenName(unternehmenName)) {
            Notification.show("Kein gültiger Unternehmensnamen (mindestens 3-30 Zeichen, nur Buchstaben, Leerzeichen und Ziffern).");
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    public static boolean validateInput(String username, String email, String password, String passwordConfirmation) {
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    private static boolean checkDefaultInput(String username, String email, String password, String passwordConfirmation) {
        if (!isValidUsername(username)) {
            Notification.show("Kein gültiger Benutzername (4-20 Zeichen, nur Buchstaben und Ziffern).");
            return false;
        }
        if (!isValidEmail(email)) {
            Notification.show("Keine gültige E-Mail Adresse (zB test@test.de).");
            return false;
        }
        if (!password.equals(passwordConfirmation)) {
            Notification.show("Die Passwörter stimmen nicht überein.");
            return false;
        }
        if (!isPasswordComplex(password)) {
            Notification.show("Das Passwort muss 8-16 Zeichen lang sein und mindestens einen Großbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten.");
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');

        if (atIndex <= 0 || dotIndex <= atIndex + 1 || dotIndex >= email.length() - 1) {
            return false;
        }

        for (char c : email.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '@' && c != '.' && c != '_' && c != '-' && c != '+') {
                return false;
            }
        }
        return true;
    }

    private static boolean isPasswordComplex(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

    private static boolean isValidUnternehmenName(String unternehmenName) {
        if (unternehmenName.length() < 3) {
            return false;
        }

        for (char c : unternehmenName.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidVorname(String vorname) {
        return isValidName(vorname);
    }

    private static boolean isValidNachname(String nachname) {
        return isValidName(nachname);
    }

    private static boolean isValidName(String name) {
        if (name.length() < 3 || name.length() > 30) {
            return false;
        }

        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ' && c != '-') {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidUsername(String username) {
        if (username.length() < 4 || username.length() > 20) {
            return false;
        }

        for (char c : username.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
}