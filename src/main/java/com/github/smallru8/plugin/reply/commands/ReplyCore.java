package com.github.smallru8.plugin.reply.commands;

import java.util.ArrayList;
import java.util.Stack;

import com.github.smallru8.plugin.reply.Reply;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ReplyCore {
	protected MessageChannel ch;
	protected String reply;
	protected String recvStr;
	protected ArrayList<String> fpath;
	public ReplyCore(Message msg) {
		ch = msg.getChannel();
		recvStr = msg.getContentRaw();
		reply = "";
		fpath = new ArrayList<String>();
	}
	protected int randomInt(int range) {//0~range-1
		return (int)(Math.random()*range);
	}
	
	public boolean analysisMsg() {//分析MSG 計算sa[2]postfix
		boolean reb = false;
		for(String[] sa:Reply.RplyCMDLs) {
			String[] postfix = sa[2].split(" ");
			Stack<String> cal = new Stack<String>();//開始計算postfix
			for(int i=0;i<postfix.length;i++) {
				if(!(postfix[i].equals("&&")||postfix[i].equals("||"))) {
					if(recvStr.contains(postfix[i]))
						postfix[i] = "1";
					else
						postfix[i] = "0";
					cal.push(postfix[i]);
				}else if(postfix[i].equals("&&")) {
					if(cal.pop().equals("1")&&cal.pop().equals("1")) 
						cal.push("1");
					else
						cal.push("0");
				}else if(postfix[i].equals("||")) {
					if(cal.pop().equals("1")||cal.pop().equals("1")) 
						cal.push("1");
					else
						cal.push("0");
				}
			}
			if(cal.pop().equals("1")) {
				fpath.add("reply/"+sa[0]+"/"+sa[1]+"/");
				reb = true;
				if(sa[3].startsWith("break"))
					break;
			}
		}
		return reb;
	}
	public void sendToDC() {
		ch.sendMessage(reply).queue();
	}
}
