import java.util.*;

// A basic general tree node
public class TreeNode<T> {
	// Each node in the general tree stores its data, its children, and it's parent
	private T data;
	private LinkedList<TreeNode<T>> children;
	private TreeNode<T> parent;
	
	// Construct a root, has no data or parent
	public TreeNode(){
		children = new LinkedList<TreeNode<T>>();
	}
	
	// Construct a root with data, has no parent
	public TreeNode(T data){
		this.data = data;
		children = new LinkedList<TreeNode<T>>();
	}
	
	// Construct with data and a known parent
	public TreeNode(T data, TreeNode<T> parent) {
		this.data = data;
		children = new LinkedList<TreeNode<T>>();
		this.parent = parent;
	}
	
	// Return a list of all the children
	public LinkedList<TreeNode<T>> children() {
		return children;
	}
	
	// Return the parent
	public TreeNode<T> parent(){
		return parent;
	}
	
	// Solver.java only ever needs the last child of a node, so return it with this method
	public TreeNode<T> child() {
		return children.get(children.size()-1);
	}
	
	// Return the data at the node
	public T data() {
		return data;
	}
	
	// Create a child TreeNode that will store the passed data
	public void add(T newData) {
		TreeNode<T> newChild = new TreeNode<T>(newData, this);
		children.addLast(newChild);
	}
}