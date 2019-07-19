import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;

public class main {

	public static void main(String[] args) throws FileNotFoundException {

		File myfile = new File("books.txt");
		Scanner input = new Scanner(myfile);
		HashMap mapForJaneEyre = new HashMap();
		HashMap mapForWarPeace = new HashMap();

		while (input.hasNext()) { // iterates through each book in books.txt
			String fileName = input.next();
			File bookFile = new File(fileName);
			Scanner input2 = new Scanner(bookFile);
			input2.useDelimiter("//Z");
			String text = input2.next();

			if (fileName.equals("janeeyre.txt")) {
				findWords(mapForJaneEyre,text);
			} else if (fileName.equals("war_peace.txt")) {
				findWords(mapForWarPeace,text);
			}
			
		}

		// once all words are added, convert hashmap to arraylist
		ArrayList<Map.Entry<String, Integer>> janeList = convert(mapForJaneEyre);
		ArrayList<Map.Entry<String, Integer>> warList = convert(mapForWarPeace);

		// sort array list through quicksort
		quickSort(janeList); // the last 100 are the most common 100 words
		quickSort(warList);
		
		HashMap combined = new HashMap();
		
		for (int i = janeList.size()-1; i > janeList.size()-100; i--) {
			combined.put(janeList.get(i).getKey(), janeList.get(i).getValue());
		}

		for (int i = warList.size()-1; i > warList.size()-100; i--) {
			if (combined.containsKey(warList.get(i).getKey())) {
				String currentKey = warList.get(i).getKey();
				int currentValue = warList.get(i).getValue();
				combined.put(currentKey, (int) combined.get(currentKey) + currentValue);
			} else {
				combined.put(warList.get(i).getKey(), warList.get(i).getValue());
			}
		}
		
		ArrayList<Map.Entry<String, Integer>> combinedList = convert(combined);
		quickSort(combinedList);
		System.out.println("Words   janeeyre   war_peace    total");
		for (int i = combinedList.size()-1; i > combinedList.size()-100; i--) {
			String currentKey = combinedList.get(i).getKey();
			String jane = "";
			if (mapForJaneEyre.get(currentKey) == null) {
				jane = "0";
			} else {
				jane = "" + mapForJaneEyre.get(currentKey);
			}
			
			String war = "";
			if (mapForWarPeace.get(currentKey) == null) {
				war = "0";
			} else {
				war = "" + mapForWarPeace.get(currentKey);
			}
			System.out.println(currentKey + "     " + jane + "     " + war + "     " + combinedList.get(i).getValue());
		}
		
	}
	
	public static void findWords(HashMap words, String text) {
		boolean isWord = false;
		char ctext;
		text = text.toLowerCase();
		String wordToAdd = "";
		for (int i = 0; i < text.length(); i++) {
			ctext = text.charAt(i);

			if (isWord) {
				if (ctext == ' ' || !Character.isLetter(ctext)) {
					isWord = false;
					if (ctext == ' ' && !wordToAdd.equals("")) {
						// add word to hashmap
						if (wordToAdd.endsWith("ing")) {
							wordToAdd = wordToAdd.substring(0, wordToAdd.length()-3);
							if (words.containsKey(wordToAdd)) {
								words.put(wordToAdd, ((int) words.get(wordToAdd)) + 1);
							}
						} else if (wordToAdd.endsWith("ed")) {
							wordToAdd = wordToAdd.substring(0, wordToAdd.length()-2);
							if (words.containsKey(wordToAdd)) {
								words.put(wordToAdd, ((int) words.get(wordToAdd)) + 1);
							}
						}

						if (words.containsKey(wordToAdd)) {
							words.put(wordToAdd, ((int) words.get(wordToAdd)) + 1);
						} else {
							words.put(wordToAdd, 1);
						}

						wordToAdd = "";
					}
					wordToAdd = "";
				} else {
					wordToAdd += ctext;
				}
			} 
			if (!isWord) {
				if (ctext == ' ') {
					isWord = true;
				}
			}
		}
	}
	
	public static ArrayList<Map.Entry<String, Integer>> convert(HashMap hashmap) {
		ArrayList<Map.Entry<String,Integer>> wordList = new ArrayList<>();
		Iterator iterator = hashmap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
			wordList.add(entry);
		}
		return wordList;
	}

	// method from quickSort.class, but I modified it to suit my needs here
	public static void quickSort(ArrayList<Map.Entry<String, Integer>> S) {
		int n = S.size();
		if (n < 2) return;                       // queue is trivially sorted
		// divide
		Map.Entry<String, Integer> pivot = S.get(0);                     // using first as arbitrary pivot
		ArrayList<Map.Entry<String, Integer>> L = new ArrayList<>();
		ArrayList<Map.Entry<String, Integer>> E = new ArrayList<>();
		ArrayList<Map.Entry<String, Integer>> G = new ArrayList<>();
		while (!S.isEmpty()) {                   // divide original into L, E, and G
			Map.Entry<String, Integer> element = S.remove(0);

			if (element.getValue() < pivot.getValue()) { // element is less than pivot
				L.add(element);
			} else if (element.getValue() == pivot.getValue()) { // element is equal to pivot
				E.add(element);
			} else { // element is greater than pivot
				G.add(element);
			}
		}
		// conquer
		quickSort(L);                      // sort elements less than pivot
		quickSort(G);                      // sort elements greater than pivot
		// concatenate results
		while (!L.isEmpty())
			S.add(L.remove(0));
		while (!E.isEmpty())
			S.add(E.remove(0));
		while (!G.isEmpty())
			S.add(G.remove(0));
	}

}
