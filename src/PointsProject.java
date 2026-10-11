import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main for CS3114 Quadtree/SkipList Point project (CS3114 Spring 2026 Project
 * 2). Usage: java PointsProject <command-file>
 *
 * @author CS Staff
 * @version September 2026
 */
public class PointsProject
{
    /**
     * Main: Process input parameters and invoke command file processor
     *
     * @param args
     *            The command line parameters
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException
    {
        if (args.length != 1)
        {
            System.out.println("Usage: PointsProject <command-file>");
            return;
        }

        String commandFile = args[0].trim();
        File theFile = new File(commandFile);
        if (!theFile.exists())
        {
            System.out.println(
                "There is no such input file as |" + commandFile + "|");
            return;
        }

        Database database = new Database();

        try (Scanner scanner = new Scanner(theFile))
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();

                if (!line.isEmpty())
                {
                    processCommand(line, database);
                }
            }
        }
    }


    /**
     * Processes a command from the input file.
     *
     * @param line
     *            input command
     * @param database
     *            database to update
     */
    private static void processCommand(String line, Database database)
    {
        String[] parts = line.split("\\s+");

        switch (parts[0])
        {
            case "insert":
                database.insert(
                    parts[1],
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]));
                break;

            case "search":
                database.search(parts[1]);
                break;

            case "dump":
                database.dump();
                break;

            case "duplicates":
                database.duplicates();
                break;

            default:
                break;
        }
    }
}
