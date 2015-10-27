package se.uu.csproject.monadclient;

//import com.google.common.base.Charsets;

/**
 *
 */
public class ClientAuthentication extends Authentication {
    private static String[] profile = new String[10];
    /*
     * 0: clientId ("1", "2", ...)
     * 1: username
     * 2: password ("0" or "1")
     * 3: email
     * 4: phone
     * 5: language ("en", ...)
     * 6: storeLocation ("0" or "1")
     * 7: notificationsAlert ("0" or "1")
     * 8 recommendationsAlert ("0" or "1")
     * 9: theme ("0", "1", ...)
     */

    public static void setClientId(String clientId) {
        profile[0] = clientId;
    }
    public static String getClientId() {
        return profile[0];
    }

    public static void setUsername(String username) {
        profile[1] = username;
    }
    public static String getUsername() {
        return profile[1];
    }

    public static void setPassword(String password) {
        profile[2] = password;
    }
    public static String getPassword() {
        return profile[2];
    }

    public static void setEmail(String email) {
        profile[3] = email;
    }
    public static String getEmail() {
        return profile[3];
    }

    public static void setPhone(String phone) {
        profile[4] = phone;
    }
    public static String getPhone() {
        return profile[4];
    }

    public static void setLanguage(String language) {
        profile[5] = language;
    }
    public static String getLanguage() {
        return profile[5];
    }

    public static void setStoreLocation(String storeLocation) {
        profile[6] = storeLocation;
    }
    public static String getStoreLocation() {
        return profile[6];
    }
    public static void updateStoreLocation() {

        if (profile[6].equalsIgnoreCase("1")) {
            profile[6] = "0";
        }
        else {
            profile[6] = "1";
        }
    }

    public static void setNotificationsAlert(String notificationsAlert) {
        profile[7] = notificationsAlert;
    }
    public static String getNotificationsAlert() {
        return profile[7];
    }
    public static void updateNotificationsAlert() {

        if (profile[7].equalsIgnoreCase("1")) {
            profile[7] = "0";
        }
        else {
            profile[7] = "1";
        }
    }

    public static void setRecommendationsAlert(String recommendationsAlert) {
        profile[8] = recommendationsAlert;
    }
    public static String getRecommendationsAlert() {
        return profile[8];
    }
    public static void updateRecommendationsAlert() {

        if (profile[8].equalsIgnoreCase("1")) {
            profile[8] = "0";
        }
        else {
            profile[8] = "1";
        }
    }

    public static void setTheme(String theme) {
        profile[9] = theme;
    }
    public static String getTheme() {
        return profile[9];
    }

    public static String profileToString() {
        String strProfile = "\nclientId: " + getClientId()
                          + "\nusername: " + getUsername()
                          + "\npassword: " + getPassword()
                          + "\nemail: " + getEmail()
                          + "\nphone: " + getPhone()
                          + "\nlanguage: " + getLanguage()
                          + "\nstoreLocation: " + getStoreLocation()
                          + "\nnotificationsAlert: " + getNotificationsAlert()
                          + "\nrecommendationsAlert: " + getRecommendationsAlert()
                          + "\ntheme: " + getTheme();
        return strProfile;
    }

    public static void initProfile() {
        setClientId("");
        setUsername("");
        setPassword("0");
        setEmail("");
        setPhone("");
        defaultSettings();
    }

    public static void defaultSettings() {
        setLanguage("en");
        setStoreLocation("1");
        setNotificationsAlert("1");
        setRecommendationsAlert("1");
        setTheme("1");
    }

    public static void updateProfile(String clientId, String username, String password, String email, String phone,
                                     String language, String storeLocation, String notificationsAlert,
                                     String recommendationsAlert, String theme) {
        setClientId(clientId);
        updateProfileData(username, email, phone);
        setPassword(password);
        updateSettings(language, storeLocation, notificationsAlert, recommendationsAlert, theme);
    }

    public static void updateProfileData(String username, String email, String phone) {
        setUsername(username);
        setEmail(email);
        setPhone(phone);
    }

    public static void updateSettings(String language, String storeLocation, String notificationsAlert,
                                      String recommendationsAlert, String theme) {
        setLanguage(language);
        setStoreLocation(storeLocation);
        setNotificationsAlert(notificationsAlert);
        setRecommendationsAlert(recommendationsAlert);
        setTheme(theme);
    }

    public static void updateProfileAfterSignUp(String clientId, String username, String password,
                                                String email, String phone) {
        setClientId(clientId);
        setPassword(password);
        updateProfileData(username, email, phone);
        defaultSettings();
    }

    public static String postSignUpRequest(String username, String password, String email, String phone) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_sign_up";
        String urlParameters = "username=" + username + "&password=" + password
                             + "&email=" + email + "&phone=" + phone;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processSignUpResponse(username, email, phone, response);
    }

    public static String processSignUpResponse(String username, String email, String phone, String response) {
        String responseMessage = "";

        /* Username already in use */
        if (response.startsWith("01")) {
            responseMessage = "Username already in use (01)";
        }
        /* Email already in use */
        else if (response.startsWith("02")) {
            responseMessage = "Email already in use (02)";
        }
        /* Phone already in use */
        else if (response.startsWith("03")) {
            responseMessage = "Phone already in use (03)";
        }
        /*
         * Successful signUp request - New client registered to the database
         * Response: "1|clientId"
         */
        else if (response.startsWith("1|")) {
            String clientId = response.substring(2);
            /* updateProfileAfterSignUp(clientId, username, password, email, phone) */
            updateProfileAfterSignUp(clientId, username, "1", email, phone);
            responseMessage = "Success (1) - User Id: " + getClientId();
        }
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postSignInRequest(String username, String password) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_sign_in";
        String urlParameters = "username=" + username + "&password=" + password;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processSignInResponse(username, response);
    }

    public static String processSignInResponse(String username, String response) {
        String responseMessage = "";
        String[] responseData = new String[8];
        /*
         * 0: clientId
         * 1: email
         * 2: phone
         * 3: language
         * 4: storeLocation
         * 5: notificationsAlert
         * 6: recommendationsAlert
         * 7: theme
         */
        String temp = "";
        int index = 0;

        /*
         * Successful signIn request
         * Response: "1|clientId|email|phone|language|
         *              storeLocation|notificationsAlert|recommendationsAlert|theme"
         */
        if (response.startsWith("1|")) {

            /* Process response and parse profile data */
            for (int i = 2; i < response.length(); i++) {
                char c = response.charAt(i);

                if (c != '|') {
                    temp = temp + c;
                }
                else {
                    responseData[index] = temp;
                    index++;
                    temp = "";
                }
            }
            responseData[index] = temp;

            /* Update profile: clientId, username, password, email, phone,
             *                 language, storeLocation, notificationsAlert,
             *                 recommendationsAlert, theme
             */
            updateProfile(responseData[0], username, "1", responseData[1], responseData[2],
                          responseData[3], responseData[4], responseData[5],
                          responseData[6], responseData[7]);

            responseMessage = "Success (1) - " + response + profileToString();
        }
        /* Wrong crendentials */
        else if (response.startsWith("0")) {
            responseMessage = "Wrong Crendentials (0)";
        }
        /* ERROR case */
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postGoogleSignInRequest(String email) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/google_sign_in";
        String urlParameters = "email=" + email;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processGoogleSignInResponse(email, response);
    }

    public static String processGoogleSignInResponse(String email, String response) {
        String responseMessage = "";
        String[] responseData = new String[9];
        /*
         * 0: clientId
         * 1: username
         * 2: password
         * 3: phone
         * 4: language
         * 5: storeLocation
         * 6: notificationsAlert
         * 7: recommendationsAlert
         * 8: theme
         *
         * email: already exists in the application
         */
        String temp = "";
        int index = 0;

        /*
         * Successful GoogleSignIn request - Client already existed in database
         * Response: "1|clientId|username|password|phone|
         *              language|storeLocation|notificationsAlert|recommendationsAlert|theme"
         */
        if (response.startsWith("1|")) {

            /* Process response and parse profile data */
            for (int i = 2; i < response.length(); i++) {
                char c = response.charAt(i);

                if (c != '|') {
                    temp = temp + c;
                }
                else {
                    responseData[index] = temp;
                    index++;
                    temp = "";
                }
            }
            responseData[index] = temp;

            /* Update profile: clientId, username, password, email, phone,
             *                 language, storeLocation, notificationsAlert,
             *                 recommendationsAlert, theme
             */
            updateProfile(responseData[0], responseData[1], responseData[2], email, responseData[3],
                          responseData[4], responseData[5], responseData[6],
                          responseData[7], responseData[8]);

            responseMessage = "Success (1) - " + response + profileToString();
        }

        /*
         * Successful GoogleSignIn request - New client registered to the database
         * Response: "2|clientId"
         */
        else if (response.startsWith("2|")) {
            String clientId = response.substring(2);
            /* updateProfileAfterSignUp(clientId, username, password, email, phone) */
            updateProfileAfterSignUp(clientId, "", "0", email, "");
            responseMessage = "Success (1) - User Id: " + getClientId();
        }
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postProfileUpdateRequest(String clientId, String username, String email, String phone) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_profile_update";
        String urlParameters = "client_id=" + clientId + "&username=" + username
                             + "&email=" + email + "&phone=" + phone;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processProfileUpdateResponse(username, email, phone, response);
    }

    public static String processProfileUpdateResponse(String username, String email, String phone, String response) {
        String responseMessage = "";

        /* Username already in use */
        if (response.startsWith("01")) {
            responseMessage = "Username already in use (01)";
        }
        /* Email already in use */
        else if (response.startsWith("02")) {
            responseMessage = "Email already in use (02)";
        }
        /* Phone already in use */
        else if (response.startsWith("03")) {
            responseMessage = "Phone already in use (03)";
        }
        /* Successful profileUpdate request */
        else if (response.startsWith("1")) {
            updateProfileData(username, email, phone);
            responseMessage = "Success (1)" + profileToString();
        }
        /* ERROR case */
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postSettingsUpdateRequest(String clientId, String language,
                                                   String storeLocation, String notificationsAlert,
                                                   String recommendationsAlert, String theme) {

        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_settings_update";
        String urlParameters = "client_id=" + clientId
                             + "&language=" + language
                             + "&store_location=" + storeLocation
                             + "&notifications_alert=" + notificationsAlert
                             + "&recommendations_alert=" + recommendationsAlert
                             + "&theme=" + theme;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processSettingsUpdateResponse(language, storeLocation, notificationsAlert,
                                             recommendationsAlert, theme, response);
    }

    public static String processSettingsUpdateResponse(String language, String storeLocation,
                                                       String notificationsAlert, String recommendationsAlert,
                                                       String theme, String response) {
        String responseMessage = "";

        /* Successful settingsUpdateRequest */
        if (response.equals("1")) {
            updateSettings(language, storeLocation, notificationsAlert, recommendationsAlert, theme);
            responseMessage = "Success (1)" + profileToString();
        }
        /* ERROR case */
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postExistingPasswordUpdateRequest(String clientId, String oldPassword, String newPassword) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_existing_password_update";
        String urlParameters = "client_id=" + clientId
                             + "&old_password=" + oldPassword
                             + "&new_password=" + newPassword;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processExistingPasswordUpdateResponse(newPassword, response);
    }

    public static String processExistingPasswordUpdateResponse(String newPassword, String response) {
        String responseMessage = "";

        /* Successful existingPasswordUpdateRequest */
        if (response.equals("1")) {
            setPassword(newPassword);
            responseMessage = "Success (1)" + profileToString();
        }
        /* ERROR case */
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }

    public static String postForgottenPasswordResetRequest(String email, String newPassword) {
        String request = AUTHENTICATION_HOST + AUTHENTICATION_PORT + "/client_forgotten_password_reset";
        String urlParameters = "email=" + email + "&new_password=" + newPassword;

        /* Send the request to the Authentication Module */
        String response = postRequest(request, urlParameters);

        /*
         * By default, Erlang adds the newline '\n' character at the beginning of response.
         * For this reason substring() function is used
         */
        response = response.substring(1);
        // response = response.trim();

        /* Process Authentication Module's response */
        return processForgottenPasswordResetResponse(newPassword, response);
    }

    public static String processForgottenPasswordResetResponse(String newPassword, String response) {
        String responseMessage = "";

        /* Successful existingPasswordUpdateRequest */
        if (response.equals("1")) {
            setPassword(newPassword);
            responseMessage = "Success (1)" + profileToString();
        }
        /* ERROR case */
        else {
            responseMessage = "ERROR - " + response;
        }
        return responseMessage;
    }
}
