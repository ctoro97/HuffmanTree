/*Christian Toro
 *Project 3
 *COP3530
 *Due Date: April 25, 2018
 */
//Code retrieved from OpenDSA
//URL: https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/Huffman.html

public class HuffInternalNode implements HuffBaseNode{
  private int frequency;
  private HuffBaseNode left;
  private HuffBaseNode right;
  private String hCode;

  public HuffInternalNode(HuffBaseNode l, HuffBaseNode r, int f){
    this.left = l;
    this.right = r;
    this.frequency = f;
    this.hCode = "";
  }

  public HuffBaseNode left(){
    return this.left;
  }

  public HuffBaseNode right(){
    return this.right;
  }

  public int freq(){
    return this.frequency;
  }

  public boolean isLeaf(){
    return false;
  }

  public void huffCode(String s){
    this.hCode = s + this.hCode;
  }

  public String getCode(){
    return this.hCode;
  }
}
