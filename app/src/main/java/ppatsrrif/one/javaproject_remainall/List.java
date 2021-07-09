package ppatsrrif.one.javaproject_remainall;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class List extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    // button for control app
    MaterialButton deleteButton, dropButton;

    // listview, adapter, and arraylist with data
    ListView listPeople;
    ArrayAdapter adapterMultiple;
    ArrayList<String> listLastNames = new ArrayList();

    // create DBManager object
    DBManager dbManager = new DBManager(this);

    // dialog and his components
    MaterialTextView namePersonD, emailPersonD, sexPersonD;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        // open database
        dbManager.openDB();

        // declaring variable
        deleteButton = findViewById(R.id.deleteButton);
        dropButton = findViewById(R.id.dropButton);

        listPeople = findViewById(R.id.listPeople);

        namePersonD = dialog.findViewById(R.id.namePersonD);
        emailPersonD = dialog.findViewById(R.id.emailPersonD);
        sexPersonD = dialog.findViewById(R.id.sexPersonD);

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
        if(!listLastNames.isEmpty() && listLastNames != null) {
            switch (view.getId()) {
                case R.id.deleteButton:

                    // create boolean list with checked positions
                    SparseBooleanArray booleanArray = listPeople.getCheckedItemPositions();

                    // create copy list to refresh listView list
                    ArrayList<String> copy = new ArrayList<>();

                    // delete from database elements whose has in boolean list
                    for(int i=0; i<booleanArray.size(); i++) {
                        if(booleanArray.get(i)) {
                            // remove element from database
                            dbManager.deleteFromDB(listLastNames.get(i));
                            // add remove element to copy list
                            copy.add(listLastNames.get(i));
                        }
                    }

                    // remove elements of list listview whose was delete from database
                    for(int i=0; i<listLastNames.size(); i++) {
                        if(listLastNames.contains(copy.get(i))) {
                            listLastNames.remove(copy.get(i));
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
        ArrayList<String> sexes = dbManager.readDB(4);
        ArrayList<String> lastName = dbManager.readDB(2);

        namePersonD.setText(names.get(i) + " " + lastName.get(i));
        emailPersonD.setText("Почта: " + emails.get(i));
        sexPersonD.setText("Стать: " + sexes.get(i));

        dialog.show();


        return false;
    }

    public void inflateList() {
        // get list from database with data lastName
        listLastNames = dbManager.readDB(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // close database when activity destroy
        dbManager.closeDB();
    }
}