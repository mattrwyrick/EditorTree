//package editortrees;
//
//// A node in a height-balanced binary tree with rank.
//// Except for the NULL_NODE (if you choose to use one), one node cannot
//// belong to two different trees.
//
//public class Node {
//	
//	enum Code {
//		SAME, LEFT, RIGHT;
//		// Used in the displayer and debug string
//		public String toString() {
//			switch (this) {
//				case LEFT:
//					return "/";
//				case SAME:
//					return "=";
//				case RIGHT:
//					return "\\";
//				default:
//					throw new IllegalStateException();
//			}
//		}
//	}
//	
//	// The fields would normally be private, but for the purposes of this class, 
//	// we want to be able to test the results of the algorithms in addition to the
//	// "publicly visible" effects
//	
//	char element;            
//	Node left, right; // subtrees
//	int rank;         // inorder position of this node within its own subtree.
//	Code balance; 
//	
//	// Node parent;  // You may want this field.
//	// Feel free to add other fields that you find useful
//	
//	//NULL_NODE 
//	public Node(){
//		this.element = (Character) null;
//		this.left = null;
//		this.right = null;
//		this.rank = 0;
//		this.balance = Code.SAME;
//	}
//	
//	public Node(char item){
//		this.element = item;
//		this.left  = EditTree.NULL_NODE;
//		this.right = EditTree.NULL_NODE;
//		this.rank  = 0;
//		this.balance = Code.SAME;
//		
//	}
//	// You will probably want to add several other methods
//
//	// For the following methods, you should fill in the details so that they work correctly
//	public int height() {
//		return 0;
//	}
//
//	public int size() {
//		return 0;
//	}
//
//	public Node add(char c) {
//		if(this == NULL_NODE){
//			return new Node(c);
//		}
//		this.right.add(c);
//		return this;
//	}
//
//}