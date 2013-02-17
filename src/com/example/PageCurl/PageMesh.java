package com.example.PageCurl;

import android.graphics.RectF;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-2-16
 * Time: 下午9:05
 * To change this template use File | Settings | File Templates.
 */
public class PageMesh {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    float backgroundColor[] = {0xff / 255f, 0xb9 / 255f, 0x12 / 255f, 1.0f};

    private float positionFactor;

    private RectF rect;

    private FloatBuffer vertexBuffer;

    private int program;

    private float ratio;

    public PageMesh() {
        rect = new RectF(-.9f, .9f, 1f, -.9f);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void resetRect(float ratio) {
        this.ratio = ratio;
        _resetRect(this.rect);
    }

    private void _resetRect(RectF rect){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 3 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();

        vertexBuffer.put(rect.left * ratio);//左上顶点
        vertexBuffer.put(rect.top);
        vertexBuffer.put(0);

        vertexBuffer.put(rect.left * ratio);//左下顶点
        vertexBuffer.put(rect.bottom);
        vertexBuffer.put(0);

        vertexBuffer.put(rect.right * ratio);//右上顶点
        vertexBuffer.put(rect.top);
        vertexBuffer.put(0);

        vertexBuffer.put(rect.right * ratio);//右下顶点
        vertexBuffer.put(rect.bottom);
        vertexBuffer.put(0);

        vertexBuffer.position(0);
    }

    public void setPositionFactor(float positionFactor) {
        this.positionFactor = positionFactor;
        RectF _rect=new RectF(this.rect);
        _rect.left=rect.left+(Math.abs(rect.left)+this.rect.right)*positionFactor;
        _resetRect(_rect);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        int position = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(position);

        GLES20.glVertexAttribPointer(position, 3,
                GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);

        int color = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(color, 1, backgroundColor, 0);

        int matrix = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        checkGlError("glGetUniformLocation");

        GLES20.glUniformMatrix4fv(matrix, 1, false, mvpMatrix, 0);
        checkGlError("glUniformMatrix4fv");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(position);

        Log.d("PageMesh", "draw");
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("PageCurl", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
