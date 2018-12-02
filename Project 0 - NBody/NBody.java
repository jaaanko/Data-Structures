public class NBody{

	public static double readRadius(String filename){	
		
		In in = new In(filename);
		int n = in.readInt();
		return in.readDouble();
	}
	
	public static Planet[] readPlanets(String filename){
		
		In in = new In(filename);
		int n = in.readInt();
		double r = in.readDouble();
		Planet[] pArray = new Planet[n];
		
		for(int i=0; i<n; i++){
			pArray[i]= new Planet(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
		}
		return pArray;
	}
	
	public static void main(String[] args){
		
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		Planet[] pArray = readPlanets(filename);
		double r = readRadius(filename);
		double time = 0;
		
		StdDraw.setScale(-r,r);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");
		
		for(Planet p:pArray){
			
			p.draw();
		}
		
		StdDraw.enableDoubleBuffering();
		double[] xForces = new double[pArray.length];
		double[] yForces = new double[pArray.length];
		while(time < T){
			
			for(int i=0;i<pArray.length;i++){
				
				xForces[i] = pArray[i].calcNetForceExertedByX(pArray);
				yForces[i] = pArray[i].calcNetForceExertedByY(pArray);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for(int i=0;i<pArray.length;i++){
				
				pArray[i].update(dt,xForces[i],yForces[i]);
				pArray[i].draw();
			}
			
			StdDraw.show();
			StdDraw.pause(10);
			time += dt;
		}		
		
		StdOut.printf("%d\n", pArray.length);
		StdOut.printf("%.2e\n", r);
		for (int i = 0; i < pArray.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
            pArray[i].xxPos, pArray[i].yyPos, pArray[i].xxVel,
            pArray[i].yyVel, pArray[i].mass, pArray[i].imgFileName);   
		}
	}
}