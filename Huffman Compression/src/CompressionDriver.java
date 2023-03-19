
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CompressionDriver {

	private String fileName, compressedName;
	private HashMap<Character,Integer> freqMap;
	private Node root;
	private PriorityQueue queue;
	private HashMap<Character, String> encodeMap;
	
	public CompressionDriver() {
		Scanner input = new Scanner(System.in);
		System.out.println("File to compress?");
		fileName = input.next()+".txt";
		compressedName = fileName.substring(0,fileName.length()-4) + "_compressed";
		input.close();
		freqMap = new HashMap<Character,Integer>();
		queue = new PriorityQueue();
		encodeMap = new HashMap<Character, String>();
		readFile();
		buildTree();
		compress();
		compressTree();
		System.out.println(encodeMap);
	}
	private void readFile()  {
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			for (int i = in.read(); i != -1; i = in.read()) {
				char c = (char)i;
				if (freqMap.containsKey(c)) 
					freqMap.put(c, freqMap.get(c)+1);
				else
					freqMap.put(c, 1);
				
			}
			in.close();
			
			for (char letter : freqMap.keySet()) {
				Node n = new Node(letter, freqMap.get(letter));
				queue.add(n);
			}
		} 
		catch (FileNotFoundException e) {
			if (fileName.indexOf(".txt") != -1) {
				fileName = fileName.substring(0,fileName.length()-4);
				readFile();
			}
			else
				System.out.println("File not found :(");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void buildTree() {
		while (queue.size() > 1) {
			Node n1 = queue.pop();
			Node n2 = queue.pop();
			Node n3 = new Node(n1,n2);
			queue.add(n3);
		}
		root = queue.pop();
		encode();
	}
	
	private void encode() {
		encode(root, "");
	}
	
	private void encode(Node n, String code) {
		if (n.isLeaf) 
			encodeMap.put(n.getLetter(), code);
		else {
			encode(n.child1, code + "0");
			encode(n.child2, code + "1");
		}
	}
	
	private void compress() {
		String output = "";
		int charnum = 0;
		try {
			BufferedBitWriter writer = new BufferedBitWriter(compressedName);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			for (int i = reader.read(); i != -1; i = reader.read()) {
				char c = (char)i;
				output = encodeMap.get(c);
				for (int j = 0; j < output.length(); j++) 
					if (output.charAt(j) == '1')
						writer.writeBit(true);
					else
						writer.writeBit(false);
				charnum ++;
				if (charnum %10000 == 0)
					System.out.println(charnum);
			}
			System.out.println("Done encoding");
			/*charnum = 0;
			for (int i = 0; i < output.length(); i++) {
				if (output.charAt(i) == '1')
					writer.writeBit(true);
				else
					writer.writeBit(false);
				charnum ++;
				if (charnum %10000 == 0)
					System.out.println(charnum);
			}
			System.out.println("Done Compressing");*/
			reader.close();
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void compressTree() {
		String output = "";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName.substring(0,fileName.length()-4) + "_tree"));
			for (char c : encodeMap.keySet()) {
				output += c;
				output += encodeMap.get(c);
				output += (char)(0);
			}
			writer.write(output);
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void compressTree2() {
		String output = "";
		try {
			BufferedBitWriter writer = new BufferedBitWriter(fileName.substring(0,fileName.length()-4) + "_tree");
			for (char c : encodeMap.keySet()) {
				String binary = toBinary(c) ;
				for (int i = 0; i < binary.length(); i++) 
					if (binary.charAt(i) == '1')
						writer.writeBit(true);
					else
						writer.writeBit(false);
				
				/*output += c;
				output += encodeMap.get(c);
				output += (char)(0);*/
			}
			//writer.write(output);
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toBinary(char c) {
		int n = (int)c;
		String bin = "";
		for (int i = 6; i >= 0; i--) {
			if (Math.pow(2, i) <= n) {
				bin += '1';
				n -= Math.pow(2,i);
			}
			else
				bin += '0';
		}
		return bin;
	}
	
	public char toChar(String bin) {
		int num = 0;
		for (int i = 0; i < bin.length(); i++) {
			if (bin.charAt(i) == '1')
				num += Math.pow(2, 6-i);
		}
		return (char)num;
	}
	
	public static void main(String[] args) {
		CompressionDriver driver = new CompressionDriver();
	}
}
