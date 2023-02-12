package com.example.playme;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.util.ArrayList;
//import com.example.playme.Manifest.*;
public class MainActivity extends AppCompatActivity {
    private ListView listView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list1);
    0    Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String items[] = new String[mySongs.size()];
                        for(int i=0;i<mySongs.size();i++){
                            items[i]=mySongs.get(i).getName().replace(".mp3","");
                        }

                        ArrayAdapter ad =  new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(ad);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                                String currentSong = listView.getItemAtPosition(pos).toString();
                                intent.putExtra("mySongs",mySongs);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",pos);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {}
                }).check();

    }

    // Adding To ArrayList
    public ArrayList<File> fetchSongs(File file){
        ArrayList songList = new ArrayList();
        File[] songs = file.listFiles();
        if(songs != null){
            for(File song:songs){
                if(!song.isHidden() && song.isDirectory()){
                    songList.addAll(fetchSongs(song));
                }
                else{
                    if(song.getName().endsWith(".mp3") && !song.getName().startsWith(".")){
                        songList.add(song);
                    }
                }
            }
        }

        return songList;
    }
}