import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Cluster {
	private Store s = null;
	private int k = 0;
	public ArrayList<SingleCluster> c = null;
	private double [][] pi = null;//pi（i,k)
	private final int ITER = 5;
	private int hNum;
	private int sNum;
	private int fcNum;
	private double a;//smooth
	//herb-symptom
	private HashMap<Integer,HashMap<Integer,Double>> rsw;
	private HashMap<Integer,HashMap<Integer,Double>> rhw;
	private HashMap<Integer,Double> wry;
	private HashMap<Integer,Double> wrx;
	//herb-function
	private HashMap<Integer,HashMap<Integer,Double>> rsw1;
	private HashMap<Integer,HashMap<Integer,Double>> rhw1;
	private HashMap<Integer,Double> wry1;
	private HashMap<Integer,Double> wrx1;
	public Cluster(Store s,int k,double a){
		this.s = s;
		this.k = k;
		this.a = a;
		this.hNum = s.v_h.size();
		this.sNum = s.v_s.size();
		this.fcNum = s.v_fc.size();
		this.c = new ArrayList<SingleCluster>();
		for (int i=0; i<k; i++) {
			this.c.add(new SingleCluster(k));
		}
		
		Iterator<String> it = this.s.v_h.keySet().iterator();
		int cn = 0;
		while (it.hasNext()) {
			Vertex v = this.s.v_h.get(it.next());			
			int clusterNum = cn++%k;
			v.cluster = clusterNum;
			this.c.get(clusterNum).l.add(v.id);

		}
		//herb-symptom
		rhw = SparseMatrixUtil.multiplyMatrix(s.whs,s.wsh);//a matrix used to compute rank of herb
		wrx = SparseMatrixUtil.computeMatrixEigenVector(rhw,10);
		rsw = SparseMatrixUtil.multiplyMatrix(s.wsh,s.whs);//a matrix used to compute rank of symptom
		wry = SparseMatrixUtil.computeMatrixEigenVector(rsw,10);
		//herb-function
		rhw1 = SparseMatrixUtil.multiplyMatrix(s.whfc,s.wfch);
		wrx1 = SparseMatrixUtil.computeMatrixEigenVector(rhw1,10);
		rsw1 = SparseMatrixUtil.multiplyMatrix(s.wfch,s.whfc);
		wry1 = SparseMatrixUtil.computeMatrixEigenVector(rsw1,10);
	}
	public void iterate(){
		step1();
		step11();
		step2();
		step21();
		step3();
	}
	private void step1() {
		for(int i = 0;i < k;i++){
			//构建改聚类下的herb的生成图（生成矩阵）
			HashMap<Integer,HashMap<Integer,Double>> hs = new HashMap<Integer,HashMap<Integer,Double>>();
			Iterator<Integer> it = this.c.get(i).l.iterator();
			while(it.hasNext()){
				int x = it.next();//id of herb
				for(int y = 0;y < s.v_s.size();y++){
					if(!hs.containsKey(x)){
						hs.put(x, new HashMap<Integer,Double>());
					}
					if(s.whs.get(x).containsKey(y)){
						hs.get(x).put(y, s.whs.get(x).get(y));
					}	
				}
			}
			HashMap<Integer,HashMap<Integer,Double>> sh = SparseMatrixUtil.getMatrixTranspose(hs);
			HashMap<Integer,HashMap<Integer,Double>> rsm = SparseMatrixUtil.multiplyMatrix(sh,hs);//a matrix used to compute rank of symptom
			HashMap<Integer,HashMap<Integer,Double>> rhm = SparseMatrixUtil.multiplyMatrix(hs,sh);//a matrix used to compute rank of herb
			
			c.get(i).ry = SparseMatrixUtil.computeMatrixEigenVector(rsm,10);
			c.get(i).ri = SparseMatrixUtil.computeMatrixEigenVector(rhm,10);
			
			HashMap<Integer,HashMap<Integer,Double>> temp = new HashMap<Integer,HashMap<Integer,Double>>();
			temp.put(0, this.c.get(i).ry);
			c.get(i).rx = SparseMatrixUtil.multiplyMatrix(s.whs,temp).get(0);
			
			HashMap<Integer,Double> rya = SparseMatrixUtil.matrixMultiplyNumber(c.get(i).ry , 1-a);
			if(c.get(i).rx == null)
				System.out.println("");
			HashMap<Integer,Double> rxa = SparseMatrixUtil.matrixMultiplyNumber(c.get(i).rx , 1-a);
			HashMap<Integer,Double> wrya = SparseMatrixUtil.matrixMultiplyNumber(wry , a);
			HashMap<Integer,Double> wrxa = SparseMatrixUtil.matrixMultiplyNumber(wrx , a);
			
			c.get(i).ry = SparseMatrixUtil.matrixAdd(rya, wrya);
			c.get(i).rx = SparseMatrixUtil.matrixAdd(rxa, wrxa);
		}
		
		
	}
	private void step11() {
		for(int i = 0;i < k;i++){
			//构建改聚类下的herb的生成图（生成矩阵）
			HashMap<Integer,HashMap<Integer,Double>> hfc = new HashMap<Integer,HashMap<Integer,Double>>();
			Iterator<Integer> it = this.c.get(i).l.iterator();
			while(it.hasNext()){
				int x = it.next();
				for(int y = 0;y < s.v_fc.size();y++){
					if(!hfc.containsKey(x)){
						hfc.put(x, new HashMap<Integer,Double>());
					}
					if(s.whfc.get(x).containsKey(y)){
						hfc.get(x).put(y, s.whfc.get(x).get(y));
					}	
				}
			}
			HashMap<Integer,HashMap<Integer,Double>> fch = SparseMatrixUtil.getMatrixTranspose(hfc);
			HashMap<Integer,HashMap<Integer,Double>> rfcm = SparseMatrixUtil.multiplyMatrix(fch,hfc);
			HashMap<Integer,HashMap<Integer,Double>> rhm = SparseMatrixUtil.multiplyMatrix(hfc,fch);
			
			c.get(i).ry1 = SparseMatrixUtil.computeMatrixEigenVector(rfcm,10);
			c.get(i).ri1 = SparseMatrixUtil.computeMatrixEigenVector(rhm,10);
			
			HashMap<Integer,HashMap<Integer,Double>> temp = new HashMap<Integer,HashMap<Integer,Double>>();
			temp.put(0, this.c.get(i).ry1);
			c.get(i).rx1 = SparseMatrixUtil.multiplyMatrix(s.whfc,temp).get(0);
			
			HashMap<Integer,Double> rya = SparseMatrixUtil.matrixMultiplyNumber(c.get(i).ry1 , 1-a);
			if(c.get(i).rx1 == null)
				System.out.println("");
			HashMap<Integer,Double> rxa = SparseMatrixUtil.matrixMultiplyNumber(c.get(i).rx1 , 1-a);
			HashMap<Integer,Double> wrya = SparseMatrixUtil.matrixMultiplyNumber(wry1 , a);
			HashMap<Integer,Double> wrxa = SparseMatrixUtil.matrixMultiplyNumber(wrx1 , a);
			
			c.get(i).ry1 = SparseMatrixUtil.matrixAdd(rya, wrya);
			c.get(i).rx1= SparseMatrixUtil.matrixAdd(rxa, wrxa);
		}
		
		
	}
	private void step2() {

		pi = new double[hNum][k*2];
		double [] pz = new double[k];
		for (int x=0; x<k; x++) {
			for (int y=0; y<hNum; y++) {
				pi[y][x] = (1.0/k);
			}
			pz[x] = (1.0/k);
		}
		for (int j=0; j<ITER; j++) {
			for (int z=0; z<k; z++) {
				double den = 0.0, num = 0.0;

				for (int x=0; x<hNum; x++) {
					for (int y=0; y<sNum; y++) {
						if(s.whs.get(x).containsKey(y)){	
							den += s.whs.get(x).get(y);
							if(c.get(z).rx.containsKey(x) && c.get(z).ry.containsKey(y)){
								num += s.whs.get(x).get(y)*pz[z]*(this.c.get(z).rx.get(x))*(this.c.get(z).ry.get(y));
							}
						}

					}
				}

				pz[z] = num/den;
			}
		}
		for (int x=0; x<hNum; x++) {
			double den = 0.0;

			for (int y=0; y<k; y++) {
				if(c.get(y).rx.containsKey(x))
					den += this.c.get(y).rx.get(x)*pz[y];
			}

			for (int y=0; y<k; y++) {
				if(c.get(y).rx.containsKey(x))
					pi[x][y] = (this.c.get(y).rx.get(x)*pz[y])/den;
			}
		}
		//算中心
		for (int x=0; x<k; x++) {
			int cs = this.c.get(x).l.size();

			for (int y=0; y<cs; y++) {
				for (int z=0; z<k; z++) {
					this.c.get(x).s[z] += pi[this.c.get(x).l.get(y)][z];
				}
			}

			for (int y=0; y<k; y++) {
				this.c.get(x).s[y] /= cs;
			}
		}
		System.gc();		
	}
	
	private void step21() {

		//pi = new double[hNum][k];
		double [] pz = new double[k];
		for (int x=0; x<k; x++) {
			for (int y=0; y<hNum; y++) {
				pi[y][x+k] = (1.0/k);
			}
			pz[x] = (1.0/k);
		}
		for (int j=0; j<ITER; j++) {
			for (int z=0; z<k; z++) {
				double den = 0.0, num = 0.0;

				for (int x=0; x<hNum; x++) {
					for (int y=0; y<fcNum; y++) {
						if(s.whfc.get(x).containsKey(y)){	
							den += s.whfc.get(x).get(y);
							if(c.get(z).rx1.containsKey(x) && c.get(z).ry1.containsKey(y)){
								num += s.whfc.get(x).get(y)*pz[z]*(this.c.get(z).rx1.get(x))*(this.c.get(z).ry1.get(y));
							}
						}

					}
				}

				pz[z] = num/den;
			}
		}
		for (int x=0; x<hNum; x++) {
			double den = 0.0;

			for (int y=0; y<k; y++) {
				if(c.get(y).rx1.containsKey(x))
					den += this.c.get(y).rx1.get(x)*pz[y];
			}

			for (int y=0; y<k; y++) {
				if(c.get(y).rx1.containsKey(x))
					pi[x][y+k] = (this.c.get(y).rx1.get(x)*pz[y])/den;
			}
		}
		//算中心
		for (int x=0; x<k; x++) {
			int cs = this.c.get(x).l.size();

			for (int y=0; y<cs; y++) {
				for (int z=0; z<k; z++) {
					//2k
					this.c.get(x).s[z + k] += pi[this.c.get(x).l.get(y)][z+k];
				}
			}

			for (int y=0; y<k; y++) {
				this.c.get(x).s[y+k] /= cs;
			}
		}
		System.gc();		
	}
	private void step3() {
		//m代代表每个herb	属于哪个cluster 
		int [] m = new int[hNum];

		Iterator<String> it = this.s.v_h.keySet().iterator();
		while (it.hasNext()) {
			Vertex v = this.s.v_h.get(it.next());
			if (v.type == Vertex.HERB) {
				double min = Double.MAX_VALUE;

				for (int i=0; i<k; i++) {
					double num = 0.0, denp1 = 0.0, denp2 = 0.0;

					for (int j=0; j<k * 2; j++) {
						num += pi[v.id][j]*this.c.get(i).s[j];
						denp1 += Math.pow(pi[v.id][j], 2);
						denp2 += Math.pow(this.c.get(i).s[j], 2);
					}

					double d = (1 - (num / Math.sqrt(denp1*denp2)));
					if (d < min) {
						min = d;
						m[v.id] = i;
					}
				}
			}
		}

		pi = null;
		clearAll();
		this.c = new ArrayList<SingleCluster>();
		for (int i=0; i<k; i++) {
			this.c.add(new SingleCluster(k));
		}

		for (int i=0; i<hNum; i++) {
			this.c.get(m[i]).l.add(i);
		}

		System.gc();
		
	}
	private void clearAll() {
		for (int i=0; i<k; i++) {
			this.c.get(i).l.clear();
		}
	}
	public void print(int k1){

		this.step1();
		for(int i=0;i<k;i++){
			 String fileName = "cluster" + i + ".txt";  
			 BufferedWriter  writer = null;
			 try {
				writer = new BufferedWriter(new FileWriter(fileName));
	    		Iterator<Integer> it = c.get(i).l.iterator();
	    		
	    		while(it.hasNext()){
	    			int key = it.next();//id of herb
	    			Iterator<String> it2 = s.v_h.keySet().iterator();
	    			while(it2.hasNext()){
	    				Vertex v = s.v_h.get(it2.next());
	    				if(v.id == key){
	    					
	    					writer.write("---" +v.name + "---");  
	    					Iterator<Integer> sit = s.whs.get(v.id).keySet().iterator();
	    					while(sit.hasNext()){
	    						int sid = sit.next();
	    						if(c.get(i).ry.containsKey(sid))
	    							writer.write(s.index_v_s.get(sid).name + c.get(i).ry.get(sid) + " ");
	    						else
	    							writer.write(s.index_v_s.get(sid).name + " ");
	    					}
	    		            writer.newLine();  
	    				}
	    					
	    			}
	    		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally{  
	            try {
					writer.flush();
					writer.close();  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	           
	        }  
		}
		
		try {
			String fileName = "allClusters.txt";
			BufferedWriter  writer = null;
			writer = new BufferedWriter(new FileWriter(fileName));
			for(int i = 0;i < k;i++){				
				Iterator<Integer> it = c.get(i).l.iterator();
				while(it.hasNext()){
	    			int key = it.next();
	    			writer.write(i + " " + s.index_v_h.get(key).name + "---");
	    			Iterator<Integer> sit = s.whs.get(key).keySet().iterator();
	    			while(sit.hasNext()){
	    				int sid = sit.next();
	    				writer.write(s.index_v_s.get(sid).name + " " + c.get(i).ry.get(sid) + " ");

	    			}
	    			writer.newLine();
				}		
			}
			writer.flush();
			writer.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
