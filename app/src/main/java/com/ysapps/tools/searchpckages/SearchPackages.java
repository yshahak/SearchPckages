package com.ysapps.tools.searchpckages;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by B.E.L on 13/04/2015.
 */
public class SearchPackages {


    /**
     *
     * @param ctx context to use
     * @param fileUrl the file url on web
     * @return index of first package that not installed in device
     */
    public static int findFirstUninstalledPackage(Context ctx, String fileUrl){
        int count = 1;
        String packageName;
        PackageManager pm = ctx.getPackageManager();
        try {
            java.net.URL url = new java.net.URL(fileUrl);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(url.openStream()));
            String validation = in1.readLine();
            if (!('1' == (validation.charAt(0)))){
                return 0;
            }
            while ((packageName = in1.readLine()) != null){
                if (packageName.length() > 5){
                    Pattern pattern = Pattern.compile("\\s");
                    Matcher matcher = pattern.matcher(packageName);
                    if (matcher.find())
                       packageName = packageName.replace(" ", "");
                    if (pm.getLaunchIntentForPackage(packageName) == null)
                        return count;
                    else
                        count++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return count;
    }

}
