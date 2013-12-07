package ua.edu.cdu.appliedMathematics.courseWork;

/**
 * Represent information about wave file
 * 
 * @author kol
 */
public class Wave {
	private char[] chunkID;
	private int chunkSize;
	private char[] format;
	private char[] subChunk1ID;
	private int subChunk1Size;
	private int audioFormat;
	private int numChanels;
	private int sampleRate;
	private int byteRate;
	private int blockAlign;
	private int bitsPerSample;
	private char[] subChunk2ID;
	private int subChunk2Size;
	private int[][] data;
	private int[] sysInfo;
	
	/**
	 * Gets all info about wave file from vector which represent it
	 * @param file - vector which represent file
	 */
	public Wave(int[] file) {
		sysInfo = getSysInfo(file, 44); 
		chunkID = readID(sysInfo, 0, 4);
		chunkSize = readNumber(sysInfo, 4, 4);
		format = readID(sysInfo, 8, 4);
		subChunk1ID = readID(sysInfo, 12, 4);
		subChunk1Size = readNumber(sysInfo, 16, 4);
		audioFormat = readNumber(sysInfo, 20, 2);
		numChanels = readNumber(sysInfo, 22, 2);
		sampleRate = readNumber(sysInfo, 24, 4);
		byteRate = readNumber(sysInfo, 28, 4);
		blockAlign = readNumber(sysInfo, 32, 2);
		bitsPerSample = readNumber(sysInfo, 34, 2);
		subChunk2ID = readID(sysInfo, 36, 4);
		subChunk2Size = readNumber(sysInfo, 40, 4);
		data = readData(file, 44, subChunk2Size/4);
	}
	
	/**
	 * @return double variant of actual sound data
	 */
	public double[][] getDoubleData(){
		double[][] dData = new double[data.length][2];
		for (int i = 0; i < dData.length; i++) {
			for (int j = 0; j < 2; j++) {
				dData[i][j] = data[i][j]/Integer.MAX_VALUE;
			}
		}
		return dData;
	}
	
	/**
	 * @param chann - name of channel
	 * @return vector with representation of one channel (left or right)
	 */
	public double[] getDoubleChannel(Channel chann){
		int channelID = 0;
		switch (chann) {
			case LEFT:
				channelID = 0;
				break;
			case RIGHT:
				channelID = 1;
				break;
		}
		double[] channel = new double[data.length];
		for (int i = 0; i < channel.length; i++) {
			channel[i] = data[i][channelID]/(32765 * 1.0);
			//channel[i] = data[i][channelID];
		}
		return channel;
	}
	
	/**
	 * Print all information about wave file
	 */
	public void outInfoAboutWave(){
		System.out.print("ChunkID \t= ");
		for (int i = 0; i < chunkID.length; i++) {
			System.out.print(chunkID[i]);
		}
		System.out.println();
		System.out.println("Chunk size \t= " + chunkSize);
		System.out.print("Format \t\t= ");
		for (int i = 0; i < format.length; i++) {
			System.out.print(format[i]);
		}
		System.out.println();
		System.out.print("SubChunk1ID \t= ");
		for (int i = 0; i < subChunk1ID.length; i++) {
			System.out.print(subChunk1ID[i]);
		}
		System.out.println();
		System.out.println("SubChunk1 size \t= " + subChunk1Size);
		System.out.println("Audio format \t= " + audioFormat);
		System.out.println("Number chanels \t= " + numChanels);
		System.out.println("Sample rate \t= " + sampleRate);
		System.out.println("Byte rate \t= " + byteRate);
		System.out.println("Block align \t= " + blockAlign);
		System.out.println("Bits per sample = " + bitsPerSample);
		System.out.print("SubChunk2ID \t= ");
		for (int i = 0; i < subChunk2ID.length; i++) {
			System.out.print(subChunk2ID[i]);
		}
		System.out.println();
		System.out.println("SubChunk2 size \t= " + subChunk2Size);
	}
	
	/**
	 * Modify vector which represent wave file 
	 * for reading system information
	 * @param file - vector which represent file
	 * @param length - length or part with system information
	 * @return modified vector with system information
	 */
	private int[] getSysInfo(int[] file, int length){
		int[] sysInfo = new int[length];
		for (int i = 0; i < length; i++) {
			if(file[i] < 0){
				sysInfo[i] = 256 + file[i];
			} else {
				sysInfo[i] = file[i];
			}
		}
		return sysInfo;
	}
 	
	/**
	 * Gets the actual sound data from representation
	 * of wave file
	 * @param file - vector which represent file
	 * @param start - start index
	 * @param length - length or current part of vector
	 * @return modified actual sound data
	 */
	private int[][] readData(int[] file, int start, int length){
		int[][] data = new int[length][2];
		int[] leftChannel = new int[2];
		int[] rightChannel = new int[2];
		for (int i = 0, j = start; j+4 < file.length; i++, j+=4) {
			leftChannel[0] = file[j];
			leftChannel[1] = file[j+1];
			rightChannel[0] = file[j+2];
			rightChannel[1] = file[j+3];
			if(leftChannel[0] < 0){
				leftChannel[0] = 256 + leftChannel[0];
			}
			if(rightChannel[0] < 0){
				rightChannel[0] = 256 + rightChannel[0];
			}
			data[i][0] = leftChannel[0] + 256 * leftChannel[1];
			data[i][1] = rightChannel[0] + 256 * rightChannel[1];
		}
		return data;
	}
	
	/**
	 * Read chunk id from vector which represent wave file
	 * @param file - vector which represent file
	 * @param start - start index
	 * @param length - length or current part of vector
	 * @return char array
	 */
	private char[] readID(int[] file, int start, int length){
		char[] id = new char[length];
		for (int i = 0, j = start; i < id.length; i++, j++) {
			id[i] = (char)file[j];
		}
		return id;
	}

	/**
	 * Transform numbers from wave file into another numbers for
	 * correct work with them, for example: fft, ifft, dft
	 * @param file - all numbers from audio file
	 * @param length - length of current part
	 * @return chunk size
	 */
	private int readNumber(int[] file, int start, int length){
		int[] tmp = new int[length];
		for (int i = 0, j = start; i < tmp.length; i++, j++) {
			tmp[i] = file[j];
		}
		return parse(tmp);
	}
	
	/**
	 * Gets vector of numbers transform them to hex and counts one 
	 * number as result
	 * 
	 *  int[] tenSystem = {36, 8, 0, 0};
	 *  in hex -> String hexSystem = {"24", "08", "00", "00"};
	 *  int result = (2*16+4)*256^0 + (0*16+8)*256^1 + (0*16+0)*256^2 + (0*16+0)*256^3
	 *  
	 * @param tmp - integer vector
	 * @return result
	 */
	private int parse(int[] tmp){
		StringBuilder[] hexLine = new StringBuilder[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			hexLine[i] = new StringBuilder(Integer.toHexString(tmp[i]));
			if(hexLine[i].length() == 1){
				hexLine[i].insert(0, '0');
			}
		}
		int result = 0;
		for (int i = 0; i < hexLine.length; i++) {
			String firstNumber = hexLine[i].charAt(0) + "";
			String secondNumber = hexLine[i].charAt(1) + "";
			int tmp1 = Integer.parseInt(firstNumber, 16) * 16 + 
					Integer.parseInt(secondNumber, 16);
			tmp1 *= PowerOfNumber.powerInt(256, i);
			result += tmp1; 
		}
		return result;
	}

	
	public char[] getChunkID() {
		return chunkID;
	}
	

	public int getChunkSize() {
		return chunkSize;
	}
	

	public char[] getFormat() {
		return format;
	}
	

	public char[] getSubChunk1ID() {
		return subChunk1ID;
	}
	

	public int getSubChunk1Size() {
		return subChunk1Size;
	}
	

	public int getAudioFormat() {
		return audioFormat;
	}
	

	public int getNumChanels() {
		return numChanels;
	}
	

	public int getSampleRate() {
		return sampleRate;
	}
	

	public int getByteRate() {
		return byteRate;
	}
	

	public int getBlockAlign() {
		return blockAlign;
	}
	

	public int getBitsPerSample() {
		return bitsPerSample;
	}
	

	public char[] getSubChunk2ID() {
		return subChunk2ID;
	}
	

	public int getSubChunk2Size() {
		return subChunk2Size;
	}
	

	public int[][] getData() {
		return data;
	}
}