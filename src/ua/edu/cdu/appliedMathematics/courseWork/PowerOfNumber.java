package ua.edu.cdu.appliedMathematics.courseWork;

/**
 * @author kol
 */
public class PowerOfNumber {

	/**
	 * Check is number is power two or not
	 * @param number
	 * @return true of false
	 */
	public static boolean ifNumberIsPowerOf2(int number){
		if(number % 2 != 0){
			return false;
		}
		while(number % 2 == 0){
			number /= 2;
		}
		if(number == 1){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Count next power of 2 for given number
	 * @param number
	 * @return power of 2
	 */
	public static int whatPowerOf2(int number){
		int power = 0;
		int count = 0;
		while(number > 1){
			if(number % 2 == 1){
				count++;
			}
			number /= 2;
			power++;
		}
		if(count == 0){
			return power;
		} else {
			return power + 1;
		}
	}

	/**
	 * Counts power of number for int values
	 * @param num - number
	 * @param power
	 * @return number^power
	 */
	public static int powerInt(int num, int power){
		if(power == 0){
			return 1;
		} else if(power > 0){ 
			int pow = 1;
			for (int i = 0; i < power; i++) {
				pow *= num;
			}
			return pow;
		} else {
			return 0;
		}
	}
}