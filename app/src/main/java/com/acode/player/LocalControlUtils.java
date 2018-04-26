package com.acode.player;

import android.util.Log;

import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;

/**
 * user:yangtao
 * date:2018/4/251744
 * email:yangtao@bjxmail.com
 * introduce:功能
 */
public class LocalControlUtils implements LoadControl {
    @Override
    public void onPrepared() {
        Log.d("post","onPrepared");
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d("post","onTracksSelected");
    }

    @Override
    public void onStopped() {
        Log.d("post","onStopped");
    }

    @Override
    public void onReleased() {
        Log.d("post","onReleased");
    }

    @Override
    public Allocator getAllocator() {
        Log.d("post","getAllocator");
        return null;
    }

    @Override
    public long getBackBufferDurationUs() {
        Log.d("post","getBackBufferDurationUs");
        return 0;
    }

    @Override
    public boolean retainBackBufferFromKeyframe() {
        Log.d("post","retainBackBufferFromKeyframe");
        return false;
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
        Log.d("post","shouldContinueLoading");
        return false;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, float playbackSpeed, boolean rebuffering) {
        Log.d("post","shouldStartPlayback");
        return false;
    }
}
