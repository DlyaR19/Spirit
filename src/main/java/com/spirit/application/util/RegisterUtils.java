package com.spirit.application.util;


import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Utility-Klasse mit statischen Methoden zur Validierung von Registrierungsdaten
 */

public class RegisterUtils {

    /**
     * Privater Konstruktor verhindert Instanziierung
     * @throws UnsupportedOperationException wenn versucht wird, die Klasse zu instanziieren
     */
    private RegisterUtils() {
        throw new UnsupportedOperationException("Class should not be instantiated, it is a utility class.");
    }

    /**
     * Validiert Registrierungsdaten für Studenten
     * @return true wenn alle Eingaben valid sind
     * @param username Benutzername
     * @param firstName Vorname
     * @param lastName Nachname
     * @param email E-Mail Adresse
     * @param password Passwort
     * @param passwordConfirmation Passwortbestätigung
     */
    public static boolean validateInput(String username, String firstName, String lastName, String email, String password, String passwordConfirmation) {
        if (!isValidVorname(firstName)) {
            Notification.show("Kein gültiger Vorname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!isValidNachname(lastName)) {
            Notification.show("Kein gültiger Nachname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Validiert Registrierungsdaten für Unternehmen
     * @return true wenn alle Eingaben valid sind
     * @param username Benutzername
     * @param unternehmenName Unternehmensname
     * @param email E-Mail Adresse
     * @param password Passwort
     * @param passwordConfirmation Passwortbestätigung
     */
    public static boolean validateInput(String username, String unternehmenName, String email, String password, String passwordConfirmation) {
        if (!isValidUnternehmenName(unternehmenName)) {
            Notification.show("Kein gültiger Unternehmensnamen (mindestens 3-30 Zeichen, nur Buchstaben, Leerzeichen und Ziffern).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Validiert Registrierungsdaten für Admins
     * @return true wenn alle Eingaben valid sind
     * @param username Benutzername
     * @param email E-Mail Adresse
     * @param password Passwort
     * @param passwordConfirmation Passwortbestätigung
     */
    public static boolean validateInput(String username, String email, String password, String passwordConfirmation) {
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Basis-Validierung für alle Benutzertypen
     * @return true wenn alle Eingaben valid sind
     * @param username Benutzername
     * @param email E-Mail Adresse
     * @param password Passwort
     * @param passwordConfirmation Passwortbestätigung
     */
    private static boolean checkDefaultInput(String username, String email, String password, String passwordConfirmation) {
        if (!isValidUsername(username)) {
            Notification.show("Kein gültiger Benutzername (3-20 Zeichen, nur Buchstaben und Ziffern).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!isValidEmail(email)) {
            Notification.show("Keine gültige E-Mail Adresse (zB test@test.de).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!password.equals(passwordConfirmation)) {
            Notification.show("Die Passwörter stimmen nicht überein.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!isPasswordComplex(password)) {
            Notification.show("Das Passwort muss 8-16 Zeichen lang sein und mindestens einen Großbuchstaben, einen Kleinbuchstaben und eine Zahl enthalten.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Überprüft, ob die E-Mail Adresse gültig ist
     * @return true wenn die E-Mail Adresse gültig ist
     * @param email E-Mail Adresse
     */
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

        // Zusätzlich: keine doppelten Punkte oder @
        if (email.contains("..") || email.contains("@@")) {
            return false;
        }

        return true;
    }

    /**
     * Überprüft, ob das Passwort komplex genug ist
     * @return true wenn das Passwort komplex genug ist
     * @param password Passwort
     */
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

    /**
     * Überprüft, ob der Unternehmensname gültig ist
     * @return true wenn der Unternehmensname gültig ist
     * @param unternehmenName Unternehmensname
     */
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

    /**
     * Überprüft, ob der Vorname gültig ist
     * @return true wenn der Vorname gültig ist
     * @param vorname Vorname
     */
    private static boolean isValidVorname(String vorname) {
        return isValidName(vorname);
    }

    /**
     * Überprüft, ob der Nachname gültig ist
     * @return true wenn der Nachname gültig ist
     * @param nachname Nachname
     */
    private static boolean isValidNachname(String nachname) {
        return isValidName(nachname);
    }

    /**
     * Überprüft, ob der Name gültig ist
     * @return true wenn der Name gültig ist
     * @param name Name
     */
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

    /**
     * Überprüft, ob der Benutzername gültig ist
     * @return true wenn der Benutzername gültig ist
     * @param username Benutzername
     */
    private static boolean isValidUsername(String username) {
        if (username.length() < 3 || username.length() > 20) {
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