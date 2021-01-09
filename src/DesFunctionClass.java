
import java.io.File;
	import java.io.IOException;
	import java.io.RandomAccessFile;
	import java.util.Scanner;

public class DesFunctionClass {

	int shiftby=2;

	Scanner inputscanner =new Scanner(System.in);

	//encrypt function.
	void encrypt(String filename,String dirname,String key)	{
			try{
				File dir = new File("TempFiles");
				if(!dir.exists())
					dir.mkdir();			//make a folder(if do not exist) for temporary files
				System.out.println("Enter Name for the Encrypted File:");
				String name = inputscanner.nextLine();
				RandomAccessFile fn = new RandomAccessFile(filename, "rw");  
				RandomAccessFile inTemp = new RandomAccessFile("TempFiles/cp-temp.ars", "rw");
				RandomAccessFile outTemp = new RandomAccessFile("TempFiles/enc-T.ars", "rw");
				RandomAccessFile out = new RandomAccessFile(dirname+"/"+name+".ars", "rw");
				
				HelperFunctions.copyFile(filename, "TempFiles/cp-temp.ars");//Copying File to be encrypted into temp file (in).

				HelperFunctions.rounds(inTemp, outTemp, key, shiftby,"Encrypting");//XOR

				HelperFunctions.shuffle(outTemp, out);//shuffle->Simple reversing of outTemp

				//Release Resources
				fn.close();
				inTemp.close();
				outTemp.close();
				out.close();

				File f1 = new File("TempFiles/cp-temp.ars");
				File f2 = new File("TempFiles/enc-T.ars");
				f1.delete();
				f2.delete();

		    	}    	
		catch ( IOException e) {
			System.out.println(e);
			}
	    }

	void decrypt(String filename,String extname, String dirname,String key) { //decrypt fxn
		try{
			File dir = new File("TempFiles");
			if(!dir.exists())
				dir.mkdir();			//make a folder(if donot exist) for temporary files which will b deleted at end of prg

			RandomAccessFile fn = new RandomAccessFile(filename, "rw");
			System.out.println("Enter Name for the Decrypted File:");
			String name = inputscanner.nextLine();
			RandomAccessFile in = new RandomAccessFile("TempFiles/cp-temp.ars", "rw");
			RandomAccessFile out = new RandomAccessFile(dirname+"/"+name+"."+extname, "rw");

			HelperFunctions.shuffle(fn, in);//deshuffle
			HelperFunctions.rounds(in, out, key, shiftby,"Decrypting");//XOR

			//release resources
			in.close();
			out.close();
			fn.close();

			File f = new File("TempFiles/cp-temp.ars");
			f.delete();

			System.out.println("Do you want to delete "+filename+"?\nEnter 1 for yes and 2 for No:");
			int opt=0;
			if(inputscanner.hasNextInt())
			{
				opt=inputscanner.nextInt();
				HelperFunctions.delencf(filename,opt);
			}
			else
				System.out.println("Wrong Option!");
		}
		catch ( IOException e) {
			System.out.println(e);
		}
	}

}
