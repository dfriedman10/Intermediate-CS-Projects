

public class Node {
	
	private int freq;
	protected char letter;
	protected boolean isLeaf;
	protected Node child1, child2;
	
	public Node(char letter, int freq) {
		this.freq = freq;
		this.letter = letter;
		isLeaf = true;
	}
	public Node(Node n1, Node n2) {
		child1 = n1;
		child2 = n2;
		if (n1 != null && n2 != null)
			this.freq = n1.getFreq()+n2.getFreq();
		isLeaf = false;
	}
	
	public int getFreq() {
		return freq;
	}
	public char getLetter() {
		return letter;
	}
	
	public String toString() {
		return letter +": " + freq+", "+child1 +", "+child2;
	}
}
