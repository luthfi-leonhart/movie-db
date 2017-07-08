package com.upwardproject.moviedb.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Dark on 13/02/2016.
 */
public class EmailUtil {

    public static boolean isEmailValidated(String email) {
        int pos = email.indexOf("@");
        if (pos == -1 || pos == 0)
            return false; // inserted email doesn't contain "@" or positioned at the first character

        pos = email.indexOf(".", pos); // Search if there is any character "." after "@"

        // inserted email doesn't contain character "." after "@" or it's at the end of the word
        return !(pos == -1 || pos == email.length() - 1);
    }

    public static void sendEmail(Context ctx, String[] recipients, String subject, String message) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (message != null) email.putExtra(Intent.EXTRA_TEXT, message);

        email.setType("message/rfc822");

        ctx.startActivity(Intent.createChooser(email, "Choose client"));
    }
}
