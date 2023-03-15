import java.io.*;
import java.util.*;

public class InvertedIndex 
{
	private File[] collection;
	private String term;
	int index = 0;
	private boolean isWC = false;
	Hashtable<String, Hashtable<String, Integer>> processed = new Hashtable<String, Hashtable<String, Integer>>();// k: processed term; v: posting(docID, freq)
	Hashtable<String, String> permuterm = new Hashtable<String, String>();// k: permuterm; v: processed term
	Hashtable<String, Integer> docs = new Hashtable<String, Integer>();// k: docID; v: total terms in doc 
	Hashtable<String, String> cards = new Hashtable<String, String>();// k: term; v: term

	public InvertedIndex()
	{
		collection = getFiles();
		term = getTerm();
	}

	public File[] getFiles()
	{
		File[] files = null;
		try
		{
			System.out.println();
			System.out.print("Enter name of a directory> ");
			Scanner scan = new Scanner(System.in);
			File dir = new File(scan.nextLine());
			files = dir.listFiles();
			System.out.println();
		}
		catch (Exception e)
		{
			System.out.println("Caught error in getFiles: " + e.toString());
		}
		return files;
	}

	public String getTerm() 
	{
		//get query term from user
		String t;
		System.out.print("Enter a search term> ");
		Scanner scan = new Scanner(System.in);
		t = scan.nextLine();

		return t;
	}

	public String process(String w)
	{
		/**********************************************/
		/*  This is where you process each input term */
		/*  Case, Punctuation, Stop Words, Stemming   */
		/**********************************************/
		w = w.toLowerCase();

		for (int i = 0; i < w.length(); i++) 
		{
			if (w.charAt(i) == '(') 
			{
				w = w.substring(i+1);
			}

			if (w.charAt(i) == '.' || w.charAt(i) == '?' || w.charAt(i) == '!' || w.charAt(i) == ';' || w.charAt(i) == ',' || w.charAt(i) == ':' || w.charAt(i) == '\'' || w.charAt(i) == ')') 
			{

				w = w.substring(0, i);
			}

		}  	  
		return w;
	}

	public void seenBefore(String w, String f)
	{
		boolean found = false;

		Integer count = 0;// term count per doc
		Integer words = 0;// word count per doc
		Hashtable<String, Integer> temp;// posting structure 

		// increment word count if already found in doc; else start new count if new doc
		if (docs.containsKey(f)) 
		{
			words = docs.get(f);
			docs.put(f, words+1);
		}
		else 
		{
			docs.put(f, words+1);
		}

		if(index == 0)// first term in first doc; create new entry 
		{
			processed.put(w, new Hashtable<String, Integer>(){{put(f, 1);}});

			count = processed.get(w).get(f);
			temp = processed.get(w);

			index++;
			permutermIndex(w);
		}
		else// check if term already exists
		{ 
			if (processed.containsKey(w)) 
			{
				found = true;
			}
		}

		if (found)// increment if term exists; else add new entry 
		{
			count = processed.get(w).get(f);
			temp = processed.get(w);

			// increment term count if already found in doc; else start new count if new doc
			if (processed.get(w).containsKey(f))
			{ 				  
				temp.put(f, count +1);				 			 
				processed.put(w, temp);			  
			}
			else 
			{
				temp.put(f, 1);		 			 
				processed.put(w, temp);
			}
			found = false;
		} 
		else 
		{			
			processed.put(w, new Hashtable<String, Integer>(){{put(f, 1);}});			  
			index++;
			permutermIndex(w);
		} 
	}

	public void permutermIndex(String w) 
	{		
		// get permuterms for term
		String p = w +"$";

		for (int i = 0; i < w.length()+1; i++) 
		{
			permuterm.put(p, w);
			String temp1 = p.substring(1);
			char temp2 = p.charAt(0);
			p = temp1 + temp2;
		}
	}			

	public void wildCard(String t) 
	{
		// perform wildcard term search of permuterms
		String temp1;
		String temp2;
		String temp3;
		int j=0;

		t = t.toLowerCase();

		for (int i = 0; i < t.length(); i++) 
		{
			if (t.charAt(i) == '*') 
			{
				j = i;
			}
		}

		temp1 = t.substring(j)+"$";
		temp2 = t.substring(0, j);
		temp3 = temp1.substring(1) + temp2 + "*";

		permuterm.forEach((k, v) -> {
			if (k.contains(temp3.substring(0, temp3.length()-1))) 
			{
				cards.put(v, v);
			}			
		});		
	}

	public float score(String t, String f) 
	{
		//calculate tf-idf score 		
		if(processed.get(t).containsKey(f))
		{
			float tf = (float)processed.get(t).get(f) / (float)docs.get(f);
			float idf = 1 / (float)processed.get(t).size();
			float s = tf * idf;

			return s;
		}
		else 
		{
			return 0;
		}		
	}

	public void start()
	{
		try
		{
			for (File f : collection)
			{
				Scanner sc = new Scanner(f);
				while (sc.hasNextLine())
				{
					StringTokenizer st = new StringTokenizer(sc.nextLine());
					while (st.hasMoreTokens())
					{
						String inputWord = st.nextToken();
						String outputWord = process(inputWord);
						seenBefore(outputWord, f.getName());
					}
				}
			}

			if(term.contains("*")) 
			{
				wildCard(term);
				isWC = true;
			}
			else 
			{
				if (processed.containsKey(term)) 
				{
					cards.put(term, term);
				}
				else 
				{
					System.out.println("Search term not found.");
					return;
				}
			}

			List<String> docList = new ArrayList<String>();
			cards.forEach((k, v) -> processed.get(v).forEach((k1,v1) -> {if(!docList.contains(k1))docList.add(k1);}));

			float[] scores = new float[docList.size()];

			if(isWC) 
			{				
				for(int i = 0; i < docList.size(); i++) 
				{
					scores[i] = 0;					
				}

				cards.forEach((k, v)->
				{
					for(int i = 0; i < docList.size(); i++) 
					{
						scores[i] += score(v, docList.get(i));
					}
				});
			}
			else 
			{								
				for(int i = 0; i < docList.size(); i++) 
				{
					scores[i] = score(term, docList.get(i));
				}

			}

			String[] rankIndex = new String[docList.size()];

			for(int i = 0; i < docList.size(); i++) 
			{
				rankIndex[i] = docList.get(i);
			}

			int max= 1;
			for (int i = 0; i < docList.size(); i++)
			{
				max = i;
				for (int j = i ; j <= docList.size()-1; j++)
				{
					if (scores[j] > scores[max])
					{
						max = j;
					}	        	
				}
				float temp = scores[i];
				scores[i] = scores[max];
				scores[max] = temp;
				String temp2 = rankIndex[i];
				rankIndex[i] = rankIndex[max];
				rankIndex[max] = temp2;

			}
			System.out.println();
			System.out.println("Results: ");
			for(int i = 0; i < docList.size(); i++) 
			{
				System.out.println(rankIndex[i]);
			}

		}
		catch(Exception e)
		{
			System.out.println("Error in start:  " + e.toString());
		}
	}
}
