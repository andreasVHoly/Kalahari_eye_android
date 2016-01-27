package avh.com.kalaharieye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    private ToggleButton btnShootMode;
    private ToggleButton btnLiveMode;
    private Button btnNextShot;
    private TextView debugText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(avh.com.kalaharieye.R.layout.activity_main);
        assignUIVariables();
    }

    //finds the UI varibles that we need to find as the app starts
    private void assignUIVariables(){
        btnLiveMode = (ToggleButton) findViewById(R.id.button_live_mode);
        btnShootMode = (ToggleButton) findViewById(R.id.button_shooting_mode);
        btnNextShot =  (Button) findViewById(R.id.button_next_shot);
        debugText = (TextView) findViewById(R.id.textView);

    }

    //handles the toggle buttons being clicked on and off
    public void buttonOnClick(View v){
        if (v.getId() == btnLiveMode.getId()){
            debugText.setText("live register");
            if (btnLiveMode.isChecked()){
                btnShootMode.setChecked(false);
            }
            else{
                btnShootMode.setChecked(true);
            }
        }
        else if (v.getId() == btnShootMode.getId()){
            debugText.setText("shoot register");
            if (btnShootMode.isChecked()){
                btnLiveMode.setChecked(false);
            }
            else{
                btnLiveMode.setChecked(true);
            }
        }
        else if (v.getId() == btnNextShot.getId()){
            debugText.setText("next shot register");
        }
    }



}
