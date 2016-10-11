package ca.uwaterloo.ece358.thunder.data;

public class LinkBuffer {
    public int length;
    public LinkNode head;
    public LinkNode tail;
    public int max;
    public int last;

    public LinkBuffer() {
        length = 0;
        last = 0;
        max = -1;
    }

    public LinkBuffer(int size) {
        length = 0;
        this.max = size;
        last = 0;
    }

    public boolean Append(int data) {
        if (length + 1 <= max || max == -1) {
            LinkNode newnode = new LinkNode(data);

            if (length == 0) {
                head = newnode;
                tail = newnode;
                head.time = data;
                last = data;
            } else {
                tail.next = newnode;
                tail = newnode;
                last = tail.info;
            }

            length++;
            return true;
        } else
            last = data;
        return false;
    }

    public int Last() {
        return last;
    }

    public int check(int x, int service) {
        if (length != 0) {
            if (head.time + service == x) {

                LinkNode temp;
                if (length == 1) {
                    temp = head;
                    head = null;
                } else {
                    temp = head;
                    head = head.next;
                    head.time = x + 1;
                }
                length--;
                return x - temp.info;
            }
        }
        return 0;
    }

}

