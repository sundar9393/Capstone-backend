package com.upgrad.FoodOrderingApp.api.Util;

import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

public class Utility {

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

    public static String[] decodeAuthHeather(String authHeader) throws AuthenticationFailedException {

        // Check the format of the Authorization Header
        if(!(authHeader.substring(0,5).equals("Basic"))) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        byte[] decode = Base64.getDecoder().decode(authHeader.split("Basic ")[1]);
        String decodedText = new String(decode);
        if(!decodedText.contains(":")){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        //If the format is correct return the decoded String array by splitting it based on colon(:)
        return decodedText.split(":");

    }

    public static String getAccessTokenFromHeader(String accessTokenHeader) throws AuthorizationFailedException {
        if(StringUtils.isEmpty(accessTokenHeader)) {
           throw new AuthorizationFailedException("ATHR-008", "Auth header missing!!");
        }
        if(!(accessTokenHeader.substring(0,6).equals("Bearer"))) {
            throw new AuthorizationFailedException("ATHR-006","Incorrect format of access token, access token needs to be suffixed by the term 'Bearer'.");
        }

        String accessToken = accessTokenHeader.split("Bearer ")[1];
        return accessToken;
    }

    public static boolean isPasswordStrong(String password) throws SignUpRestrictedException {
        boolean isStrong = false;
        if(password.matches(PASSWORD_PATTERN)) {
           isStrong = true;
           return isStrong;
        }
        return isStrong;
    }

}
