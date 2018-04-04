import java.util.ArrayList;
import java.util.HashMap;




public class SingleCluster {

	public ArrayList<Integer> l;

	public HashMap<Integer,Double> ri = null;
	public HashMap<Integer,Double> ry = null;
	public HashMap<Integer,Double> rx = null;
	
	//herb(x)-function,
	public HashMap<Integer,Double> ri1 = null;
	public HashMap<Integer,Double> ry1 = null;
	public HashMap<Integer,Double> rx1 = null;
	
	public double [] s;

	public SingleCluster(int k) {
		this.l = new ArrayList<Integer>();
		this.s = new double[k*2];
	}
}
