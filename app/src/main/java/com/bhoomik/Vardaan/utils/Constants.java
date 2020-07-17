package com.bhoomik.Vardaan.utils;

public class Constants {
    public static final String MY_PREFERENCE = "myPref";
    public static final String name = "nameKey";
    public static String email = "emailKey";
    public static final String password = "passwordKey";
    public static final String isactive = "isactiveKey";
    public static final String PREF_NAME = "LoggedInPref";
    public static String name_all = "nameKey";
    public static final String QUIZ_LANGUAGE_PREFERENCE = "quizLanguagePreference";
    public static final String GUEST_USER_NAME = "Guest user";
    public static final String GUEST_USER_PREF = "guestUSerPref";


    public static class Register {

        public static String Register_Url = "https://script.google.com/macros/s/AKfycbyplmgmCGEXvd7rs8jVnJozsOHRRrmXQyR4RApGI_pz5ysRH_oV/exec";
        public static final String ADD_USER_URL = Register_Url;
        public static final String KEY_LOST_IMAGE = "uImage";
        //public static final String KEY_LIST_IMAGE = "uImageList";
        public static final String KEY_Name_kp = "uNamekp";
        public static final String KEY_uName = "uName";
        public static final String KEY_Age = "uAge";
        public static final String KEY_Mobile = "uMobile";
        public static final String KEY_Category = "uCategory";
        public static final String KEY_City = "uCity";
        public static final String KEY_Fala = "uFala";
        public static final String KEY_Village = "uVillage";
        public static final String KEY_Rajasav = "uRajasav";
        public static final String KEY_GramPanchayat = "uGramPanchayat";
        public static final String KEY_Samiti = "uSamiti";
        public  static final String KEY_ACTION = "action";
    }

    public static class Forms {

        public static final int FORM_KIT = 0;
        public static final int FORM_STUDENTS = 1;

        public static final String KEY_ACTION = "action";
        public static final String STUDENTS_FORM_URL = "https://script.google.com/macros/s/AKfycbyNXhLVSp6LONDqEuqX5LCR03mC4NvkC5gMd1MB-ihx0cWw6pE/exec";
        static final String APP_SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycbx8CDXxPYSGA7lEokuAyzdMIoSih6hLrDoykl7M00frHGVyBxQ/exec";
        // public static final String APP_SCRIPT_WEB_APP_URL =   "https://script.google.com/macros/s/AKfycbxOJ_pvdpMzI8AbD8EYBSUIxr1FcZKhjbbHflPPxtBGIKUyvt2P/exec";
        public static final String ADD_USER_URL = APP_SCRIPT_WEB_APP_URL;

        public static final String KEY_LOST_IMAGE = "uImage";
        public static final String KEY_LIST_IMAGE = "uImageList";
        public static final String KEY_Name_kp = "uNamekp";
        public static final String KEY_uName = "uName";
        public static final String KEY_Age = "uAge";
        public static final String KEY_Date = "uDate";
        public static final String KEY_Category = "uCategory";
        public static final String KEY_Place = "uPlace";
        public static final String KEY_Fala = "uFala";
        public static final String KEY_Village = "uVillage";
        public static final String KEY_Rajasav = "uRajasav";
        public static final String KEY_GramPanchayat = "uGramPanchayat";
        public static final String KEY_Samiti = "uSamiti";
    }

    public static class User {
        public static final String USER_NAME = "name";
        public static final String USER_CONTACT_NUMBER = "contact number";
        public static final String ACCESS_MODULE = "access_module";
        public static final String ACCESS_QUESTION = "access_question";
        public static final String PROGRESS_LINK = "progress_Link";
        public static final String PROGRESS_PDF = "progress_Pdf";
        public static final String PROGRESS_LECTURE = "progress_Lecture";

        public static int accessModule;
        public static int accessQuestion;
        public static boolean progressLink;
        public static boolean progressPdf;
        public static boolean progressLecture;
    }

    public static class Organisation {
        public static final String ORGANISATION_NAME = "organisation_name";
        public static final int SHRUSHTI = 0;
        public static final int JUMIO = 1;
    }

}
