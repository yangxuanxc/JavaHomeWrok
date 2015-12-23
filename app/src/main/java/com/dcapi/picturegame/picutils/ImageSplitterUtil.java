package com.dcapi.picturegame.picutils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dcapi on 2015/12/23.
 * 工具类，用来切图，将一张完好的图进行切块
 */
public class ImageSplitterUtil {
    /**
     * @param bitmap
     *             传入的一张待切的图
     * @param piece
     *            切成piece*piece块
     * @return List<ImagePiece>
     *             返回切好的 piece*piece块 图的 List
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece)
    {
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();
        //获取传入的图的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //因为切出来是正方形，所以我们取长和宽的最小值
        int pieceWidth = Math.min(width, height) / piece;

        for (int i = 0; i < piece; i++)
        {
            for (int j = 0; j < piece; j++)
            {
                //新建每个小的图片类
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * piece);
                //左上角顶点坐标
                int x = j * pieceWidth;
                int y = i * pieceWidth;
                //切图，获取每一块。
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
                        pieceWidth, pieceWidth));
                imagePieces.add(imagePiece);
            }
        }

        return imagePieces;
    }
}
