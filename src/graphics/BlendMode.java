package graphics;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import main.Scene;

public class BlendMode
	{
	public final int srcFactor,dstFactor,equation;
	
	public final static BlendMode NORMAL = new BlendMode(GL.GL_SRC_ALPHA ,GL.GL_ONE_MINUS_SRC_ALPHA,GL.GL_FUNC_ADD);
	public final static BlendMode MULTIPLY = new BlendMode(GL.GL_ZERO,GL.GL_SRC_COLOR,GL.GL_FUNC_ADD);
	public final static BlendMode ADD = new BlendMode(GL.GL_ONE,GL.GL_ONE,GL.GL_FUNC_ADD);
	public final static BlendMode MIN = new BlendMode(GL.GL_ONE,GL.GL_ONE,GL2.GL_MIN);
	public final static BlendMode MAX = new BlendMode(GL.GL_ONE,GL.GL_ONE,GL2.GL_MAX);
	public final static BlendMode SUBTRACT = new BlendMode(GL.GL_SRC_ALPHA,GL.GL_ONE,GL.GL_FUNC_REVERSE_SUBTRACT);
	public final static BlendMode INVERT = new BlendMode(GL.GL_ONE_MINUS_DST_COLOR,GL.GL_ONE_MINUS_SRC_ALPHA,GL.GL_FUNC_ADD);
	
	public BlendMode(int srcFactor,int dstFactor,int equation)
		{
		this.srcFactor=srcFactor;
		this.dstFactor=dstFactor;
		this.equation=equation;
		}
	public void set()
		{
		Scene.gl.glBlendFunc(srcFactor, dstFactor);
		Scene.gl.glBlendEquation(equation);
		}
	}
