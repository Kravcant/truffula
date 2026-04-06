## Pre-note:

The provided tests are VERY MINIMAL. You are expected to be adding thorough tests at every wave. You may even consider making the tests before you start implementation. You will be docked significant points if you do not test throughly.

You must NOT use the `java.nio` package for any wave! You can only use `java.io`.

## Wave 0: Understand
In this wave you will be working to undertsand the files that are provided for you. Open `notes.md` to see a list of the files in the order I recommend you look at them. For each file, write notes in `notes.md` summarizing your understanding and putting down any questions you want to better understand later. This will take a long time, but it is worth it! Do not skimp on this part, you will regret it! A very large part of this project is understanding the OOP structure.

Make frequent commits to your `notes.md` as you learn.

## Wave 1: Color Printer
In this wave you will implement a class that can print text to the console in color. To achieve this you implement `print(String message, boolean reset)` in `ColorPrinter.java`. You do not need to alter the other methods.
Hints:
- You will make use of `ConsoleColor.java`. It is an enum, which we haven't covered in class yet. Look at it with curiosity and do some minor research on what enums are in Java. You do not need to modify this file.
- Look at `ColorPrinterTest.java` for an example of how to use `ConsoleColor`
- Consider adding a main method to `ColorPrinter.java` so you can experiment with how it works and what it looks like on the command line.

YOU MUST ALSO IMPLEMENT THOROUGH UNIT TESTS!!!

## Wave 2: TruffulaOptions
In this wave you will be implementing a command line argument parser! You will do so by implementing `TruffulaOptions(String[] args)`. You do not need to modify the other methods. There is a detailed comment at the top of the file explaining how the args should be parsed. READ IT CAREFULLY!

This will later be responsible for actually parsing the commands the user will run from the command line. For example if the user types:

```
truffula -nc -h /path/to/example/dir
```

Then your TruffulaOptions will recieve a String array that looks like:
```
["-nc", "-h", "/path/to/example/dir"]
```

Hints:
- Note that the order of -nc and -h is unimportant and that either or both may be present or not present.
- However, the path will always be the final argument in the array
- It's a really good idea to add tests before the implementation!
- You will need to use the `java.io.File` class. First, create a new `File` with the path. Then, the `.exists()` and `.isDirectory()` methods will be your friend.
- Do a little bit of research into how `java.io.File` works, but you definitely don't need to become an expert on it

You must NOT use the `java.nio` package for any wave! You can only use `java.io`.

YOU MUST ALSO IMPLEMENT THOROUGH UNIT TESTS!!!

## Wave 3: App
In this wave you will set up your main method in `App.java`. This is where your program will actually start when called from the command line! When you have it implemented, you will be able to do the following command from the command line (make sure you are in the root of the project repository):

```
java src/App.java -nc -h src
```

If it is working properly, it should print:
```
printTree was called!
```
And display the options beneath it.

You will need to do a number of things to make it work.
1. Create a new TruffulaOptions by passing it `args`
2. Create a new TruffulaPrinter usting the TruffulaOptions.
3. Call `printTree` on the TruffulaPrinter

You do not need to handle invalid command line arguments at this point (that is an extra credit wave).

## Wave 4: Initial TruffulaPrinter
In this wave you will make it so the TruffulaPrint can print a directory tree with `printTree()`. For this wave do not worry about colors or alphabetization. The given test will NOT yet pass because of the lack of color or alphabetical order, I STRONGLY recommend making a simpler test that does not require these to be implemented first. Look at the exisiting test to understand how to make a test.

Look closely at the Javdoc to understand how the output should be structured.

Hints:
- You will find the following java.io.File methods helpful: `.getName()` `.isDirectory()` and `listFiles()`
- Add a recursive helper method
- Make a simpler test that does not test order or color
- Consider testing it through your main.
- DO NOT USE SYSTEM.OUT.PRINTLN
- USE out.println instead (will use your ColorPrinter)

YOU MUST ALSO ADD UNIT TESTS!!! Look at the existing test to understand how it uses a temp directory for testing.

## Wave 5: Hidden files
Update your `printTree()` implementation so it respects the option of whether or not to show hidden files/folders. Use `isHidden()` to check this!

YOU MUST ALSO ADD UNIT TESTS!!! Make a test that tests color but does not test colors or alphabetical order. When making unit tests to handle hidden files, make sure to use the `createHiddenFile` method in `TruffulaPrinterTest` 

## Wave 6: Colors!
Update your `printTree()` implementation so it cycles through colors according to the default color scheme. The root folder should be white, its direct children should be purple, their children should be yellow, their children should be white again, and so on.

YOU MUST ALSO ADD UNIT TESTS!!! Make a test that tests color but does not test alphabetical order.

## Wave 7: Alphabetization
Update your `printTree()` so that the files/folders are sorted alphabetically. Use the provided `AlphabeticalFileSorter.java` for help with this. Note with curiosity how it's working with lambdas, but you do not need to edit or fully understand `AlphabeticalFileSorter.java`

It should now finally pass the provided test! Congrats!

## Wave 8: Creating a command line utility
In this wave we will make it so that you can call truffula from the command line. We will do it in two steps, first creating an executable JAR file, then adding it to our PATH. There are a lot of steps to this wave, but don't worry, it's all just copying commands. You will not need to commit anything from this wave.

### Creating an executable JAR
JAR is short for "Java Archive". It's a collection of our compiled code that we can share with others. You may have noticed in the lib folder that there is a JAR for JUnit. This is how we've been able to use all of the JUnit functionality.

Make sure you are in the root of the repository, then compile your code:

```
javac -d target src/App.java src/ColorPrinter.java src/ConsoleColor.java src/TruffulaOptions.java src/TruffulaPrinter.java src/AlphabeticalFileSorter.java 
```

This places the compiled files in a new directory called `target`. You should see `target` directory be created with a bunch of `.class` files. `.class` files are compiled Java files. The `-d target` is responsible for specifying the name of the directory where you want the compiled files.

Next, create the jar.

```
jar cfe target/truffula.jar App -C target .
```

This should create a new file `truffula.jar` in the `target` directory.

Here's how it works:
- `jar`: The command to create JARs. This was installed when you installed the Java JDK
- `c`: Create a new jar
- `f` and `target/truffula.jar`: Specify that the name of the file should be `truffula.jar` and that it should be in the `target` directory.
- `e` and `App`: Specify that the `App` class has the `main` method to be executed.
- `-C target .`: Specify that the `.class` files are in the `target` directory.

### Validating the JAR

Try running your jar! Use the following command:

```
java -jar target/truffula.jar -h .
```

You should see it print a lot! It'll be both the normal files and the hidden `.git` directory. Try experimenting with the -nc and -h flags. Try running it on different directories too!

### Placing the JAR on the path (Mac)
These steps are for Mac computers. Please skip to the Windows section if on Windows.

1. Copy your jar to the `bin` directory:
   ```
   sudo cp target/truffula.jar /usr/local/bin/truffula.jar
   ```
   You may be asked for your password. This is the password to your computer. Type it in if prompted and then hit enter. You will not see the letters showing up but that's OK. It's to prevent people from seeing your password over your shoulder.
1. Create a new shell script using `vim`. Run the below command to open `vim` and create a new file named `truffula` in your `bin` directory:
   ```
   sudo vim /usr/local/bin/truffula
   ```
   You may or may not be asked again for your password.
1. Press `i` to enter insert mode in vim. You should see the word `INSERT` show up at the bottom of the terminal.
1. Copy the following contents to the new `truffula` file
   ```
   #!/bin/bash
   java -jar "/usr/local/bin/truffula.jar" "$@"
   ```
   The first line tells the computer that the file should be executed using the Bash shell. The second line describes a command to run the `truffula.jar`. The `@` says to pass along any command line flags. We do this so you can just type `truffula` instead of the whole `java -jar` command.
1. Exit insert mode by pressing the Escape (esc) key.
1. Exit vim and save the file by typing `:wq` then  hitting enter. `w` is for "write" (save the file). `q` is for quit.
1. Make the `truffula` file executable:
   ```
   sudo chmod +x /usr/local/bin/truffula
   ```
   You may or may not be asked for your password. `chmod` stands for "change mode" and it allows you to change the permissions of a file or directory. The `+x` adds the permission to run the file as an executable.
1. Open a new terminal window in your home directory `~`. (This is where it opens by default.) Try running the `truffula` command!
   ```
   truffula ~
   ```
   This should show the entire contents of your home directory. It will be huge! You may get a pop-up asking for permission to open other files. Say yes. Congrats! You've made a real command line utility! Feel free to use it your day-to-day work. Congratulate yourself and take a well-deserved break.

### Placing the JAR on the path (Windows)
1. Open a new Git Bash terminal. THIS WILL NOT WORK IN POWERSHELL. Navigate to the root of truffula project directory using `cd`.
1. Make a `bin` directory inside your home directory. This is where we will place the JAR.
   ```
   mkdir ~/bin
   ```
1. Copy the truffula jar to the `bin`
   ```
   cp target/truffula.jar ~/bin
   ```
1. Create a new script `truffula` in the bin directory using vim. The below command will open the vim editor.
   ```
   vim ~/bin/truffula
   ```
1. Press `i` to enter insert mode in vim. You should see the word `INSERT` show up at the bottom of the terminal.
1. Copy the following contents to the new `truffula` file (use right click and paste, ctrl-c will likely not work).
   ```
   #!/bin/bash
   java -jar "$HOME/bin/truffula.jar" "$@"
   ```
   The first line tells the computer that the file should be executed using the Bash shell. The second line describes a command to run the `truffula.jar`. The `@` says to pass along any command line flags. We do this so you can just type `truffula` instead of the whole `java -jar` command.
1. Exit insert mode by pressing the Escape (esc) key.
1. Exit vim and save the file by typing `:wq` then  hitting enter. `w` is for "write" (save the file). `q` is for quit.
1. Make the `truffula` file executable:
   ```
   chmod +x ~/bin/truffula
   ```
   `chmod` stands for "change mode" and it allows you to change the permissions of a file or directory. The `+x` adds the permission to run the file as an executable.
1. Open your `.bashrc` file using vim. This is a configuration file for bash. It may or may not exist, and may or may not have contents in it already.
   ```
   vim ~/.bashrc
   ```
1. Press `i` to enter insert mode. If there are already contents in the file, use the arrow keys to move to the bottom of the file and hit enter to add a new line. We will be adding new lines at the end. DO NOT MODIFY THE EXISITING LINES IN THE FILE. If it's currently empty, that's OK too!
1. Add the following line at the bottom of the file:
   ```
   export PATH="$HOME/bin:$PATH"
   ```
   This adds your `bin` directory to the path. This means that when you execute a command on the command line, it checks whether there's a program in the `bin` directory that matches the command you're trying to run. In this case, it will allow us to run your truffula command!
1. Exit insert mode by pressing the Escape (esc) key.
1. Exit vim and save the file by typing `:wq` then  hitting enter. `w` is for "write" (save the file). `q` is for quit.
1. EXIT AND REOPEN GIT BASH AND ANY OTHER OPEN TERMINALS. You need to restart your terminal when you want new changes from your `.bashrc` configuration files to be picked up.
1. Open a new terminal window in your home directory `~`. (This is where it opens by default.) Try running the `truffula` command!
   ```
   truffula ~
   ```
   This should show the entire contents of your home directory. It will be huge! You may get a pop-up asking for permission to open other files. Say yes. Congrats! You've made a real command line utility! Feel free to use it your day-to-day work. Congratulate yourself and take a well-deserved break.
