package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.TreeNode;

public class UnitTest_TreeNode {

	@Test
	public void testInitialize() {
		TreeNode<Integer> t1 = new TreeNode<Integer>();
		assertNotNull(t1);
		TreeNode<Integer> t2 = new TreeNode<Integer>(2);
		assertNotNull(t2);
		TreeNode<Integer> t3 = new TreeNode<Integer>(3, t1);
		assertNotNull(t3);
	}

	@Test
	public void testGetData() {
		TreeNode<Integer> t1 = new TreeNode<Integer>();
		assertNull(t1.data());
		
		TreeNode<Integer> t2 = new TreeNode<Integer>(2);
		int data = t2.data();
		assertEquals(2, data);
	}
	
	@Test
	public void testParent() {		
		TreeNode<Integer> t2 = new TreeNode<Integer>(2);
		TreeNode<Integer> t3 = new TreeNode<Integer>(3, t2);
		TreeNode<Integer> t4 = new TreeNode<Integer>(4, t3);
		
		assertNull(t2.parent());
		
		int parentData = t3.parent().data();
		assertEquals(2, parentData);
		
		parentData = t4.parent().data();
		assertEquals(3, parentData);
		
		parentData = t4.parent().parent().data();
		assertEquals(2, parentData);
	}

	@Test
	public void testChildren() {		
		TreeNode<Integer> t1 = new TreeNode<Integer>(10);
		t1.add(20);
		t1.add(21);
		t1.add(22);
		
		int lastChildData = t1.child().data();
		assertEquals(22, lastChildData);
		
		TreeNode<Integer> t1LastChild = t1.child();
		t1LastChild.add(30);
		t1LastChild.add(31);
		t1LastChild.add(32);
		t1LastChild.add(33);
		t1LastChild.add(34);
		t1LastChild.add(35);
		
		lastChildData = t1LastChild.child().data();
		assertEquals(35, lastChildData);
		
		assertEquals(3, t1.children().size());
		assertEquals(6, t1LastChild.children().size());
	}
}
