package Render2D;

public class List<T> {
	Node head=null,at=null;
	public int size;
	public void add(T value) {
		if(at==null) {
			head=new Node(value,null);
			at=head;
		}
		else {
			at.next=new Node(value,null);
			at=at.next;
		}
		size++;
	}
	protected Node getNode(int ind) {
		if (ind>=size) return null;
		Node out=head;
		for (int i=0;i<ind;i++) {
			out=out.next;
		}
		return out;
	}
	public T getValue(int i) {
		return getNode(i).value;
	}
	public boolean has(T value) {
		for(int i=0;i<size;i++) {
			if(getValue(i).equals(value)) {
				return true;
			}
		}
		return false;
	}
	public static <T extends Integer> int[] toArray(List<Integer> list) {
		int arr[]=new int[list.size];
		for(int i=0;i<list.size;i++) {
			arr[i]=list.getValue(i);
		}
		return arr;
	}
	class Node{
		T value;
		Node next;
		Node(T value,Node next){
			this.value=value;
			this.next=next;
		}
	}
}
