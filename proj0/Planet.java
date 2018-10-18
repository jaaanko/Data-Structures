public class Planet{

	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	public static final double G = 6.67e-11;
	
	public Planet(double xP, double yP, double xV,double yV, double m, String img){
		
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	
	public Planet(Planet p){
		
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}
	
	public double calcDistance(Planet p){
		
		return Math.sqrt(((p.xxPos - xxPos) * (p.xxPos - xxPos)) + ((p.yyPos - yyPos) * (p.yyPos - yyPos)));
	}
	
	public double calcForceExertedBy(Planet p){
		
		return (G * p.mass * mass) / (calcDistance(p) * calcDistance(p));
	}
	
	public double calcForceExertedByX(Planet p){
		
		return (calcForceExertedBy(p) * (p.xxPos - xxPos)) / calcDistance(p);
	}	
	
	public double calcForceExertedByY(Planet p){
		
		return (calcForceExertedBy(p) * (p.yyPos - yyPos)) / calcDistance(p);
	}
	
	public double calcNetForceExertedByX(Planet[] allPlanets){
		
		double netForceX = 0;
		for(Planet p:allPlanets){
			
			if(p.equals(this)){
				continue;
			}
			netForceX = netForceX + calcForceExertedByX(p);
			
		}
		return netForceX;
	}
	
	public double calcNetForceExertedByY(Planet[] allPlanets){
		
		double netForceY = 0;
		for(Planet p:allPlanets){
			
			if(p.equals(this)){
				continue;
			}
			netForceY = netForceY + calcForceExertedByY(p);
			
		}

		return netForceY;
	}
	
	public void update(double dt, double fX, double fY){
		
		xxVel = xxVel + (dt * (fX/mass));
		yyVel = yyVel + (dt * (fY/mass));
		xxPos = xxPos + (dt * xxVel);
		yyPos = yyPos + (dt * yyVel);
	}
	
	public void draw(){
		
		StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
	}
	
}