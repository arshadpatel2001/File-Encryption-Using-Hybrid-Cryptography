
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.Scanner;

public class HelperFunctions {
	Scanner inscan=new Scanner(System.in);

	//	checks for file/Directory
	//	returns 1 for file
	//	returns 2 for Directory
	//	returns 0 if not file or directory
	public static int check(String filename){
		int flag=0;
		File filechk = new File(filename);
		if(filechk.isFile())
			flag=1;
		else if(filechk.isDirectory())
			flag=2;
		if(flag!=1&&flag!=2)
			System.out.println("'"+filename+"'"+" is not a Valid FILE/DIRECTORY!");
		return flag;
	}

	//Generates Random Key from the Input Key.
	//Uses Random and Fisher Yates Shuffle.
	//Returns String.
	public static String KeyGen(String key){	//make a 200 byte key from user key
		int len=key.length(),i=0,n=1;

		do{
			int x= key.charAt(i)+n;			//more randomness by 'n'
			key+=x;
			n++;i++;

			if(i==len-1)
				i=0;

		}while(key.length()<len+100);

		key=key.substring(len);

		int [] kArray=new int[key.length()];
		for(int m =0;m<=key.length()-1;m++)
		{
			kArray[m]=key.charAt(m);
			double temp= HelperFunctions.percentage(m, key.length()-1);
			if (temp%20==0.00){
				System.out.println("Generating 200 byte key from Entered key: "+temp+"%");
			}
		}

		int kArrLen=kArray.length;
		for (int j = kArrLen - 1; j >= 1; j--)		//FISHER-YATES Random Shuffle
		{
			Random rand = new Random();
			// generate a random number k such that 0 <= k <= j
			int k = rand.nextInt(j + 1);

			// swap current element with randomly generated index
			int temp = kArray[j];
			kArray[j] = kArray[k];
			kArray[k] = temp;
		}

		key="";
		for(int m =0;m<=kArrLen-1;m++)
		{
			key+=kArray[m];
		}
		System.out.println(key.length()+" Byte Key Generated Successfully!");
		return key;
	}

	//Gives Percentage of task
	public static double percentage(long no, long total){			//percent function
		double per = (no*100.0)/total;
		per = per * 100;
		per = Math.round(per);
		per = per/100;
		return per;
	}

	//For copying files using File channel=>faster method
	public static void copyFile(String source, String dest) throws IOException {
		File src=new File(source);
		File dst=new File(dest);
		FileChannel sourceCh = null;
		FileChannel destCh = null;
		try {
			sourceCh = new FileInputStream(src).getChannel();
			destCh = new FileOutputStream(dst).getChannel();
			destCh.transferFrom(sourceCh, 0, sourceCh.size());
		}
		finally{
			sourceCh.close();destCh.close();
		}
	}

	public static void rounds(RandomAccessFile in,RandomAccessFile out,String key,int shiftby,String mode){		//round Encryption/decryption
		HelperFunctions obj=new HelperFunctions();

		int round=0,ch=0;
		String roundname="";
		System.out.println("=========================================================================");
		System.out.println("Enter Mode:\n1:->02 Round Enc/Dec\t\t\n2:->04 Round Enc/Dec\t\t\n3:->08 Round Enc/Dec\t\t\n4.:->12 Round Enc/Dec\t\n5.:->16 Round Enc/Dec\t\t\n\t\tUse Same Mode for Decryption with which the File was Encrypted!");
		System.out.println("=========================================================================");
		if(obj.inscan.hasNextInt())
			ch=obj.inscan.nextInt();

		if(ch==1){
			round=2;
			roundname="2 Round Enc/Dec";
		}
		else if(ch==2){
			round=4;
			roundname="4 Round Enc/Dec";
		}
		else if(ch==3){
			round=8;
			roundname="8 Round Enc/Dec";
		}
		else if(ch==4)
		{round=12;
			roundname="12 Round Enc/Dec";
		}
		else if(ch==5){
			round=16;
			roundname="16 Round Enc/Dec";
		}
		else
			System.out.println("Invalid Option!");

		if(key.length()%2!=0)
			round--;

		for(int i=1;i<=round;i++)		//Apply Alternate rounds
		{

			if(i%2!=0)
			{
				System.out.println("\t\t\tROUND--"+i);
				key= HelperFunctions.shiftKey(key,shiftby);
				HelperFunctions.xor(in, out, key,mode,"Round-"+i);
			}
			else
			{
				System.out.println("\t\t\tROUND--"+i);
				key= HelperFunctions.shiftKey(key, shiftby);
				HelperFunctions.xor(out, in, key, mode,"Round-"+i);
			}
		}
		System.out.println("\t"+roundname+ " Successfully Completed!");

	}

	//left shift by factor "shiftby'
	public static String shiftKey(String key,int shiftby){
		int keylen=key.length();
		String s1=key.substring(shiftby,keylen);
		String s2=key.substring(0,shiftby);
		key=s1+s2;
		return key;
	}

	//XOR function.
	public static void xor(RandomAccessFile in,RandomAccessFile temp,String key,String mode,String round){
		int len_var=0;
		try
		{
			long incount=in.length();
			int p=0;
			double percent;
			in.seek(0);
			temp.seek(0);
			for(int j=0; j<=incount-1;j++)
			{
				int intchr=in.read();
				//write at beginning
				temp.write(intchr ^ key.charAt(len_var));//XORing
				len_var++;

				if(len_var>key.length()-1)
					len_var=0;

				percent=percentage(p,incount);
				p++;
				if (percent%20==100.00 && p%10==0) {
					System.out.println("[" + round + "]" + " " + mode + " Characters to File:" + percent + "%");
				}
			}
		}
		catch(Exception e){

		}
	}

	//Simple Reversing
	public static void shuffle(RandomAccessFile in,RandomAccessFile out){
		try {
			int p=0;
			long count=in.length();

			for(long i=count-1;i>=0;i--)	
				{
				    //Writing on file>>
				 	in.seek(i);			//set cursor of in file at last >> i=count-1
					out.seek(p);		//set cursor of output file to start >> p=0
					int ch =in.read(); 	//read() from in
					out.write(ch);		//write it at start of output file				
					p=p+1;				//increment p
					
				}
		} catch (IOException e) {
			System.out.println(e); 	
		}
	}

	public static void delencf(String filename, int ch){
		if(ch==1)
		{
			File f = new File(filename);
			if(f.delete())
				System.out.println(filename+" deleted Successfully!");
			else
				System.out.println("\nCouldn't Locate "+filename+" to delete!");
		}
	}

}
