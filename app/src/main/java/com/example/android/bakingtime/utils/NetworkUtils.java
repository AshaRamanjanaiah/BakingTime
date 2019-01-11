package com.example.android.bakingtime.utils;

import android.webkit.MimeTypeMap;

public class NetworkUtils {

    private static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static boolean isFileImage(String fileUrl){
        String extension = getMimeType(fileUrl);
        if(extension.equals("image/jpeg") || extension.equals("image/png") || extension.equals("image/gif")){
            return true;
        }
        return false;
    }
}
