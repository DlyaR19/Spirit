package com.spirit.application.util;


import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for validating user input during registration.
 * Contains various methods for input validation such as username, email, password, and others.
 * This class is not instantiable.
 */
public class RegisterUtils {

    /**
     * Private constructor to prevent instantiation.
     * @throws UnsupportedOperationException if an attempt is made to instantiate the class
     */
    private RegisterUtils() {
        throw new UnsupportedOperationException("Class should not be instantiated, it is a utility class.");
    }

    /**
     * Validates the input for student registration.
     * @param username              the username to validate
     * @param firstName             the first name to validate
     * @param lastName              the last name to validate
     * @param email                 the email to validate
     * @param password              the password to validate
     * @param passwordConfirmation  the confirmation password to validate
     * @param birth                 the birthdate to validate
     * @return true if all inputs are valid, false otherwise
     */
    public static boolean validateInput(String username, String firstName, String lastName, String email, String password, String passwordConfirmation, LocalDate birth) {
        if (!isValidVorname(firstName)) {
            Notification.show("Kein gültiger Vorname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!isValidNachname(lastName)) {
            Notification.show("Kein gültiger Nachname (3-30 Zeichen, nur Buchstaben, Leerzeichen und Bindestriche).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (!isValidBirthdate(birth)){
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Validates the input for company registration.
     * @param username              the username to validate
     * @param unternehmenName       the company name to validate
     * @param email                 the email to validate
     * @param password              the password to validate
     * @param passwordConfirmation  the confirmation password to validate
     * @return true if all inputs are valid, false otherwise
     */
    public static boolean validateInput(String username, String unternehmenName, String email, String password, String passwordConfirmation) {
        if (!isValidUnternehmenName(unternehmenName)) {
            Notification.show("Kein gültiger Unternehmensnamen (mindestens 3-30 Zeichen, nur Buchstaben, Leerzeichen und Ziffern).", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Validates the input for general user registration.
     * @param username              the username to validate
     * @param email                 the email to validate
     * @param password              the password to validate
     * @param passwordConfirmation  the confirmation password to validate
     * @return true if all inputs are valid, false otherwise
     */
    public static boolean validateInput(String username, String email, String password, String passwordConfirmation) {
        return checkDefaultInput(username, email, password, passwordConfirmation);
    }

    /**
     * Checks the default inputs for validity: username, email, password, and password confirmation.
     * @param username              the username to validate
     * @param email                 the email to validate
     * @param password              the password to validate
     * @param passwordConfirmation  the confirmation password to validate
     * @return true if all default inputs are valid, false otherwise
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
     * Validates an email address.
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
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
     * Checks if a password is complex enough (length, uppercase, lowercase, digit).
     * @param password the password to validate
     * @return true if the password meets complexity requirements, false otherwise
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
     * Validates a company name (length and valid characters).
     * @param unternehmenName the company name to validate
     * @return true if the company name is valid, false otherwise
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
     * Validates a first name.
     * @param vorname the first name to validate
     * @return true if the first name is valid, false otherwise
     */
    private static boolean isValidVorname(String vorname) {
        return isValidName(vorname);
    }

    /**
     * Validates a last name.
     * @param nachname the last name to validate
     * @return true if the last name is valid, false otherwise
     */
    private static boolean isValidNachname(String nachname) {
        return isValidName(nachname);
    }

    /**
     * Validates a general name (used for both first name and last name).
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
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
     * Validates a username (length and valid characters).
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
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

    /**
     * Validates a birthdate (must not be in the future).
     * @param dateTime the birthdate to validate
     * @return true if the birthdate is valid, false otherwise
     */
    private static boolean isValidBirthdate(LocalDate dateTime) {
        if (dateTime == null) {
            Notification.show("Bitte geben Sie ein gültiges Geburtsdatum ein.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        if (dateTime.isAfter(today)){
            Notification.show("Das Geburtsdatum darf nicht in der Zukunft liegen.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        } else if (dateTime.isBefore(LocalDate.of(1900, 1, 1))){
            Notification.show("Das Geburtsdatum darf nicht vor 1900 liegen.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        } else if (dateTime.isAfter(today.minusYears(16))){
            Notification.show("Sie müssen mindestens 16 Jahre alt sein.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }
}