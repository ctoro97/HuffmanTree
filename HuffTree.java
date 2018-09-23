/*Christian Toro
 *Project 3
 *COP3530
 *Due Date: April 25, 2018
 */
//Code retrieved from OpenDSA
//URL: https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/Huffman.html

import java.io.*;
import java.util.Scanner;

public class HuffTree implements Comparable{
  private HuffBaseNode root;

  public HuffTree(char ch, int freq){
    this.root = new HuffLeafNode(ch, freq);
  }

  public HuffTree(HuffBaseNode l, HuffBaseNode r, int freq){
    this.root = new HuffInternalNode(l, r, freq);
  }

  public HuffTree(char ch, int freq, String code){
    this.root = new HuffLeafNode(ch, freq);
    this.root.huffCode(code);
  }

  public HuffTree(HuffBaseNode l, HuffBaseNode r, int freq, String code){
    this.root = new HuffInternalNode(l, r, freq);
    this.root.huffCode(code);
  }

  public HuffBaseNode getRoot(){
    return this.root;
  }

  public int frequency(){
    return this.root.freq();
  }

  public int compareTo(Object t){
    HuffTree that = (HuffTree)t;
    if (this.root.freq() < that.getRoot().freq())
      return -1;
    else if (this.root.freq() == that.getRoot().freq())
      return 0;
    else
      return 1;
  }

}
