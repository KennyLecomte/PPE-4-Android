package companytest3.applicationtest3;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextVille;
    EditText editTextCodePostal;
    EditText editTextRaison;
    Spinner spinnerBudgets;
    Spinner spinnerTranchesAge;
    CheckBox checkBoxVip;
    Spinner spinnerCategorieSociale;
    Spinner spinnerBateau1;
    Spinner spinnerBateau2;
    Spinner spinnerBateau3;
    Button ajouterBateau;
    boolean etatInternet;

    Button buttonRegister;

    public static final String ROOT_URL = "http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etatInternet = isNetworkAvailable();
        if(etatInternet==false)
        {
            Toast.makeText(MainActivity.this, "Vous devez avoir un accès à Internet",Toast.LENGTH_LONG).show();
        }
        else
        {
            editTextVille = (EditText) findViewById(R.id.ville);
            editTextCodePostal = (EditText) findViewById(R.id.codePostal);
            editTextRaison = (EditText) findViewById(R.id.raison);
            spinnerBudgets = (Spinner) findViewById(R.id.budget);
            spinnerTranchesAge = (Spinner) findViewById(R.id.trancheAge);
            checkBoxVip = (CheckBox) findViewById(R.id.vip);
            spinnerCategorieSociale = (Spinner) findViewById(R.id.categorieSociale);
            ajouterBateau = (Button) findViewById(R.id.ajouterBateau);
            spinnerBateau1 = (Spinner) findViewById(R.id.bateau1);
            spinnerBateau2 = (Spinner) findViewById(R.id.bateau2);
            spinnerBateau3 = (Spinner) findViewById(R.id.bateau3);

            buttonRegister = (Button) findViewById(R.id.buttonRegister);
            buttonRegister.setOnClickListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.tranchesAge, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTranchesAge.setAdapter(adapter);

            getJSON("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getBudgetsJSON");
            getJSON("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getCategoriesSocialesJSON");
            getJSON("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getBateauxJSON");

            ajouterBateau.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ajouterBateauActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void insertEnquete()
    {
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        RegisterAPI api = adapter.create(RegisterAPI.class);

        if( TextUtils.isEmpty(editTextVille.getText()))
        {
            Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs",Toast.LENGTH_LONG).show();
        }
        else
        {
            //Defining the method insertuser of our interface
            api.insertEnquete(

                    //Passing the values by getting it from editTexts
                    editTextVille.getText().toString(),
                    editTextCodePostal.getText().toString(),
                    editTextRaison.getText().toString(),
                    spinnerBudgets.getSelectedItem().toString(),
                    spinnerTranchesAge.getSelectedItem().toString(),
                    checkBoxVip.isChecked(),
                    spinnerCategorieSociale.getSelectedItem().toString(),
                    spinnerBateau1.getSelectedItem().toString(),
                    spinnerBateau2.getSelectedItem().toString(),
                    spinnerBateau3.getSelectedItem().toString(),

                    //Creating an anonymous callback
                    new Callback<Response>() {
                        @Override
                        public void success(Response result, Response response) {
                            //On success we will read the server's output using bufferedreader
                            //Creating a bufferedreader object
                            BufferedReader reader = null;

                            //An string to store output from the server
                            String output = "";

                            try {
                                //Initializing buffered reader
                                reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                                //Reading the output in the string
                                output = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Displaying the output as a toast
                            //Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //If any error occured displaying the error as toast
                            Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
            );

            editTextVille.setText(null);
            editTextCodePostal.setText(null);
            editTextRaison.setText(null);
            checkBoxVip.setChecked(false);
            spinnerBateau1.setSelection(0);
            spinnerBateau2.setSelection(0);
            spinnerBateau3.setSelection(0);
        }
    }

    //Overriding onclick method
    @Override
    public void onClick(View v)
    {
        insertEnquete();
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoSpinner(s, urlWebService);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoSpinner(String json, String url) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        if(url.equals("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getBudgetsJSON"))
        {
            String[] budgets = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                budgets[i] = obj.getString("min") + "-" + obj.getString("max");
            }
            spinnerBudgets.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    budgets));
        }
        else if(url.equals("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getCategoriesSocialesJSON"))
        {
            String[] categoriesSociales = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                categoriesSociales[i] = obj.getString("nomCategorie");
            }
            spinnerCategorieSociale.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    categoriesSociales));
        }
        else if(url.equals("http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/getBateauxJSON"))
        {
            String[] bateaux = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                bateaux[i] = obj.getString("modele");
            }
            spinnerBateau1.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    bateaux));

            spinnerBateau1.setSelection(0);

            spinnerBateau2.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    bateaux));

            spinnerBateau2.setSelection(0);

            spinnerBateau3.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    bateaux));

            spinnerBateau3.setSelection(0);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}