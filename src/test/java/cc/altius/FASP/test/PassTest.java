/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.altius.FASP.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author akil
 */
public class PassTest {

    public static void main(String[] args) {
        PasswordEncoder p = new BCryptPasswordEncoder();
        String pass = "Weitl@v4";
        String encodedPass = p.encode(pass);
        System.out.println("Password = " + pass);
        System.out.println("Encoded password = " + encodedPass);
        System.out.println(p.matches("Weitl@v4", encodedPass));
    }

//    UPDATE us_user u SET u.PASSWORD='$2a$10$1SWHXQoQqtbEKOUEEYArmOvsFyKTzPeEFIjq0O44SIIb1LfcHBunO', u.FAILED_ATTEMPTS=0 WHERE u.USER_ID=9;
}
