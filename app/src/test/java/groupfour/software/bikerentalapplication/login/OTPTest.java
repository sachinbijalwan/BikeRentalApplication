package groupfour.software.bikerentalapplication.login;

import android.text.Editable;

import android.widget.EditText;

import org.junit.runner.RunWith;
import org.mockito.Mock;


import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)

public class OTPTest {

    private final OTP      testObject = new OTP();
    @Mock         EditText password, otp, confirmPassword ;
    @Mock  Editable pswdText, otpText, confirmPasswordEditText ;

    @Test
    public void checkPasswordValidationWrongCase() {
        assertThat(testObject.validatePassword("piyush")).isFalse();
    }

    @Test
    public void checkPasswordValidationRightCase() {
        assertThat(testObject.validatePassword("password123")).isTrue();
    }
//
//    @Test
//    public void checkValidationWrongPassword(){
//
//        String pswdtext = "password123" ;
//
//
//        String cnfmtext = "password1234" ;
//
//
//        when(password.getText()).thenReturn(pswdText);
//        when(pswdText.toString()).thenReturn(pswdtext);
//        when(otp.getText()).thenReturn(otpText);
//        when(otpText.toString()).thenReturn("1234");
//        when(confirmPassword.getText()).thenReturn(confirmPasswordEditText);
//        when(confirmPasswordEditText.toString()).thenReturn(cnfmtext);
//
//
//        assertThat(testObject.validationResult()).isEqualTo("Password doesn't match" );
//
//    }

}