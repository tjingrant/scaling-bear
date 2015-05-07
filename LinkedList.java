import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkedList {
	boolean debug = false;
	boolean destructiveChecking = false;
	boolean hybernate = false;
	Semaphore gsem;
	Node start;
	public enum Methods{
		DLSL_INSERT, DLSL_CONTAIN, DLSL_DELETE;
	}
	
	public void checkAndHybernate() {
		if (hybernate) {
			try {
				Thread.sleep(1000*100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class Node {
		private int _a;
		private Node _prev;
		private Node _next;
		AtomicInteger _ver;
		Semaphore _sem;
		public Node(int a, Node prev, Node next) {
			_a = a; _prev = prev; _next = next; _ver = new AtomicInteger(0);
			_sem = new Semaphore(1);
		}
		
		public Node getPrev() {
			return _prev;
		}
		
		public void setPrev(Node n) {
			_prev = n;
		}
		
		public Node getNext() {
			return _next;
		}
		
		public void setNext(Node n) {
			_next = n;
		}
		
		public int getInt() {
			return _a;
		}
		
		public void setInt(int a) {
			_a = a;
		}
	}

	public LinkedList() {
		this.start = new Node(Integer.MIN_VALUE, null, null);
	}
	
    public void Insert(int a) throws InterruptedException {
    	checkAndHybernate();
    	System.out.println("Inserting");
		Node now = this.start;
    	Node next = this.start.getNext();
    	Node newNode = new Node(a, null, null);
    	while(true) {
	    	try {
		    	while (next != null && !( (now.getInt() <= a) && (next.getInt() >= a) )) {
		    		now = next;
		        	next = next.getNext();
		    	}
		    	if (next == null) {
		    		now._sem.acquire();
		    		now.setNext(newNode);
					newNode.setPrev(now);
					now._sem.release();
					return;
				}
		    	if ( (now.getInt() <= a) && (next.getInt() >= a) ) {
		    		now._sem.acquire();
		    		next._sem.acquire();
		    		// at this point, now and next are all claimed
		    		now.setNext(newNode);
		    		newNode.setPrev(now);
		    		next.setPrev(newNode);
		    		newNode.setNext(next);
		    		now._sem.release();
		    		next._sem.release();
		    		return;
		      	}
		    	break;
	    	} catch (NullPointerException e) {
	    		System.out.println("Insert retries");
	    	}
    	}
    }
	/** marks two consecutive atomic integers as dirty, if either of them are already dirty
	 *  fail right away.
	 * @throws InterruptedException 
	 */
    // have to make use of hashcode
	public boolean Contains(int val) throws InterruptedException {   //valid only for positive tests, negative test may compromise concurrency significantly
		System.out.println("Contains");
		Node next = this.start;
    	while(next != null) {
    		while(true) {
    			try{
		    		if ((val == next.getInt())) {
		    			return true;
		    		}
		    		next = next.getNext();
		    		break;
    			} catch (NullPointerException e) {
    				//retries
    				System.out.println("Contain retries");
    			}
    		}
    	}
    	return false;
	}

	public void Delete(int val) throws InterruptedException {
		while(true) {
			try { //You could potentially lose next during this process;
					System.out.println("Deleting");
					checkAndHybernate();
					Node next = this.start;
		
					while(next != null) {
						Node n = next.getNext();
						Node p = next.getPrev();
						if (n == null) {
							p._sem.acquire();
							p.setNext(null);
							p._sem.release();
							return;
						} 
						if (val == next.getInt()) {
							p._sem.acquire();
							next._sem.acquire();
							n._sem.acquire();
							if (n.getPrev()==null || p.getNext()==null) {
								p._sem.release();
								next._sem.release();
								n._sem.release();
								System.out.println("Something wrong...");
								this.printOut();
								throw new NullPointerException();
							}
			    		// at this point, now and next are all claimed
							p.setNext(n);
							n.setPrev(p);
							next.setPrev(null);
							next.setNext(null);
							n._sem.release();
							next._sem.release();
							p._sem.release();
							return;
						}
						next = next.getNext();
					}
					break;
			}
			catch (NullPointerException e) {
			//retries
			System.out.println("Delete retries");
		}
		}
	   return;
}
//	
    public void printOut() {
    	Node next = this.start.getNext();
    	while(next != null) {
    		System.out.print(next.getInt() + " ");
    		next = next.getNext();
    	}
    }
    
	public boolean invariantCheck() {
		Node now = this.start;
    	Node next = this.start.getNext();
    	boolean inv = true;
    	
    	while (next != null) {
    	  inv = inv && (now._a <= next._a);	
    	  now = next;
    	  next = next.getNext();
    	}
		return true;
	}
	
}

/** each node is associated with an atomicInteger which records the latest "clean state"
 *  A txnal acquire records that atomicInteger's val
 *  A txnal release checks if that val has changed and if it has, then critical section should be re-performed.
 *  Even number state - complete clean state
 *  Odd number state - unstable state
 **/


