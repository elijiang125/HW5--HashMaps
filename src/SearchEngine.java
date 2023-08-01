import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchEngine {
    private int mode;
    private Dictionary<String, Node> nodeTable;

    // build everything bahahah
    // TODO: mode 5 = Openaddressing mode 6 = Chaining; build
    public SearchEngine(int mode) throws IOException {
        this.mode = mode;

        if (mode == 5) {
            nodeTable = new HashTableOpenAddressing<>();
        }
        else if (mode == 6) {
            nodeTable = new HashTableWithChaining<>();
        }
    }

    public Dictionary<String, Node> getNodeTree(){
        return this.nodeTable;
    }

    // TODO: assumes that the file exists already
    public void buildList() throws IOException {
        System.out.println("reading");
        BufferedReader reader = new BufferedReader(new FileReader("dataset.txt"));
        String url;
        while((url = reader.readLine()) != null){
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text().toLowerCase();
            if(url.equals("https://en.wikipedia.org/wiki/Kamala_Harris"))            System.out.println(text);

            // System.out.println(text);
            String[] words = text.split("\\s+"); // splits by whitespace
            int count = 0;
            for (String word : words) {
                // TODO: add the word as a keyword
                // Key = word, Value = Node -> keyword + references

                //NOTE: if word already exists, then add it to list of references
                if (this.nodeTable.containsKey(word)) {
                    Node existingNode = this.nodeTable.get(word);
                    existingNode.insertReference(url); //add url link to the list of references
                }
                else { //if the word hasn't existed, then add it to the nodeTable as a new Node
                    this.nodeTable.put(word, new Node(word));
                }

            }
        }
        reader.close();
        System.out.println("Finished reading through all URLs");
    }

    // TODO: return the results from one term
    public ArrayList<String> search(String term) {
        System.out.println("Searching for " + term + " using data structure mode " + mode + "...");
        //HTC/HTOA -> Node -> ArrayList<String>
        return this.nodeTable.get(term).getReferences();
    }

    public static void main(String[] args) throws IOException{
        Scanner input = new Scanner(System.in);

        System.out.println("Enter mode as in what data structure to use:");
        System.out.println("    5. HashTableOpenAddressing ");
        System.out.println("    6. HashTableWithChaining");

        int mode = input.nextInt();

        System.out.println("Building Search Engine...");
        SearchEngine engine = new SearchEngine(mode);

        String answer = "y";
        while (answer.equals("y")) {
            input.nextLine(); // consume the remaining newline character
            System.out.print("Search (enter a term to query): ");
            String term = input.nextLine();
            engine.search(term);
            System.out.print("Would you like to search another term (y/n)? ");
            answer = input.nextLine();
        }
        input.close();
    }
}