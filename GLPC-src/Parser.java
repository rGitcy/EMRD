import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Parser {
	//used for herb funciton
	public Parser(String file, Store s) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("newdata\\" + file),"UTF-8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String temp;
			int id_h = 0;
			int id_s = 0;
			while((temp = br.readLine()) != null){
				if(temp.equals("\t")||temp.equals(""))
					continue;
				//调换前后
				
				//决定顺序 
				
				String[] hsarray = temp.split("\t");
				//String[] hsarray = temp.split(" ");
/*				
				String t = hsarray[0].substring(0,hsarray[0].length());
				hsarray[0] = hsarray[1].substring(0,hsarray[1].length());
				hsarray[1] = t;*/
				
				
				
				if(!s.v_h.containsKey(hsarray[0])){
					//第一次遇到这个herb
					Vertex v = new Vertex(hsarray[0],id_h,Vertex.HERB);
					s.v_h.put(hsarray[0], v);
					s.index_v_h.put(id_h, v);
					s.whs.put(id_h, new HashMap<Integer,Double>());
					id_h++;

				}
				
				if(!s.v_s.containsKey(hsarray[1])){
					Vertex v = new Vertex(hsarray[1],id_s,Vertex.SYMPTOM);
					s.v_s.put(hsarray[1],v);
					s.index_v_s.put(id_s, v);
					id_s++;
				}
				
				s.whs.get(s.v_h.get(hsarray[0]).id).put(s.v_s.get(hsarray[1]).id, 1.0);

			}
			s.wsh = SparseMatrixUtil.getMatrixTranspose(s.whs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//parse herb-function
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("newdata\\" + "herbFunction.txt"),"UTF-8"));
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("newdata\\" + "paper_author.txt"),"UTF-8"));
			
			String temp;
			int id_h = 0;
			int id_fc = 0;
			while((temp = br.readLine()) != null){
				if(temp.equals("\t")||temp.equals(""))
					continue;
				//调换前后
				
				//决定顺序 
				
				String[] hfcarray = temp.split("\t");
				//String[] hsarray = temp.split(" ");
/*				
				String t = hsarray[0].substring(0,hsarray[0].length());
				hsarray[0] = hsarray[1].substring(0,hsarray[1].length());
				hsarray[1] = t;*/
				
				if(!s.v_h.containsKey(hfcarray[0])){
					//第一次遇到这个herb
/*					Vertex v = new Vertex(hsarray[0],id_h,Vertex.HERB);
					s.v_h.put(hsarray[0], v);
					s.index_v_h.put(id_h, v);
					s.whs.put(id_h, new HashMap<Integer,Double>());
					id_h++;*/
					//必须保证两个矩阵中的herb是一样的，也就是说现在所有的herb，都在v_h中已经有了
					System.out.println(hfcarray[0] + " more");
				}
				
				if(!s.whfc.containsKey(s.v_h.get(hfcarray[0]).id)){
					s.whfc.put(s.v_h.get(hfcarray[0]).id, new HashMap<Integer,Double>());
				}
				
				if(!s.v_fc.containsKey(hfcarray[1])){
					Vertex v = new Vertex(hfcarray[1],id_fc,Vertex.SYMPTOM);
					s.v_fc.put(hfcarray[1],v);
					s.index_v_fc.put(id_fc, v);
					id_fc++;
				}
				
				s.whfc.get(s.v_h.get(hfcarray[0]).id).put(s.v_fc.get(hfcarray[1]).id, 1.0);
				
			}
			s.wfch = SparseMatrixUtil.getMatrixTranspose(s.whfc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
