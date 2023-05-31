package com.unir.retrofit;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


interface LocationService {
    @GET("76801058/json/")
    Call<Address> getAddress(@Path("cep") String cep);
}


public class MainActivity extends AppCompatActivity {
    private EditText cepEditText;
    private Button searchButton;
    private TextView addressTextView;

    private static final String BASE_URL = "https://viacep.com.br/ws";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cepEditText = findViewById(R.id.editText_cep);
        searchButton = findViewById(R.id.button_search);
        addressTextView = findViewById(R.id.textView_address);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        LocationService locationService = retrofit.create(LocationService.class);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = cepEditText.getText().toString();


                Call<Address> call = locationService.getAddress(cep);
                call.enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        if (response.isSuccessful()) {
                            Address address = response.body();


                            addressTextView.setText(
                                    "CEP: " + address.getCep() + "\n" +
                                            "Logradouro: " + address.getLogradouro() + "\n" +
                                            "Complemento: " + address.getComplemento() + "\n" +
                                            "Bairro: " + address.getBairro() + "\n" +
                                            "Localidade: " + address.getLocalidade() + "\n" +
                                            "UF: " + address.getUf()
                            );
                        } else {
                            addressTextView.setText("Erro na chamada à API: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        addressTextView.setText("Erro na requisição: " + t.getMessage());
                    }
                });
            }
        });
    }
}
