/*  Author : Abhisek Mohanty
**  UFID : 34931899
**  Comments will always be above the code.
*/
public class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1;

    //The RedBlackNode class with its properties

    class RedBlackNode {

        Event element = new Event(-1, 1);
        int color = BLACK;
        RedBlackNode left = nil, right = nil, parent = nil;

        RedBlackNode(Event element) {
            this.element = element;
        }
    }

    //default nil node for the RB Tree
    private final RedBlackNode nil = new RedBlackNode(new Event(-1, 1));

    //initialize root as nil initially and continue
    private RedBlackNode root = nil;

    /* We always have the rightmostnode in the variable below, so that we
       spend only O(1) time instead of log(n) time finding the position to insert the node.
       This works here as the input is in ascending order. Hence we acheive
       RedBlackTree initialization in O(n) time instead of O(nlog n) time.
    */
    private RedBlackNode rightMostNode = nil;

    //This is used in the delete method to check if the node exists
    //Check for find in tree with root = node
    
    private RedBlackNode doesNodeExist(RedBlackNode find, RedBlackNode node) {
        if (root == nil) {
            return null;
        }

        if (find.element.Id < node.element.Id) {
            if (node.left != nil) {
                return doesNodeExist(find, node.left);
            }
        } else if (find.element.Id > node.element.Id) {
            if (node.right != nil) {
                return doesNodeExist(find, node.right);
            }
        } else if (find.element.Id == node.element.Id) {
            return node;
        }
        return null;
    }

    //Insert node into the RBT
    public void insert(Event item) {
        RedBlackNode node = new RedBlackNode(item);

        RedBlackNode temp = root;

        //Insert at root if tree is empty
        if (root == nil) {
            root = node;
            rightMostNode = root;
            node.color = BLACK;
            node.parent = nil;
        } else {
            /* Otherwise use the rightMostNode value to directly get
            to the insert position in O(1) time */

            node.color = RED;
            temp = rightMostNode;
            temp.right = node;
            node.parent = temp;
            rightMostNode = node;

            // Call RB_Insert_Fixup to fix the Red Black Tree Properties

            RB_Insert_Fixup(node);
        }
    }

    /* This method does the post insert fixes
    *  to maintain the properties of the Red Black tree
    *  We pass the newly inserted node as argument
    *  Source : Cormen
    *  */
    private void RB_Insert_Fixup(RedBlackNode node) {
        /* We insert new node with Color = Red
        *  RBT property violated if the parent is RED */

        while (node.parent.color == RED) {
            RedBlackNode y = nil;

            /* Two cases. When :
            *  1. Parent is left child of it parent - Rotate Right
            *  2. Parent is right child of its parent - Rotate Left
            *  */

            if (node.parent == node.parent.parent.left) {
                y = node.parent.parent.right;

                if (y != nil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    node = node.parent;
                    leftRotate(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                rightRotate(node.parent.parent);
            } else {
                y = node.parent.parent.left;
                if (y != nil && y.color == RED) {
                    node.parent.color = BLACK;
                    y.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    node = node.parent;
                    rightRotate(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                leftRotate(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    void leftRotate(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {
            // The root node is rotated to the left
            RedBlackNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rightRotate(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {
            //the root node is rotated to the right
            RedBlackNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    /* The Transplant method for tree deleteNode method. Used to
       replace one subtree as a child of its parent with another subtree. */

    void RB_transplant(RedBlackNode u, RedBlackNode v){
        if(u.parent == nil){
            root = v;
        }else if(u == u.parent.left){
            u.parent.left = v;
        }else
            u.parent.right = v;
        v.parent = u.parent;
    }

    // The primary method to delete a node. Called only from the reduce method
    boolean deleteNode(RedBlackNode z){
        if((z = doesNodeExist(z, root))==null)
            return false;
        RedBlackNode x;
        RedBlackNode y = z;
        int y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            RB_transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            RB_transplant(z, z.left);
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                RB_transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            RB_transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==BLACK)
            RB_Delete_Fixup(x);
        return true;
    }

    // deleteNode helper method to fix colors and RB Tree properties post delete

    void RB_Delete_Fixup(RedBlackNode x){
        while(x!=root && x.color == BLACK){
            if(x == x.parent.left){
                RedBlackNode w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }else{
                RedBlackNode w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    //Find the minimum element of the tree rooted at subTreeRoot

    RedBlackNode treeMinimum(RedBlackNode subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    /* Method to search for an element in the Red Black Tree
    * This method checks if there is a node in the RBT with the given Id = theID */

    public RedBlackNode findNodebyId(int theID)
    {
        return findNodebyId(root, theID);
    }
    private RedBlackNode findNodebyId(RedBlackNode root, int theID)
    {
        while (root != nil)
        {
            int rootvalue = root.element.Id;
            if (theID < rootvalue)
                root = root.left;
            else if (theID > rootvalue)
                root = root.right;
            else
            {
                return root;
            }
        }
        return null;
    }

    //#######  1. Increase(theID, m)  ########
    // O(log n) time

    public void Increase(int theID, int m) {
        //Check if node exists
        RedBlackNode node = findNodebyId(theID);

        //If node does not exist, insert node. else increase nodeCount
        if(node == null)
            insert(new Event(theID, m));
        else
            node.element.Count = node.element.Count + m;

        //Call the method to print the Count
        printCountforID(node, theID);
    }

    //#######  2. Reduce(theID, m)  ########
    // O(log n) time

    public void Reduce(int theID, int m){
        //Check if node exists
        RedBlackNode node = findNodebyId(theID);

        //If node does not exist, print 0
        if(node == null){
            System.out.println("0");
            return;
        }

        //If the Count falls below 1, deleteNode node and print 0
        else if (node.element.Count - m <= 0) {
            deleteNode(node);
            System.out.println("0");
        }

        //Reduce the Count of the node and call print method
        else {
            node.element.Count = node.element.Count - m;
            printCountforID(node, theID);
        }
    }

    //#######  3. Count(theID)  ########
    // O(log n) time

    public void Count(int theID){
        //Check if node exists
        RedBlackNode node = findNodebyId(theID);

        if(node != null)
            printCountforID(node, theID);
        else
            System.out.println("0");

    }

    //#######  4. InRange(ID1, ID2)  ########
    // This returns the total count in O(log n + s) time

    public int Inrange(int id1, int id2){
        return Inrange(root, id1, id2);
    }

    private int Inrange(RedBlackNode root, int id1, int id2){
        // Base cases
        if (root == nil) return 0;

        if (root.element.Id == id1 && root.element.Id == id2)
            return root.element.Count;

        /* If node is in given range, recursively hit the child nodes */

        if (root.element.Id >= id1 && root.element.Id <= id2)
            return root.element.Count + Inrange(root.left, id1, id2) +
                    Inrange(root.right, id1, id2);

        // If node is smaller than id1, we need to only recursively check the right nodes
        else if (root.element.Id < id1)
            return Inrange(root.right, id1, id2);

        /* Last Case : If node is greater than id2, we need to only
        recursively check the left nodes */
        else return Inrange(root.left, id1, id2);
    }

    //#######  5. Next(theID)  ########
    // O(log n) time

    public void Next(int theID){
        RedBlackNode temp = root;
        RedBlackNode answer = null;

        /* Initialize temp to root. If temp > theID, we have got a greater value
        ** but there might be other values less than temp but greater than theID.
        ** So, check in the left subtree. If we get a smaller element in temp, check
        ** the right subtree. Break when we reach end of the tree
        */
        while(true) {
            if (temp.element.Id > theID) {
                answer = temp;
                if(temp.left != nil)
                    temp = temp.left;
                else
                    break;
            } else if (temp.element.Id <= theID) {
                if (temp.right != nil)
                    temp = temp.right;
                else
                    break;
            }
        }

        // answer == null => could not find any such node.
        if (answer == null)
            System.out.println("0 0");
        else
            System.out.println(answer.element.Id + " " + answer.element.Count);
    }

    //#######  6. Previous(theID)  ########
    // O(log n) time

    public void Previous(int theID){
        RedBlackNode temp = root;
        RedBlackNode answer = null;


        /* Initialize temp to root. If temp < theID (condition #2 below), we have got a smaller value
        ** but there might be other values greater than temp but less than theID.
        ** So, check in the right subtree. If we get a greater element in temp, check
        ** the left subtree.(condition # 1 below) Break when we reach end of the tree .
        */
        while(true) {
            if (temp.element.Id >= theID) {
                if(temp.left != nil)
                    temp = temp.left;
                else
                    break;
            } else if (temp.element.Id < theID) {
                answer = temp;
                if (temp.right != nil)
                    temp = temp.right;
                else
                    break;
            }
        }

        // answer == null => could not find any such node.

        if (answer == null)
            System.out.println("0 0");
        else
            System.out.println(answer.element.Id + " " + answer.element.Count);
    }

    /* Helper method to print the Count for a specified Id.
    * Here node and Id will be pointing to the same node */

    public void printCountforID(RedBlackNode node, int Id){
        if(node == null)
            Count(Id);
        else
            System.out.println(node.element.Count);
    }
}
