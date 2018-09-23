/*Christian Toro
 *Project 3
 *COP3530
 *Due Date: April 25, 2018
 */
//Code retrieved from OpenDSA
//URL: https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/Huffman.html

public class HuffLeafNode implements HuffBaseNode{
  private char character;
  private int frequency;
  private String hCode;

  public HuffLeafNode(char x, int y){
    this.character = x;
    this.frequency = y;
    this.hCode = "";
  }

  public char getChar(){
    return this.character;
  }

  public int freq(){
    return this.frequency;
  }

  public boolean isLeaf(){
    return true;
  }

  public void huffCode(String s){
    this.hCode = s + this.hCode;
  }

  public String getCode(){
    return this.hCode;
  }
}
