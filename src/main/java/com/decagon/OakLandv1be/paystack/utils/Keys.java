package com.decagon.OakLandv1be.paystack.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Keys {
    private String TEST_SECRET_KEY;
    private String TEST_PUBLIC_KEY;
    String KEY_IN_USE;


    void initKeys() throws FileNotFoundException{

        JSONObject keyObject;
        String fileContent = "";
        File file = new File("Keys.json");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){
            fileContent += scanner.nextLine();
        }
        keyObject = new JSONObject(fileContent).getJSONObject("API_KEYS");

        this.KEY_IN_USE = keyObject.getString("KEY_IN_USE");
        this.TEST_SECRET_KEY = keyObject.getString("TEST_SECRET_KEY");
        this.TEST_PUBLIC_KEY = keyObject.getString("TEST_PUBLIC KEY");


    }

}
