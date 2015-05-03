package com.ysapps.tools.searchpckages;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements BannerAd.MyXListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerAd.openBanner(this, "http://www.myoffernet.com/text.txt");
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                int firstUninstalledPackage = SearchPackages.findFirstUninstalledPackage(getApplication(), "http://myoffernet.com/pkg/pkg.txt");
            }
        }).start();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onXClicked() {
        Log.i("x clicked", "clicked");
    }
}
