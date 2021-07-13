package ppatsrrif.one.javaproject_remainall.HelperClass;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ppatsrrif.one.javaproject_remainall.DataBase.DBManager;
import ppatsrrif.one.javaproject_remainall.R;

public class CheckStr {

    private final Context context;

    public CheckStr(Context context) {
        this.context = context;
    }

    // check name and lastName
    public boolean checkName(String str, String reg, int i, TextInputLayout editText) {

        // regex for checking
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(str);

        // set off all errors with TextInput
        editText.setError(null);

        // checking on length stroke
        if(!str.equals(" ") && str.length() > i) {
            // check on special regex
            if(m.matches()) {
                return true;
            } else {
                editText.setError(context.getResources().getString(R.string.error_caps));
                return false;
            }
        } else {
            editText.setError(context.getResources().getString(R.string.error_characters));
            return false;
        }
    }

    // check email on coincidence and contains special symbol
    public boolean checkLoginEmail(String str, int i, TextInputLayout editText, int who, DBManager dbManager) {

        // set off all errors on TextInput
        editText.setError(null);

        // checking on length stroke
        if(!str.equals(" ") && str.length() > i) {
            // check on special symbol
            if(str.contains("@")) {
                // check on coincidence
                if(dbManager.checkLogin(str, who)) {
                    return true;
                } else {
                    editText.setError(context.getResources().getString(R.string.error_another_data));
                    return false;
                }
            } else {
                editText.setError(context.getResources().getString(R.string.error_contain));
                return false;
            }
        } else {
            editText.setError(context.getResources().getString(R.string.error_characters));
            return false;
        }
    }

}
