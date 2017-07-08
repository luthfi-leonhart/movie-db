package com.upwardproject.moviedb.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class LNumber {

    public static final String CURRENCY_RUPIAH = "IDR",
            CURRENCY_DOLLAR = "USD";

    public static String thousandFormat(int value, boolean abbreviate) {
        if (abbreviate) {
            int index = 0;
            String suffix = "";
            double temp = value;

            while (temp > 1000) {
                temp /= 1000;
                index++;
            }

            temp = decimalLimiter(String.valueOf(temp), 2);

            if (index == 1) suffix = "K";
            else if (index == 2) suffix = "M";

            // Convert temp to int if it doesn't need a decimal (in case like n.0)
            return (temp % 1 == 0) ? ((int) temp + suffix) : (temp + suffix);
        }

        return String.format(Locale.getDefault(), "%,d", value);
    }

    public static String thousandFormat(double value, boolean abbreviate) {
        if (abbreviate) {
            int index = 0;
            String suffix = "";
            double temp = value;

            while (temp > 1000) {
                temp /= 1000;
                index++;
            }

            temp = decimalLimiter(String.valueOf(temp), 2);

            if (index == 1) suffix = "K";
            else if (index == 2) suffix = "M";

            // Convert temp to int if it doesn't need a decimal (in case like n.0)
            return (temp % 1 == 0) ? ((int) temp + suffix) : (temp + suffix);
        }

        return String.format(Locale.getDefault(), "%,.0f", value);
    }

    public static double decimalLimiter(String value, int limitValue) {
        double dValue = Double.parseDouble(value.replace(",", "."));
        return decimalLimiter(dValue, limitValue);
    }

    public static double decimalLimiter(double value, int limitValue) {
        if (value == 0) return value;

		/*
         * limit to limitValue decimal numbers
		 * change decimal format using ".", since there are some phones that uses "," as decimal format
		 */
        String limiter = "#0.";
        for (int i = 0; i < limitValue; i++) {
            limiter += "#";
        }

        DecimalFormat df = new DecimalFormat(limiter);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        String formattedDistance = df.format(value);

        return Double.parseDouble(formattedDistance);
    }

    public static String currencyFormat(int value, String currency) {
        if (value == 0) return "-";

        return currency + " " + String.format(Locale.getDefault(), "%,d", value);
    }

    public static String currencyFormat(int value, DecimalFormatSymbols symbols) {
        DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        numberFormat.setDecimalFormatSymbols(symbols);
        return numberFormat.format(value);
    }

    public static DecimalFormatSymbols getRupiahCurrencySymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("IDR ");
        symbols.setGroupingSeparator('.');
        symbols.setMonetaryDecimalSeparator(',');
        return symbols;
    }

    // Change formatted number of currency to integer value
    public static int currencyToNumber(String value) {
        if (TextUtils.isEmpty(value)) return 0;

        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).parse(value).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] splits = value.split(" ");

        String number = "";

        if (splits.length == 1) { // No currency before value
            number = splits[0];
        } else if (splits.length == 2) { // ex : Rp 4,000,000
            number = splits[1];
        }

        number = number.replace(",", "").replace(".", "");

        return Integer.parseInt(number);
    }

}