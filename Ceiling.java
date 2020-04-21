import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.GradientPaint;
import java.awt.Color;
public class Ceiling{
	
	private int [] x;
	private int [] y;
	private int a,b,c,d;
	private int r,g,bb;
	private Boolean bool;

	public Ceiling(int a,int b,int c,int d, Boolean bool){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	 	this.bool = bool;
	 }
	public Ceiling(int[] x, int[] y, int r, int g, int bb){
		this.x=x;
		this.y=y;
		this.r=r;
		this.g=g;
		this.bb=bb;
	}
	public Ceiling(int[] x, int[] y){
		this.x=x;
		this.y=y;
	}
	public int[] getX(){
		return this.x;
	} 
	public int[] getY(){
		return this.y;
	}
	public Boolean getBool(){
		return this.bool;
	} 
	public void setBool(Boolean boool){
		this.bool = boool;
	}
	public GradientPaint getPaint(){
		return new GradientPaint(y[3],x[3],new Color(r,bb,g),y[0],x[0],new Color(r-30,g-30,bb-30));
	}
	public Polygon getPoly(){
		return new Polygon(y,x,x.length);
	}
	public Rectangle getRect(){
		return new Rectangle(a,b,c,d);

	}
}