package com.lcw.library.imagepicker.task;

import android.content.Context;

import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.listener.MediaLoadCallback;
import com.lcw.library.imagepicker.loader.ImageScanner;
import com.lcw.library.imagepicker.loader.MediaHandler;
import com.lcw.library.imagepicker.loader.VideoScanner;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 媒体库扫描任务（图片、视频）
 * Create by: chenWei.li
 * Date: 2018/8/25
 * Time: 下午12:31
 * Email: lichenwei.me@foxmail.com
 */
public class MediaLoadTask implements Runnable {

    private Context mContext;
    private long maxDuration = 0;
    private long minDuration = 0;
    private ImageScanner mImageScanner;
    private VideoScanner mVideoScanner;
    private MediaLoadCallback mMediaLoadCallback;

    public MediaLoadTask(Context context, MediaLoadCallback mediaLoadCallback) {
        this.mContext = context;
        this.mMediaLoadCallback = mediaLoadCallback;
        mImageScanner = new ImageScanner(context);
        mVideoScanner = new VideoScanner(context);
    }

    @Override
    public void run() {
        //存放所有照片
        ArrayList<MediaFile> imageFileList = new ArrayList<>();
        //存放所有视频
        ArrayList<MediaFile> videoFileList = new ArrayList<>();

        if (mImageScanner != null) {
            imageFileList = mImageScanner.queryMedia();
        }
        if (mVideoScanner != null) {
            videoFileList = mVideoScanner.queryMedia();
            Iterator it = videoFileList.iterator();
            while (it.hasNext()) {
                MediaFile mediaFile = (MediaFile) it.next();
                if (maxDuration > 0 && mediaFile.getDuration() > maxDuration) {
                    it.remove();
                }
                if (minDuration > 0 && mediaFile.getDuration() < minDuration) {
                    it.remove();
                }
            }
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.loadMediaSuccess(MediaHandler.getMediaFolder(mContext, imageFileList, videoFileList));
        }

    }

    public void setMaxDuration(long duration){
        this.maxDuration = duration;
    }

    public void setMinDuration(long duration){
        this.minDuration = duration;
    }
}
