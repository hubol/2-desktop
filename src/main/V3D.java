package main;

import main.Calc;


/**
 * This is a rigid 3D vector class for abstracting the representation and computing of a wide range of values. 
 * Many of the methods are designed so to not allocate any heap space;
 * Excercise caution so you do not accidentally overwrite your vectors. 
 * In addition, many of the methods return the vector on which the call was made, so the calls can be chained.
 */
public class V3D
	{
	//Some standard vectors. Be very careful not to alter these.
	public static final V3D ZERO = new V3D();
	public static final V3D X = new V3D(1,0,0);
	public static final V3D Y = new V3D(0,1,0);
	public static final V3D Z = new V3D(0,0,1);
	
	public double[] p = new double[3];
	/**
	 * Construct with zero length
	 */
	public V3D()
		{
		this(0,0,0);
		}
	/**
	 * Construct with the specified components
	 */
	public V3D(double x,double y,double z)
		{
		p[0]=x;
		p[1]=y;
		p[2]=z;
		}
	/**
	 * Construct from the float array, starting from the offset 
	 */
	public V3D(float[] vector, int offset)
		{
		this(vector[offset],vector[offset+1],vector[offset+2]);
		}
	/**
	 * Set all components
	 * @return this vector
	 */
	public V3D set(double x,double y,double z)
		{
		p[0]=x;
		p[1]=y;
		p[2]=z;
		return this;
		}
	/**
	 * Set all components
	 * @return this vector
	 */
	public V3D set(V3D source)
		{
		p[0]=source.p[0];
		p[1]=source.p[1];
		p[2]=source.p[2];
		return this;
		}
	/**
	 * Set all components
	 * @return this vector
	 */
	public V3D set(float[] src)
		{
		p[0]=src[0];
		p[1]=src[1];
		p[2]=src[2];
		return this;
		}
	/**
	 * Set all components
	 * @return this vector
	 */
	public V3D set(double[] src)
		{
		p[0]=src[0];
		p[1]=src[1];
		p[2]=src[2];
		return this;
		}

	/**
	 * Calculate the length
	 */
	public double len()
		{
		return Math.sqrt(Math.pow(p[0],2)+Math.pow(p[1],2)+Math.pow(p[2],2));
		}
	/**Calculate the length of this-other*/
	public double distance(V3D other)
		{
		return Math.sqrt(Math.pow(p[0]-other.p[0],2)+Math.pow(p[1]-other.p[1],2)+Math.pow(p[2]-other.p[2],2));
		}
	/**Calculate the squared length of this-other*/
	public double sqDistance(V3D other)
		{
		return Math.pow(p[0]-other.p[0],2)+Math.pow(p[1]-other.p[1],2)+Math.pow(p[2]-other.p[2],2);
		}
	/**Calculate the angle(radians) between this and other*/
	public double angle(V3D other)
		{
		return Calc.acos(dot(other)/(len()*other.len()));
		}
	
	/**
	 * Make the length equal to 1 while keeping the direction.
	 * Makes the vector (1,0,0) if length is 0.
	 * @return this vector
	 */
	public V3D normalize()
		{
		double d=Math.sqrt(Math.pow(p[0],2)+Math.pow(p[1],2)+Math.pow(p[2],2));
		if (d==0)
			{
			p[0]=1;
			p[1]=0;
			p[2]=0;
			}
		else
			{
			p[0]/=d;
			p[1]/=d;
			p[2]/=d;
			}
		return this;
		}
	/**
	 * Multiply all components with the value
	 * @return this vector
	 */
	public V3D multiply(double m)
		{
		p[0]*=m;
		p[1]*=m;
		p[2]*=m;
		return this;
		}
	/**
	 * Add the components to the current values
	 * @return this vector
	 */
	public V3D add(double x,double y,double z)
		{
		p[0]+=x;
		p[1]+=y;
		p[2]+=z;
		return this;
		}
	/**
	 * Add the vector's components to the current values. 
	 * This method allows the input to be null.
	 * @return this vector
	 */
	public V3D addAllowNull(V3D v)
		{
		if(v!=null)
			{
			p[0]+=v.p[0];
			p[1]+=v.p[1];
			p[2]+=v.p[2];
			}
		return this;
		}

	/**
	 * Add the vector's components to the current values
	 * @return this vector
	 */
	public V3D add(V3D v)
		{
		p[0]+=v.p[0];
		p[1]+=v.p[1];
		p[2]+=v.p[2];
		return this;
		}
	/**
	 * Subtract the vector's components from the current values
	 * @return this vector
	 */
	public V3D sub(V3D v)
		{
		p[0]-=v.p[0];
		p[1]-=v.p[1];
		p[2]-=v.p[2];
		return this;
		}
	/**
	 * Add the vector multiplied with the value
	 * @return this vector
	 */
	public V3D add(V3D v,double multiplicator)
		{
		p[0]+=multiplicator*v.p[0];
		p[1]+=multiplicator*v.p[1];
		p[2]+=multiplicator*v.p[2];
		return this;
		}
	
	/**
	 * Set this vector to the cross product of the specified vectors.
	 * Warning: It is unsafe to specify this vector as one of the arguments.
	 * @return this vector
	 */
	public V3D setCross(V3D v1, V3D v2)
		{
		if(v1==this || v2==this) throw new IllegalArgumentException("Invalid cross product arguments...");
		p[0]=v1.p[1]*v2.p[2]-v1.p[2]*v2.p[1];
		p[1]=v1.p[2]*v2.p[0]-v1.p[0]*v2.p[2];
		p[2]=v1.p[0]*v2.p[1]-v1.p[1]*v2.p[0];
		return this;
		}
	/**
	 * Set this vector to the cross product of this and the specified vector.
	 * @return this vector
	 */
	public V3D setCross(V3D v2)
		{
		double x=p[0], y=p[1], z=p[2];
		p[0]=y*v2.p[2]-z*v2.p[1];
		p[1]=z*v2.p[0]-x*v2.p[2];
		p[2]=x*v2.p[1]-y*v2.p[0];
		return this;
		}
	/**
	 * Return a new vector which is the cross product of this and the specified vector
	 */
	public V3D createCross(V3D v)
		{
		double x,y,z;
		x=p[1]*v.p[2]-p[2]*v.p[1];
		y=p[2]*v.p[0]-p[0]*v.p[2];
		z=p[0]*v.p[1]-p[1]*v.p[0];
		return new V3D(x,y,z);
		}
	/**
	 * Make this vector the average of this and the specified vector(weight=0 -> this, weight=1 -> other)
	 * @return this vector
	 */
	public V3D makeAverage(V3D other,double weight)
		{
		p[0]=(p[0]*(1-weight)+other.p[0]*weight);
		p[1]=(p[1]*(1-weight)+other.p[1]*weight);
		p[2]=(p[2]*(1-weight)+other.p[2]*weight);
		return this;
		}

	/**
	 * Make this vector the average of this and the specified components(weight=0 -> this, weight=1 -> components)
	 * @return this vector
	 */
	public V3D makeAverage(double x, double y, double z, double weight)
		{
		p[0]=(p[0]*(1-weight)+x*weight);
		p[1]=(p[1]*(1-weight)+y*weight);
		p[2]=(p[2]*(1-weight)+z*weight);
		return this;
		}
	/**
	 * Return the dot product of this and the specified vector
	 */
	public double dot(V3D v)
		{
		return p[0]*v.p[0]+p[1]*v.p[1]+p[2]*v.p[2];
		}
	/**
	 * Return the dot product of this and the specified vector
	 */
	public double dot(double x,double y,double z)
		{
		return p[0]*x+p[1]*y+p[2]*z;
		}
	/**
	 * Return the x-component
	 */
	public double x()
		{
		return p[0];
		}
	/**
	 * Return the y-component
	 */
	public double y()
		{
		return p[1];
		}
	/**
	 * Return the z-component
	 */
	public double z()
		{
		return p[2];
		}
	/**
	 * Set the x-component
	 * @return this vector
	 */
	public V3D x(double x)
		{
		p[0]=x;
		return this;
		}
	/**
	 * Set the y-component
	 * @return this vector
	 */
	public V3D y(double y)
		{
		p[1]=y;
		return this;
		}
	/**
	 * Set the z-component
	 * @return this vector
	 */
	public V3D z(double z)
		{
		p[2]=z;
		return this;
		}
	/**
	 * Return a float array representing this vector
	 */
	public float[] toFloat()
		{
		float[] result = {(float)p[0],(float)p[1],(float)p[2]};
		return result;
		}
	/**
	 * Make a copy
	 */
	@Override
	public V3D clone()
		{
		return new V3D(p[0],p[1],p[2]);
		}
	/**
	 * Print the coordinates
	 */
	@Override
	public  String toString()
		{
		return "x = "+p[0]+", y = "+p[1]+", z = "+p[2]+". ";
		}
	/**
	 * Accelerate in the current direction by an absolute value. 
	 * If length is under the negative of the value, the vector is set to zero.*/
	public void accelerate(double d)
		{
		double sp = len();
		if(sp!=0)
			{
			if (sp<=-d)
				{
				p[0]=0; p[1]=0; p[2]=0;
				return;
				}
			d=1+d/sp;
			p[0]*=d; p[1]*=d; p[2]*=d;
			}
		}
	/**
	 * Accelerate towards becoming identical to other by an absolute value.*/
	public void accelerateTowards(V3D other, double d)
		{
		double sp = distance(other);
		if (sp<=-d)
			{
			p[0]=other.p[0]; p[1]=other.p[1]; p[2]=other.p[2];
			return;
			}
		d=d/sp;
		p[0]=p[0]*(1-d)+other.p[0]*(d); 
		p[1]=p[1]*(1-d)+other.p[1]*(d); 
		p[2]=p[2]*(1-d)+other.p[2]*(d);
		}
	/**
	 * Calculate the squared length. Cheaper than calculating length since there is no square root.
	 */
	public double sqLen()
		{
		return (Math.pow(p[0],2)+Math.pow(p[1],2)+Math.pow(p[2],2));
		}
	@Override
	public boolean equals(Object other)
		{
		if(other instanceof V3D)
			return (((V3D)other).p[0]==p[0] && ((V3D)other).p[1]==p[1] && ((V3D)other).p[2]==p[2]);
		return false;
		}
	/**Checks if this vector is a multiple of the other vector.
	 * Returns false if exactly one vector is zero.
	 * Returns true if both vectors are zero.*/
	public boolean isMultipleOf(V3D other)
		{
		if(isZero() ^ other.isZero())
			return false;
		if(isZero() && other.isZero())
			return true;
		double m = other.p[0]/p[0];
		return (other.p[1]==p[1]*m && other.p[2]==p[2]*m);
		}
	/**Checks if all components are exactly zero.*/
	public boolean isZero()
		{
		return p[0]==0 && p[1]==0 && p[2]==0;
		}
	/**Sets thie vector to the vector from 'from' to 'to', 
	 * then adds a multiple of orthogonalVector so that the
	 * resulting vector is orthogonal to it.
	 * orthogonalVector should be of length 1.
	 * Is not normalized.
	 * @return this vector*/
	public V3D setFromToOrthogonal(V3D from, V3D to, V3D orthogonalVector)
		{
		set(to);
		sub(from);
		double d = -dot(orthogonalVector);
		add(orthogonalVector,d);
		return this;
		}
	}
