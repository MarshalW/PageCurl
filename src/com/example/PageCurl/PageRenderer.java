package com.example.PageCurl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-2-16
 * Time: 下午8:41
 * To change this template use File | Settings | File Templates.
 */
public class PageRenderer implements GLSurfaceView.Renderer {

    private PageMesh pageMesh;

    private final float[] projectionMatrix = new float[16];

    public PageMesh getPageMesh() {
        return pageMesh;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        this.pageMesh = new PageMesh();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        this.pageMesh.resetRect(ratio);

        Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -10f, 10f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        this.pageMesh.draw(projectionMatrix);

        Log.d("PageCurl", "page renderer on draw frame: " + Thread.currentThread());
    }
}
