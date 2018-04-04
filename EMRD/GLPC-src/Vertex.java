
public class Vertex {
	public int id = -1;
	public int type = -1;
	public int cluster = -1;

	public String name = null;

	public static final int SYMPTOM = 0;
	public static final int HERB = 1;
	public static final int FUNCTION = 2;
	
	public Vertex(String name, int id, int type) {
		this.name = name;
		this.id = id;
		this.type = type;
	}
}
