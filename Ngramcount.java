/**
 * A Class for counting ngrams
 * Kasim Terzic, 10 Feb 2017
 */

class NgramCount implements Comparable<NgramCount> {

   /**
    * Represents the actual ngram string.
    */
   private String ngram;
   /**
    * Represents the number of occurences the ngram has.
    */
   private int count;

   /**
    * Constructor that sets the ngram and ngram attributes to the inputs.
    * @param name  stores the value of the n-gram
    * @param  count stores the count of the n-gram (frequency). 
    */
   NgramCount(String name, int count) {
       this.count = count;
       this.ngram = name;
   }

   // The code below will allow Java to sort theses objects
   // This is provided to ensure that all students use the
   // same sorting order and make
   // 
   public int compareTo(NgramCount o) {
       if (this.count == o.count) {
              return o.ngram.compareTo(this.ngram);
         }
       return this.count - o.count;
   }
   
   /**
    * returns the count number of the object.
    * @return count attribute representing the frequency of the n-gram.
    */
   public String getCount() {
       return String.valueOf(this.count);
   }

   /**
    * Returns the ngram value of the objec.
    * @return ngram attribute representing the frequency of the n-gram.
    */
   public String getNGram() {
       return this.ngram;
   }

   /**
    * Increments the count attribute by +1.
    */
   public void incrementCount() {
    this.count++;
   }
}
