package com.upgrad.FoodOrderingApp.api.Util;

import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;

import java.util.Base64;

public class Utility {

    public static String[] decodeAuthHeather(String authHeader) throws AuthenticationFailedException {

        // Check the format of the Authorization Header
        if(!(authHeader.substring(0,5).equals("Basic"))) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        byte[] decode = Base64.getDecoder().decode(authHeader.split("Basic ")[1]);
        String decodedText = new String(decode);
        if(!authHeader.contains(":")){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        //If the format is correct return the decoded String array by splitting it based on colon(:)
        return decodedText.split(":");

    }

}
