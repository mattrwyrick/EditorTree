package editortrees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;


// A height-balanced binary tree with rank that could be the basis for a text editor.
//Samp sucks

public class EditTree {

	private Node root;
	private int rotationCount =0; 
	private Node NULL_NODE = new Node();
	public static boolean continueBalance = true;// flag for balancing the tree after add (insert)
	public static boolean continueBalanceR = true;// flag for balancing the tree after removal
	private char removedChar;// tells us which character is being removed at a specified position
	private EditTree thisTree = this; // debugging tool
	private int size = 0;// gives the number of nodes on the tree

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.root = NULL_NODE;
	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		this.root = new Node(c);
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		root = NULL_NODE;
		if(s.length()>0)
			for(int i=0; i<s.length(); i++)// temporary method which repeatedly calls add. Nlog(N). Plan to change it 
				add(s.charAt(i));
		rotationCount = 0;
	}
	
//	public Node fillTree(String s){
//		Node newNode = new Node(s.charAt(0));
//		
//	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		this.size = e.size;// making the current tree the size of the tree passed in 
		if(e.size<0)
			this.root = NULL_NODE;
		else{
			this.root = e.root;
			this.root = root.copyTree(e.root);
		}

	}
	
	
	
	/**
	 * says if the tree is empty
	 *
	 * @return boolean true if the tree is empty and false if the tree is not empty
	 */
	public boolean isEmpty(){
		return this.root == null || this.root == NULL_NODE;
	}

	/**
	 * the height of the tree
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return root.findHeight();
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree since it was
	 * created. A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		
		return this.rotationCount; // replace by a real calculation.
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		String s = "";
		s = this.root.toStrings();
		
		
		return s;
	}

	/**
	 * This one asks for more info from each node. You can write it like 
	 * the arraylist-based toString() method from the
	 * BST assignment. However, the output isn't just the elements, but the
	 * elements, ranks, and balance codes. Former CSSE230 students recommended
	 * that this method, while making it harder to pass tests initially, saves
	 * them time later since it catches weird errors that occur when you don't
	 * update ranks and balance codes correctly.
	 * For the tree with node b and children a and c, it should return the string:
	 * [b1=, a0=, c0=]
	 * There are many more examples in the unit tests.
	 * 
	 * @return The string of elements, ranks, and balance codes, given in
	 *         a pre-order traversal of the tree.
	 */
	public String toDebugString() {
		ArrayList<String> array = new ArrayList<String>();
		return root.fillArrayToDebugString(array).toString();
		
		
	}

	
	
	
	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if(pos<0 || pos>size) // IF position passed in is greater than the size. Illegal so exception thrown
			throw new IndexOutOfBoundsException();

		char value = root.get(pos).element;
		if(value=='\0')
			throw new IndexOutOfBoundsException();
		return value;
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		
		this.size++;
		this.continueBalance = true;
		this.root = this.root.add(c);
		this.continueBalance = false;

	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
		if(pos>size || pos<0)
			throw new IndexOutOfBoundsException();
		this.size++;
		this.continueBalance = true;
		this.root = this.root.add(c,pos);
		this.continueBalance = false;
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		//return this.size; // replace by a real calculation.
		return root.size();
	}

	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		continueBalanceR= true;
		continueBalance = true;
		removedChar = '\0';
		if(pos<0 || pos>size-1)
			throw new IndexOutOfBoundsException();
		root = root.delete(pos);
		
		size--;
		continueBalanceR= false;
		continueBalance = false;
		return removedChar;
		
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		if(pos<0 || length+pos>size)
			throw new IndexOutOfBoundsException();
		
		String string = "";
		for(int i=0; i<length; i++){
			string+=this.get(pos+i);
		}
		
		return string;
		
	}

	/**
	 * This method is provided for you, and should not need to be changed. If
	 * split() and concatenate() are O(log N) operations as required, delete
	 * should also be O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length)
			throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete"
							: "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {
		if(this == other){
			throw new IllegalArgumentException();
		}
		
		if(other.isEmpty())
			return;
		
		if(this.isEmpty()){
			this.root = other.root;
			other.root = NULL_NODE;
		}
		
		continueBalance = true;
		int h1 = this.height();
		int h2 = other.height();
		
		if(h2>h1){
			Node q = new Node(this.delete(this.size()-1));
			continueBalance = true;
			this.root = other.root.concatenateLeft(other.height(), this.height(), q, this.root);
			other.root = NULL_NODE;
		}
		else{
			Node q = new Node(other.delete(0));
			continueBalance = true;
			this.root = this.root.concatenateRight(this.height(), other.height(), q, other.root);
			other.root = NULL_NODE;
		}
			
		
		
	
		
		
		
	}


	/**
	 * This operation must be done in time proportional to the height of this
	 * tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		return null; // replace by a real calculation.
	}

	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		if(s.equals(""))
			return 0;
		
		Iterator iter = new InOrderIterator(this.root);
		boolean check = false;
		int currentPos = 0;
		int pos = 0;
		String currentString = "";
		while(iter.hasNext()){
			currentString+=iter.next();
			if(currentString.equals(s))
				return pos;
			
			if(currentString.charAt(currentString.length()-1)!=s.charAt(currentString.length()-1)){
				pos = currentPos+1;
				currentString = "";
			}
			
			currentPos++;
		}
		
		return -1;
		
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		if(s.equals(""))
			return 0;
		Iterator iter = new InOrderIterator(this.root);
		int count = 0;
		
		while(iter.hasNext()){
			if(count==pos)
				break;
			iter.next();
			count++;
		}
		
		if(count!=pos)
			return -1;
		
		
		boolean check = false;
		int currentPos = 0;
		int posStart = 0;
		String currentString = "";
		while(iter.hasNext()){
			currentString+=iter.next();
			if(currentString.equals(s))
				return posStart+count;
			
			if(currentString.charAt(currentString.length()-1)!=s.charAt(currentString.length()-1)){
				posStart = currentPos+1;
				currentString = "";
			}
			
			currentPos++;
		}
		
		return -1;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	enum Code {
		SAME, LEFT, RIGHT;
		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
				case LEFT:
					return "/";
				case SAME:
					return "=";
				case RIGHT:
					return "\\";
				default:
					throw new IllegalStateException();
			}
		}
	}
	
	
	/**
	 * an in order iterator using a stack
	 *
	 * @author sampcr.
	 *         Created Apr 13, 2016.
	 */
	public class InOrderIterator implements Iterator<Character> {
		private Stack<Node> s;
		public int currentPos;
		public char calledNext = '\0';

		/**
		 * initializes the stack, greats a temperary root, and pushs what needs to be
		 * pushed onto the stack
		 *
		 */
		public InOrderIterator(Node node) {
			this.s = new Stack();
			Node temp = node;
			while (temp != NULL_NODE) {
				s.push(temp);
				temp = temp.left;
			}
		}
		
		/**
		 *	removes the element you called next to get
		 */
		public void remove(){
			if (calledNext != '\0') {
				EditTree.this.delete(calledNext);
				calledNext = '\0';
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		public boolean hasNext() {
			return !s.isEmpty();
		}

		@Override
		public Character next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			Node output = s.pop();
			Node tempChildren = output.right;

			while (tempChildren != NULL_NODE) {
				s.push(tempChildren);
				tempChildren = tempChildren.left;

			}
			calledNext = output.element;
			return output.element;

		}

	}
	
	
	
	
	
	public class Node {
		
		
		
		// The fields would normally be private, but for the purposes of this class, 
		// we want to be able to test the results of the algorithms in addition to the
		// "publicly visible" effects
		
		char element;            
		Node left, right; // subtrees
		int rank;         // inorder position of this node within its own subtree.
		Code balance; 
		
		// Node parent;  // You may want this field.
		// Feel free to add other fields that you find useful
		
		//NULL_NODE 
		public Node(){
			this.element = '\0';
			this.left = null;
			this.right = null;
			this.rank = 0;
			this.balance = Code.SAME;
		}
		
		

		/**
		 * contructs a node given an item
		 *
		 * @param item char nodes data
		 */
		public Node(char item){
			this.element = item;
			this.left  = NULL_NODE;
			this.right = NULL_NODE;
			this.rank  = 0;
			this.balance = Code.SAME;
			
		}
		
		public Node concatenateRight(int bh,int sh,Node q,Node smallRoot){
			if(this == NULL_NODE)
				return this;
			
			if(!concatCheck(bh,sh)){
				this.right = right.concatenateRight(concatHeightRight(bh), sh, q, smallRoot);
				if(continueBalance)
					return this.updateRightBalance();
				return this;
			}
			
			q.left = this;
			q.right = smallRoot;
			if ((bh - sh) == 0)
				q.balance = Code.SAME;
			else
				q.balance = Code.LEFT;
			q.rank = q.left.size();
			return q;
			
		}
		
		public Node concatenateLeft(int bh, int sh, Node q, Node smallRoot){
			if(this==NULL_NODE)
				return this;
			
			if(!concatCheck(bh,sh)){
				this.left = left.concatenateLeft(concatHeightLeft(bh), sh, q, smallRoot);
				if(continueBalance)
					return this.updateLeftBalance();
				return this;
			}
			q.right = this;
			q.left = smallRoot;
			if ((bh - sh) == 0)
				q.balance = Code.SAME;
			else
				q.balance = Code.RIGHT;
			q.rank = q.left.size();
			return q;
			
		}
		
		public boolean concatCheck(int bh, int sh){
			int value = bh-sh;
			if(value<=1 && value>=0){
				return true;
			}
			return false;
		}
		
		public int concatHeightRight(int bh){
			if(this.balance == Code.LEFT){
				bh = bh-1;
			}
			bh = bh-1;
			return bh;
		}
		
		public int concatHeightLeft(int bh){
			if(this.balance == Code.RIGHT)
				bh = bh-1;
			bh = bh-1;
			return bh;
		}
		
		public int Trivalsize(){
			if(this==NULL_NODE)
				return 0;
			return this.left.size()+1+this.right.size();
		}
		
		public int size(){
			if(this==NULL_NODE)
				return 0;
			return this.rank+1+this.right.size();
		}

		// You will probably want to add several other methods

		// For the following methods, you should fill in the details so that they work correctly
		/**
		 * uses recursion to return the height of the tree
		 *
		 * @return int height of the tree
		 */
		public int findHeight() {
			if(this == NULL_NODE){
				return -1;
			}

			if(this.balance == Code.LEFT){
				return 1+this.left.findHeight();
			}else if(this.balance == Code.RIGHT){
				return 1+this.right.findHeight();
			}else{
				if(!this.isLeaf()){
					return 1+this.left.findHeight();
				}else{
					return 0;
				}
			}

			
		}

		/**
		 * adds an element to a end of the tree with data c.
		 *
		 * @param c char data for the node
		 * @return Node returns the node that is inserted
		 */
		public Node add(char c) {
			if(this==NULL_NODE)
				return new Node(c);
			this.right = this.right.add(c);
			if(continueBalance)
				return updateRightBalance();
			else
				return this;
		}
		
		/**
		 * adds a node at the given postion with data c
		 *
		 * @param c char data for the node
		 * @param pos int position you want the node added in
		 * @return Node the node you insert
		 */
		public Node add(char c, int pos) {
			if(this == NULL_NODE)
				return new Node(c);
			//left child
			if(pos<=this.rank){
				this.rank++;
				this.left =  this.left.add(c,pos);
				
				if(continueBalance)
					return updateLeftBalance();
				else
					return this;
				
			//right child
			}else{
				this.right = this.right.add(c, pos-(this.rank+1));
				
				if(continueBalance)
					return updateRightBalance();
				else
					return this;
				
			}
		}
		
		/**
		 * deletes an element at the position given
		 *
		 * @param pos int position you want to delete the node from
		 * @return Node the node you deleted, NULL_NODE if nothing is deleted.
		 */
		public Node delete(int pos){
			
			if(this==NULL_NODE)
				return NULL_NODE;
			
			int value = this.rank;
			
			//go right
			if(value<pos){
				this.right = this.right.delete(pos-(this.rank+1));
				if(continueBalanceR)
					return updateRightRemove();
				else
					return this;
			}
			
			//go left
			if(value>pos){
				this.left = this.left.delete(pos);
				if(removedChar!='\0')
					this.rank--;
				if(continueBalanceR)
					return updateLeftRemove();
				else
					return this;
			}
			
			//remove this node
			
			if(removedChar=='\0')
				removedChar = this.element;
			
			Node tempThis = this.right.findSmall();
			
			if(tempThis==NULL_NODE) //left child or no child
				return this.left;
			
			tempThis.right = this.right.delete(0);
			tempThis.left = this.left;// left child or 2 child
			
			if(this.right==NULL_NODE)
				tempThis.rank = this.rank-1;
			else
				tempThis.rank = this.rank;
			
			tempThis.balance = this.balance;
			//tempThis.debugBalance();
			if(continueBalanceR){
				return tempThis.updateRightRemove();
			}
			
			return tempThis;
		
		}
		
		/**
		 * finds the smallest in the right tree
		 *
		 * @return Node of the smallest in the left tree
		 */
		public Node findSmall(){
			if(this==NULL_NODE)
				return this;
			
			if(this.left!=NULL_NODE)
				return this.left.findSmall();
			return this;
		}
		
		
		/**
		 * grabs an element at the given position
		 *
		 * @param pos int the position you want to grab
		 * @return Node the node at the position
		 */
		private Node get(int pos){
			if(this==NULL_NODE)
				return this;
			
			if(pos==this.rank)
				return this;
			
			if(pos<this.rank)
				return this.left.get(pos);
			
			return this.right.get(pos-(this.rank+1));
		}
		
		/**
		 * updates the tree after a left remove
		 *
		 * @return Node the node that it just balance or the root of the subtree if rotation occurs.
		 */
		private Node updateLeftRemove(){
			
			if(this==NULL_NODE)
				return this;
			
			Code tBal = this.balance;
			Code lBal = this.right.balance;
			
			
			if(tBal==Code.SAME){
				this.balance = Code.RIGHT;
				continueBalanceR = false;
				return this;
			}
			
			else if(this.balance==Code.LEFT){
				this.balance = Code.SAME;
				return this;
			}
			
			else if(this.balance==Code.RIGHT){
				if(lBal==Code.LEFT)
					return this.doubleLeft();
				else
					return this.singleLeft();
			}
			
			return this;
			
		}
		
		/**
		 * updates the tree after a right remove
		 *
		 * @return Node returns the node that was just balanced or the root if there is a rotation
		 */
		private Node updateRightRemove(){
			
			if(this==NULL_NODE)
				return this;
			
			Code tBal = this.balance;
			Code lBal = this.left.balance;
			
			
			if(tBal==Code.SAME){
				this.balance = Code.LEFT;
				continueBalanceR = false;
				return this;
			}
			
			else if(this.balance==Code.RIGHT){
				this.balance = Code.SAME;
				return this;
			}
			
			else if(this.balance==Code.LEFT){
				if(lBal==Code.RIGHT)
					return this.doubleRight();
				else
					return this.singleRight();
			}
			
			return this;
			
		}
		
		
		//insert
		/**
		 * updates the tree after a left add
		 *
		 * @return Node the node that was just balanced or the root if there was a rotation
		 */
		private Node updateLeftBalance() {
			if(this == NULL_NODE){
				return this;
			}
			Code tBal = this.balance;
			Code lBal = this.left.balance;
			
			
			//same
			if(tBal==Code.SAME){
				if(lBal==Code.SAME){
					if(this.right==NULL_NODE && this.left.isLeaf())
						this.balance = Code.LEFT;
					else
						this.balance = Code.SAME;
				}
				else if(lBal == Code.LEFT)
					this.balance = Code.LEFT;
				
				else if(lBal == Code.RIGHT)
					this.balance = Code.LEFT;
			}
			
			//left
			else if(tBal == Code.LEFT){
				if(lBal == Code.SAME)
					this.balance = Code.LEFT;
				else if(lBal == Code.LEFT)
					return this.singleRight();
				else if(lBal == Code.RIGHT)
					return this.doubleRight();
			}
			
			//right
			else if(tBal == Code.RIGHT){
				if(lBal  == Code.SAME)
					if(this.left.isLeaf()){
						this.balance=Code.SAME;
						continueBalance=false;
					}
					else
						this.balance=Code.RIGHT;
				else if(lBal == Code.RIGHT){
					this.balance=Code.SAME;
					continueBalance=false;
				}
				else if(lBal == Code.LEFT){
					this.balance=Code.SAME;
					continueBalance=false;
				}
			}
			return this;
		}
		


		/**
		 * updates the tree after a right add
		 *
		 * @return Node the node that was just balanced or the root if a rotation just occurred.
		 */
		public Node updateRightBalance(){
			if(this==NULL_NODE)
				return this;
			
			Code tBal = this.balance;
			Code rBal = this.right.balance;
			
			//Same
			if(tBal==Code.SAME){
				//child is same
				if(rBal==Code.SAME){
					//this was a leaf before current insert
					if(this.left==NULL_NODE&&this.right.isLeaf())
						this.balance=Code.RIGHT;
					else
						this.balance=Code.SAME;
				}
				else if(rBal==Code.RIGHT)
					this.balance=Code.RIGHT;
				else if(rBal==Code.LEFT)
					this.balance=Code.RIGHT;
			}
			else if (tBal==Code.LEFT){
				if(rBal==Code.SAME)
					if(this.right.isLeaf()){
						this.balance=Code.SAME;
						continueBalance=false;
					}
					else
						this.balance=Code.LEFT;
				else if(rBal==Code.RIGHT){
					this.balance=Code.SAME;
					continueBalance=false;
				}
				else if(rBal==Code.LEFT){
					this.balance=Code.SAME;
					continueBalance=false;
				}
			}
			
			else{
				if(rBal==Code.SAME)
					this.balance = Code.RIGHT;
				else if(rBal==Code.RIGHT)
					return this.singleLeft();
				else if(rBal==Code.LEFT)
					return this.doubleLeft();
			}
			
			return this;
			
		}
		
		/**
		 * performs a single right rotation
		 *
		 * @return Node returns the root
		 */
		public Node singleRight(){
			
			boolean debugCase = false;
			Node tempNode = this.left;
			if(tempNode.balance==Code.SAME)
				debugCase = true;
			this.left = this.left.right;
			tempNode.right = this;
			rotationCount++;
			if(continueBalance){
				this.balance = Code.SAME;
				tempNode.balance = Code.SAME;
			}
			if(continueBalanceR && !debugCase){
				this.balance = Code.SAME;
				tempNode.balance = Code.SAME;
			}
			
			if(continueBalanceR && debugCase){
				this.balance = Code.LEFT;
				tempNode.balance = Code.RIGHT;
				continueBalanceR = false;
			}
			this.rank -= tempNode.rank+1;
			
			
			
			continueBalance = false;
			return tempNode;
		}
		
		/**
		 * performs a single left rotation
		 *
		 * @return Node returns the root
		 */
		public Node singleLeft(){
			boolean debugCase = false;
			Node tempNode = this.right;
			if(tempNode.balance==Code.SAME)
				debugCase = true;
			this.right = this.right.left;
			tempNode.left = this;
			rotationCount++;
			if(continueBalance){
				this.balance = Code.SAME;
				tempNode.balance = Code.SAME;
			}
			if(continueBalanceR && !debugCase){
				this.balance = Code.SAME;
				tempNode.balance = Code.SAME;
			}
			
			if(continueBalanceR && debugCase){
				this.balance = Code.RIGHT;
				tempNode.balance = Code.LEFT;
				continueBalanceR = false;
			}
			tempNode.rank+=this.rank+1;
			
			
			
			continueBalance = false;
			return tempNode;
		}
		
		/**
		 * performs a double right rotation
		 *
		 * @return Node returns the root
		 */
		public Node doubleRight(){
			continueBalance = false;
			continueBalanceR= false;
			Code code = this.left.right.balance;
			this.left = this.left.singleLeft();
			Node temp = this.singleRight();
			if(code==Code.LEFT)
				temp.doubleBalance(false, Code.RIGHT);
			else
				temp.doubleBalance(true, Code.LEFT);
			
			continueBalanceR=true;
			
			return temp;
		}
		
		/**
		 * performs a double left rotation
		 *
		 * @return Node returns the root
		 */
		public Node doubleLeft(){
			continueBalance = false;
			continueBalanceR= false;
			Code code = this.right.left.balance;
			this.right = this.right.singleRight();
			Node temp = this.singleLeft();
			if(code==Code.LEFT)
				temp.doubleBalance(false,Code.RIGHT);
			else
				temp.doubleBalance(true, Code.LEFT);
			continueBalanceR=true;
			return temp;
		}
		
		/**
		 * balancing after a double rotation
		 *
		 * @param v1 boolean determines double left or double right rotation occurred
		 * @param v2 Code the code of the root.
		 */
		public void doubleBalance(boolean v1, Code v2){
			if(v1){
				this.balance = Code.SAME;
				this.right.balance = Code.SAME;
				this.left.balance = v2;
				if(this.left.isLeaf())
					this.left.balance=Code.SAME;
			}
			else{
				this.balance = Code.SAME;
				this.left.balance = Code.SAME;
				this.right.balance = v2;
				if(this.right.isLeaf())
					this.right.balance = Code.SAME;
			}
		}
			
		/**
		 * tells you if the node is a leaf
		 *
		 * @return boolean true if it is a leaf, false otherwise
		 */
		public boolean isLeaf(){
			return (this.right==NULL_NODE&&this.left==NULL_NODE);
		}
		
		/**
		 * copies the tree into a new tree given the root
		 *
		 * @param eNode Node the node that is the root of the new tree
		 * @return Node returns the node that gets assigned to the root.
		 */
		private Node copyTree(Node eNode){
			if(eNode==NULL_NODE)
				return NULL_NODE;
			
			//make clone
			Node clone = new Node(eNode.element);
			clone.rank = eNode.rank;
			clone.balance = eNode.balance;
			clone.left = clone.left.copyTree(eNode.left);
			clone.right= clone.right.copyTree(eNode.right);
			return clone;
			
			
			
			
		}
		
		/**
		 * returns the in order traversal
		 *
		 * @return String in order traversal of the tree
		 */
		public String toStrings() {
			String s = "";
			if (this == NULL_NODE) {
				return "";
			}
			s += this.left.toStrings();
			if(this.element!='\0')
				s += this.element;
			s += this.right.toStrings();

			return s;
		}
		
		/**
		 * pre order traversal including rank and balance code
		 *
		 * @param array ArrayList<String> array you want to add to
		 * @return ArrayList<String> returns the array.
		 */
		public ArrayList<String> fillArrayToDebugString(ArrayList<String> array){
			if(this == NULL_NODE)
				return array;
			array.add((this.element+"")+(this.rank+"")+this.balance.toString());
			array = this.left.fillArrayToDebugString(array);
			array = this.right.fillArrayToDebugString(array);
			return array;
		}
		
		public int TrivialHeight(){
			if(this==NULL_NODE)
				return 0;
			
			int lh = this.left.TrivialHeight();
			int rh = this.right.TrivialHeight();
			
			if(lh>rh)
				return lh+1;
			return rh+1;
		}

	}
	
	
	
}
