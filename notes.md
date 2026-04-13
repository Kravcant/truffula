# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java
- Creates a directory tree
- Can show hidden files, use different colored texts, and choose the root where to start the printing

## ConsoleColor.java
- Determines the text colors that can be used on the console
- Colors are identified as enums that can be manually set for the printStream

## ColorPrinter.java / ColorPrinterTest.java
- Used to change the color of certain text on the console
- Creates a printStream that you can set the color for, and the selected color will apply to any messages you
print from it
- Print statements can be toggled to automatically reset the color after a statement is printed

## TruffulaOptions.java / TruffulaOptionsTest.java
- Provides different options for displaying directory trees
- Can change the path which to print the tree, or set flags to toggle certain things
- Flags can either show hidden files, or disable color texts
- TruffulaOptions objects can eithwr be created with an array of arguments, or by explicitly listing the values

## TruffulaPrinter.java / TruffulaPrinterTest.java
- Prints the tree directory with optional colors
- Can also sort files and directories

## AlphabeticalFileSorter.java
- Sorts an array of files in alphabetical order, ignoring cases