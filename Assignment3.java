/******************************************************************
 
   Name:                 Nicholas Kennedy
   Course/Section:       COSC 4315.001
   Instructor:           Dr. Brown
   Program Description:  This program will parse terms contained in 
   a collection of text documents which are stored in a single directory. 
   The user is prompted for a directory name and a search term. The user will then get 
   an output showing the documents that contain the search term in ranked order
   of their TF-IDF score. The search term can have a single asterisk wildcard.
   If the term is not found in the collection, a message stating so will appear.
   
 
   This program uses the following approach for each issue
   Case: Converts everything to lower case.
   Punctuation: removes periods, commas, question marks, parenthesis,
   exclamation marks, semicolons, and colons. It also removes apostrophes 
   and all characters after it. It leaves hyphens and quotation marks alone.
   Stop Words: Dose not handle stop words.
   Stemming: Dose not handle stemming.
 ******************************************************************/

public class Assignment3 
{
	public static void main (String args[]) throws Exception
	  {
	    InvertedIndex ir = new InvertedIndex();
	    ir.start();
	  }

}
