/*Christian Toro
 *Project 3
 *COP3530
 *Due Date: April 25, 2018
 */
//Code retrieved from OpenDSA
//URL: https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/Huffman.html

public interface HuffBaseNode{
  boolean isLeaf();
  int freq();
  void huffCode(String s);
  String getCode();
}
