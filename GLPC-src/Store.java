import java.util.HashMap;


public class Store {
	public HashMap<String, Vertex> v_h;
	public HashMap<String, Vertex> v_s;
	
	public HashMap<Integer, Vertex> index_v_h;
	public HashMap<Integer, Vertex> index_v_s;
	public HashMap<Integer, HashMap<Integer,Double>> whs;
	public HashMap<Integer,HashMap<Integer,Double>> wsh;
	
	public HashMap<String,Vertex> v_fc;
	public HashMap<Integer, Vertex> index_v_fc;
	public HashMap<Integer, HashMap<Integer,Double>> whfc;
	public HashMap<Integer,HashMap<Integer,Double>> wfch;

	public Store(){
		v_h = new HashMap<String,Vertex>();
		v_s = new HashMap<String,Vertex>();
		index_v_h = new HashMap<Integer,Vertex>();
		index_v_s = new HashMap<Integer,Vertex>();
		whs = new HashMap<Integer, HashMap<Integer,Double>>();
		
		v_fc = new HashMap<String,Vertex>();
		index_v_fc=new HashMap<Integer,Vertex>();
		whfc =  new HashMap<Integer, HashMap<Integer,Double>>();
	}
}
