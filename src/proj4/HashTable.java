
package proj4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Rachael Birky
 * @version 04.18.14
 * 
 * <p> HashTable.java
 * <p> This class represents a generic hash table, which stores
 * 		key-value pairs in an index specified by the key's hash code value.
 * <p> This class also contains a sub-class "HashTableObject"
 * 		that stores the key and value pairs.
 *
 * @param <K, V> this class is generic
 */
public class HashTable<K, V>{

	private LinkedList<HashTableObject<K,V>>[] table;
	private final static int INITIAL_SIZE = 27;
	private int numEntries=0;
	private int numCollisions=0;


	/**
	 * <p> Constructor
	 * <p> Description:  Calls the other constructor,
	 * 		passing the default hash table size
	 */
	public HashTable(){
		this(INITIAL_SIZE);
	}

	/**
	 * <p> Constructor
	 * <p> Description:  Creates a new table with a size
	 * 		equal to the next prime number after the given number.
	 * 		Initializes each table index with a LinkedList for a
	 * 		SeparateChaining hash table implementation
	 * @param aSize - the given table size
	 */
	public HashTable(int aSize){
		table = new LinkedList[getPrime(aSize)];
		for (int i=0; i<table.length; i++){
			table[i] = new LinkedList<HashTableObject<K,V>>();
		}
	}

	/**
	 * <p> Method:  get
	 * <p> Description:  returns the value associated with the given key
	 * @param key
	 * @return null if the key maps to nothing, else the associated value
	 */
	public V get(K key){

		int index = getPositionHashtable(key);
		LinkedList<HashTableObject<K, V>> currentList = table[index];

		if (currentList.size()==1) return table[index].peek().getValue();
		else if (currentList.size()>1) return getFromList(key, currentList);
		else return null;

	}

	/**
	 * <p> Method:  containsKey
	 * <p> Description:  determines whether the given key is
	 * 			already in the hash table
	 * @param key - the key for which to search
	 * @return true if the key is already in the table, false otherwise
	 */
	public boolean containsKey(K key){
		int index = getPositionHashtable(key);
		LinkedList<HashTableObject<K, V>> currentList = table[index];

		if (currentList.size()>=1) return listContainsKey(key, currentList);
		else return false;

	}

	/**
	 * <p> Method:  put
	 * <p> Description:  puts the given key-value pair into the hash table 
	 * 			handles collisions using separate chaining 
	 * @param key - the key to add to the table
	 * @param  value - a corresponding value to add to the table
	 */
	public void put(K key, V value){

		int index = getPositionHashtable(key);
		LinkedList currentList = table[index];
		int initialListLength = currentList.size();

		if(!this.containsKey(key)){
			currentList.add(new HashTableObject<K, V>(key, value));

			if ((initialListLength>0) && (currentList.size() > initialListLength))
				numCollisions++;

//			numEntries++;

			if( ++numEntries > table.length)
				enlargeTable( );
		}
	}

	/**
	 * <p> Method:  numSlots
	 * <p> Description:  gives the number of slots in the current hash table
	 * @return table.length - the number of slots
	 */
	public int numSlots(){
		return table.length;
	}

	/**
	 * <p> Method:  numEntries
	 * <p> Description:  gives the number of entries in the current hash table
	 * 
	 * @return numEntries - the number of entries
	 */
	public int numEntries(){
		return numEntries;
	}

	/**
	 * <p> Method:  numCollisions
	 * <p> Description:  gives the number of collisions in the current hash table
	 * 
	 * @return numCollisions - the number of collisions
	 */
	public int numCollisions(){
		return numCollisions;
	}

	/**
	 * <p> Method:  getPositionHashtable
	 * <p> Description:  gets the slot position of the given key in the hash table
	 * @param key - the key for which we are finding the slot index
	 * @return the key's hashcode % the number of slots in the hash table
	 */
	public int getPositionHashtable(K key){
		return key.hashCode()%table.length;
	}

	/**
	 * <p> Method:  getPrime
	 * <p> Description:  a function that uses Java's
	 * 			BigInteger class to determine the next prime number
	 * 			after the given integer
	 * @param givenSize - an integer to find the next prime number
	 * @return the next prime integer
	 */
	private int getPrime(int givenSize){
		Integer aSize = new Integer(givenSize);
		BigInteger bigSize = new BigInteger(aSize.toString());
		return bigSize.nextProbablePrime().intValue();
	}

	/**
	 * <p> Method:  getFromList
	 * <p> Description:  an internal method used to return the
	 * 			value associated with the given key if it has experienced a
	 * 			collision and been placed in a chain
	 * @param key, aList - the key and the list it is in
	 * @return null if no object; the value of the key otherwise
	 */
	private V getFromList(K key, LinkedList<HashTableObject<K, V>> aList){
		for (HashTableObject<K, V> o : aList){
			if (o.getKey().equals(key)) return o.getValue();
		}
		return null;
	}

	/**
	 * <p> Method:  listContainsKey
	 * <p> Description:  an internal method used to determine whether the
	 * 			given key is in the given chain
	 * @param aKey, aList - the key for which to search, and the list to search
	 * @return true if the key is contained, false otherwise
	 */
	private boolean listContainsKey(K aKey, LinkedList<HashTableObject<K, V>> aList){
		for (HashTableObject<K, V> o : aList){
			if (o.getKey().equals(aKey)) return true;
		}
		return false;
	}

	/**
	 * <p> Method: printHashTable  
	 * <p> Description:  prints the hash table to the console as a key-value array
	 */
	public void printHashTable(){
		for (int i = 0; i<table.length; i++)
			System.out.println(table[i].toString());
	}



	/**
	* <p> Method:  enlargeTable
	* <p> Description:  makes the table approximately 2x larger
	* 		while retaining the information (rehashes too!)
	* 
	* @return none
	*/
	private void enlargeTable(){

		LinkedList<HashTableObject<K,V>>[] originalTable = table;
		int newSize = getPrime(2*table.length);
		table = new LinkedList[newSize];
		
		for(int i=0; i<newSize; i++){
			table[i] = new LinkedList<HashTableObject<K,V>>();
		}

		numEntries=0;
		numCollisions=0;
		for(int j=0; j<originalTable.length; j++){
			for (HashTableObject<K,V> a : originalTable[j]){
				put(a.getKey(), a.getValue());
			}
		}
	}



	/**
	 * @author Rachael Birky
	 * 
	 * <p> This is an internal class used in the hash table
	 * 		It represents an object with a key and value
	 *
	 * @param <K> <V> this class in generic
	 */
	public class HashTableObject<K, V>{
		private K key;
		private V value;

		/**
		 * <p> Constructor
		 * <p> Description:  creates a new key-value pair
		 * @param aKey - the key to be stored
		 * @param aValue - the value to be stored
		 */
		public HashTableObject(K aKey, V aValue){
			this.key = aKey;
			this.value = aValue;
		}

		/**
		 * <p> Method:  getKey
		 * <p> Description:  returns the key of the current hash table object
		 * 
		 * @return this.key
		 */
		public K getKey(){
			return this.key;
		}

		/**
		 * <p> Method: getValue  
		 * <p> Description:  returns the value of the current hash table object
		 * 
		 * @return this.value
		 */
		public V getValue(){
			return this.value;
		}

		public String toString(){
			return key.toString()+":"+value.toString();
		}

	}
	
}
