Code Crush
=============

This is intended as an instruction module to teach intro students about
loops, arrays, objects, and the like.
This is based off David Saltares's [code](https://github.com/siondream/freegemas-gdx),
which is based off José Tomás Tocino García's [code](http://code.google.com/p/freegemas/),
which is based off the classic game of Bejeweled.

###Setup/Installation###

follow the instructions on how to [setup](https://developer.android.com/sdk/index.html?hl=i)
the development environment. Then download the github project as a zip file and import in Eclipse.


###Assignments###

Just delete sections of the code and ask students to fill in the blanks.
Most likely, you will be interested in asking students to rewrite part of
Board.java or possibly other files in the cpsc112 folder (the logic)


#Example Assignment

Total points: 20

You may be familiar with Candy Crush or Bewjewled. The goal is to swap items on a board to create matches. A "match" refers to the same board item appearing three or more times in a row in the same column or the same row. When such a match is created, those items disappear and you are awarded points.

For this assignment we have given you a .zip file called Assignment4.zip which can be imported into your workspace. File -> Import then import "General -> Existing Projects into Workspace."

CPSC112_Assignment4.zip is a complete project that can be run as an Android or Desktop app. To run the desktop version, you open the frontend-desktop folder in the sidebar (package explorer) in eclipse then click on src and press run. Likewise with Android. Feel free to test your code using either method. The first time your run, you will want to "Run As" a "Java application" for the desktop version and a "Android application" for Android. The game isn't fully functional yet, and won't be until you complete this assignment.

The whole project has a bit of a complicated structure - there are three different folders. The one titled "cs112game" contains the file you will be editing and is where all the logic is handled, while the other two "frontend-_____" are for the front end and you won't need to touch. Specifically, you need to only need to edit cpsc112/src/cpsc112.game/BoardHelper.java

To be clear, the only file you will need to edit is BoardHelper.java located in the "cs112game" folder (cpsc112/src/cpsc112.game/BoardHelper.java). The following instructions will be in reference to this file. The file has a collection of method headers that you will need to complete. These methods will be called by the rest of the project, in ways you don't need to worry about, to make the game fully functional.

#Part 1
13 points

Methods to edit

- findMatches
- buildPossibleMatchRow
- buildPossibleMatchColumn

Before you get started coding, let's take a look at the program as it is right now. In eclipse, select the frontend-desktop folder, then click the run button in eclipse. Up comes a window with the game. Click "Play" and an initial board will appear, but there are two problems.

The first problem is that the code that detects valid matches is not currently written. This is a problem because other parts of the code need to know if matches exist in a current board configuration. This is done through a call to the findMatches method which is currently not written, but which is supposed to return all matches that exist in the current board configuration. In particular: 1) When the board is created, findMatches is called to see if the initial board has any matches already in it (if it does, modifications are performed to avoid this --- we don't want to give the user free points for doing nothing), and 2) Before the user tries to swap two items, findMatches is called to see if as a result of the swap a match was made. If not, the game will not allow the user to swap the two items (since one of the rules of the game is that a user can only swap when it creates a valid match). Your job is thus to write the "findMatches" method, along with two methods that it will call to facilitate its work: "buildPossibleMatchHorizontal" and "buildPossibleMatchVertical". Once you have completed these three methods, the other parts of the program that call findMatches should now work.

We will start by writing buildPossibleMatchRow/Column first, then work on findMatches.

###How to write buildPossibleMatchRow/Column
First a short intro to a "Point" object. In class we have discussed primitive types in Java (like int, char, or boolean) and complex types (like String or Random). Recall that while primitive types can only store one value, complex types (also called "Objects") can store multiple values. For example a String stores many 'char's. Point is another complex type, and stores two integers representing a location in a two-dimensional space. The first integer represents the "Point"'s location in the horizontal dimension, and the second integer represents the "Point"'s location in the vertical dimension. We define and use variables of type Point in a similar way to how we define and use Strings. For example, to create a new String called s and initialize it to the string "Hello", we run:   

	String s = new String("Hello");
	
Similarly to create a new Point called p and initialize it to the point at location (2, 7) in two-dimensional space, we run:

	Point p = new Point(2,7);

where 2 and 7 are the 'x' and a 'y' coordinates of p. You can later access these using dot notation, e.g.:

System.out.println(p.x); //will print 2
System.out.println(p.y); //will print 7

One more thing. Most of the global variables in BoardHelper.java you can ignore --- they are for the other methods that are already written for you. The only global variable that is relevent to what you need to code is the size variable. This global 'size' variable stores the number of columns and rows of the game board (the board is always a square). In the code we gave you, size is 8 --- i.e. the game is an 8x8 square. Please do not hard-code the number 8 anywhere in your code --- whenever the size of the board is relevent to the code you are writing, use the size variable instead of the number '8'.

buildPossibleMatchRow takes a Point (p) that specifies a position on the board, and returns an array of Points, which indicates the location of all points stating with p's location moving directly to the right on the board that consecutively have the same board piece as the piece at the location specified by p. For example, if we had the following board, calling buildPossibleMatchRow(0,0) should return an array with just one point: [(0,0)]. Calling buildPossibleMatchRow(0,3) should return an array with two points corresponding for run the of length two of the same board piece (the shield for Morse College) starting at (0,3): [(0,3),(0,4)]. buildPossibleMatchColumn works identically to buildPossibleMatchRow except it looks for runs of the same piece moving down from the given Point (instead of to the right). For example, calling buildPossibleMatchColumn(0,0) should return an array [(0,0),(0,1)].

Please note that we have provided 4 lines of code for you in both buildPossibleMatchRow and buildPossibleMatchColumn. The first three lines of code are correct and should not be modified. The last line (return null) is only there to make the program compile before you complete your implementation of these methods. You should delete this last line and replace with your solution. Eventually your methods should return an actual Point array instead of "null".

![Alt Board](/Board1.png)

Please also note that the third line of code we have given you in these methods are a call to methods that we wrote for you that will help you in this task: getColumnBools() and getRowBools(). These methods take a particular location on the board, and return an array of booleans that represent which items in that row (or column) match the square at the given location. (In that sense, these methods are somewhat similar to the checkRow and checkColumn methods we wrote in class for the Sudoku example, although they differ in how they treat the entry in the boolean array corresponding to the location received as a parameter) For example calling helper.getRowBools(0,0) on the above board will return [True,False,False,True,True]. Calling helper.getColumnBools(0,0) on the above board will return [True,True,False,True]. Calling helper.getColumnBools(0,2) on the above board will return [True,False,False,False].

Similar to the Sudoku example from class, we do not know at the beginning of the method how many Points will be returned, and thus we do not initially know how large to initialze the Point array that will eventually be returned from the method. You should use a similar solution to how we solved this problem for the "checkProblems" method demonstrated in class: you should use the boolean array returned from getColumnBools/getRowBools to figure out how many elements your Point array should have. Initialze a new Point[] of this size, and then fill in the elements of this array with the appropriate points.

###How to write findMatches

Once you have buildPossibleMatchRow/Column written, you can get started on  findMatches. findMatches will return a two dimensional "jagged" array of all the matches on a board. For example, if we had following board, calling findMatches() should return [[(0,0),(1,0),(2,0)],[(0,1),(1,1),(2,1),(3,1)]]. The order doesn't matter. If there are no matches, findMatches() should return [[],[]]  --- it doesn't matter how many secondary arrays there are, so long as they are all empty.

![Alt Board](/Board3.jpg)

Here is the basic algorithm:

1. loop through every square
2. buildPossibleMatchRow on that square
3. If the built match is of length more than three add it to foundMatches\*
4. continue looking for matches at the end of the match you just found
5. do the same thing for buildPossibleMatchColumns

\*When we try to add a match to foundMatches, we will need make sure it is large enough to hold all our entries. In order to do this, we will can use a method called expandArray(Point[][]).

If this method is working correctly, the initial board will have no matches, and you should be able to play the game!





#Part 2
7 points

Methods to edit
- findSolutions
- expandArray

Great, now you can play the game! There is only one (major) problem left. If there are no legal swaps left on the board, the game should generate a brand-new board if the user wants to continue to play. However, right now, the game doesn't notice that there are no legal swaps left, so the user is just stuck with nothing to do. Therefore, you should now add code that will be called by the game to see if there are any legal swaps currently on the board. The game will use this method you write in two places: (1) if your method says there are no legal swaps left, the game will generate a new board and (2) the "Hint" button in the game will display current swaps that the user can consider.

Note that while (1) only requires detecting whether there are any legal swaps at all, (2) requires enumerating all legal swaps the user can take. In particular, when the user clicks on the hint button, all the squares that can be swapped to create a match should be highlighted. The code that we have pre-written has already taken care of the highlighting, but you just need to implement the "findSolutions" method that will return a list of all squares that can be swapped. For example, calling findSolutions() with the following board should return [(0,0),(0,1),(1,0),(1,1), etc.]. The order doesn't matter, and there can be duplicates.

![Alt Board](/Board2.png)

###How to write findSolutions
You should swap every square in right and down and check to see if it makes a match using the hasMatches() method. If there is a match, add the square's location (new Point(x,y)) to the squaresThatCanBeSwapped[], then swap it back to its original position.

Notice that we don't know how big how list of solutions is going to be when we start. You will to write another expandArray(Point[]) method. This time it will be operating on Point[] instead of Point[][]. The actual code will look **very** similar to the previous expandArray(Point[][]) method from Part 1.



#Cool lessons/Hints

Here are some fun extra things. They aren't needed to complete the assignment though.

###Hidden Bug
There is a bit of a bug in the interface ("It's not a bug, it's a feature"). When you swap two squares, you can actually move one of those squares anywhere on the board you want. Click a square to swap it. As the swap animation happens, quickly drag your mouse somewhere else on the board. The original square you were trying to swap will end up wherever your mouse is at the end of the swap animation. It can make the game a bit more interesting. Also be carfeul of this as you test your own code!

###Tester
You can test your code using the tester() method. It will be called every time you press the "Run Tester()" buttin in the game. This would be a good place to put some print lines and have your code print to the console.

###Dynamic Arrays/Lists
By writing the expandArray function, you have just invented a version of  dynamic arrays, or lists. These are special arrays that you can use without worrying about making the array the right size. In java one implementation of this is called ArrayLists. We won't go into depth now, but feel free to investigate on your own (chapter 7.5 in the textbook)!

###Method Overloading
Did you notice that we wrote two method with the same name (expandArray) but different parameter types? This is called method overloading. As long as the parameter types or number of parameters is different, Java is smart enough to figure out which one to use on its own. you have already seen this with System.out.println which can print out a String or an int for you (or many other things).
