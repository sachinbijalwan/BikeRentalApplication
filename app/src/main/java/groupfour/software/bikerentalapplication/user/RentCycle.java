package groupfour.software.bikerentalapplication.user;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import groupfour.software.bikerentalapplication.R;
public class RentCycle extends BaseActivity {
    private String PREFS_NAME="USER";
    String text=""; // Whatever you need to encode in the QR code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_cycle);
        onCreateDrawer();
        text=getText();
        if(text.isEmpty()){
            changeString();
        }
        writeqrcode();


    }
    public void writeqrcode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        ImageView imageView=findViewById(R.id.imgqrcode);
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void changeString(){
        if(text.isEmpty()) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            text = settings.getString("USERID", "0");
            text+=String.valueOf(0);
        }
        else {
            int trail = getTrailingInteger(text);
            trail++;
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            text = settings.getString("USERID", "0");
            text += String.valueOf(trail);

        }
        writeText();

    }
    int getTrailingInteger(String str)
    {
        int positionOfLastDigit = getPositionOfLastDigit(str);
        if (positionOfLastDigit == str.length())
        {
            // string does not end in digits
            return -1;
        }
        return Integer.parseInt(str.substring(positionOfLastDigit));
    }

    int getPositionOfLastDigit(String str)
    {
        int pos;
        for (pos=str.length()-1; pos>=0; --pos)
        {
            char c = str.charAt(pos);
            if (!Character.isDigit(c)) break;
        }
        return pos + 1;
    }
    public void writeText(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TEXT", text);
        editor.apply();
    }
    public String getText(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        text = settings.getString("TEXT", "");
        return text;
    }
    public void onClick(View view){
        changeString();
        writeText();
        writeqrcode();
        Toast.makeText(getApplicationContext(),"QR CODE CHANGED SUCCESSFULLY",Toast.LENGTH_LONG).show();
    }
}
