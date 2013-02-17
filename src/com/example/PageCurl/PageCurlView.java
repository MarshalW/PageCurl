package com.example.PageCurl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-2-16
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public class PageCurlView extends GLSurfaceView {

    private static int count=0;

    private PageRenderer renderer;

    private long duration = 500;

    private long startDelay = 2000;

    ValueAnimator animator;

    public PageCurlView(Context context) {
        super(context);
        this.init();
    }

    public PageCurlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        count++;
        Log.d("PageCurlView",">>>"+count);

        this.setEGLContextClientVersion(2);

        //设置透明背景
        this.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        this.setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        this.renderer = new PageRenderer();
        this.setRenderer(this.renderer);

        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        Log.d("PageCurl", "page curl view inited.");

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(this.duration);
        animator.setStartDelay(startDelay);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                renderer.getPageMesh().setPositionFactor(valueAnimator.getAnimatedFraction());
                requestRender();
            }
        });
        animator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PageCurl","view on pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PageCurl","view on resume");
    }
}
