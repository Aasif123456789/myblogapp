package com.myblog.myblog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {

    public static void main(String[] arg){
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        System.out.println(encoder.encode("testing"));

    }

}
