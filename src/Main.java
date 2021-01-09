
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
	static boolean flag=true;
	static String filename="",dirname="",DESKEY="";
	static int chk=0,ch=0;

	public static void main(String[] args) {
		//object of class EncDec.
		//used to encrypt/decrypt files.
		DesFunctionClass obj=new DesFunctionClass();

		Scanner chscanner =new Scanner(System.in);		

		while(flag){
			System.out.println("\n===============================================================\n 1.ENCRYPT a file \t 2.DECRYPT a file \t 3.Exit Program\n===============================================================");
		 	System.out.println("Enter Your Choice:\t");
		 		if(chscanner.hasNextInt())
			    	ch=chscanner.nextInt();
		 	switch(ch)
		  	{
		    	case 1:
		    		//Checks infinitely for valid file type.
		    		do {
		    		System.out.println("Enter Name of the File to be Encrypted(include path if outside):");
					filename=chscanner.next();
					filename=filename.replaceAll("\\\\", "/");		//for windows dir scheme
					chk= HelperFunctions.check(filename);
					}while(chk!=1);

		    		//Checks for valid Directory type to store encrypted file.
					do {
					System.out.println("Enter Name of Directory to store Encrypted file:");
					dirname=chscanner.next();
					dirname=dirname.replaceAll("\\\\", "/");
					chk= HelperFunctions.check(dirname);
					}while(chk!=2);

					//Takes Valid private input key of size>10
					do{
					System.out.println("Enter Your Private Key (length>10):)");
					DESKEY=chscanner.nextLine();
					if(DESKEY.length()<10)
						System.out.println("\t\t--Private Key Size should be > 10!--");
					}while(DESKEY.length()<10);
				
					DESKEY= HelperFunctions.KeyGen(DESKEY);//generating random key from input key.

					BigInteger k=new BigInteger(DESKEY);	//convert key from string type to BigInteger type
					BigInteger RSAKEY=RsaFunctionClass.EncDec(k, RsaFunctionClass.e,RsaFunctionClass.n);	//RSA-Encryption of the DES-key
					String keyloc=RsaFunctionClass.WriteEncKey(RSAKEY, dirname, filename);	//write encrypted key to file for further use
				
					//Encrypting the File using DESKEY and DES algorithm.
					obj.encrypt(filename,dirname,DESKEY);
					
					System.out.println("\nFile ENCRYPTED Successfully, Stored at"+"'"+dirname+"'");
					System.out.println("ATTENTION! NOW Your Encrypted Private Key is:"+RSAKEY+"\n\tIt is Saved for You at '"+keyloc+"'");
		    		break;
		    	
		    	case 2:
					//Checks infinitely for valid file type.
		    		do{
						System.out.println("Enter Name of the Encrypted File that is to be Decrypted(include path if outside):");
						filename=chscanner.next();
						filename=filename.replaceAll("\\\\", "/");
						chk= HelperFunctions.check(filename);
						}while(chk!=1);

					//Get Original Extension for Decryption
					System.out.println("Enter EXTENSION to which file is to be Decrypted(e.g txt,pdf,jpg,mp3,mp4,etc):");
					String extname = chscanner.next();
					extname=extname.substring(extname.lastIndexOf(".") + 1);	//if user provided a '.' with extension

					//Checks for valid Directory type to store decrypted file.
					do{
						System.out.println("Enter Name of Directory where Decrypted file will be Stored:");
						dirname=chscanner.next();
						dirname=dirname.replaceAll("\\\\", "/");
						chk= HelperFunctions.check(dirname);
					}while(chk!=2);
				
					String regex = "[0-9]+";//Regular Expression for string to make sure key contains only numbers

					do{
						System.out.println("Enter Your Encrypted Private Key of the file:");
						DESKEY=chscanner.next();
						if(DESKEY.length()<500||!(DESKEY.matches(regex)))
							System.out.println("\t\t--Encrypted-Key Size must be > 500 and Must only contain Numeric Values!");
					}while((DESKEY.length()<500)||!(DESKEY.matches(regex)));
				
					BigInteger c=new BigInteger(DESKEY);//convert to BI
					BigInteger Deckey=RsaFunctionClass.EncDec(c, RsaFunctionClass.d,RsaFunctionClass.n);	//UNHANDLED>> make regex seq for key in EncDec fxn
					DESKEY=Deckey.toString();
				
					obj.decrypt(filename,extname,dirname,DESKEY);
				
					System.out.println("\nFile DECRYPTED Successfully,' Stored at "+"'"+dirname+"'");
	    			break;
		    		
	   			case 3:
		 			flag=false;
		  			System.out.println("Good Bye!");
		   			chscanner.close();
		   			break;
		    			
				default:
	  				System.out.println("No Such Option... Try Again!");
		   	}	
		}	
	}
}
