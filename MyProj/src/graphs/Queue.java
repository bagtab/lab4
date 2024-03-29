
package graphs;

/******************************************************************************
 *  Compilation:  javac Queue.java
 *  Execution:    java Queue < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt  
 *
 *  A generic queue, implemented using a linked list.
 *
 *  % java Queue < tobe.txt 
 *  to be or not to be (2 left on queue)
 *
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;

import graphs.WeightedGraph.Edge;

/**
 *  The {@code Queue} class represents a first-in-first-out (FIFO)
 *  queue of generic items.
 *  It supports the usual <em>enqueue</em> and <em>dequeue</em>
 *  operations, along with methods for peeking at the first item,
 *  testing if the queue is empty, and iterating through
 *  the items in FIFO order.
 *  <p>
 *  This implementation uses a singly linked list with a static nested class for
 *  linked-list nodes. See {@link LinkedQueue} for the version from the
 *  textbook that uses a non-static nested class.
 *  See {@link ResizingArrayQueue} for a version that uses a resizing array.
 *  The <em>enqueue</em>, <em>dequeue</em>, <em>peek</em>, <em>size</em>, and <em>is-empty</em>
 *  operations all take constant time in the worst case.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Item> the generic type of an item in this queue
 */
public class Queue<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int n;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Node<Item> prev;
        private Item item;
        private Node<Item> next;
        @Override
        public boolean equals(Object o) {
        	if( o instanceof Node) {
        		return ((Node) o).item.equals(item);
        	}
        	return item.equals(o);
        }
    }

    /**
     * Initializes an empty queue.
     */
    public Queue() {
        first = null;
        last  = null;
        n = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return {@code true} if this queue is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }
    /**
     * returns true if queue contains Item item
     * @param item - search item looked for in queue
     * @return {@code true} if the queue contains Item item; {@code false} otherwise
     */
    public boolean contains(Item item) {
    	Node<Item> n = first;
    	while(n != null) {
    		if(n.item.equals(item)) {
    			return true;
    		}
    		n = n.next;
    	}
    	return false;
    }
    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return n;
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    /**
     * Adds the item to this queue.
     *
     * @param  item the item to add
     */
    public void enqueue(Item item) {
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
        	first = last;
        }else {
        	last.prev = oldlast;
        	oldlast.next = last;
        }
        n++;
    }
    public void addAtStart(Item item) {
        Node<Item> oldFirst = first;   
    	first = new Node<Item>();
        first.item = item;
        if (isEmpty()) {
        	last = first;
        }else {
        	first.next = oldFirst;
        	oldFirst.prev = first;
        }
        n++;
    }
    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty()) {
        	last = null;   // to avoid loitering
        }else {
            first.prev = null;
        }
        return item;
    }
    /**
     * Returns a string representation of this queue.
     *
     * @return the sequence of items in FIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    } 
    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    public Iterator<Item> iterator()  {
        return new ListIterator(first);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }

    public Item getI(int i) {
    	Node<Item> n = first;
    	for(int j = 0; j < i; j++) {
    		n = n.next;
    	}
    	return n.item;
    }
    /**
     * Unit tests the {@code Queue} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Queue<String> queue = new Queue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty())
                StdOut.print(queue.dequeue() + " ");
        }
        StdOut.println("(" + queue.size() + " left on queue)");
    }
    /**
     * returns and removes the top item of the queue in fifo order
     * @return Item item that was popped
     */
	public Item pop() {
		// TODO Auto-generated method stub
		if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item = last.item;
        last = last.prev;
        if(last!=null) {
        	last.next = null;
        }
        n--;
        if (last == null) {
        	first = null;   // to avoid loitering
        }
        return item;
    }
	/**
	 * removes Item i if it is found in queue
	 * @param i - item to be removed from queue
	 */
	public void remove(Item i) {
		if(isEmpty()) {
			return;
		}
		Node<Item> rmv = first;
		while(rmv!= null && !rmv.item.equals(i)) {
			rmv = rmv.next;
		}
		if(rmv != null) {
			if(rmv == first) {
				first = first.next;
				if(first != null) {
					first.prev = null;
				}
				n--;
				return;
			}
			if(rmv.next != null) {
				rmv.next.prev = rmv.prev;
			}
			if(rmv.prev != null) {
				rmv.prev.next = rmv.next;
			}
		}
	}
	/**
	 * adds Item edge to queue, wrapper method for enqueue
	 * @param edge - Item to be added
	 */
	public void add(Item edge) {
		enqueue(edge);
	}

	public void push(Item e) {
		// TODO Auto-generated method stub
		enqueue(e);
		
	}

	public boolean sameOrder(Queue<Queue<Item>> queue) {
		for(Queue<Item> q:queue) {
			if(sameOrderOfQueue(q)) {
				return true;
			}
		}
		return false;
	}

	private boolean sameOrderOfQueue(Queue<Item> q) {
		// TODO Auto-generated method stub
		if(q.size()-1 != size()) {
			return false;
		}
		boolean retVal = false;
		Item itemQ = q.pop();
		for(int i = 0; i < q.size(); i++) {
			boolean tmp = true;
			for(int j = 0; j < q.size(); j++) {
				if(!q.getI(j).equals(getI(j))) {
					tmp = false;
				}
			}
			if(tmp == true) {
				retVal = true;
			}
			q.enqueue(q.dequeue());
		}
		q.enqueue(itemQ);
		return retVal;
	}
}

/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
