package com.github.smallru8.plugin.reply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CfgCreater {

	public CfgCreater() throws IOException {
		System.out.println("[INFO][PLUGIN][REPLY]:Checking cfg.");
		if(!(new File("reply").exists()))
			new File("reply").mkdir();
		if(!(new File("reply/OnTag.yml").exists()))
			new File("reply/OnTag.yml").createNewFile();
		if(!(new File("reply/cmd").exists()))
			new File("reply/cmd").mkdir();
		if(!(new File("reply/img").exists())) {
			new File("reply/img").mkdir();
			createImgSample();
		}
		if(!(new File("reply/msg").exists())) {
			new File("reply/msg").mkdir();
			createMsgSample();
		}
	}
	
	private void createImgSample() throws IOException {
		/*
		 * GET SAMPLE IMG
		 * reply/img/Sample/SmallNight.png
		 * reply/cmd/Sample.yml
		*/
		System.out.print("[INFO][PLUGIN][REPLY]:Creating IMG Sample...");
		URL url = new URL("http://w23.loxa.edu.tw/mm974401/SmallNight2019.png");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0");
		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		byte[] data = new byte[1024];
		new File("reply/img/Sample").mkdir();
		File sampImg = new File("reply/img/Sample/SmallNight2019.png");
		FileOutputStream fos = new FileOutputStream(sampImg);
		int n;
		while((n=in.read(data,0,1024))>=0) 
			fos.write(data,0,n);
		fos.flush();
		fos.close();
		in.close();
		FileWriter sampCmd = new FileWriter("reply/cmd/SampleImg.yml");
		sampCmd.write("img\n");
		sampCmd.write("Sample\n");
		sampCmd.write("sample||Sample||TEST||test\n");
		sampCmd.write("break\n");
		sampCmd.flush();
		sampCmd.close();
		System.out.println("DONE!");
	}
	private void createMsgSample() throws IOException {
		/*
		 * GET SAMPLE MSG
		 * reply/msg/Sample/Hello.txt
		 * reply/cmd/SampleMsg.yml
		*/
		new File("reply/msg/Sample").mkdir();
		System.out.print("[INFO][PLUGIN][REPLY]:Creating MSG Sample...");
		FileWriter sampMsg = new FileWriter("reply/msg/Sample/Hello.txt");
		sampMsg.write("Hello world!\n");
		sampMsg.flush();
		sampMsg.close();
		FileWriter sampCmd = new FileWriter("reply/cmd/SampleMsg.yml");
		sampCmd.write("msg\n");
		sampCmd.write("Sample\n");
		sampCmd.write("Hello&&world!\n");
		sampCmd.write("break\n");
		sampCmd.flush();
		sampCmd.close();
		System.out.println("DONE!");
	}
	
}
