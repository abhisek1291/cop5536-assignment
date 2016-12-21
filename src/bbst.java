import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class bbst {
    public static void main(String[] args) {
	      File file= null;
        Scanner input = new Scanner(System.in);

        //argument validity check, since this program expects the user to pass the file.
        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(0);
        }

        RedBlackTree rbt = new RedBlackTree();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            int line = 1;

            br = new BufferedReader(new FileReader(file));

            while ((sCurrentLine = br.readLine()) != null) {
                // Line #1 has the total count and should not be inserted into the RBT.
                if(line == 1) {
                    line++;
                    continue;
                }

                //split the lines using the space delimiter. the 1st split is the Id and 2nd is the Count
                String[] splitString = sCurrentLine.split("\\s+");

                //sanity check
                if (splitString.length > 2){
                    System.err.println("Invalid arguments count:" + splitString.length);
                    System.exit(0);
                }

                //variable of type Event.
                Event data = new Event(Integer.parseInt(splitString[0]), Integer.parseInt(splitString[1]));

                //Insert into the Red Black Tree
                rbt.insert(data);
            }

            String query;
            String[] tokenizedInput;

            // Run the console input till it encounters a quit command
            do
            {
                query = input.nextLine();

                /* split the commands into parts, space delimiter.
                [0] -> command, used in the switch statement
                [1] & [2] -> arguments { theID, m, id1 and id2 in case of inrange}
                */
                tokenizedInput = query.split("\\s+");

                switch (tokenizedInput[0]){
                    case "increase":
                        rbt.Increase(Integer.parseInt(tokenizedInput[1]), Integer.parseInt(tokenizedInput[2]));
                        break;
                    case "reduce":
                        rbt.Reduce(Integer.parseInt(tokenizedInput[1]), Integer.parseInt(tokenizedInput[2]));
                        break;
                    case "count":
                        rbt.Count(Integer.parseInt(tokenizedInput[1]));
                        break;
                    case "inrange":
                        int totalCount = rbt.Inrange(Integer.parseInt(tokenizedInput[1]), Integer.parseInt(tokenizedInput[2]));
                        System.out.println(totalCount);
                        break;
                    case "next":
                        rbt.Next(Integer.parseInt(tokenizedInput[1]));
                        break;
                    case "previous":
                        rbt.Previous(Integer.parseInt(tokenizedInput[1]));
                        break;
                    case "quit":
                        break;
                    default:
                        System.out.println("Unsupported Operation");
                }
            } while (!tokenizedInput[0].equalsIgnoreCase("quit"));
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
