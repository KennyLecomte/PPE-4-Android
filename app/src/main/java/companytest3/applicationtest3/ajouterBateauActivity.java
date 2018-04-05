package companytest3.applicationtest3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ajouterBateauActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextModele;
    EditText editTextCategorie;

    Button buttonAjouter;

    public static final String ROOT_URL = "http://gr08.sio-cholet.fr/PPE4Symfony/Tobat/Symfony/web/app_dev.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_bateau);

        editTextModele = (EditText) findViewById(R.id.modele);
        editTextCategorie = (EditText) findViewById(R.id.categorie);

        buttonAjouter = (Button) findViewById(R.id.buttonAjouter);
        buttonAjouter.setOnClickListener(this);
    }

    private void insertBateau()
    {
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        AjoutAPI api = adapter.create(AjoutAPI.class);

        //Defining the method insertuser of our interface
        api.insertBateau(

                //Passing the values by getting it from editTexts
                editTextModele.getText().toString(),
                editTextCategorie.getText().toString(),

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
                        Toast.makeText(ajouterBateauActivity.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(ajouterBateauActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        editTextModele.setText(null);
        editTextCategorie.setText(null);
    }

    @Override
    public void onClick(View v)
    {
        insertBateau();
    }

}
