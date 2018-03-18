package com.example.anjaniprasad.medicine;

/**
 * Created by ANJANIPRASAD on 3/11/2018.
 */

import java.io.File;

class AsyncParams {
    String url;
    File file ;

    public AsyncParams(String url, File file) {
        this.url = url;
        this.file = file;
    }
}
