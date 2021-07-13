package ppatsrrif.one.javaproject_remainall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ppatsrrif.one.javaproject_remainall.DataBase.DBManager;
import ppatsrrif.one.javaproject_remainall.HelperClass.CheckStr;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // variable for registration
    private MaterialButton nextButton, skipButton;
    private MaterialRadioButton radioButtonMale, radioButtonFemale;
    private TextInputLayout editName, editLastName, editGmail;

    // create a object DBManager
    private DBManager dbManager = new DBManager(this);

    //create object for check str
    CheckStr checkStr = new CheckStr(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // open database
        dbManager.openDB();

        // initializing all variable
        initialize();

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
                String gender;

                // get sex type
                if(radioButtonMale.isChecked()) {
                    gender = getResources().getString(R.string.male);
                } else gender = getResources().getString(R.string.female);

                // check name, lastName, email on regex
                if(checkStr.checkName(name, "^[A-Z][a-z]+$", 2, editName) &&
                        checkStr.checkName(lastName, "^[A-Z][a-z]+$", 2, editLastName) &&
                        checkStr.checkLoginEmail(gmail,  4, editGmail, 3, dbManager)) {

                    // write data to database
                    dbManager.writeInDB(name, lastName, gmail, gender);

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



    private void initialize() {
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        editName = findViewById(R.id.editName);
        editLastName = findViewById(R.id.editLastName);
        editGmail = findViewById(R.id.editGmail);
    }

    // saving when screen changing orientation
    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("textName", editName.getEditText().getText().toString());
        outState.putString("textLastName", editLastName.getEditText().getText().toString());
        outState.putString("textEmail", editGmail.getEditText().getText().toString());
    }

    // get saved data if needed
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        editName.getEditText().setText(savedInstanceState.getString("textName"));
        editLastName.getEditText().setText(savedInstanceState.getString("textLastName"));
        editGmail.getEditText().setText(savedInstanceState.getString("textEmail"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close database when activity destroy
        dbManager.closeDB();
    }

}
