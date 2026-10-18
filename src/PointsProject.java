import java.io.File;
import java.util.Scanner;
import java.io.IOException;

/**
 * Main for CS3114 Quadtree/SkipList Point project (CS3114 Spring 2026 Project
 * 2). Usage: java PointsProject <command-file>
 *
 * @author CS Staff
 * @version September 2026
 */
public class PointsProject {
    /**
     * Main: Process input parameters and invoke command file processor
     *
     * @param args
     *            The command line parameters
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: PointsProject <command-file>");
            return;
        }

        String commandFile = args[0].trim();
        File theFile = new File(commandFile);
        if (!theFile.exists()) {
            System.out.println("There is no such input file as |" + commandFile
                + "|");
            return;
        }
        
        Database database = new Database();

        try (Scanner scanner = new Scanner(theFile)) 
        {
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) 
                {
                    continue;
                }

                String[] parts = line.split("\\s+");

                switch (parts[0]) 
                {
                    case "duplicates":
                        database.duplicates();
                        break;

                    case "regionsearch":
                        database.regionSearch(
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4]));
                        break;

                    case "dump":
                        database.dump();
                        break;
                }
            }
        }
    }
}

