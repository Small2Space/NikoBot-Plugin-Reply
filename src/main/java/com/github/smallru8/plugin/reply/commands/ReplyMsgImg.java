package com.github.smallru8.plugin.reply.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Message;

public class ReplyMsgImg extends ReplyCore{
	
	private ArrayList<File> msg_fpath;
	private ArrayList<File> img_fpath;
	
	public ReplyMsgImg(Message msg) {
		super(msg);
		msg_fpath = new ArrayList<File>();
		img_fpath = new ArrayList<File>();
	}
	public void getFile() {
		for(String path:fpath) {
			if(path.split("/")[1].equals("msg")) {
				msg_fpath.add(new File(""+new File(path).listFiles()[randomInt(new File(path).listFiles().length)]));
			}else {
				img_fpath.add(new File(""+new File(path).listFiles()[randomInt(new File(path).listFiles().length)]));
			}
		}
	}
	private void sendFileToDC() {
		for(int i=0;i<img_fpath.size();i++) {
			ch.sendFile(img_fpath.get(i)).queue();
		}
		img_fpath.clear();
	}
	@Override
	public void sendToDC() {
		getFile();
		for(int i=0;i<msg_fpath.size();i++) {
			try {
				FileReader fr = new FileReader(msg_fpath.get(i));
				BufferedReader br = new BufferedReader(fr);
				String tmp = null;
				String sum = "";
				while((tmp = br.readLine())!=null)
					sum += (tmp+"\n");
				br.close();
				fr.close();
				ch.sendMessage(sum).queue();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msg_fpath.clear();
		sendFileToDC();
	}
	
}
