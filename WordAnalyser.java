import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import java.io.File;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtilities;



/**
 * Class representing the Ngram analyser.
 */
public class WordAnalyser {
	/**
	 * ArrayList that stores each valid word from the file to read.
	 */
	private ArrayList<String> words = new ArrayList<String>();
	/**
	 * Stores the filepath of the file to read from.
	 */
	private static String file_read;
	/**
	 * Stores the filepath of the file to write to.
	 */
	private static String file_write;
	/**
	 * Stores the ngram number, should always be ngram-1 due to the method that calculates the n-grams.
	 */
	private static int N_GRAMS;
	/**
	 * HashMap to store the ngram as the key and the corresponding NgramCount object as the value.
	 */
	private HashMap<String, NgramCount> map = new HashMap<String, NgramCount>();

	private static String file_read_next;

	/**
	 * Constructors used to initialise the file_read, file_write and N_GRAMS variables.
	 * @param file_read  stores the value of the filepath of the input file.
	 * @param  file_write  stores the value of the filepath of the file to write to.
	 * @param  ngrams  stores the value of the number of ngrams that should be analysed - 1.
	 */
	WordAnalyser(String file_read, String file_read_next, String file_write, int ngrams)  {
		this.file_read_next = file_read_next;
		this.file_read = file_read;
		this.file_write = file_write;
		this.N_GRAMS = ngrams - 1;
	}

	/**
	 * Method that is used to read from the given input file and stores each
	 * valid word in the words array list. The method makes words valid by
	 * converting them to the lower case forms of themselves and also removing
	 * any non-alphabetical letters, that is; numbers, punctuation, etc. If it
	 * encounters some white space, it simply ignores it and continues to the next
	 * iteration.
	 *
	 * @throws IOException IOException has to be thrown when reading from a file.
	 */
	private void readInputFile() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.file_read));
		BufferedReader readerNew = new BufferedReader(new FileReader(this.file_read_next));

		for (String contents = reader.readLine(); contents != null; contents = reader.readLine()) { /* Iterates through all lines in the file to read */
		
			for (String retval: contents.split(" ")) { /* Iterates through all words split around white space */
				if (!retval.replaceAll("[^a-zA-Z]", "").isEmpty()) { /* If the word when stripped of non-alphabetical characters is not empty then add the word to the words list */
					words.add(retval.replaceAll("[^a-zA-Z]", "").toLowerCase()); /* replaces all non-alphabetical characters and converts this replacement to its lower case form, adding such a string to the words list */
					
				} /* end of if statement */
			} /* end of inner loop */
		}

		for (String contentsNew = readerNew.readLine(); contentsNew!=null; contentsNew=readerNew.readLine()) {
			for (String retval: contentsNew.split(" ")) { /* Iterates through all words split around white space */
				if (!retval.replaceAll("[^a-zA-Z]", "").isEmpty()) { /* If the word when stripped of non-alphabetical characters is not empty then add the word to the words list */
					words.add(retval.replaceAll("[^a-zA-Z]", "").toLowerCase()); /* replaces all non-alphabetical characters and converts this replacement to its lower case form, adding such a string to the words list */

				} /* end of if statement */
			} /* end of inner loop */
		} /* end of outer loop */	
		
		
	
	}

	/**
	 * This method gets the Ngramsm from the words list and adds them to the ngram array list.
	 * If the ngram list already contains an ngram that is being passed to it then it increments the
	 * count attribute of the object that stores the ngram as an attribute; NgramCount. Otherwise, it creates
	 * an instance of NgramCount with a count attribute of 1 and an ngram attribute of the ngram that the method is
	 * currently inspecting.
	 */
	private void getAndInsertNGrams() {

		for (int i = this.N_GRAMS; i < words.size(); i++) { /* Iterates through all elements in the words list */
			String x = getNGrams(i); /* Gets the current element in the words list */
			if (map.containsKey(x)) { /* map has a key equivalent to the current n-gram being inspected */
				map.get(x).incrementCount(); /* n-gram already appears so increment its count */
			}
			else {
				NgramCount ng = new NgramCount(x, 1); /*ngram instance with default count value */
				map.put(x, ng); /* key is ngram (unique) and value is object of class NgramCount */
			}
		}
	}

	/**
	 * Method that gets each word that make up an ngram and stores them in a String, terminating the loop when the ngram has been found.
	 *
	 * @param  current_position stores the int value representing the current position of the word being analysed (that is, the last word in an ngram).
	 * @return     returns a String that stores the ngram calculated.
	 */
	private String getNGrams(int current_position) {

		String x = ""; /* Initialises the string variable */
		for (int i = this.N_GRAMS; i >= 0; i--) { /* Itelrates through all words that make up an ngram; i.e. 3 words if n is 3, 4 words if n is 4, and so on */
			if (i > 0) { /* If the word being added to x isn't the last word then add white space to the end of the word */
				x = x + words.get(current_position - i) + " ";
			}
			else { /* Otherwise the current word is the last word so don't add white space to the end of the word */
				x = x + words.get(current_position - i);
			}
		}
		return x; /* returns the string */
	}

	/**
	 * Method that prints the contents of the Ngram list
	 * to the given .csv file.
	 * @throws IOException IOException has to be thrown in order to write to the specified file.
	 **/
	private void printToCSV() throws IOException {

		ArrayList<NgramCount> list = new ArrayList<NgramCount>(map.values()); /* list with elements equal to the values of the map */
		Collections.sort(list); /* sorts the list in ascending order */
		Collections.reverse(list); /* reverses the sorted list so it is in descending order */

		PrintWriter writer = new PrintWriter(this.file_write); /*PrintWriter instance to write to the .csv file */
		for (NgramCount ngram: list){ /* iterates through all objects in the ngrams list */
			writer.write('"' + ngram.getNGram() + '"'); /* writes the ngram attribute of the current object to the .csv file */
			writer.write(","); /* writes a comma to the .csv file as this is the standard .csv delimiter */
			writer.write(ngram.getCount()); /* writes the count attribute of the current object to the .csv file */
			writer.write("\n"); /* newline escape character to write a new line */
		}

		/*flushes and closes the writer */
		writer.flush();
		writer.close();


		System.out.println("Finished. Read from file: " + this.file_read + " and wrote to file: " /* default msg to inform the user that the contents have been written to the .csv file */
				+ file_write);
	}

	/**
	 * Method that calls all of the other methods in order to
	 * read from the given file and then write the resultant ngrams to
	 * the other given file.
	 * @throws IOException IOException needs to be thrown in order to call the methods
	 *         that read from/write to files.
	 */
	public void start() throws IOException {

		readInputFile(); /* calls the readInputFile() method in order to get the contents of the file to read from */
		getAndInsertNGrams(); /* calls the getAndInsertNGrams method in order to group the words into NGrams and pass them to a list of NgramCounts */
		printToCSV(); /* calls the printToCSV() method in order to print the contents of the ngrams list to the given .csv file */
		if(map.size() > 1 ) { /* no point making a graph if the number of ngrams is only equal to one */
			make10Chart(); /* calls the make10Chart() function to create a graph of the 10 most frequent n-grams and save the chart as a jpeg */
		}

	}

	/**
	 * Method that creates a bar chart of the 10 most frequent n-grams and
	 * saves the chart as a jpeg
	 * @throws IOException  needs to throw IOException in order to save the jpeg.
	 */
	private void make10Chart() throws IOException {

		ArrayList<NgramCount> list = new ArrayList<NgramCount>(map.values());
		Collections.sort(list);
		Collections.reverse(list);

		final String num = "Number of occurences";
		final DefaultCategoryDataset data = new DefaultCategoryDataset( );

		for(int i = 0 ; i < 10; i++) {
			data.addValue(Integer.parseInt(list.get(i).getCount()), num, list.get(i).getNGram());
		}

		final String title = "Analysis of 10 most frequent n-grams";
		final String horizontalTitle = "N-Gram group";
		final String verticalTitle = "Number of occurences";
		final String fileName = "Top10NGrams.jpeg";



		/* adds the values to the chart that represent X day of the week */


		JFreeChart barChart = ChartFactory.createBarChart(title, horizontalTitle, verticalTitle, data, PlotOrientation.HORIZONTAL, true, true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File WordAnalyser  = new File(fileName);

		ChartUtilities.saveChartAsJPEG( WordAnalyser , barChart , width , height ); /* saves chart a jpeg */

	}
	/**
	 * Main method.
	 * @param args Array of strings that stores each command-line argument.
	 * @throws IOException IOException has to be thrown in order for the method to call the start() method of the WordAnalyser instance
	 * @throws FileNotFoundException FileNotFoundException has to be thrown in order for the method to call the start() method of the WordAnalyser instance
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException {

		try {
			String file_read = args[0];
			String file_next_read = args[1];
			String file_write = args[2];
			int ngrams = Integer.parseInt(args[3]);

			if(ngrams < 2) {
				System.out.println("Usage: java WordAnalyser input.txt output.csv ngrams; where ngram is an integer >=2");
				return; /* exit the program */
			}

			System.out.println("One file to read from: " + file_read);
            System.out.println("Another file to read from: " + file_next_read);
			System.out.println("File to write to: " + file_write);
			System.out.println("NGrams: " + ngrams);

			WordAnalyser analyser = new WordAnalyser(file_read,file_next_read, file_write, ngrams); /* WordAnalyser instance */
			analyser.start(); /* calls start() method to actually analyse the txt file */


		} catch (ArrayIndexOutOfBoundsException e) { /* not enough args */
			System.out.println("Usage: java WordAnalyser input.txt moreinput.txt output.csv ngrams");
			return;
		} catch (FileNotFoundException f) { /* input file not found */
			System.out.println("Input file is unavailable");
			return;
		} catch(NumberFormatException n) { /* doesn't inpit ngram as integer */
			System.out.println("Usage: java WordAnalyser input.txt moreinput.txt output.csv ngrams; where ngrams is an integer >=2 ");
			return;
		}

	}
}
