//Name: Nicholas Kennedy
//Date: September 8, 2021
import java.util.Scanner;

public class Sieve {

	// Implements the Sieve of Eratosthenes
	
	public static void main(String[] args) {
		
		int num = 0;
		Scanner sc = new Scanner(System.in);
		
		//Get user input
		while (num<2) 
		{	
			System.out.println("Enter an integer greater than 1:  ");
			num = sc.nextInt();
		
			if (num < 2) 
			{
				System.out.println("Entry Invalad! ");
			}
		}
		
		int[] prime = sieve(num);
		
		//Display output
		System.out.println("The prime numbers less than " +num+ " are: ");
		
		for (int i = 0; i < prime.length; i++) 
		{
			System.out.println(prime[i]);
		}
		
	}

		public static int[] sieve (int n)
		{
			int[]a = new int[n+2];
			int k = 0;
			
			//List all numbers to n
			for (int p = 2; p <= n; p++ )
			{
				a[p] = p;
			}
			
			//Replace all non prime numbers with 0
			for (int p = 2; p <= Math.sqrt(n); p++)
			{
				if(a[p]!= 0) 
				{
					int j = p * p;
					
					while (j < n) 
					{
						a[j] = 0;
						j = j + p;
					}
				}
			}
			
			//Get length for new array
			for (int p = 2; p < n; p++)
			{
				if (a[p] != 0) 
				{
					k++;
				}
			}
			
			int[]l = new int[k];
			int i = 0;
			
			//Copy all prime numbers to new array
			for (int p = 2; p < n; p++)
			{
				if (a[p] != 0) 
				{
					 l[i] = a[p];	 
					 i = i + 1;
				}
			}
			
			return l;
		}
}
