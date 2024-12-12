package com.spirit.application.util;


import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Zentrale Konstanten-Klasse der Anwendung
 */

public class Globals {

    // Globale Konstanten
    public static final String CURRENT_USER = "current_user";
    public static final String BASE_URL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    private static final String EXCEPTION_1 = "This is a utility class and cannot be instantiated";

    /**
     * Privater Konstruktor verhindert Instanziierung
     */
    private Globals() {
        throw new UnsupportedOperationException(EXCEPTION_1);
    }

    /**
     * Innere Klasse für URL-Pfade
     */
    public static class Pages {

        public static final String MAIN = "";
        public static final String APP = "main/";
        public static final String LOGIN = "login";
        public static final String FORGOT_PASSWORD = "forgot/password";
        public static final String RESET_PASSWORD = "reset/password";
        public static final String UPDATE_PASSWORD = "update/password";
        public static final String SIGNUP = "register";
        public static final String PROFIL_STUDENT = "student/profile";
        public static final String PROFIL_UNTERNEHMEN = "unternehmen/profile";
        public static final String MY_JOBPOSTS = "unternehmen/my-jobposts";
        public static final String SHOW_BEWERBUNG = "unternehmen/show/bewerbung";
        public static final String JOBPOST = "unternehmen/jobpost";
        public static final String SUCHE_STUDENT = "student/suche";
        public static final String BEWERBUNG_STUDENT = "student/bewerbung";
        public static final String ABOUTUS = "about";
        public static final String DASHBOARD = "dashboard";
        public static final String CHAT_STUDENT = "student/chat";
        public static final String CHAT_UNTERNEHMEN = "unternehmen/chat";

        private Pages() {
            throw new UnsupportedOperationException(EXCEPTION_1);
        }
    }

    /**
     * Innere Klasse für Benutzerrollen
     */
    public static class Roles {

        public static final String STUDENT = "student";
        public static final String UNTERNEHMEN = "unternehmen";

        private Roles() {
            throw new UnsupportedOperationException(EXCEPTION_1);
        }
    }
}
