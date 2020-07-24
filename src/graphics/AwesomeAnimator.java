package graphics;

import java.io.PrintStream;
import javax.media.opengl.GLAnimatorControl;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;

public class AwesomeAnimator implements GLAnimatorControl
	{
	public int targetFPS;
	long startTime, currentTime, lastTime;
	public int updateInterval, updateCounter, totalCounter;
	PrintStream outStream = null;
	Thread thread = null;
	boolean startTimeSet = false;
	volatile boolean isRunning = false;
	volatile boolean isPaused = false;
	volatile GLAutoDrawable myDrawable;

	public AwesomeAnimator(GLCanvas canvas, int fps)
		{
		canvas.setAnimator(this);
		targetFPS = fps;
		updateInterval = fps;
		myDrawable = canvas;
		}

	@Override
	public synchronized long getFPSStartTime()
		{
		return startTime;
		}

	@Override
	public synchronized float getLastFPS()
		{
		if(currentTime == lastTime)
			return 0;
		else
			return (float)updateInterval * 1000 / (currentTime - lastTime);
		}

	@Override
	public synchronized long getLastFPSPeriod()
		{
		return currentTime - lastTime;
		}

	@Override
	public synchronized long getLastFPSUpdateTime()
		{
		return currentTime;
		}

	@Override
	public synchronized float getTotalFPS()
		{
		if(currentTime == startTime)
			return 0;
		else
			return (float)totalCounter * 1000 / (currentTime - startTime);
		}

	@Override
	public synchronized long getTotalFPSDuration()
		{
		return currentTime - startTime;
		}

	@Override
	public synchronized int getTotalFPSFrames()
		{
		return totalCounter;
		}

	@Override
	public synchronized int getUpdateFPSFrames()
		{
		return updateInterval;
		}

	@Override
	public synchronized void resetFPSCounter()
		{
		startTimeSet = false;
		startTime = 0;
		currentTime = 0;
		lastTime = 0;
		totalCounter = 0;
		updateCounter = 0;
		}

	@Override
	public synchronized void setUpdateFPSFrames(int frames, PrintStream out)
		{
		updateInterval = frames;
		outStream = out;
		}

	@Override
	public synchronized Thread getThread()
		{
		return thread;
		}

	@Override
	public synchronized boolean isAnimating()
		{
		return myDrawable!=null && isRunning && ! isPaused;
		}

	@Override
	public synchronized boolean isPaused()
		{
		return isPaused;
		}

	@Override
	public synchronized boolean isStarted()
		{
		return isRunning;
		}

	@Override
	public synchronized boolean pause()
		{
		boolean wasPaused = isPaused;
		isPaused = true;
		return !wasPaused && isRunning;
		}

	@Override
	public synchronized void remove(GLAutoDrawable drawable)
		{
		if(drawable == myDrawable)
			{
			drawable.setAnimator(null);
			myDrawable = null;
			}
		}

	@Override
	public synchronized boolean resume()
		{
		boolean wasPaused = isPaused;
		isPaused = false;
		return wasPaused && isRunning;
		}

	@Override
	public synchronized boolean start()
		{
		if(!isRunning)
			{
			isPaused = false;
			isRunning = true;
			thread = new Thread()
				{
				@Override public void run()
					{
					while(isRunning && myDrawable != null)
						{
						if(isPaused)
							{
							try {Thread.sleep(Math.max(1,1000/targetFPS));}
							catch (InterruptedException e){System.err.println("AwesomeAnimator interrupted!?");}
							}
						else
							{
							synchronized (AwesomeAnimator.this)
								{
								if(!startTimeSet)
									{
									startTime = System.currentTimeMillis();
									startTimeSet = true;
									lastTime = startTime;
									currentTime = startTime;
									}
								if(updateCounter>=updateInterval)
									{
									lastTime = currentTime;
									currentTime = System.currentTimeMillis();
									updateCounter = 0;
									if(outStream != null)
										outStream.println("FPS: "+AwesomeAnimator.this.getLastFPS());
									}
								updateCounter++;
								totalCounter++;
								}
							
							myDrawable.display();
							
							final long time;
							synchronized (AwesomeAnimator.this)
								{
								time = (currentTime+(updateCounter*1000)/targetFPS)-System.currentTimeMillis();
								}
							//the time for the next repaint should be currentTime + updateCounter*1000/targetFPS 
							try {Thread.sleep(Math.max(0, time));}
							catch (InterruptedException e){System.err.println("AwesomeAnimator interrupted!?");}
							}
						}
					}
				};
			thread.start();
			return true;
			}
		return false;
		}

	@Override
	public synchronized boolean stop()
		{
		boolean wasStarted = isRunning;
		thread = null;
		isRunning = false;
		return wasStarted;
		}

	@Override
	public void add(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	}
