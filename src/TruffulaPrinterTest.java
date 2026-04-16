import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    /**
     * Checks if the current operating system is Windows.
     *
     * This method reads the "os.name" system property and checks whether it
     * contains the substring "win", which indicates a Windows-based OS.
     * 
     * You do not need to modify this method.
     *
     * @return true if the OS is Windows, false otherwise
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    /**
     * Creates a hidden file in the specified parent folder.
     * 
     * The filename MUST start with a dot (.).
     *
     * On Unix-like systems, files prefixed with a dot (.) are treated as hidden.
     * On Windows, this method also sets the DOS "hidden" file attribute.
     * 
     * You do not need to modify this method, but you SHOULD use it when creating hidden files
     * for your tests. This will make sure that your tests work on both Windows and UNIX-like systems.
     *
     * @param parentFolder the directory in which to create the hidden file
     * @param filename the name of the hidden file; must start with a dot (.)
     * @return a File object representing the created hidden file
     * @throws IOException if an I/O error occurs during file creation or attribute setting
     * @throws IllegalArgumentException if the filename does not start with a dot (.)
     */
    private static File createHiddenFile(File parentFolder, String filename) throws IOException {
        if(!filename.startsWith(".")) {
            throw new IllegalArgumentException("Hidden files/folders must start with a '.'");
        }
        File hidden = new File(parentFolder, filename);
        hidden.createNewFile();
        if(isWindows()) {
            Path path = Paths.get(hidden.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        return hidden;
    }

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        createHiddenFile(myFolder, ".hidden.txt");

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        ConsoleColor reset = ConsoleColor.RESET;
        ConsoleColor white = ConsoleColor.WHITE;
        ConsoleColor purple = ConsoleColor.PURPLE;
        ConsoleColor yellow = ConsoleColor.YELLOW;

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_SimpleStructure_NoColorNoOrder(@TempDir File tempDir) throws IOException {
        // Build a flat directory structure:
        // simpleFolder/
        //    file1.txt
        //    file2.txt

        File simpleFolder = new File(tempDir, "simpleFolder");
        assertTrue(simpleFolder.mkdir());

        new File(simpleFolder, "file1.txt").createNewFile();
        new File(simpleFolder, "file2.txt").createNewFile();

        TruffulaOptions options = new TruffulaOptions(simpleFolder, false, false);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bytes);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        String output = bytes.toString();

        // Check structure is present without enforcing order or color
        assertTrue(output.contains("simpleFolder/"), "Should contain root folder name");
        assertTrue(output.contains("file1.txt"), "Should contain file1.txt");
        assertTrue(output.contains("file2.txt"), "Should contain file2.txt");
        assertTrue(output.contains("   file"), "Should have 3-space indentation");
    }

    @Test
    public void testPrintTree_HiddenFilesNotShown_WhenShowHiddenFalse(@TempDir File tempDir) throws IOException {
        // Build structure:
        // testFolder/
        //    visible.txt
        //    .hidden.txt      ← should NOT appear
        //    .hiddenSub/      ← should NOT appear (nor its contents)
        //       secret.txt

        File testFolder = new File(tempDir, "testFolder");
        assertTrue(testFolder.mkdir());

        new File(testFolder, "visible.txt").createNewFile();
        createHiddenFile(testFolder, ".hidden.txt");

        File hiddenSub = new File(testFolder, ".hiddenSub");
        hiddenSub.mkdir();
        if (isWindows()) {
            Path path = Paths.get(hiddenSub.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        new File(hiddenSub, "secret.txt").createNewFile();

        TruffulaOptions options = new TruffulaOptions(testFolder, false, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TruffulaPrinter printer = new TruffulaPrinter(options, new PrintStream(baos));
        printer.printTree();

        String output = baos.toString();

        assertTrue(output.contains("testFolder/"),  "Should show root folder");
        assertTrue(output.contains("visible.txt"),   "Should show visible file");
        assertFalse(output.contains(".hidden.txt"),  "Should NOT show hidden file");
        assertFalse(output.contains(".hiddenSub"),   "Should NOT show hidden directory");
        assertFalse(output.contains("secret.txt"),   "Should NOT show contents of hidden directory");
    }

    @Test
        public void testPrintTree_ColorCyclesByDepth(@TempDir File tempDir) throws IOException {
        // Build structure:
        // rootDir/
        //    file.txt       ← depth 1: PURPLE
        //    subDir/        ← depth 1: PURPLE
        //       nested.txt  ← depth 2: YELLOW

        File rootDir = new File(tempDir, "rootDir");
        assertTrue(rootDir.mkdir());
        new File(rootDir, "file.txt").createNewFile();
        File subDir = new File(rootDir, "subDir");
        assertTrue(subDir.mkdir());
        new File(subDir, "nested.txt").createNewFile();

        TruffulaOptions options = new TruffulaOptions(rootDir, false, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TruffulaPrinter printer = new TruffulaPrinter(options, new PrintStream(baos));
        printer.printTree();

        String output = baos.toString();
        String nl = System.lineSeparator();
        String reset = ConsoleColor.RESET.toString();

        // Root is depth 0: WHITE
        assertTrue(output.contains(ConsoleColor.WHITE + "rootDir/" + nl + reset),
            "Root should be WHITE");

        // depth 1 items (file.txt and subDir/) should be PURPLE
        assertTrue(output.contains(ConsoleColor.PURPLE + "   file.txt" + nl + reset),
            "Depth 1 file should be PURPLE");
        assertTrue(output.contains(ConsoleColor.PURPLE + "   subDir/" + nl + reset),
            "Depth 1 directory should be PURPLE");

        // depth 2 items (nested.txt) should be YELLOW
        assertTrue(output.contains(ConsoleColor.YELLOW + "      nested.txt" + nl + reset),
            "Depth 2 file should be YELLOW");
    }
}
