import java.io.*;
import java.util.Scanner;
import java.util.PriorityQueue;

public class HuffmanEncoder implements HuffmanCoding{

  //Constructor for HuffmanEncoder
  public HuffmanEncoder(){

  }

//---------------------------------------------------------------------------------------------------------------------------

  //take a file as input and create a table with characters and frequencies
	//print the characters and frequencies
  public String getFrequencies(File inputFile){
    String freq = "";
    //Hash table for the characters and frequencies
    int[] cFreq = new int[256];
    //Try catch block file read error
    try{
      //Methods gets character frequency and copy into array cFreq
      copyArray(cFreq, freqArray(inputFile));
      //Builds the string to be returned
      for(int i = 0; i < 256; i++){
        char x = (char) i;
        if(cFreq[i] != 0){
          if(freq == ""){
            freq = freq + x + " " + cFreq[i];
          }
          else{
            freq = freq + "\n" + x + " " + cFreq[i];
          }
        }
      }
    }
    //Prints error if the file is not found.
    catch (Exception e){
      System.out.println("File not found");
    }
    return freq;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //take a file as input and create a Huffman Tree
  public HuffTree buildTree(File inputFile){
    //Try catch block for file read error
    try{
      //Copy frequency array into cFreq
      int[] cFreq = new int[256];
      copyArray(cFreq, freqArray(inputFile));
      //Creates the priority queue and adds leafs with characters and frequencies into it
      PriorityQueue<HuffTree> buildTheTree = new PriorityQueue<HuffTree>();
      for(int i = 0; i < 256; i++){
        if(cFreq[i] != 0){
          buildTheTree.add(new HuffTree((char) i, cFreq[i]));
        }
      }
      //Creates the huffman tree by popping out the first two values from priority queue and then creating
      //tree with root as the same of the frequency of the sum of the first two nodes.
      while(buildTheTree.peek() != null){
        HuffTree left = buildTheTree.poll();
        if(buildTheTree.peek() != null){
          HuffTree right = buildTheTree.poll();
          buildTheTree.add(new HuffTree(left.getRoot(), right.getRoot(), (left.getRoot().freq() + right.getRoot().freq())));
        }
        //Once the PriorityQueue is empty return the last popped tree
        else
          return left;
      }

    }
    //Catches error if the file isn't found
    catch(Exception e){
      e.printStackTrace();
      System.out.println("File not found");
    }

    return null;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //take a file and a HuffTree and encode the file.
	//output a string of 1's and 0's representing the file
  public String encodeFile(File inputFile, HuffTree huffTree){
    //If tree is empty then returns empty string
    if(huffTree == null){
      return "";
    }
    //If the tree only has one character frequency, then only print out 0 the number of times it shows up in the file
    if(huffTree.getRoot() instanceof HuffLeafNode){
      //String builder for faster computation and building string
      StringBuilder quick2 = new StringBuilder();
      //Try catch for file error
      try{
        //Used to read the file
        BufferedReader quick = new BufferedReader(new FileReader(inputFile));
        int cha = 0;
        //Loops until every character in the file has been read.
        while((cha = quick.read()) != -1){
          if(cha == (int)((HuffLeafNode) huffTree.getRoot()).getChar()){
            quick2.append("0");
          }
        }
      }
      //If file not found prints file not found
      catch(Exception a){
        System.out.println("File Not Found");
      }
      return quick2.toString();
    }
    //If neither of the above then created string builder and array of strings
    String encoded = "";
    StringBuilder eCode= new StringBuilder();
    String[] binary = new String[256];
    //Use method to parse through string and fill binary string with the proper codes with
    //ascii value as hash code
    encodeArray(traverseHuffmanTree(huffTree), binary);
    //Try catch for file error
    try{
      //Reader to read through the file by character
      BufferedReader data = new BufferedReader(new FileReader(inputFile));
      int c = 0;
      //Loops thorugh the text file by character until there is no more character
      //Increments the frequency of each character
      while((c = data.read()) != -1){
        if(c == 10){

        }
        //Appends to string if c int is ascii by adding the string at that array value.
        else if(c >= 0 && c <= 255){
          eCode.append(binary[c]);
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      System.out.println("File Not Found");
    }
    encoded = eCode.toString();
    return encoded;
  }

//---------------------------------------------------------------------------------------------------------------------------

	//take a String and HuffTree and output the decoded words
  public String decodeFile(String code, HuffTree huffTree){
    String decoded = "";
    //Adjust string for loop exit
    String codeAdjusted = code + "0";
    //String builder for faster computation and building string
    StringBuilder dCode = new StringBuilder();
    //Checks to see if tree is null
    if(huffTree != null){
      if(huffTree.getRoot() instanceof HuffLeafNode){
        for(int i = 0; i < code.length(); i++){
          if(code.charAt(i) == 48){
            dCode.append(((HuffLeafNode) huffTree.getRoot()).getChar());
          }
        }
        decoded = dCode.toString();
        return decoded;
      }
      //Creates a temp value to traverse the tree
      HuffBaseNode temp = huffTree.getRoot();
      for(int i = 0; i < codeAdjusted.length(); i++){
        //Used to terminate the loop
        if(i == codeAdjusted.length() - 1){
          if(temp instanceof HuffLeafNode){
            dCode.append(((HuffLeafNode) temp).getChar());
            break;
          }
        }
        //If the temp is a leaf them add its char value to string and
        //reset temp to the root
        //Decrement to adjust for traversal
        if(temp instanceof HuffLeafNode){
           dCode.append(((HuffLeafNode) temp).getChar());
           temp = huffTree.getRoot();
           i--;
        }
        //If temp is internal then if its a 0 follow the left child and if its a 1 follow
        //the right child
        else{
           if(code.charAt(i) == 48){
             temp = ((HuffInternalNode) temp).left();
           }
           else if(code.charAt(i) == 49){
             temp = ((HuffInternalNode) temp).right();
           }
          //If the code has anything other than 0s and 1s then there is an error
           else{
             System.out.println("Incorrect String Value. Returning blank");
             return "";
           }
        }
      }
    }
    else{
      //Returns blank if the tree is null
      return "";
    }
    //Converts stringbuilder to string to return string
    decoded = dCode.toString();
    return decoded;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //print the characters and their codes
  //Uses recursion to make building the string easier
  public String traverseHuffmanTree(HuffTree huffTree){
    String code = "";
    //Checks to make sure tree is not null
    if(huffTree != null){
      //If the root of the tree is a leaf, then adds char and code to string
      if(huffTree.getRoot() instanceof HuffLeafNode){
        //Checks to see if tree only contains one node which is a leaf
        if(((HuffLeafNode) huffTree.getRoot()).getCode() == ""){
          return ((HuffLeafNode) huffTree.getRoot()).getChar() + " 0";
        }
        else{
          return ((HuffLeafNode) huffTree.getRoot()).getChar() + " " + ((HuffLeafNode) huffTree.getRoot()).getCode();
        }
      }
      //Otherwise the node as an internal node
      else{
        //If both children of the node are then create recursively call method with the string as left + right
        //Organizes string afterward to ensure ASCII order
        if(((HuffInternalNode) huffTree.getRoot()).left() instanceof HuffLeafNode && ((HuffInternalNode) huffTree.getRoot()).right() instanceof HuffLeafNode){
          code = code + traverseHuffmanTree(new HuffTree(((HuffLeafNode) ((HuffInternalNode) huffTree.getRoot()).left()).getChar(), ((HuffLeafNode)((HuffInternalNode)huffTree.getRoot()).left()).freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "0"))
                      + "\n" +  traverseHuffmanTree(new HuffTree(((HuffLeafNode) ((HuffInternalNode) huffTree.getRoot()).right()).getChar(), ((HuffLeafNode)((HuffInternalNode)huffTree.getRoot()).right()).freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "1"));
          code = organizeString(code);
          return code;
        }
        //If left child is leaf and right child is internal node then same as above if statement
        //Organizes String to ensure ascii order
        else if(((HuffInternalNode) huffTree.getRoot()).left() instanceof HuffLeafNode && ((HuffInternalNode) huffTree.getRoot()).right() instanceof HuffInternalNode){
          code = code + traverseHuffmanTree(new HuffTree(((HuffLeafNode) ((HuffInternalNode) huffTree.getRoot()).left()).getChar(), ((HuffLeafNode)((HuffInternalNode)huffTree.getRoot()).left()).freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "0"))
                      + "\n" +  traverseHuffmanTree(new HuffTree(((HuffInternalNode) ((HuffInternalNode) huffTree.getRoot()).right()).left(), ((HuffInternalNode)((HuffInternalNode)huffTree.getRoot()).right()).right(), ((HuffInternalNode) huffTree.getRoot()).right().freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "1"));
          code = organizeString(code);
          return code;
        }
        //If left child is internal and right child is leaf node then same as above if statement
        //Organizes String to ensure ascii order
        else if(((HuffInternalNode) huffTree.getRoot()).left() instanceof HuffInternalNode && ((HuffInternalNode) huffTree.getRoot()).right() instanceof HuffLeafNode){
          code = code + traverseHuffmanTree(new HuffTree(((HuffInternalNode) ((HuffInternalNode) huffTree.getRoot()).left()).left(), ((HuffInternalNode)((HuffInternalNode)huffTree.getRoot()).left()).right(), ((HuffInternalNode) huffTree.getRoot()).left().freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "0"))
                      + "\n" +  traverseHuffmanTree(new HuffTree(((HuffLeafNode) ((HuffInternalNode) huffTree.getRoot()).right()).getChar(), ((HuffLeafNode)((HuffInternalNode)huffTree.getRoot()).right()).freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "1"));
          code = organizeString(code);
          return code;
        }
        //If left child and right child are internal node then same as above if statement
        //Organizes String to ensure ascii order
        else if(((HuffInternalNode) huffTree.getRoot()).left() instanceof HuffInternalNode && ((HuffInternalNode) huffTree.getRoot()).right() instanceof HuffInternalNode){
          code = code + traverseHuffmanTree(new HuffTree(((HuffInternalNode) ((HuffInternalNode) huffTree.getRoot()).left()).left(), ((HuffInternalNode)((HuffInternalNode)huffTree.getRoot()).left()).right(), ((HuffInternalNode) huffTree.getRoot()).left().freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "0"))
                      + "\n" +  traverseHuffmanTree(new HuffTree(((HuffInternalNode) ((HuffInternalNode) huffTree.getRoot()).right()).left(), ((HuffInternalNode)((HuffInternalNode)huffTree.getRoot()).right()).right(), ((HuffInternalNode) huffTree.getRoot()).right().freq(), ((HuffInternalNode) huffTree.getRoot()).getCode() + "1"));
          code = organizeString(code);
          return code;
        }
      }
    }
    else
      //Return blank if tree is null
      return "";
    return code;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //Creates integer array of frequencies from the file
  //Used in multiple methods above
  public int[] freqArray(File inputFile){
    int[] charFreq = new int[256];
    try{
      BufferedReader data = new BufferedReader(new FileReader(inputFile));
      int c = 0;
      //Loops thorugh the text file by character until there is no more character
      //Increments the frequency of each character
      while((c = data.read()) != -1){
        if(c >= 0 && c <= 255){
          charFreq[c]++;
        }
      }
      charFreq[10] = 0;
    }
    //Prints error if the file is not found.
    catch (Exception e){
      System.out.println("File not found");
    }
    return charFreq;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //A simple array copy algorithm for 1-D arrays
  //Used multiple times so method made reading code easier
  public void copyArray(int[] copy, int[] copied){
    if(copy.length == copied.length){
      for(int i = 0; i < copied.length; i++){
        copy[i] = copied[i];
      }
    }
  }

//---------------------------------------------------------------------------------------------------------------------------

  //Takes in string from traverseHuffmanTree and organizes it by ASCII order
  //per the instructions for output format
  public String organizeString(String x){
    String fixed = "";
    //Creates array with each value in array being separated by newline
    String[] org = x.split("\n");
    //Uses merge sort algorithm
    sort(org, 0, org.length - 1);
    //Loop to recreated string that is now organized
    for(int i = 0; i < org.length; i++){
      if(i == 0){
        fixed = org[0];
      }
      else{
        fixed = fixed + "\n" + org[i];
      }
    }
    return fixed;
  }

//---------------------------------------------------------------------------------------------------------------------------

  //Merge Sort algorithm obtained from previous sorting analysis project and modified for string array
  //Code retrieved from ZYbooks
  //Faster sorting becuase of the better computational complexity
  public void sort(String[] x, int min, int max){
    int mid = 0;
    if (min < max){
      mid = (min + max) / 2;
      sort(x, min, mid);
      sort(x, mid + 1, max);
      merge(x, min, mid, max);
    }
  }

//---------------------------------------------------------------------------------------------------------------------------

  //Merge Sort algorithm obtained from previous sorting analysis project and modified for string array
  //Code retrieved from ZYbooks
  //Faster sorting becuase of the better computational complexity
  public void merge(String[] x, int min, int mid, int max){
    int size = max - min + 1;
    String[] mergedList = new String[size];
    int current = 0;
    int left = 0;
    int right = 0;
    left = min;
    right = mid + 1;
    while(left <= mid && right <= max){
      if(x[left].compareTo(x[right]) > 0){
        mergedList[current] = x[right];
        right++;
      }
      else{
        mergedList[current] = x[left];
        left++;
      }
      current++;
    }
    while(left <= mid){
      mergedList[current] = x[left];
      left++;
      current++;
    }
    while(right <= max){
      mergedList[current] = x[right];
      right++;
      current++;
    }
    for(current = 0; current < size; current++){
      x[min + current] = mergedList[current];
    }
  }

//---------------------------------------------------------------------------------------------------------------------------

  //This method is used to organize a string with huffcodes into a a string array
  //for better organization and easier access.
  public void encodeArray(String x, String[] y){
    //Loop through size of string
    //Adds huffcode by finding the character after each newline and then
    //skipping the space and grabbing the substring from that point until the next newline
    for(int i = 0; i < x.length(); i++){
      if(i == 0){
        y[x.charAt(0)] = x.substring(2,x.indexOf(10));
        i = x.indexOf(10);
      }
      else{
        String temp = x.substring(i);
        if(temp.indexOf(10) == -1){
          y[temp.charAt(0)] = temp.substring(2);
          break;
        }

        y[temp.charAt(0)] = temp.substring(2, temp.indexOf(10));
        i = i + temp.indexOf(10);

      }
    }
  }

}
