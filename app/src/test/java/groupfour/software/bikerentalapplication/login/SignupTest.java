package groupfour.software.bikerentalapplication.login;


import org.junit.runner.RunWith;



import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class SignupTest {
    private final Signup testObject = new Signup();

    @Test
    public void validateEmailWrongCase(){
        String email = "2015csb1023@gmail.com" ;
        assertThat(testObject.validateEmail(email)).isFalse();
    }

    @Test
    public void validateEmailRightCase(){
        String email = "2015csb1023@iitrpr.ac.in" ;
        assertThat(testObject.validateEmail(email)).isTrue();
    }
    @Test
    public void validateUsernamelWrongCase(){
        String username = "2015csb" ;
        assertThat(testObject.validateUsername(username)).isFalse();
    }

    @Test
    public void validateUsernameRightCase(){
        String username = "2015csb1023" ;
        assertThat(testObject.validateUsername(username)).isTrue();
    }
    @Test
    public void validatePhonelWrongCase(){
        String phone = "2015csb" ;
        assertThat(testObject.validatePhone(phone)).isFalse();
    }

    @Test
    public void validatePhoneRightCase(){
        String phone = "9876543210" ;
        assertThat(testObject.validatePhone(phone)).isTrue();
    }
}

