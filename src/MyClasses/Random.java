package MyClasses;

public class Random {
	static public long MSPRNG(long seed){
		long res;
		res= (long)(seed>>16)&0xFFFFFFFF;
		res*=res;
		return res;
	}
}
