package com.spirit.application.util;

/**
 * Utility class for managing global constants, such as URLs and roles.
 */
public class Globals {

    public static final String CURRENT_USER = "current_user";
    private static final String EXCEPTION_1 = "This is a utility class and cannot be instantiated";


    private Globals() {
        throw new UnsupportedOperationException(EXCEPTION_1);
    }

    /**
     * Nested class for defining constants related to pages.
     */
    public static class Pages {

        public static final String MAIN = "";
        public static final String APP = "main/";
        public static final String LOGIN = "login";
        //public static final String FORGOT_PASSWORD = "forgot/password";
        //public static final String RESET_PASSWORD = "reset/password";
        //public static final String UPDATE_PASSWORD = "update/password";
        public static final String SIGNUP = "register";
        public static final String PROFIL_STUDENT = "student/profil";
        public static final String PROFIL_UNTERNEHMEN = "unternehmen/profil";
        public static final String MY_JOBPOSTS = "unternehmen/my-jobposts";
        public static final String SHOW_BEWERBUNG = "unternehmen/show/bewerbung";
        public static final String JOBPOST = "unternehmen/jobpost";
        public static final String SUCHE_STUDENT = "student/suche";
        public static final String BEWERBUNG_STUDENT = "student/bewerbung";
        public static final String ABOUTUS = "about";
        public static final String DASHBOARD = "dashboard";
        public static final String CHATLIST = "chatlist";
        public static final String CHAT = "chat";
        public static final String PROFIL_SEARCH = "profilsearch";

        private Pages() {
            throw new UnsupportedOperationException(EXCEPTION_1);
        }
    }

    /**
     * Nested class for defining role constants.
     */
    public static class Roles {

        public static final String STUDENT = "student";
        public static final String UNTERNEHMEN = "unternehmen";

        private Roles() {
            throw new UnsupportedOperationException(EXCEPTION_1);
        }
    }
}
