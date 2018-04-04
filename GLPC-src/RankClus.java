
public class RankClus {
	public static void main(String[] args){
		Store s = new Store();
		//Parser p= new Parser("test.txt",s);
		Parser p= new Parser("herbSymptom.txt",s);
		//NewParser p= new NewParser("herbSymptom.txt",s);
		System.out.print(1);
		Cluster c = new Cluster(s,7,0.2);
		//iterate 10times
		for (int i=0; i<10; i++) {
			c.iterate();
		}
		c.print(0);
		
		
		
		
	}
	
	
}

