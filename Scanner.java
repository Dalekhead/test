package MJ;

import java.io.*;

public class Scanner {

    private static final char eofCh = '\u0080';
    private static final char eol = '\n';
    private static final int // token codes
            none = 0,
            ident = 1,
            number = 2,
            charCon = 3,
            plus = 4,
            minus = 5,
            times = 6,
            slash = 7,
            rem = 8,
            eql = 9,
            neq = 10,
            lss = 11,
            leq = 12,
            gtr = 13,
            geq = 14,
            assign = 15,
            semicolon = 16,
            comma = 17,
            period = 18,
            lpar = 19,
            rpar = 20,
            lbrack = 21,
            rbrack = 22,
            lbrace = 23,
            rbrace = 24,
            class_ = 25,
            else_ = 26,
            final_ = 27,
            if_ = 28,
            new_ = 29,
            print_ = 30,
            program_ = 31,
            read_ = 32,
            return_ = 33,
            void_ = 34,
            while_ = 35,
            eof = 36;
    private static final String key[] = { // sorted list of keywords
        "class", "else", "final", "if", "new", "print",
        "program", "read", "return", "void", "while"
    };
    private static final int keyVal[] = {
        class_, else_, final_, if_, new_, print_,
        program_, read_, return_, void_, while_
    };

    private static char ch;			// lookahead character
    public static int col;			// current column
    public static int line;		// current line
    private static int pos;			// current position from start of source file
    private static Reader in;  	// source file reader
    private static char[] lex;	// current lexeme (token string)

    //----- ch = next input character
    private static void nextCh() {
        try {
            ch = (char) in.read();
            col++;
            pos++;
            if (ch == eol) {
                line++;
                col = 0;
            } else if (ch == '\uffff') {
                ch = eofCh;
            }
        } catch (IOException e) {
            ch = eofCh;
        }
    }

    //--------- Initialize scanner
    public static void init(Reader r) {
        in = new BufferedReader(r);
        lex = new char[64];
        line = 1;
        col = 0;
        nextCh();
    }

    //---------- Return next input token
    public static Token next() {
        while (ch <= ' ') {
            nextCh(); // skip blanks, tabs, eols
        }
        Token t = new Token();
        t.line = line;
        t.col = col; //creates new token where column and line are current reader position

			//All code below this point was added by me **
        switch (ch) {													//switch chooses from cases below, for letters of either case
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                readName(t);
                break;												//calls the method to deal with the letters above

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': //Switch for choosing from numbers
                readNumber(t);
                break;											//calls the method to deal with the numbers above

            // the following will deal with all acceptable characters
            case '+':
                nextCh();
                t.kind = plus;
                break;
            case '-':
                nextCh();
                t.kind = minus;
                break;
            case '*':
                nextCh();
                t.kind = times;
                break;

            case '/':
                nextCh();
                if (ch == '/') {
                    do {
                        nextCh();
                    } while (ch != '\n' && ch != eofCh);
                    t = next(); //????????
                } else {
                    t.kind = slash;
                }
                break;

            case '%':
                nextCh();
                t.kind = rem;
                break;

            case '=':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    t.kind = eql;
                } else {
                    t.kind = assign;
                }
                break;

            case '!':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    t.kind = neq;
                } else {
                    System.out.println("unused exclaimation mark !");///need to return error?;
                }
                break;

            case '<':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    t.kind = leq;
                } else {
                    t.kind = lss;
                }
                break;

            case '>':
                nextCh();
                if (ch == '=') {
                    nextCh();
                    t.kind = geq;
                } else {
                    t.kind = gtr;
                }
                break;

            case ';':
                nextCh();
                t.kind = semicolon;
                break;
            case ',':
                nextCh();
                t.kind = comma;
                break;
            case '.':
                nextCh();
                t.kind = period;
                break;
            case '(':
                nextCh();
                t.kind = lpar;
                break;
            case ')':
                nextCh();
                t.kind = rpar;
                break;
            case '[':
                nextCh();
                t.kind = lbrack;
                break;
            case ']':
                nextCh();
                t.kind = rbrack;
                break;
            case '{':
                nextCh();
                t.kind = lbrace;
                break;
            case '}':
                nextCh();
                t.kind = rbrace;
                break;

            case '\'':
                readCharCon(t);
                break;				//calls the method to deal with potential character constants

            case eofCh:
                t.kind = eof;
                break; // no nextCh() any more
            default:
                nextCh();
                t.kind = none;
                break;
        }
        return t;
    }

    private static void readName(Token t) {
        StringBuilder nameBuild = new StringBuilder();
        nameBuild.append(ch);
        String keyTest;       								//set up variables to store the string startign with a letter, either keyword or ident
        boolean stillName = true;

        while (stillName == true) { 						//will this remains true contiune to add to the string

            nextCh();
            switch (ch) {										//switch chooses from cases below
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '_':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    nameBuild.append(ch);
                    break;                   //adds character to string then repeats to test next character
                default:
                    stillName = false;
                    break;               // does nothing once not valid character for ident or keyword, but changes stillname to false to break while loop
            }
        }
        t.string = nameBuild.toString();
        for (int i = 0; i < key.length; i++) {
            if (key[i].equals(t.string)) {
                t.kind = keyVal[i];							// test name and returns keyword if it is in kayval or returns as ident if not
                break;
            } else {
                t.kind = ident;
            }
        }
    }

    private static void readNumber(Token t) {
        t.val = Character.getNumericValue(ch);
        boolean stillNum = true;

        do {
            nextCh();
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (t.val < 214748363) {
                        t.val = (t.val * 10) + Character.getNumericValue(ch);
                    } //makes sure t.val is low enough to not produce an overflow (>2147483647) after equation
                    else {
                        System.out.println("ERROR. Number reset to zero as too large, on line " + t.line);
                        t.val = 0;
                    }
                    ;
                    break;
                default:
                    stillNum = false;
                    break;
            }
        } while (stillNum == true);				//if the number has not gone over it will store as t.kind
        t.kind = number;

    }

    private static void readCharCon(Token t) {
        nextCh();

        StringBuilder charBuild = new StringBuilder();
        int i = (int) ch;													//this is called after ' is read

        switch (i) {
            case 96:
                System.out.println("ERROR. No char entered");
                break;			// this is ' so creates error
            case 92:
                nextCh();
                if (ch == 'r') {
                    t.val = 12;
                    break;						//if first of char is \ follows rules to output as relevant code for \r, \t or \t . //
                } else if (ch == 'n') {
                    t.val = 10;
                    break;
                } else if (ch == 't') {
                    t.val = 9;
                    break;
                } else {
                    charBuild.append(ch);
                }
                t.string = charBuild.toString();
                t.val = Character.getNumericValue(ch);
                break; // if none above meet character added to string

            case 32:
                System.out.println("ERROR. The character is empty at line " + t.line);
                t.val = i;
                break;//case 32: space, if nothing after ' returns error
            default:
                charBuild.append(ch);
                t.string = charBuild.toString();
                t.val = Character.getNumericValue(ch);
                break;
        } //default will add to string

        nextCh();									//looks to next char
        switch (ch) {
            case '\'':
                nextCh();
                t.kind = charCon;
                break;							//if ' then closeas as accurate
            case ' ':
                nextCh();
                t.kind = charCon;
                System.out.println("ERROR. No character close found, character stored as " + t.string + " on line " + t.line);
                break; /// 'x  If space after charcter, returns as t.val but also error.
            default:
                System.out.println("ERROR. The charater not closed properly, or invalid length on line " + t.line);
                break; //anything else is an error message
        }
    }
}
