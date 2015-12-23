package com.dcapi.picturegame.picutils;

import android.graphics.Bitmap;

/**
 * Created by Dcapi on 2015/12/23.
 * 图片表示，用于存切成的每一小块图片
 */
public class ImagePiece {
    //index表示序号
    private int index ;
    private Bitmap bitmap ;

    public ImagePiece()
    {
    }

    public ImagePiece(int index, Bitmap bitmap)
    {
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    @Override
    public String toString()
    {
        return "ImagePiece [index=" + index + ", bitmap=" + bitmap + "]";
    }
}
