package com.firstapp.ratingapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileViewActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewContact;
    private TextView textViewVehicleDetails;
    private TextView textViewLicensePlate;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        // Inițializarea elementelor vizuale
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewContact = findViewById(R.id.textViewContact);
        textViewVehicleDetails = findViewById(R.id.textViewVehicleDetails);
        textViewLicensePlate = findViewById(R.id.textViewLicensePlate);
        editProfileButton = findViewById(R.id.editProfileButton);

        // Aici puteți încărca datele profilului din baza de date, de exemplu din Firebase Realtime Database
        // și să le afișați în textViews
        // Exemplu:
        String name = "Numele Utilizatorului";
        String email = "email@example.com";
        String contact = "123-456-7890";
        String vehicleDetails = "Detalii vehicul";
        String licensePlate = "AB123CD";

        textViewName.setText("Nume: " + name);
        textViewEmail.setText("Adresă de email: " + email);
        textViewContact.setText("Număr de contact: " + contact);
        textViewVehicleDetails.setText("Detalii vehicul: " + vehicleDetails);
        textViewLicensePlate.setText("Număr înmatriculare: " + licensePlate);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschide activitatea de editare a profilului
                Intent intent = new Intent(ProfileViewActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }
}
