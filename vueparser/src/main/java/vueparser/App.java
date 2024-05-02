package vueparser;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadVueXML readFile = new ReadVueXML();
		readFile.readTheFile(args[0]);
		readFile.saveTheDotFile(args[1]);
	}

}
