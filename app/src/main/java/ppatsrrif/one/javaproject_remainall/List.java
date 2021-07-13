package ppatsrrif.one.javaproject_remainall;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import ppatsrrif.one.javaproject_remainall.DataBase.DBManager;

public class List extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    // button for control app
    private MaterialButton deleteButton, dropButton;

    // listview, adapter, and arraylist with data
    private ListView listPeople;
    private ArrayAdapter adapterMultiple;

    // lists lastName & email
    private ArrayList<String> listLastNames = new ArrayList();
    private ArrayList<String> listEmail = new ArrayList();

    // create DBManager object
    private final DBManager dbManager = new DBManager(this);

    // dialog and his components
    private MaterialTextView namePersonD, emailPersonD, genderPersonD;
    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // creating dialog
        createDialog();

        // open database
        dbManager.openDB();

        // initializing variable
        initialize();

        // inflate arraylist with data
        inflateList();

        // create adapter
        adapterMultiple = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listLastNames);

        listPeople.setAdapter(adapterMultiple);

        // setOnclick on buttons and listview
        deleteButton.setOnClickListener(this);
        dropButton.setOnClickListener(this);

        listPeople.setOnItemLongClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(!listEmail.isEmpty()) {
            switch (view.getId()) {
                case R.id.deleteButton:

                    // create boolean list with checked positions
                    SparseBooleanArray booleanArray = listPeople.getCheckedItemPositions();

                    // create copy list to refresh listView list
                    ArrayList<String> copyEmail = new ArrayList<>();
                    ArrayList<String> copyLastName = new ArrayList<>();

                    // delete from database elements whose has in boolean list
                    for(int i=0; i<booleanArray.size(); i++) {
                        if(booleanArray.get(i)) {
                            // remove element from database
                            dbManager.deleteFromDB(listEmail.get(i));

                            // add remove element to copy list
                            copyEmail.add(listEmail.get(i));
                            copyLastName.add(listLastNames.get(i));
                        }
                    }

                    // remove elements of list listview whose was delete from database
                    for(int i=0; i<copyEmail.size(); i++) {
                        if(listEmail.contains(copyEmail.get(i))) {
                            listEmail.remove(copyEmail.get(i));
                            listLastNames.remove(copyLastName.get(i));
                        }
                    }

                    // update adapter listview
                    adapterMultiple.notifyDataSetChanged();

                case R.id.dropButton:
                    // set uncheck all elements
                    for(int i=0; i<listLastNames.size(); i++) {
                        listPeople.setItemChecked(i, false);
                    }
                    break;
            }
        }

    }

    

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        // get all data from database and show dialog
        ArrayList<String> names = dbManager.readDB(1);
        ArrayList<String> emails = dbManager.readDB(3);
        ArrayList<String> genders = dbManager.readDB(4);
        ArrayList<String> lastName = dbManager.readDB(2);

        namePersonD.setText(names.get(i) + " " + lastName.get(i));
        emailPersonD.setText(getResources().getString(R.string.email_dialog) + emails.get(i));
        genderPersonD.setText(getResources().getString(R.string.gender_dialog) + genders.get(i));

        dialog.show();


        return false;
    }

    private void inflateList() {
        // get list from database with data lastName
        listLastNames = dbManager.readDB(2);

        // get list from database with data email
        listEmail = dbManager.readDB(3);
    }

    private void createDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
    }

    private void initialize() {

        deleteButton = findViewById(R.id.deleteButton);
        dropButton = findViewById(R.id.dropButton);

        listPeople = findViewById(R.id.listPeople);

        namePersonD = dialog.findViewById(R.id.namePersonD);
        emailPersonD = dialog.findViewById(R.id.emailPersonD);
        genderPersonD = dialog.findViewById(R.id.sexPersonD);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close database when activity destroy
        dbManager.closeDB();
    }
}