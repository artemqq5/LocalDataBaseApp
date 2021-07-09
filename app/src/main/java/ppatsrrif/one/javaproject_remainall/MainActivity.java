package ppatsrrif.one.javaproject_remainall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // variable for registration
    MaterialButton nextButton, skipButton;
    MaterialRadioButton radioButtonMale, radioButtonFemale;
    TextInputLayout editName, editLastName, editGmail;

    // create a object DBManager
    DBManager dbManager = new DBManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // open database
        dbManager.openDB();

        // declaring variable for registration
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        editName = findViewById(R.id.editName);
        editLastName = findViewById(R.id.editLastName);
        editGmail = findViewById(R.id.editGmail);

        // setOnClick on buttons
        nextButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);



    }


    // set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextButton:

                // get data from all TextInput
                String name = editName.getEditText().getText().toString();
                String lastName = editLastName.getEditText().getText().toString();
                String gmail = editGmail.getEditText().getText().toString();
                String sex;

                // get sex type
                if(radioButtonMale.isChecked()) {
                    sex = "Мужчина";
                } else sex = "Женщина";

                // check name, lastName, email on regex
                if(checkName(name, "^[A-Z][a-z]+$", 2, editName) &&
                        checkName(lastName, "^[A-Z][a-z]+$", 2, editLastName) &&
                        checkLoginEmail(gmail,  4, editGmail, 3)) {

                    // write data to database
                    dbManager.writeInDB(name, lastName, gmail, sex);

                    // start next activity
                    Intent intent = new Intent(this, List.class);
                    startActivity(intent);

                }


                break;

            case R.id.skipButton:
                // start next activity without write to database
                Intent intent = new Intent(this, List.class);
                startActivity(intent);
                break;
        }
    }

    // check email on coincidence and contains special symbol
    public boolean checkLoginEmail(String str, int i, TextInputLayout editText, int who) {

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
                    editText.setError("Это данные другого пользователя");
                    return false;
                }
            } else {
                editText.setError("Поле не содержит специальный символ @");
                return false;
            }
        } else {
            editText.setError("Поле содержит мало символов");
            return false;
        }
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
                editText.setError("Поле должно начинаться с большой буквы и не содержать посторонних символов");
                return false;
            }
        } else {
            editText.setError("Поле содержит мало символов");
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close database when activity destroy
        dbManager.closeDB();
    }

}
