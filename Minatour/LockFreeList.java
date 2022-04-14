import java.util.concurrent.atomic.*;

// Based off the book psuedocode
class LockFreeList {
  // Static values
  public static final int HEAD = Integer.MIN_VALUE, TAIL = Integer.MAX_VALUE, FAIL = -1;
  Node head;

  // Init head and tail to enforce order
  public LockFreeList() {
    this.head = new Node(HEAD);
    Node tail = new Node(TAIL);
    while (!head.next.compareAndSet(null, tail, false, false))
      ;
  }

  // Remove tag
  public int remove() {
    int nextTag = head.next.getReference().tag;
    if (nextTag == TAIL)
      return FAIL;

    return removeHelper(nextTag) ? nextTag : FAIL;
  }

  // Add tag
  public boolean add(int tag) {
    while (true) {
      Window window = find(head, tag);
      Node prev = window.prev, curr = window.curr;

      if (curr.tag != tag) {
        Node node = new Node(tag);
        node.next = new AtomicMarkableReference<Node>(curr, false);

        if (prev.next.compareAndSet(curr, node, false, false))
          return true;
      } else
        return false;
    }
  }

  public boolean removeHelper(int tag) {
    boolean snip;

    while (true) {
      Window window = find(head, tag);
      Node prev = window.prev, curr = window.curr;

      if (curr.tag == tag) {
        Node next = curr.next.getReference();
        snip = curr.next.attemptMark(next, true);

        if (!snip)
          continue;

        prev.next.compareAndSet(curr, next, false, false);

        return true;
      } else
        return false;
    }
  }

  public boolean contains(int tag) {
    Window window = find(head, tag);
    Node curr = window.curr;
    return curr.tag == tag;
  }

  public Window find(Node head, int tag) {
    Node prev = null, curr = null, next = null;
    boolean[] marked = { false };
    boolean snip;

    retry: while (true) {
      prev = head;
      curr = prev.next.getReference();

      while (true) {
        next = curr.next.get(marked);

        while (marked[0]) {
          snip = prev.next.compareAndSet(curr, next, false, false);

          if (!snip)
            continue retry;

          curr = prev.next.getReference();
          next = curr.next.get(marked);
        }

        if (curr.tag >= tag)
          return new Window(prev, curr);

        prev = curr;
        curr = next;
      }
    }
  }

  class Node {
    int tag;
    AtomicMarkableReference<Node> next;

    Node(int tag) {
      this.tag = tag;
      this.next = new AtomicMarkableReference<Node>(null, false);
    }
  }

  class Window {
    public Node prev;
    public Node curr;

    Window(Node prev, Node curr) {
      this.prev = prev;
      this.curr = curr;
    }
  }
}