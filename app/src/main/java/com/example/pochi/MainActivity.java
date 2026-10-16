package com.example.pochi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private PetModel pet;
    private TextView tvPetArt;
    private TextView tvPetName;
    private TextView tvMood;
    private TextView tvHunger;
    private TextView tvHappiness;
    private TextView tvEnergy;
    private TextView tvAge;
    private ProgressBar pbHunger;
    private ProgressBar pbHappiness;
    private ProgressBar pbEnergy;
    private Button btnFeed;
    private Button btnPlay;
    private Button btnSleep;
    private Button btnPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pet = new PetModel(this);

        tvPetArt = findViewById(R.id.tvPetArt);
        tvPetName = findViewById(R.id.tvPetName);
        tvMood = findViewById(R.id.tvMood);
        tvHunger = findViewById(R.id.tvHunger);
        tvHappiness = findViewById(R.id.tvHappiness);
        tvEnergy = findViewById(R.id.tvEnergy);
        tvAge = findViewById(R.id.tvAge);
        pbHunger = findViewById(R.id.pbHunger);
        pbHappiness = findViewById(R.id.pbHappiness);
        pbEnergy = findViewById(R.id.pbEnergy);
        btnFeed = findViewById(R.id.btnFeed);
        btnPlay = findViewById(R.id.btnPlay);
        btnSleep = findViewById(R.id.btnSleep);
        btnPet = findViewById(R.id.btnPet);

        btnFeed.setOnClickListener(v -> { pet.feed(); updateUI(); });
        btnPlay.setOnClickListener(v -> { pet.play(); updateUI(); });
        btnSleep.setOnClickListener(v -> { pet.sleep(); updateUI(); });
        btnPet.setOnClickListener(v -> { pet.pet(); updateUI(); });

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        tvPetArt.setText(pet.getAsciiArt());
        tvMood.setText("Mood: " + pet.getMood());

        int hunger = pet.getHunger();
        int happiness = pet.getHappiness();
        int energy = pet.getEnergy();

        tvHunger.setText(String.format(getString(R.string.status_hunger), hunger));
        tvHappiness.setText(String.format(getString(R.string.status_happiness), happiness));
        tvEnergy.setText(String.format(getString(R.string.status_energy), energy));
        tvAge.setText(String.format(getString(R.string.status_age), pet.getAgeMinutes()));

        pbHunger.setProgress(hunger);
        pbHappiness.setProgress(happiness);
        pbEnergy.setProgress(energy);
    }
}